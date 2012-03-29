"Flickr on Dynamo" is a Grails app with these 6 main features:

1. Downloads entries from the Flickr public photos Atom feed on a schedule and caches them all in Amazon DynamoDB ("zaps atoms into Dynamo").

2. Creates the feed-entries table in DynamoDB on app startup if it doesn't already exist (hands-free bootstrapping, including configuring provisioned read and write capacity).

3. Lets you search for cached feed entries by primary key (author + published date/time range) or by table scan.

4. For table scan, lets you search by a combination of any or all (or none) of:
(a) exact match for author
(b) title containing words you specify
(c) tags containing a tag you specify
(d) published in a given date/time range
(e) only photos which have "likes"

5. Lets you like a photo, resulting in the atomic like counter (there's that word again, atom :) for the item in DynamoDB getting incremented.

6. Gives you a web UI for your search results (see the video).


Video
=====

Demo & Code Walkthrough: http://screencast.com/t/MAVgm7xeqDpr


Project Setup
=============

*Note*: you need to put aws-java-sdk-1.3.0.jar into the lib folder of the project because it's not available on Maven central (I didn't include it with my submission to keep the zip small).

Local deployment
----------------

Flickr on Dynamo is a Grails 1.3.7 app so make sure you have that installed and that you have the Grails plugin installed in Eclipse. Import the project into Eclipse, right click on it, and do Grails Tools/Refresh Dependencies to install the plugins I used which aren't in the default Grails install.

Configuration
-------------

Go to Config.groovy in grails-app/conf and update the configuration with your custom settings:

fodd.flickr.feedLoadIntervalMinutes = 5 // download from the Atom feed every 5 minutes

fodd.aws.accessKey = 'xxx'
fodd.aws.secretKey = 'yyy'
fodd.aws.dynamodb.endpoint = 'http://dynamodb.us-east-1.amazonaws.com'
fodd.aws.dynamodb.readsPerSecond = 10
fodd.aws.dynamodb.writesPerSecond = 5

(note: fodd = *F*lickr *O*n *D*ynamo*D*B)

Run
---

Now just run "grails run-app" and go to: http://localhost:8080/flickrondynamo

The first time you run the app it will create the feed-entries table (assuming you haven't manually created it). This will delay the app startup by a minute or two more than normal as the app polls for the new table it created moving to active status before proceeding.

Cloud Foundry deployment
------------------------

Assuming you have the Cloud Foundry Grails plugin installed from the Refresh Dependencies step, run "grails cf-push". Say yes to building the war. Say no to binding any services since Flickr on Dynamo doesn't require them. Assuming you've already run the app locally to get the feed-entries table created, there will be no extra delay on startup this time.

Logs
----

After the app has been deployed to Cloud Foundry, you can run "grails cf-logs" to view the logs. Flickr on Dynamo does a log each time it puts an item from the Flickr feed to DynamoDB, each time it does a query or scan, and each time it does an update to an item (for incrementing the atomic like counter). For each of these, the number of reads or writes per second is also logged, along with the time taken for the request.

Polling
-------

Set the feed polling interval as high as you want according to how much storage you are willing to consume on DynamoDB. My experience is that Flickr has enough public upload traffic that you can get new entries even if you poll very frequently. (Note that if by chance you get any already cached entries, that's fine because they will be replaced in DynamoDB based on the composite primary key.) My experience projects that if you poll for feed entries every 5 minutes, you can run for 73 days before you use up 100 MB of (free) storage given what the app stores from the feed entry. This is because Flickr returns only 20 entries each time you access the feed.

Note, however, that the more items you have in your table, the more read capacity will be consumed by a table scan, even with a scan filter. This is because the filter conditions are applied _after_ the items are returned by the scan. With this in mind, I use eventually consistent reads in the app to consume half as much capacity as strongly consistent reads. Given that the data set size is not huge, eventual consistency is as good as strong consistency here since sharding shouldn't be needed (even if it's done transparently by DynamoDB).

For my own deployment to Cloud Foundry I set the polling interval to every 2 hours since I'm on the AWS free tier and want to conserve capacity.


The Code
========

Services
--------

There are two services under services/flickrondynamo: FlickFeedService and DynamoDBFeedCacheService.

FlickFeedService is called on a schedule by FlickrFeedJob (a Quartz job under jobs/flickrondynamo) to download entries from the Flickr public photo feed: http://api.flickr.com/services/feeds/photos_public.gne

Each entry is parsed from XML to a transient FeedEntry domain object which is passed to DynamoDBFeedCacheService, which does a putItem into DynamoDB with attributes mapped like this:
- published, updated, dateTaken, likes -> saved as Number (with the Date attributes converted to time Longs)
- authorName, title, pageLink, imageLink -> saved as String
- categories ("tags") -> saved as Set of Strings

DynamoDBFeedCacheService also exposes searchFeedEntries and registerLikes methods. The search method accepts a FeedEntrySearchCriteria object and checks if a search by primary key fields only was requested. If so, then a DynamoDB query is performed. If not, then a DynamoDB scan is, for the combination of 5 criteria mentioned in the introduction by applying various ComparisonOperators. The register method calls DynamoDB updateItem to add one to the item's likes counter.

Note that I designed DynamoDBFeedCacheService to be usable for caching any Atom feed, not just the Flickr feed. The domain abstraction consists of a generic Atom FeedEntry and FeedEntrySearchCriteria, which is why "tags" in Flickr lingo are represented as "categories" in the Atom feed terminology. Following this design, I also named the DynamoDB table itself simply "feed-entries". There's no coupling to Flickr for the caching.

View
----

index.gsp under views/search provides the complete UI for Flickr on Dynamo.

Controller
----------

Likewise there is only one controller, SearchController under controllers/flickrondynamo, which maps the form inputs to a FeedEntrySearchCriteria domain object, and maps a list of FeedEntry domain objects to models for rendering by the index.gsp view. The controller delegates search responsibility to the DynamoDBFeedCacheService.


Enjoy "zappin' atoms into Dynamo" with Flickr on Dynamo!
========================================================

William Cheung
Toronto
Friday Jan 27, 2012
