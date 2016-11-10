import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import au.com.bytecode.opencsv.CSVReader;



public class StockDataUploader {

	static String[] today = new String[3];

	private void findToday() {
		Calendar currentDate = Calendar.getInstance();                    // gets the current date
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); 
		String dateNow = formatter.format(currentDate.getTime());
		today[0] = (dateNow.substring(5,7));
		today[1] = (dateNow.substring(8,10));
		today[2] = (dateNow.substring(0,4));

	}

	private String getURL() {
		String beginning = "http://ichart.yahoo.com/table.csv?s=";
		String url = beginning.concat("^GSPC" + "&a=0&b=1&c=1800&d=" + today[0] + "&e=" + today[1] + "&f=" + today[2] + "&g=d&ignore=.csv");
		return url;

	}

	public void generateData() throws MalformedURLException {

		URL url = new URL(getURL());

		//System.out.println(getURL());
		try {

			CSVReader webReader = new CSVReader(new FileReader("/Users/TheNik/Documents/LaVeda/Nasdaq copy.csv"));


			try {

				Class.forName("com.mysql.jdbc.Driver");
				String URL = "jdbc:mysql://192.95.31.34/VedaBase";
				String USER = "Nik";
				String PASS = "Sc00ter!";
				Connection conn = null;
				Statement stmt = null;
				Statement stmt1 = null;
				try {
					conn = DriverManager.getConnection(URL, USER, PASS);

					System.out.println("The Driver is working!");
					String[] nextLine;
					int count = 1;

					ArrayList<String> a = new ArrayList<String>();
					webReader.readNext();
					while ((nextLine = webReader.readNext()) != null) {

						stmt = conn.createStatement();
						//stmt1 = conn.createStatement();

						String sql = "insert into StockData (stockID,dateID,open,high,low,close,volume,adjustedClose) VALUES((" + 
								"select stockID from Stock where symbol = '" + nextLine[0] +"'), " + 
								"(select dateID from Date where date = '" + nextLine[1] + "'), '"
								+ nextLine[2] + "', '" + nextLine[3] + "', '" + nextLine[4] + "', '" + 
								nextLine[5] + "', '" + nextLine[6] + "', '" + nextLine[7] + "');";

						System.out.println(sql);
						stmt.executeUpdate(sql);
						//count++;

					}


					webReader.close();

				} catch (SQLException e) {
					System.out.println("Error: SQL messed up: ");
					e.printStackTrace();
				}


			}
			catch(ClassNotFoundException ex) {
				System.out.println("Error: Unable to load driver class!");
				System.exit(1);
			}

		} catch (IOException e) {
			System.out.println("Error: Can't connect to Yahoo! Finance");
			System.exit(1);

		}
	}

	public static void main(String[] args) throws MalformedURLException {
		StockDataUploader n = new StockDataUploader();
		n.findToday();
		n.generateData();


	}

}
