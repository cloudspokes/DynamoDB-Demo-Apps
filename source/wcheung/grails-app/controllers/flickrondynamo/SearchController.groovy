package flickrondynamo

class SearchController {

    def dynamoDBFeedCacheService

    private static final DATE_FIELDS = ['publishedFrom', 'publishedTo']
    private static final INT_FIELDS = ['limit']
    private static final SEARCH_FIELDS

    static {
        SEARCH_FIELDS = ['authorName', 'category', 'titlePhrase']
        SEARCH_FIELDS.addAll(DATE_FIELDS)
        SEARCH_FIELDS.addAll(INT_FIELDS)
    }

    private static final LIKE_KEY_PREFIX = 'like|'

    def index = {
        if (params.doSearch) {
            def values = paramsToSearchValues(params)

            def criteria = new FeedEntrySearchCriteria()
            SEARCH_FIELDS.each { name ->
                def value = values[name]
                if (!value)
                    return // skip

                if (name in DATE_FIELDS)
                    value = new Date(value as long)
                else if (name in INT_FIELDS)
                    value = value as int

                criteria."${name}" = value
                values[name] = value
            }

            criteria.likesOnly = values.likesOnly

            def entries = dynamoDBFeedCacheService.searchFeedEntries(criteria)
            values.entries = entries

            values.likesOnly = criteria.likesOnly // return boolean type used to set checkbox

            return values
        }
    }

    def search = {
        def values = paramsToSearchValues(params)
        redirect(action: 'index', params: values)
    }

    def registerLikes = {
        // like keys are encoded as "like|published(as a long)|authorName"
        def likeKeys = params.findAll {it.key.startsWith(LIKE_KEY_PREFIX)}
        def primaryKeys = []
        likeKeys.each { key, value ->
            def tokens = value.split('\\|')
            def primaryKey = [:]
            primaryKey.published = new Date(tokens[1] as long)
            primaryKey.authorName = tokens[2]
            primaryKeys << primaryKey
        }
        dynamoDBFeedCacheService.registerLikes(primaryKeys)

        search() // refresh
    }

    private def paramsToSearchValues(params) {
        def values = [:]

        params.each { key, value ->
            if (key in SEARCH_FIELDS)
                values[key] = value instanceof Date ? value.time : value
        }

        values.likesOnly = params.likesOnly

        values.doSearch = true

        values
    }

    def clear = {
        def values = [:]

        SEARCH_FIELDS.each { name ->
            values."${name}" = ''
        }

        redirect(action: 'index', params: values)
    }
}
