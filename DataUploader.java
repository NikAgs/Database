import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import au.com.bytecode.opencsv.CSVReader;


public class DataUploader {

	private static String[] today = new String[3];
	private static String[] lastUpdate = new String[3];

	private void findToday() {
		Calendar currentDate = Calendar.getInstance();                    // gets the current date
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); 
		String dateNow = formatter.format(currentDate.getTime());
		today[0] = (dateNow.substring(5,7));
		today[1] = (dateNow.substring(8,10));
		today[2] = (dateNow.substring(0,4));

	}

	private String getURL(String stock) {
		String beginning = "http://ichart.yahoo.com/table.csv?s=";
		String url = beginning.concat(stock + "&a=07" + "&b=14" + "&c=2014" + "&d=11" + "&e=23" + "&f=2014" + "&g=d&ignore=.csv");
		return url;

	}

	private void findLastUpdate(Connection conn) throws MalformedURLException {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "select MAX(date) from LastUpdate;";

		Statement statement;
		try {
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql); 

			try {
				Date date = rs.getDate(0);
				c.setTime(date);
				c.add(Calendar.DATE, 1);
				String d = sdf.format(c.getTime());
				lastUpdate[0] = (d.substring(5,7));
				lastUpdate[1] = (d.substring(8,10));
				lastUpdate[2] = (d.substring(0,4));
				
				rs.close();
				statement.close();

			} catch (SQLException sql1) {
				System.out.println("Error: SQL messed up: no lastUpdate");
			}

		} catch (SQLException sql2) {
			System.out.println("Error: SQL messed up: Problem executing getLastUpdate sql statement");
			
		}
	}

	private void retrieveAllData(Connection conn) throws IOException, SQLException {

		String sql = "select symbol from Stock where stockID >= 3450 and stockID <= 5000;";

		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(sql); 

		while(rs.next()) {
			String symbol = rs.getString("symbol");
			generateData(symbol, conn);
		}

		rs.close();
		statement.close();


	}


	public void generateData(String str,Connection conn) throws MalformedURLException {

		URL url = new URL(getURL(str));
		System.out.println(url);

		try {

			InputStream is = url.openStream();

			try {

				CSVReader webReader = new CSVReader(new InputStreamReader(is));
				System.out.println("The Driver is working!");
				String[] nextLine;

				webReader.readNext();

				while ((nextLine = webReader.readNext()) != null) {

					String sql = "insert into StockData (stockID,date,open,high,low,close,volume,adjustedClose) VALUES("
							+ "(select stockID from Stock where symbol = '" + str + "')" + "," +"'"+ nextLine[0] +"'"
							+ "," + nextLine[1] + "," + nextLine[2] + "," + nextLine[3] + "," + nextLine[4] + "," + nextLine[5] + "," +
							nextLine[6] + ");";


					//String sql1 = "insert into Date (date) VALUES(" + "'" + nextLine[0] + "'"+ ");";
					//String sql2 = "insert ignore into Industry (name) VALUES(" + "'" + nextLine[7] + "'" + ");";
					System.out.println(sql);

					conn.prepareStatement(sql).executeUpdate(sql);

				}

				webReader.close();

			} catch (SQLException e) {
				System.out.println("Error: SQL messed up: ");
				e.printStackTrace();

			} finally {
				is.close();
			}

		} catch (IOException e) {
			System.out.println("Error: Can't connect to Yahoo! Finance");
			//System.exit(1);

		}
	}
	
	private void updateLastUpdate(Connection conn) {
		String sql = "insert into LastUpdate(date) VALUES('2014-12-23');";
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver");
		String URL = "jdbc:mysql://192.95.31.34/VedaBase";
		String USER = "Nik";
		String PASS = "Sc00ter!";
		Connection conn = DriverManager.getConnection(URL, USER, PASS);

		DataUploader n = new DataUploader();
		n.findToday();
		n.findLastUpdate(conn);
		n.updateLastUpdate(conn);
		n.retrieveAllData(conn);
		conn.close();

		/**
		n.generateData("ARWR",conn);
		n.generateData("FEYE",conn);
		n.generateData("ISIS",conn);
		n.generateData("KERX",conn);
		n.generateData("LIOX",conn);
		n.generateData("RLYP",conn);
		n.generateData("ASX",conn);
		n.generateData("ANR",conn);
		n.generateData("CUDA",conn);
		n.generateData("CHK",conn);
		n.generateData("PBF",conn);
		n.generateData("PQ",conn);
		n.generateData("SD",conn);
		n.generateData("INO",conn);
		n.generateData("WATT",conn);
		n.generateData("AAPL",conn);
		n.generateData("RFMD",conn);
		n.generateData("SWKS",conn);
		n.generateData("EA",conn);
		n.generateData("LRCX",conn);
		n.generateData("^GSPC",conn);


		//n.generateData("TFSCU");


		BufferedReader r = new BufferedReader(new FileReader ("/Users/TheNik/Documents/LaVeda/vedastocks.txt"));
		for (String x = r.readLine(); x != null; x = r.readLine()) {
			n.generateData( x ,conn);
		}

		 */




	}

}
