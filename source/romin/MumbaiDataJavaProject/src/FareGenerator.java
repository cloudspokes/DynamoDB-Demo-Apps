/*
This is an internal class used to generate method calls for insert new Taxi and Auto Rate Cards. This is not to be used frequently and only when the 
Government announces new Rate Cards, typically once in 2 years or so.
*/

public class FareGenerator {

    private static double[] auto_rates = {0.0,1.5,3.0,4.0,5.5,7.0,8.50,10.0,11.0,12.5};
    private static double[] auto_midnight_rates = {0.0,1.5,3.5,4.5,6.5,8.5,10.50,12.0,13.50,15.5};

    private static double[] taxi_rates = {0.0,1.5,3.5,5.0,6.5,8.5};
    private static double[] taxi_midnight_rates = {0.0,2.0,4.5,6.5,8.0,10.5};
    
    /**
	 * @param args
	 */
	public static void main(String[] args) {
		//generateAutoFareDynamoStatements();
		generateTaxiFareDynamoStatements();

	}
	private static void generateAutoFareDynamoStatements() {
		// TODO Auto-generated method stub
		double base_fare = 11.00;
		double base_fare_night = 14.00;
		for (int i = 1; i <=10; i++) {
			double s = 0.0;
			for (int j=1;j<=10;j++) {
				double remainder = (i-1);
				double basecost_day   = base_fare + (remainder*14);
				double basecost_night = base_fare_night + (remainder*17.5);
				double fractioncost_day = auto_rates[j-1];
				double fractioncost_night = auto_midnight_rates[j-1];
				double totalcost_day   = basecost_day   + fractioncost_day;
				double totalcost_night = basecost_night + fractioncost_night;
				String strReading = Double.toString(i + s);
				System.out.println("addAutoFareRecord(" + "\"" + strReading + "\"" + "," + "\"" + totalcost_day + "\"" + "," + "\"" + totalcost_night + "\"" + ");");
				s = s + 0.1;
			}
		}
	}

	private static void generateTaxiFareDynamoStatements() {
		// TODO Auto-generated method stub
		float base_fare = 16.00f;
		float base_fare_night = 20.00f;
		for (int i = 1; i <=50; i++) {
				float s = 0.0f;
				for (int j=1;j<=10;j++) {
				int remainder = (int)(s*10);
				int fullnumber = (i*10 + remainder) - 10;
				int multiplier = (int)fullnumber/6;
				float totalcost_day = (float)base_fare + (float)(multiplier*10) + (float)taxi_rates[remainder%6];
				float totalcost_night = (float)base_fare_night + (float)(multiplier*12.5) + (float)taxi_midnight_rates[remainder%6];
				String strReading = Float.toString(i + s);
				System.out.println("addTaxiFareRecord(" + "\"" + strReading + "\"" + "," + "\"" + totalcost_day + "\"" + "," + "\"" + totalcost_night + "\"" + ");");
				//System.out.println(Float.toString(i + s) + "," + total + "," + night_total);
				s = s + 0.1f;
			}				
		}
	}
}
