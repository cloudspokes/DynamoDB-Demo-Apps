var util = require("util"),  
    http = require("http"),  
    url = require("url"),  
    path = require("path"),  
    fs = require("fs");  
	var credentials = {AccessKeyId : "YOUR_ACCESS_KEY", 
					   SecretKey   : "YOUR_SECRET_KEY"}; 
	var dynamoDB = require('./lib/dynamoDB').DynamoDB(credentials);

/**************************************************************
* Function to return the HTML file specified in the URL.      *
* Example: If someone invokes http://mynodeapp.com            *
* the code will make a call to load_static_file('index.html') *
* to return back the default home page.                       *
**************************************************************/
function load_static_file(uri, response) {  
    var filename = path.join(process.cwd(), uri);  
    path.exists(filename, function(exists) {  
        if(!exists) {  
            response.writeHead(404, {"Content-Type": "text/plain"});  
            response.write("404 Not Found\n");  
            response.end();  
            return;  
        }  
  
        fs.readFile(filename, "binary", function(err, file) {  
            if(err) {  
                response.writeHead(500, {"Content-Type": "text/plain"});  
                response.write(err + "\n");  
                response.end();  
                return;  
            }  
  
            response.writeHead(200);  
            response.write(file, "binary");  
            response.end();  
        });  
    });  
}  

function sendResponse(tableName,response) {
    var responseData = "";
	dynamoDB.scan(
			{"TableName"    :tableName}
				, function(resp,result) {
					resp.on('data', function(chunk){
						//console.log(""+chunk);
					});
					result.on('ready', function(data){
						clearTimeout(timeout);
						responseData = JSON.stringify(data.Items);
						response.writeHead(200, { "Content-Type" : "application/JSON" });  
						response.write(responseData);  
						response.end(); 
					});
				});

				var timeout = setTimeout(function() {  
					response.writeHead(200, { "Content-Type" : "application/JSON" });  
					response.write(JSON.stringify([]));  
					response.end();  
					}, 10000); 	
}

/*************************************************************
* Global Exception Handler for unCaughtException in Node     *
**************************************************************/
process.on('uncaughtException', function (err) {
  console.log('Caught exception: ' + err);
});

/*************************************************************
* Main Server logic to process each incoming HTTP Request    *
*                                                            *
* Logic:                                                     *
* 1. Parse the incoming request url                          *
* 2. if '/', return back the index.html                      *
* 3. if '/service', return back the JSON result              *
* 4. Anything else, return back the Error                    *
**************************************************************/
http.createServer(function(request, response) {  
    var uri = url.parse(request.url).pathname;  
	var responseData = "";
	if (uri === "/") {
	   load_static_file('/index.html',response);
	}
	else if (uri === "/index.html") {
	   load_static_file('/index.html',response);
	}
	else if (uri === "/versions") {
		sendResponse("MUMBAI_DATA_VERSION",response);
	}
	else if (uri === "/autofare") {
		sendResponse("AUTO_FARE",response);
	}
	else if (uri === "/taxifare") {
		sendResponse("TAXI_FARE",response);
	}
	else if (uri === "/helplines") {
		sendResponse("HELPLINES",response);
	}
	else if (uri === "/bloodbanks") {
	    sendResponse("MUMBAI_BLOODBANK",response);
	}
	else {
	   load_static_file('/error.html',response);
	}
}).listen(process.env.VCAP_APP_PORT || 3000);  	 
console.log('Node Mumbai Dataset Server has started listening');