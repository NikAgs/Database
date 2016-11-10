import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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



public class Updater {

	static String[] today = new String[3];

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
		String url = beginning.concat(stock + "&a=08&b=17&c=2014&d=" + today[0] + "&e=" + today[1] + "&f=" + today[2] + "&g=d&ignore=.csv");
		return url;

	}


	private void retrieveAllData() throws IOException {

		CSVReader reader = new CSVReader(new FileReader("/Users/TheNik/Documents/LaVeda/stockLists/NASDAQlist.csv"));

		String[] nextLine;
		Connection conn = null;

		reader.readNext();

		while ((nextLine = reader.readNext()) != null) {

			String stock = nextLine[0];

			generateData(stock,conn);
		}
		reader.close();
	}


	public void generateData(String str,Connection conn) throws MalformedURLException {

		URL url = new URL(getURL(str));
		System.out.println(url);


		try {

			CSVReader webReader = new CSVReader(new InputStreamReader(url.openStream()));


			try {
				

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
			}

		} catch (IOException e) {
			System.out.println("Error: Can't connect to Yahoo! Finance");
			//System.exit(1);

		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");
		String URL = "jdbc:mysql://192.95.31.34/VedaBase";
		String USER = "Nik";
		String PASS = "laveda2014";
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		
		Updater n = new Updater();
		n.findToday();
		n.retrieveAllData();
		


		BufferedReader r = new BufferedReader(new FileReader ("/Users/TheNik/Documents/LaVeda/vedastocks.txt"));
		for (String x = r.readLine(); x != null; x = r.readLine()) {
			n.generateData( x ,conn);
		}
		 




	}

}
