package flickrondynamo

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class FlickrFeedJob {

    def flickFeedService

    def timeout = ConfigurationHolder.config.fodd.flickr.feedLoadIntervalMinutes * 60 * 1000
    def concurrent = false

    def execute() {
        flickFeedService.loadFeed()
    }
}
