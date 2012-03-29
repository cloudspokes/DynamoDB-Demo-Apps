/*
 * Copyright 2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.ComparisonOperator;
import com.amazonaws.services.dynamodb.model.Condition;
import com.amazonaws.services.dynamodb.model.DescribeTableRequest;
import com.amazonaws.services.dynamodb.model.PutItemRequest;
import com.amazonaws.services.dynamodb.model.PutItemResult;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.amazonaws.services.dynamodb.model.TableDescription;
import com.amazonaws.services.dynamodb.model.TableStatus;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class AmazonDynamoDBPopulator {

    /*
     * Important: Be sure to fill in your AWS access credentials in the
     *            AwsCredentials.properties file before you try to run this
     *            sample.
     * http://aws.amazon.com/security-credentials
     */

	public static final String ACCESSKEY = "YOUR_ACCESS_KEY";
	public static final String SECRETKEY = "YOUR_SECRET_KEY";
	
	public static final String DATA_VERSION_TABLENAME = "MUMBAI_DATA_VERSION";
	public static final String AUTOFARE_TABLENAME     = "AUTO_FARE";
	public static final String TAXIFARE_TABLENAME     = "TAXI_FARE";
	public static final String HELPLINES_TABLENAME    = "HELPLINES";
	public static final String BLOODBANK_TABLENAME    = "MUMBAI_BLOODBANK";
    static AmazonDynamoDBClient dynamoDB;

    /**
     * The only information needed to create a client are security credentials
     * consisting of the AWS Access Key ID and Secret Access Key. All other
     * configuration, such as the service endpoints, are performed
     * automatically. Client parameters, such as proxies, can be specified in an
     * optional ClientConfiguration object when constructing a client.
     *
     * @see com.amazonaws.auth.BasicAWSCredentials
     * @see com.amazonaws.auth.PropertiesCredentials
     * @see com.amazonaws.ClientConfiguration
     */
    private static void init() throws Exception {
    	
    	BasicAWSCredentials creds = new BasicAWSCredentials(ACCESSKEY,SECRETKEY);
		dynamoDB = new AmazonDynamoDBClient(creds);
		dynamoDB.setEndpoint("http://dynamodb.us-east-1.amazonaws.com");
    }


    public static void main(String[] args) throws Exception {
        init();

        try {
		    //The methods below are commented but any of them will be used to refresh the AmazonDynamo DB database when the data changes.
			//New Data sets will be added in the same fashion.
        	//populateDataVersions();
            //populateBloodBanks();
        	//populateAutoFare();
        	//populateTaxiFare();
        	//populateHelplines();

        	// SAMPLE SCAN FUNCTION - Scan items for bloodbanks in a particular area
            /*HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
            Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS("Parel"));
            scanFilter.put("AREA", condition);
            ScanRequest scanRequest = new ScanRequest(BLOODBANK_TABLENAME).withScanFilter(scanFilter);
            ScanResult scanResult = dynamoDB.scan(scanRequest);
            System.out.println("Result: " + scanResult);*/
    		
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private static void populateDataVersions() {
    	addDataVersionRecord(BLOODBANK_TABLENAME,"1.0","01-26-2012");
    	addDataVersionRecord(AUTOFARE_TABLENAME,"1.0","10-17-2011");
    	addDataVersionRecord(TAXIFARE_TABLENAME,"1.0","06-24-2010");
    	addDataVersionRecord(HELPLINES_TABLENAME,"1.0","01-26-2012");
    }

	private static void populateBloodBanks() {
		addBloodBankRecord("1","Samarpan Blood Bank","Ghatkopar","25111313");
		addBloodBankRecord("2","Jaslok Hospital","Kemps Corner","66573333");
		addBloodBankRecord("3","Anviksha Pathology Lab and Blood Bank","Ghatkopar","25136290");
		addBloodBankRecord("4","Nanavati Hospital Blood Bank","Vile Parle","26119924");
		addBloodBankRecord("5","P D Hinduja Hospital","Mahim","24440431");
		addBloodBankRecord("6","Cama Hospital","Fort","22611648");
		addBloodBankRecord("7","Cooper Hospital","Juhu","26207254,26207256,26207257");
		addBloodBankRecord("9","Esis Blood Blank","MIDC","24933143");
		addBloodBankRecord("10","Mahatma Gandhi Memorial Hospital","Parel","24132575");
		addBloodBankRecord("11","Holy Spirit Institute Of Nursing Education","Chakala","28248500");
		addBloodBankRecord("12","St George Blood Bank","Fort","22620344");
		addBloodBankRecord("13","Rajawadi Hospital","Ghatkopar","25094149");
		addBloodBankRecord("14","Hi Tech Blood Transfusion and Allied Services","Malad","28886484");
		addBloodBankRecord("15","JJ Hospital Blood Bank","Byculla","23735555");
		addBloodBankRecord("16","Borivali Blood Bank","Borivali","28935219");
		addBloodBankRecord("17","V M Desai Municipal Hospital","Santacruz","26182081");
		addBloodBankRecord("18","Sir Harkisandas Narottamdas Hospital","Girgaum","30095555,23884015,23855555,23808932");
		addBloodBankRecord("19","Parsi Hospital","Tardeo","23685350");
		addBloodBankRecord("20","GT Hospital","Kalbadevi","22630552");
		addBloodBankRecord("21","Sewree Hospital","Sewree","24146993");
		addBloodBankRecord("22","Sarvodaya Hospital","Ghatkopar","25152237");
		addBloodBankRecord("23","Red Cross Blood Bank","Fort","22663195");
		addBloodBankRecord("24","Rotary DG Goenkar Blood Bank","Andheri","26283557");
		addBloodBankRecord("25","Yamunabai Nair Blood Bank","Mumbai Central","23098150");
		addBloodBankRecord("26","Esic Blood Bank","Kalbadevi","22621466");
		addBloodBankRecord("27","Cumballa Hill Hospital and Heart Institute","Kemps Corner","23803336");
		addBloodBankRecord("28","Red Cross Blood Bank","Fort","22663560");
		addBloodBankRecord("29","Navjivan Blood Bank and Lab","Thane","25400425");
		addBloodBankRecord("30","Blood Bank-Holy Family Hospital","Bandra","30610300");
		addBloodBankRecord("31","Ambika Blood Bank","Ghatkopar","25124322");
		addBloodBankRecord("32","Ambaji Blood Bank","Govandi","25507553");
		addBloodBankRecord("33","Ashirwad Blood Bank","Dadar","24154826");
		addBloodBankRecord("34","Balaji Blood Bank","Malad","28412451");
		addBloodBankRecord("35","BARC Hospital Blood Bank","Anushaktinagar","25563137");
		addBloodBankRecord("36","Bhatia General Hospital Blood Bank","Tardeo","56660000");
		addBloodBankRecord("37","Bhiwandi Blood Bank","Bhiwandi","02522-258065");
		addBloodBankRecord("38","Bombay Hospital Trust Blood Bank","Marine Lines","22067676");
		addBloodBankRecord("39","Civil Hospital (Vitthat Sayanna) Blood Bank","Thane","25471541");
		addBloodBankRecord("40","Desai Blood Bank","Khar","26000089");
		addBloodBankRecord("41","Dr. B A Memorial (Central Rlway) Hospital Blood Bank","Byculla","23791964");
		addBloodBankRecord("42","Harilal Bhagwati Municipal Hospital Blood Bank","Borivali","28932461,28932463");
		addBloodBankRecord("43","Holy Spirit Hospital Blood Bank","Andheri","28378822,28372748");
		addBloodBankRecord("44","JVP Blood Bank","Vashi","27894490,27890185");
		addBloodBankRecord("45","K.E.M Hospital Blood Bank","Parel","24136051,24131419,24134977,24135189");
		addBloodBankRecord("46","K J .Somaiya Blood Bank","Sion","24090253");
		addBloodBankRecord("47","Lilavati Hospital Blood Bank","Bandra","26455891 Ext. 2223");
		addBloodBankRecord("48","Mahatma Gandhi Mission Hospital Blood Bank","New Mumbai","27422459,27423002");
		addBloodBankRecord("49","Wadia Blood Bank","Parel","24169809,24129786,24147843");
		addBloodBankRecord("50","Vaidya's (Chetna) Blood Bank","Thane","25406460,25380787");
		addBloodBankRecord("51","Tishiya Blood Bank","Thane","25971196,25803263");
		addBloodBankRecord("52","Tata Memorial Hospital Blood Bank","Parel","24149750,24161413,24170000,24127096");
		addBloodBankRecord("53","Swami Vivekanand Medical Mission’s Sanjivani Blood Bank","Virar","250-2502284,250-2505029");
		addBloodBankRecord("54","Nanavati Hospital Blood Bank","Vile Parle","26182262,26182255");
		addBloodBankRecord("55","Pooja Blood Bank","Mulund","25693688");
		addBloodBankRecord("56","Yamunabai Nair Blood Bank","Mumbai Central","23098150");
		addBloodBankRecord("57","MASINA Hospital Blood Bank","Byculla","23700715,23714890");
	}
	
	private static void populateAutoFare() {
		addAutoFareRecord("1.0","11.0","14.0");
		addAutoFareRecord("1.1","12.5","15.5");
		addAutoFareRecord("1.2","14.0","17.5");
		addAutoFareRecord("1.3","15.0","18.5");
		addAutoFareRecord("1.4","16.5","20.5");
		addAutoFareRecord("1.5","18.0","22.5");
		addAutoFareRecord("1.6","19.5","24.5");
		addAutoFareRecord("1.7","21.0","26.0");
		addAutoFareRecord("1.8","22.0","27.5");
		addAutoFareRecord("1.9","23.5","29.5");
		addAutoFareRecord("2.0","25.0","31.5");
		addAutoFareRecord("2.1","26.5","33.0");
		addAutoFareRecord("2.2","28.0","35.0");
		addAutoFareRecord("2.3","29.0","36.0");
		addAutoFareRecord("2.4","30.5","38.0");
		addAutoFareRecord("2.5","32.0","40.0");
		addAutoFareRecord("2.6","33.5","42.0");
		addAutoFareRecord("2.7","35.0","43.5");
		addAutoFareRecord("2.8","36.0","45.0");
		addAutoFareRecord("2.9","37.5","47.0");
		addAutoFareRecord("3.0","39.0","49.0");
		addAutoFareRecord("3.1","40.5","50.5");
		addAutoFareRecord("3.2","42.0","52.5");
		addAutoFareRecord("3.3","43.0","53.5");
		addAutoFareRecord("3.4","44.5","55.5");
		addAutoFareRecord("3.5","46.0","57.5");
		addAutoFareRecord("3.6","47.5","59.5");
		addAutoFareRecord("3.7","49.0","61.0");
		addAutoFareRecord("3.8","50.0","62.5");
		addAutoFareRecord("3.9","51.5","64.5");
		addAutoFareRecord("4.0","53.0","66.5");
		addAutoFareRecord("4.1","54.5","68.0");
		addAutoFareRecord("4.2","56.0","70.0");
		addAutoFareRecord("4.3","57.0","71.0");
		addAutoFareRecord("4.4","58.5","73.0");
		addAutoFareRecord("4.5","60.0","75.0");
		addAutoFareRecord("4.6","61.5","77.0");
		addAutoFareRecord("4.7","63.0","78.5");
		addAutoFareRecord("4.8","64.0","80.0");
		addAutoFareRecord("4.9","65.5","82.0");
		addAutoFareRecord("5.0","67.0","84.0");
		addAutoFareRecord("5.1","68.5","85.5");
		addAutoFareRecord("5.2","70.0","87.5");
		addAutoFareRecord("5.3","71.0","88.5");
		addAutoFareRecord("5.4","72.5","90.5");
		addAutoFareRecord("5.5","74.0","92.5");
		addAutoFareRecord("5.6","75.5","94.5");
		addAutoFareRecord("5.7","77.0","96.0");
		addAutoFareRecord("5.8","78.0","97.5");
		addAutoFareRecord("5.9","79.5","99.5");
		addAutoFareRecord("6.0","81.0","101.5");
		addAutoFareRecord("6.1","82.5","103.0");
		addAutoFareRecord("6.2","84.0","105.0");
		addAutoFareRecord("6.3","85.0","106.0");
		addAutoFareRecord("6.4","86.5","108.0");
		addAutoFareRecord("6.5","88.0","110.0");
		addAutoFareRecord("6.6","89.5","112.0");
		addAutoFareRecord("6.7","91.0","113.5");
		addAutoFareRecord("6.8","92.0","115.0");
		addAutoFareRecord("6.9","93.5","117.0");
		addAutoFareRecord("7.0","95.0","119.0");
		addAutoFareRecord("7.1","96.5","120.5");
		addAutoFareRecord("7.2","98.0","122.5");
		addAutoFareRecord("7.3","99.0","123.5");
		addAutoFareRecord("7.4","100.5","125.5");
		addAutoFareRecord("7.5","102.0","127.5");
		addAutoFareRecord("7.6","103.5","129.5");
		addAutoFareRecord("7.7","105.0","131.0");
		addAutoFareRecord("7.8","106.0","132.5");
		addAutoFareRecord("7.9","107.5","134.5");
		addAutoFareRecord("8.0","109.0","136.5");
		addAutoFareRecord("8.1","110.5","138.0");
		addAutoFareRecord("8.2","112.0","140.0");
		addAutoFareRecord("8.3","113.0","141.0");
		addAutoFareRecord("8.4","114.5","143.0");
		addAutoFareRecord("8.5","116.0","145.0");
		addAutoFareRecord("8.6","117.5","147.0");
		addAutoFareRecord("8.7","119.0","148.5");
		addAutoFareRecord("8.8","120.0","150.0");
		addAutoFareRecord("8.9","121.5","152.0");
		addAutoFareRecord("9.0","123.0","154.0");
		addAutoFareRecord("9.1","124.5","155.5");
		addAutoFareRecord("9.2","126.0","157.5");
		addAutoFareRecord("9.3","127.0","158.5");
		addAutoFareRecord("9.4","128.5","160.5");
		addAutoFareRecord("9.5","130.0","162.5");
		addAutoFareRecord("9.6","131.5","164.5");
		addAutoFareRecord("9.7","133.0","166.0");
		addAutoFareRecord("9.8","134.0","167.5");
		addAutoFareRecord("9.9","135.5","169.5");
		addAutoFareRecord("10.0","137.0","171.5");
		addAutoFareRecord("10.1","138.5","173.0");
		addAutoFareRecord("10.2","140.0","175.0");
		addAutoFareRecord("10.3","141.0","176.0");
		addAutoFareRecord("10.4","142.5","178.0");
		addAutoFareRecord("10.5","144.0","180.0");
		addAutoFareRecord("10.6","145.5","182.0");
		addAutoFareRecord("10.7","147.0","183.5");
		addAutoFareRecord("10.8","148.0","185.0");
		addAutoFareRecord("10.9","149.5","187.0");
	}
	
	private static void populateTaxiFare() {
		addTaxiFareRecord("1.0","16.0","20.0");
		addTaxiFareRecord("1.1","17.5","22.0");
		addTaxiFareRecord("1.2","19.5","24.5");
		addTaxiFareRecord("1.3","21.0","26.5");
		addTaxiFareRecord("1.4","22.5","28.0");
		addTaxiFareRecord("1.5","24.5","30.5");
		addTaxiFareRecord("1.6","26.0","32.5");
		addTaxiFareRecord("1.7","27.5","34.5");
		addTaxiFareRecord("1.8","29.5","37.0");
		addTaxiFareRecord("1.9","31.0","39.0");
		addTaxiFareRecord("2.0","26.0","32.5");
		addTaxiFareRecord("2.1","27.5","34.5");
		addTaxiFareRecord("2.2","39.5","49.5");
		addTaxiFareRecord("2.3","41.0","51.5");
		addTaxiFareRecord("2.4","42.5","53.0");
		addTaxiFareRecord("2.5","44.5","55.5");
		addTaxiFareRecord("2.6","36.0","45.0");
		addTaxiFareRecord("2.7","37.5","47.0");
		addTaxiFareRecord("2.8","49.5","62.0");
		addTaxiFareRecord("2.9","51.0","64.0");
		addTaxiFareRecord("3.0","46.0","57.5");
		addTaxiFareRecord("3.1","47.5","59.5");
		addTaxiFareRecord("3.2","49.5","62.0");
		addTaxiFareRecord("3.3","51.0","64.0");
		addTaxiFareRecord("3.4","62.5","78.0");
		addTaxiFareRecord("3.5","64.5","80.5");
		addTaxiFareRecord("3.6","56.0","70.0");
		addTaxiFareRecord("3.7","57.5","72.0");
		addTaxiFareRecord("3.8","59.5","74.5");
		addTaxiFareRecord("3.9","61.0","76.5");
		addTaxiFareRecord("4.0","66.0","82.5");
		addTaxiFareRecord("4.1","67.5","84.5");
		addTaxiFareRecord("4.2","69.5","87.0");
		addTaxiFareRecord("4.3","71.0","89.0");
		addTaxiFareRecord("4.4","72.5","90.5");
		addTaxiFareRecord("4.5","74.5","93.0");
		addTaxiFareRecord("4.6","76.0","95.0");
		addTaxiFareRecord("4.7","77.5","97.0");
		addTaxiFareRecord("4.8","79.5","99.5");
		addTaxiFareRecord("4.9","81.0","101.5");
		addTaxiFareRecord("5.0","76.0","95.0");
		addTaxiFareRecord("5.1","77.5","97.0");
		addTaxiFareRecord("5.2","89.5","112.0");
		addTaxiFareRecord("5.3","91.0","114.0");
		addTaxiFareRecord("5.4","92.5","115.5");
		addTaxiFareRecord("5.5","94.5","118.0");
		addTaxiFareRecord("5.6","86.0","107.5");
		addTaxiFareRecord("5.7","87.5","109.5");
		addTaxiFareRecord("5.8","99.5","124.5");
		addTaxiFareRecord("5.9","101.0","126.5");
		addTaxiFareRecord("6.0","96.0","120.0");
		addTaxiFareRecord("6.1","97.5","122.0");
		addTaxiFareRecord("6.2","99.5","124.5");
		addTaxiFareRecord("6.3","101.0","126.5");
		addTaxiFareRecord("6.4","112.5","140.5");
		addTaxiFareRecord("6.5","114.5","143.0");
		addTaxiFareRecord("6.6","106.0","132.5");
		addTaxiFareRecord("6.7","107.5","134.5");
		addTaxiFareRecord("6.8","109.5","137.0");
		addTaxiFareRecord("6.9","111.0","139.0");
		addTaxiFareRecord("7.0","116.0","145.0");
		addTaxiFareRecord("7.1","117.5","147.0");
		addTaxiFareRecord("7.2","119.5","149.5");
		addTaxiFareRecord("7.3","121.0","151.5");
		addTaxiFareRecord("7.4","122.5","153.0");
		addTaxiFareRecord("7.5","124.5","155.5");
		addTaxiFareRecord("7.6","126.0","157.5");
		addTaxiFareRecord("7.7","127.5","159.5");
		addTaxiFareRecord("7.8","129.5","162.0");
		addTaxiFareRecord("7.9","131.0","164.0");
		addTaxiFareRecord("8.0","126.0","157.5");
		addTaxiFareRecord("8.1","127.5","159.5");
		addTaxiFareRecord("8.2","139.5","174.5");
		addTaxiFareRecord("8.3","141.0","176.5");
		addTaxiFareRecord("8.4","142.5","178.0");
		addTaxiFareRecord("8.5","144.5","180.5");
		addTaxiFareRecord("8.6","136.0","170.0");
		addTaxiFareRecord("8.7","137.5","172.0");
		addTaxiFareRecord("8.8","149.5","187.0");
		addTaxiFareRecord("8.9","151.0","189.0");
		addTaxiFareRecord("9.0","146.0","182.5");
		addTaxiFareRecord("9.1","147.5","184.5");
		addTaxiFareRecord("9.2","149.5","187.0");
		addTaxiFareRecord("9.3","151.0","189.0");
		addTaxiFareRecord("9.4","162.5","203.0");
		addTaxiFareRecord("9.5","164.5","205.5");
		addTaxiFareRecord("9.6","156.0","195.0");
		addTaxiFareRecord("9.7","157.5","197.0");
		addTaxiFareRecord("9.8","159.5","199.5");
		addTaxiFareRecord("9.9","161.0","201.5");
		addTaxiFareRecord("10.0","166.0","207.5");
		addTaxiFareRecord("10.1","167.5","209.5");
		addTaxiFareRecord("10.2","169.5","212.0");
		addTaxiFareRecord("10.3","171.0","214.0");
		addTaxiFareRecord("10.4","172.5","215.5");
		addTaxiFareRecord("10.5","174.5","218.0");
		addTaxiFareRecord("10.6","176.0","220.0");
		addTaxiFareRecord("10.7","177.5","222.0");
		addTaxiFareRecord("10.8","179.5","224.5");
		addTaxiFareRecord("10.9","181.0","226.5");
		addTaxiFareRecord("11.0","176.0","220.0");
		addTaxiFareRecord("11.1","177.5","222.0");
		addTaxiFareRecord("11.2","189.5","237.0");
		addTaxiFareRecord("11.3","191.0","239.0");
		addTaxiFareRecord("11.4","192.5","240.5");
		addTaxiFareRecord("11.5","194.5","243.0");
		addTaxiFareRecord("11.6","186.0","232.5");
		addTaxiFareRecord("11.7","187.5","234.5");
		addTaxiFareRecord("11.8","199.5","249.5");
		addTaxiFareRecord("11.9","201.0","251.5");
		addTaxiFareRecord("12.0","196.0","245.0");
		addTaxiFareRecord("12.1","197.5","247.0");
		addTaxiFareRecord("12.2","199.5","249.5");
		addTaxiFareRecord("12.3","201.0","251.5");
		addTaxiFareRecord("12.4","212.5","265.5");
		addTaxiFareRecord("12.5","214.5","268.0");
		addTaxiFareRecord("12.6","206.0","257.5");
		addTaxiFareRecord("12.7","207.5","259.5");
		addTaxiFareRecord("12.8","209.5","262.0");
		addTaxiFareRecord("12.9","211.0","264.0");
		addTaxiFareRecord("13.0","216.0","270.0");
		addTaxiFareRecord("13.1","217.5","272.0");
		addTaxiFareRecord("13.2","219.5","274.5");
		addTaxiFareRecord("13.3","221.0","276.5");
		addTaxiFareRecord("13.4","222.5","278.0");
		addTaxiFareRecord("13.5","224.5","280.5");
		addTaxiFareRecord("13.6","226.0","282.5");
		addTaxiFareRecord("13.7","227.5","284.5");
		addTaxiFareRecord("13.8","229.5","287.0");
		addTaxiFareRecord("13.9","231.0","289.0");
		addTaxiFareRecord("14.0","226.0","282.5");
		addTaxiFareRecord("14.1","227.5","284.5");
		addTaxiFareRecord("14.2","239.5","299.5");
		addTaxiFareRecord("14.3","241.0","301.5");
		addTaxiFareRecord("14.4","242.5","303.0");
		addTaxiFareRecord("14.5","244.5","305.5");
		addTaxiFareRecord("14.6","236.0","295.0");
		addTaxiFareRecord("14.7","237.5","297.0");
		addTaxiFareRecord("14.8","249.5","312.0");
		addTaxiFareRecord("14.9","251.0","314.0");
		addTaxiFareRecord("15.0","246.0","307.5");
		addTaxiFareRecord("15.1","247.5","309.5");
		addTaxiFareRecord("15.2","249.5","312.0");
		addTaxiFareRecord("15.3","251.0","314.0");
		addTaxiFareRecord("15.4","262.5","328.0");
		addTaxiFareRecord("15.5","264.5","330.5");
		addTaxiFareRecord("15.6","256.0","320.0");
		addTaxiFareRecord("15.7","257.5","322.0");
		addTaxiFareRecord("15.8","259.5","324.5");
		addTaxiFareRecord("15.9","261.0","326.5");
		addTaxiFareRecord("16.0","266.0","332.5");
		addTaxiFareRecord("16.1","267.5","334.5");
		addTaxiFareRecord("16.2","269.5","337.0");
		addTaxiFareRecord("16.3","271.0","339.0");
		addTaxiFareRecord("16.4","272.5","340.5");
		addTaxiFareRecord("16.5","274.5","343.0");
		addTaxiFareRecord("16.6","276.0","345.0");
		addTaxiFareRecord("16.7","277.5","347.0");
		addTaxiFareRecord("16.8","279.5","349.5");
		addTaxiFareRecord("16.9","281.0","351.5");
		addTaxiFareRecord("17.0","276.0","345.0");
		addTaxiFareRecord("17.1","277.5","347.0");
		addTaxiFareRecord("17.2","289.5","362.0");
		addTaxiFareRecord("17.3","291.0","364.0");
		addTaxiFareRecord("17.4","292.5","365.5");
		addTaxiFareRecord("17.5","294.5","368.0");
		addTaxiFareRecord("17.6","286.0","357.5");
		addTaxiFareRecord("17.7","287.5","359.5");
		addTaxiFareRecord("17.8","299.5","374.5");
		addTaxiFareRecord("17.9","301.0","376.5");
		addTaxiFareRecord("18.0","296.0","370.0");
		addTaxiFareRecord("18.1","297.5","372.0");
		addTaxiFareRecord("18.2","299.5","374.5");
		addTaxiFareRecord("18.3","301.0","376.5");
		addTaxiFareRecord("18.4","312.5","390.5");
		addTaxiFareRecord("18.5","314.5","393.0");
		addTaxiFareRecord("18.6","306.0","382.5");
		addTaxiFareRecord("18.7","307.5","384.5");
		addTaxiFareRecord("18.8","309.5","387.0");
		addTaxiFareRecord("18.9","311.0","389.0");
		addTaxiFareRecord("19.0","316.0","395.0");
		addTaxiFareRecord("19.1","317.5","397.0");
		addTaxiFareRecord("19.2","319.5","399.5");
		addTaxiFareRecord("19.3","321.0","401.5");
		addTaxiFareRecord("19.4","322.5","403.0");
		addTaxiFareRecord("19.5","324.5","405.5");
		addTaxiFareRecord("19.6","326.0","407.5");
		addTaxiFareRecord("19.7","327.5","409.5");
		addTaxiFareRecord("19.8","329.5","412.0");
		addTaxiFareRecord("19.9","331.0","414.0");
		addTaxiFareRecord("20.0","326.0","407.5");
		addTaxiFareRecord("20.1","327.5","409.5");
		addTaxiFareRecord("20.2","339.5","424.5");
		addTaxiFareRecord("20.3","341.0","426.5");
		addTaxiFareRecord("20.4","342.5","428.0");
		addTaxiFareRecord("20.5","344.5","430.5");
		addTaxiFareRecord("20.6","336.0","420.0");
		addTaxiFareRecord("20.7","337.5","422.0");
		addTaxiFareRecord("20.8","349.5","437.0");
		addTaxiFareRecord("20.9","351.0","439.0");
		addTaxiFareRecord("21.0","346.0","432.5");
		addTaxiFareRecord("21.1","347.5","434.5");
		addTaxiFareRecord("21.2","349.5","437.0");
		addTaxiFareRecord("21.3","351.0","439.0");
		addTaxiFareRecord("21.4","362.5","453.0");
		addTaxiFareRecord("21.5","364.5","455.5");
		addTaxiFareRecord("21.6","356.0","445.0");
		addTaxiFareRecord("21.7","357.5","447.0");
		addTaxiFareRecord("21.8","359.5","449.5");
		addTaxiFareRecord("21.9","361.0","451.5");
		addTaxiFareRecord("22.0","366.0","457.5");
		addTaxiFareRecord("22.1","367.5","459.5");
		addTaxiFareRecord("22.2","369.5","462.0");
		addTaxiFareRecord("22.3","371.0","464.0");
		addTaxiFareRecord("22.4","372.5","465.5");
		addTaxiFareRecord("22.5","374.5","468.0");
		addTaxiFareRecord("22.6","376.0","470.0");
		addTaxiFareRecord("22.7","377.5","472.0");
		addTaxiFareRecord("22.8","379.5","474.5");
		addTaxiFareRecord("22.9","381.0","476.5");
		addTaxiFareRecord("23.0","376.0","470.0");
		addTaxiFareRecord("23.1","377.5","472.0");
		addTaxiFareRecord("23.2","389.5","487.0");
		addTaxiFareRecord("23.3","391.0","489.0");
		addTaxiFareRecord("23.4","392.5","490.5");
		addTaxiFareRecord("23.5","394.5","493.0");
		addTaxiFareRecord("23.6","386.0","482.5");
		addTaxiFareRecord("23.7","387.5","484.5");
		addTaxiFareRecord("23.8","399.5","499.5");
		addTaxiFareRecord("23.9","401.0","501.5");
		addTaxiFareRecord("24.0","396.0","495.0");
		addTaxiFareRecord("24.1","397.5","497.0");
		addTaxiFareRecord("24.2","399.5","499.5");
		addTaxiFareRecord("24.3","401.0","501.5");
		addTaxiFareRecord("24.4","412.5","515.5");
		addTaxiFareRecord("24.5","414.5","518.0");
		addTaxiFareRecord("24.6","406.0","507.5");
		addTaxiFareRecord("24.7","407.5","509.5");
		addTaxiFareRecord("24.8","409.5","512.0");
		addTaxiFareRecord("24.9","411.0","514.0");
		addTaxiFareRecord("25.0","416.0","520.0");
		addTaxiFareRecord("25.1","417.5","522.0");
		addTaxiFareRecord("25.2","419.5","524.5");
		addTaxiFareRecord("25.3","421.0","526.5");
		addTaxiFareRecord("25.4","422.5","528.0");
		addTaxiFareRecord("25.5","424.5","530.5");
		addTaxiFareRecord("25.6","426.0","532.5");
		addTaxiFareRecord("25.7","427.5","534.5");
		addTaxiFareRecord("25.8","429.5","537.0");
		addTaxiFareRecord("25.9","431.0","539.0");
		addTaxiFareRecord("26.0","426.0","532.5");
		addTaxiFareRecord("26.1","427.5","534.5");
		addTaxiFareRecord("26.2","439.5","549.5");
		addTaxiFareRecord("26.3","441.0","551.5");
		addTaxiFareRecord("26.4","442.5","553.0");
		addTaxiFareRecord("26.5","444.5","555.5");
		addTaxiFareRecord("26.6","436.0","545.0");
		addTaxiFareRecord("26.7","437.5","547.0");
		addTaxiFareRecord("26.8","449.5","562.0");
		addTaxiFareRecord("26.9","451.0","564.0");
		addTaxiFareRecord("27.0","446.0","557.5");

	}
	
	private static void populateHelplines() {
		addHelplineRecord("1","POWER SUPPLY","Reliance","30303030");
		addHelplineRecord("2","POWER SUPPLY","MSEB","25686666");
		addHelplineRecord("3","WEATHER","Weather Inquiry","22150431");
		addHelplineRecord("4","EARTH QUAKE","Control Room","2202 5274,22837259,22837262");
		addHelplineRecord("5","BMC","Brihanmumbai Municipal Corporation (BMC) Control room","22694727");
		addHelplineRecord("6","BMC","Toll Free Disaster Control Room Helpline","108");
		addHelplineRecord("7","BMC","For Emergency Complaints like Building or Wall collapses","1916");
		addHelplineRecord("8","BMC","For Fallen trees, Short Circuits or Fire","23085991");
		addHelplineRecord("9","BMC-DRAINAGE","Main Conrol Room","1916");
		addHelplineRecord("10","BMC-DRAINAGE","Mumbai City","23678109");
		addHelplineRecord("11","BMC-DRAINAGE","Western suburbs","26146852");
		addHelplineRecord("12","BMC-DRAINAGE","Eastern suburbs","25153258");
		addHelplineRecord("13","CENTRAL RAILWAY","Central Railway Control Room","22622685,22624555,22659563");
		addHelplineRecord("14","WESTERN RAILWAY","Western Railway Control Room","23070564,23017379,23635959,22084287");
		addHelplineRecord("15","MUMBAI POLICE","Mumbai Police Control Room","22625020,22621855,22641440,22623054,22621983,22644140,22620111,100");
		addHelplineRecord("16","TRAFFIC POLICE","Traffic Police Control Room","24937755,24937746,30403040,24927234,24937747");
		addHelplineRecord("17","MMRDA","MMRDA Control Room","26594190,26591582");
		addHelplineRecord("18","BLOOD BANKS","Main Number","1910");
		addHelplineRecord("19","AMBULANCE SERVICES","Main Number","101");
		addHelplineRecord("20","CHILDREN HELPLINE","Main Number","1098");
	}
	
	private static void addHelplineRecord(String id,String domain, String name, String phonenumbers) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("ID", new AttributeValue(id));
        item.put("DOMAIN", new AttributeValue(domain));
        item.put("NAME", new AttributeValue(name));
        item.put("PHONENUMBERS", new AttributeValue(phonenumbers));
        PutItemRequest putItemRequest = new PutItemRequest(HELPLINES_TABLENAME, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        //Optionally print the result
        System.out.println("Result: " + putItemResult);
	}
	
	private static void addDataVersionRecord(String datasetname,String version, String lastupdate) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("DATASETNAME", new AttributeValue(datasetname));
        item.put("VERSION", new AttributeValue(version));
        item.put("LASTUPDATE", new AttributeValue(lastupdate));
        PutItemRequest putItemRequest = new PutItemRequest(DATA_VERSION_TABLENAME, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        //Optionally print the result
        System.out.println("Result: " + putItemResult);
	}
    
    private static void addBloodBankRecord(String id, String name, String area, String phonenumbers) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("ID", new AttributeValue(id));
        item.put("NAME", new AttributeValue(name));
        item.put("AREA", new AttributeValue(area));
        item.put("PHONENUMBERS", new AttributeValue(phonenumbers));
        PutItemRequest putItemRequest = new PutItemRequest(BLOODBANK_TABLENAME, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        //Optionally print the result
        System.out.println("Result: " + putItemResult);
    }    
    
    private static void addAutoFareRecord(String reading, String dayfare, String nightfare) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("READING", new AttributeValue(reading));
        item.put("DAYFARE", new AttributeValue(dayfare));
        item.put("NIGHTFARE", new AttributeValue(nightfare));
        PutItemRequest putItemRequest = new PutItemRequest(AUTOFARE_TABLENAME, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        //Optionally print the result
        System.out.println("Result: " + putItemResult);
    }    

    private static void addTaxiFareRecord(String reading, String dayfare, String nightfare) {
    	Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("READING", new AttributeValue(reading));
        item.put("DAYFARE", new AttributeValue(dayfare));
        item.put("NIGHTFARE", new AttributeValue(nightfare));
        PutItemRequest putItemRequest = new PutItemRequest(TAXIFARE_TABLENAME, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        //Optionally print the result
        System.out.println("Result: " + putItemResult);
    }    
}
