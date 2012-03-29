package flickrondynamo

class FlickFeedService {

    static private URL = 'http://api.flickr.com/services/feeds/photos_public.gne'

    static transactional = true

    def dynamoDBFeedCacheService

    def loadFeed() {
        def entries = new XmlParser().parse(URL).entry
        for (entry in entries) {
            def model = new FeedEntry(
                title: entry.title.text(),
                pageLink: entry.link.find{it.@rel == 'alternate'}.@href,
                entryId: entry.id.text(),
                published: parseDate(entry.published.text()),
                updated: parseDate(entry.updated.text()),
                dateTaken: parseDate(entry['dc:date.Taken'].text()),
                content: entry.content.text(),
                authorName: entry.author.name.text(),
                authorUri: entry.author.uri.text(),
                authorNsid: entry.author['flickr:nsid'].text(),
                authorBuddyIcon: entry.author['flickr:buddyicon'].text(),
                imageLink: entry.link.find{it.@rel == 'enclosure'}.@href
            )

            model.categories = []
            entry.category.each { category ->
                def term = category.@term
                if (term)
                    model.categories << term
            }

            def result = dynamoDBFeedCacheService.addFeedEntry(model)
            log.debug("[${model.title}] by [${model.authorName}] published [${model.published}] added to DynamoDB ${result}")
        }
    }

    private def parseDate(text) {
        // samples: 2012-01-21T20:38:46Z 2011-10-30T15:07:59-08:00
        Date.parse("yyyy-MM-dd'T'HHmmssZ", text.replace('Z', '-0000').replace(':', ''))
    }
}
