package flickrondynamo

class FeedEntry {

    static mapWith = "none" // not persisted by GORM

    String title // ex: P1060269
    String pageLink // ex: http://www.flickr.com/photos/quinnhung/6737928501/
    String entryId // ex: tag:flickr.com,2005:/photo/6737928501
    Date published // ex: 2012-01-21T20:38:46Z, parsed as Date object
    Date updated // ex: 2012-01-21T20:38:46Z, parsed as Date object
    Date dateTaken // ex: 2011-12-27T14:40:13-08:00, parsed as Date object
    String content // ex: html
    String authorName // ex: quinnhung
    String authorUri // ex: http://www.flickr.com/people/quinnhung/
    String authorNsid // ex: 29676337@N00
    String authorBuddyIcon // ex: http://farm1.staticflickr.com/97/buddyicons/29676337@N00.jpg?1158891554#29676337@N00
    String imageLink // ex: http://farm8.staticflickr.com/7153/6737928501_0e94b8dff0_b.jpg
    List<String> categories // ex: [temple, laos]

    int likes
}
