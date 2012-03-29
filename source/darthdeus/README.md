# AWS DynamoDB Task Management

This application is a demonstration of the use of [DynamoDB](http://aws.typepad.com/aws/2012/01/amazon-dynamodb-internet-scale-data-storage-the-nosql-way.html). It uses a custom ORM called DynamoRecord to access the database.

The app is created using Ruby framework [Sinatra](http://sinatrarb.com) and the source code is available on [GitHub](https://github.com/darthdeus/dynamorecord). The layout is done using [Twitter Bootstrap framework](http://twitter.github.com/bootstrap) and is backed up by some jQuery for all the AJAXy stuff.

The ORM itself has a decent test coverage using RSpec and excercises basic functionality of DynamoDB. IDs are generated via the [UUIDTools](https://github.com/sporkmonger/uuidtools/tree/) gem. Main idea was to get at least some of the ActiveRecord-ish API to DynamoDB using some basic metaprogramming.

Tasks have two attributes, name, which is strored in a string, and tags, which are split by `,` and stored in an array.

If the application stops responding, refresh the page. There might be an issue with internet connectivity.

If you wish to run this applicatin yourself, you need to create a `tasks` table via the AWS management console and add a primary key called `id`. Then supply your AWS login credentials via environment variables `AMAZON_ACCESS_KEY_ID` and `AMAZON_SECRET_ACCESS_KEY`. On heroku, these can be set via `heroku config:add AMAZON_ACCESS_KEY_ID=ABCDEF` etc.