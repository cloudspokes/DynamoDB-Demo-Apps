# Posterity: A DynamoDB Demo by Michael Bleigh

Posterity is a simple, barebones Twitter-esque service created using Ruby and DynamoDB. It is far from complete but uses a few of DynamoDB's features including Hash/Range Keys and Atomic Set Push Operations.

Feel free to dig through the code, it is organized as follows:

* **environment.rb** bootstraps the environment and creates the DynamoDB tables if they do not exist
* **application.rb** is the Sinatra application
* **models.rb** contain the barebones model structures I created to wrap basic functionality for this demo

## Usage

1. Sign up for DynamoDB at aws.amazon.com
2. Set the following environment variables for your application:
    * `AWS_KEY`: Your AWS key.
    * `AWS_SECRET`: Your AWS secret access key.
3. Run the application using Rackup or your server of choice. It can also easily be deployed to Heroku or other services.

## License

Copyright (c) 2011 Michael Bleigh

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.