package flickrondynamo


import org.codehaus.groovy.grails.commons.ConfigurationHolder

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.dynamodb.AmazonDynamoDB
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient
import com.amazonaws.services.dynamodb.model.AttributeAction
import com.amazonaws.services.dynamodb.model.AttributeValue
import com.amazonaws.services.dynamodb.model.AttributeValueUpdate
import com.amazonaws.services.dynamodb.model.ComparisonOperator
import com.amazonaws.services.dynamodb.model.Condition
import com.amazonaws.services.dynamodb.model.CreateTableRequest
import com.amazonaws.services.dynamodb.model.DescribeTableRequest
import com.amazonaws.services.dynamodb.model.Key
import com.amazonaws.services.dynamodb.model.KeySchema
import com.amazonaws.services.dynamodb.model.KeySchemaElement
import com.amazonaws.services.dynamodb.model.ProvisionedThroughput
import com.amazonaws.services.dynamodb.model.PutItemRequest
import com.amazonaws.services.dynamodb.model.QueryRequest
import com.amazonaws.services.dynamodb.model.ScalarAttributeType;
import com.amazonaws.services.dynamodb.model.ScanRequest
import com.amazonaws.services.dynamodb.model.UpdateItemRequest

class DynamoDBFeedCacheService {

    private static final FEED_ENTRIES_TABLENAME = 'feed-entries'
    private static final AUTHORNAME_ATTRIBUTENAME = 'authorName'
    private static final PUBLISHED_ATTRIBUTENAME = 'published'
    private static final LIKES_ATTRIBUTENAME = 'likes'

    private static AmazonDynamoDB dynamoDB // thread-safe singleton

    static {
        setupLoginForDynamoDB()
        createDynamoDBTableIfNeeded()
    }

    private static def setupLoginForDynamoDB() {
        def creds = new BasicAWSCredentials(
            ConfigurationHolder.config.fodd.aws.accessKey,
            ConfigurationHolder.config.fodd.aws.secretKey)
        dynamoDB = new AmazonDynamoDBClient(creds)
        dynamoDB.endpoint = ConfigurationHolder.config.fodd.aws.dynamodb.endpoint
    }

    private static def createDynamoDBTableIfNeeded() {
        def tables = dynamoDB.listTables()
        if (tables.tableNames.contains(FEED_ENTRIES_TABLENAME))
            return

        def hashKeyElement = new KeySchemaElement().
                withAttributeName(AUTHORNAME_ATTRIBUTENAME).
                withAttributeType(ScalarAttributeType.S)

        def rangeKeyElement = new KeySchemaElement().
                withAttributeName(PUBLISHED_ATTRIBUTENAME).
                withAttributeType(ScalarAttributeType.N)

        def keySchema = new KeySchema().
                withHashKeyElement(hashKeyElement).
                withRangeKeyElement(rangeKeyElement)

        def throughput = new ProvisionedThroughput().
                withReadCapacityUnits(ConfigurationHolder.config.fodd.aws.dynamodb.readsPerSecond).
                withWriteCapacityUnits(ConfigurationHolder.config.fodd.aws.dynamodb.writesPerSecond)

        def request = new CreateTableRequest(FEED_ENTRIES_TABLENAME, keySchema).
                withProvisionedThroughput(throughput)
        def result = dynamoDB.createTable(request)
        // now wait until table actually created since the request is asynchronous
        def tableStatus = result.tableDescription.tableStatus
        while (tableStatus != 'ACTIVE') {
            Thread.sleep(1500)
            result = dynamoDB.describeTable(new DescribeTableRequest().withTableName(FEED_ENTRIES_TABLENAME))
            tableStatus = result.table.tableStatus
        }
    }

    def addFeedEntry(FeedEntry entry) {
        def request = new PutItemRequest(FEED_ENTRIES_TABLENAME, buildItem(entry))
        def start = System.currentTimeMillis()
        def result = dynamoDB.putItem(request)
        def end = System.currentTimeMillis()
        // return status:
        "using ${result.consumedCapacityUnits} writes per second in ${(end - start)/1000} seconds"
    }

    private def buildItem(entry) {
        def attributeValues = [:] // map of attribute name to value
        // for these fields from the entry we want to cache:
        [AUTHORNAME_ATTRIBUTENAME,
         PUBLISHED_ATTRIBUTENAME,
         'title',
         'pageLink',
         'updated',
         'dateTaken',
         'imageLink',
         'categories'].each { field ->
             def value = entry."${field}"
             if (value)
                 attributeValues[field] = asAttributeValue(value)
             // else is empty value, so skip it
        }
        attributeValues
    }

    private def buildEntry(attributeValues) {
        def entry = new FeedEntry()

        // Date fields
        [PUBLISHED_ATTRIBUTENAME, 'updated', 'dateTaken'].each { field ->
            if (attributeValues[field])
                entry."${field}" = new Date(attributeValues[field].n as long)
        }
        // String fields
        [AUTHORNAME_ATTRIBUTENAME, 'title', 'pageLink', 'imageLink'].each { field ->
            if (attributeValues[field])
                entry."${field}" = attributeValues[field]?.s
        }
        // String list fields
        ['categories'].each { field ->
            if (attributeValues[field])
                entry."${field}" = attributeValues[field]?.SS
        }
        // int fields
        [LIKES_ATTRIBUTENAME].each { field ->
            if (attributeValues[field])
                entry."${field}" = attributeValues[field]?.n as int
        }

        entry
    }

    private def asAttributeValue(value) {
        if (value instanceof Date)
            return new AttributeValue().withN(value.time as String)

        if (value instanceof List)
            return new AttributeValue().withSS(value)

        if (value instanceof Integer)
            return new AttributeValue().withN(value as String)

        assert value instanceof String
        new AttributeValue(value)
    }

    def searchFeedEntries(FeedEntrySearchCriteria criteria) {
        // note: DynamoDB is case-SENsitive
        def result
        def searchMethod

        def start = System.currentTimeMillis()
        if (searchQualifiesForPrimaryKeyQuery(criteria)) {
            searchMethod = 'query'
            result = primaryKeyQuery(criteria) // prefer primary key query whenever possible
        } else {
            searchMethod = 'scan'
            result = tableScan(criteria)
        }
        def end = System.currentTimeMillis()

        log.debug("DynamoDB ${searchMethod} used ${result.consumedCapacityUnits} reads per second in ${(end - start)/1000} seconds and returned ${result.count} items")

        def maxEntriesToReturn = Math.min(criteria.limit, 100) // to avoid blowing memory (OOM)
        def entries = []
        for (item in result.items) {
            entries << buildEntry(item)
            if (entries.size() == maxEntriesToReturn)
                break
        }

        searchMethod = 'scan' ? entries.reverse() : entries
        // If we did a table scan, reverse the entries so newest ones are first.
        // Not necessary for PK query because that supports backward scanning
        // which we took advantage of.
    }

    private def searchQualifiesForPrimaryKeyQuery(criteria) {
        // has primary key fields
        criteria.authorName && criteria.publishedFrom && criteria.publishedTo &&
        // and doesn't have any other fields
        !criteria.category && !criteria.titlePhrase && !criteria.likesOnly
    }

    private def primaryKeyQuery(FeedEntrySearchCriteria criteria) {
        def rangeCondition = new Condition().
            withComparisonOperator(ComparisonOperator.BETWEEN).
            withAttributeValueList(
                asAttributeValue(criteria.publishedFrom),
                asAttributeValue(criteria.publishedTo))

        def request = new QueryRequest().
            withTableName(FEED_ENTRIES_TABLENAME).
            withHashKeyValue(asAttributeValue(criteria.authorName)).
            withRangeKeyCondition(rangeCondition).
            withScanIndexForward(false) // want newest first
        // note: not request consistent read since not warranted by dataset size
        if (criteria.limit)
            request.limit = criteria.limit

        dynamoDB.query(request)
    }

    private def tableScan(FeedEntrySearchCriteria criteria) {
        def filter = [:]

        if (criteria.authorName) {
            filter.authorName = new Condition().
                withComparisonOperator(ComparisonOperator.EQ).
                withAttributeValueList(asAttributeValue(criteria.authorName))
        }

        if (criteria.titlePhrase) {
            filter.title = new Condition().
                withComparisonOperator(ComparisonOperator.CONTAINS).
                withAttributeValueList(asAttributeValue(criteria.titlePhrase))
        }

        if (criteria.category) {
            filter.categories = new Condition().
                withComparisonOperator(ComparisonOperator.CONTAINS).
                withAttributeValueList(asAttributeValue(criteria.category))
            // Since the categories attribute is a list, CONTAINS here means
            // the list containing the specified value in its entirety
        }

        if (criteria.likesOnly) { // then filter likes > 0
            filter.likes = new Condition().
                withComparisonOperator(ComparisonOperator.GT).
                withAttributeValueList(asAttributeValue(0))
        }

        if (criteria.publishedFrom && criteria.publishedTo) {
            filter.published = new Condition().
                withComparisonOperator(ComparisonOperator.BETWEEN).
                withAttributeValueList(
                    asAttributeValue(criteria.publishedFrom),
                    asAttributeValue(criteria.publishedTo))
        } else if (criteria.publishedFrom) {
            filter.published = new Condition().
                withComparisonOperator(ComparisonOperator.GE).
                withAttributeValueList(asAttributeValue(criteria.publishedFrom))
        } else if (criteria.publishedTo) {
            filter.published = new Condition().
                withComparisonOperator(ComparisonOperator.LE).
                withAttributeValueList(asAttributeValue(criteria.publishedTo))
        }

        def request = new ScanRequest(FEED_ENTRIES_TABLENAME).withScanFilter(filter)
        // Note: Don't set limit for table scan because while scanning, DynamoDB
        // applies limit _first_ to get each page of results, then applies the
        // filter to each page (doesn't go thru table accumulating matches until
        // limit reached)

        dynamoDB.scan(request)
    }

    def registerLikes(primaryKeys) {
        primaryKeys.each { primaryKey ->
            def key = new Key().
                withHashKeyElement(asAttributeValue(primaryKey.authorName)).
                withRangeKeyElement(asAttributeValue(primaryKey.published))

            def attributeUpdates = [:]
            attributeUpdates[LIKES_ATTRIBUTENAME] = new AttributeValueUpdate(
                asAttributeValue(1), AttributeAction.ADD) // atomic counter increment

            def request = new UpdateItemRequest().
                withTableName(FEED_ENTRIES_TABLENAME).
                withKey(key).
                withAttributeUpdates(attributeUpdates)

            def start = System.currentTimeMillis()
            def result = dynamoDB.updateItem(request)
            def end = System.currentTimeMillis()

            log.debug("DynamoDB updateItem used ${result.consumedCapacityUnits} writes per second in ${(end - start)/1000} seconds")
        }
    }

    /* comment out because turns out DynamoDB supports atomic increment so don't need get counter and then update it
    def getLikesForEntry(key) {
        def request = new GetItemRequest(FEED_ENTRIES_TABLENAME, key).
            withAttributesToGet([LIKES_ATTRIBUTENAME])

        def start = System.currentTimeMillis()
        def result = dynamoDB.getItem(request)
        def end = System.currentTimeMillis()

        log.debug("DynamoDB getItem used ${result.consumedCapacityUnits} reads per second in ${(end - start)/1000} seconds")

        result.item[LIKES_ATTRIBUTENAME].n as int ?: 0
    }
    */
}
