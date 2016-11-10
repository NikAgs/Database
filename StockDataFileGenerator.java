
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

public class StockDataFileGenerator {

	static ArrayList<String> stocks = new ArrayList<String>();
	static ArrayList<Integer> stockCount = new ArrayList<Integer>();	
	static ArrayList<Integer> stockIDs = new ArrayList<Integer>();
	

	static ArrayList<Integer> dates = new ArrayList<Integer>();

	public static void main(String[] args) throws IOException {

		initStocks();
		//initDates();
		//generateFile();
	}

	public static void initStocks() throws IOException {

		Connection conn = null;
		Statement stmt = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");
			String URL = "jdbc:mysql://192.95.31.34/VedaBase";
			String USER = "user";
			String PASS = "pass";

			try {
				conn = DriverManager.getConnection(URL, USER, PASS);
				stmt = conn.createStatement();

				CSVReader r = new CSVReader(new FileReader("/Users/TheNik/Documents/LaVeda/fixedNasdaq.csv"));

				String[] nextLine;
				int count = 1;

				r.readNext();

				while ((nextLine = r.readNext()) != null) {

					if (stocks.contains(nextLine[0])) {
						count++;
					} else {
						stocks.add(nextLine[0]);
						stockCount.add(count);

						System.out.println(nextLine[0]);
						System.out.println(count);
						count = 1;

					}

				}

				r.close();

				stockCount.remove(0);
				stockCount.add(count);

				for (int l = 0; l < stockCount.size(); l++) {
					System.out.print(stockCount.get(l).toString());
					System.out.print(", ");
				}


				for(int i = 0; i < stocks.size(); i++) {   
					String sql = "select stockID from Stock where symbol = '" + stocks.get(i) + "';";                
					ResultSet rs = stmt.executeQuery(sql); 

					rs.next();
					stockIDs.add(rs.getInt("stockID"));



				}  



				System.out.println();

				for(int i = 0; i < stockIDs.size(); i++) {   
					System.out.print(stockIDs.get(i).toString());
					System.out.print(", ");
				}  


			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		catch(ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		}

	}

	public static void initDates() throws IOException {
		
		ArrayList<String> dates = new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");
			String URL = "jdbc:mysql://192.95.31.34/VedaBase";
			String USER = "user";
			String PASS = "pass";

			try {
				conn = DriverManager.getConnection(URL, USER, PASS);
				stmt = conn.createStatement();

				CSVReader r = new CSVReader(new FileReader("/Users/TheNik/Documents/LaVeda/fixedNasdaq.csv"));

				String[] nextLine;
				String currDate = "deleteThis";
				String currCompany = "AAIT";

				r.readNext();

				while ((nextLine = r.readNext()) != null) {

					if (!nextLine[0].equals(currCompany)) {
						dates.add(currDate);
						currCompany = nextLine[0];
						
					} else {
						currDate = nextLine[1];

						

					}

				}

				r.close(); 

				System.out.println();

				for(int i = 0; i < dates.size(); i++) {   
					System.out.print("'" + dates.get(i).toString() + "', ");
	
				}  


			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		catch(ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		}

	}

	public static void generateFile() {

		Connection conn = null;
		Statement stmt = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");
			String URL = "jdbc:mysql://192.95.31.34/VedaBase";
			String USER = "user";
			String PASS = "pass";

			try {
				conn = DriverManager.getConnection(URL, USER, PASS);
				stmt = conn.createStatement();


				String headers = "stockID,dateID,open, high, low, close, volume, adjustedClose";
				String sql = "load data local infile '/Users/TheNik/Documents/LaVeda/USEnasdaq copy.csv' into table StockData fields terminated by ','" +
						" enclosed by '\"'" +
						" lines terminated by '\\n'" +
						" (" + headers + ");";

				System.out.println(sql);
				stmt.executeUpdate(sql);



			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		catch(ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		}



	}

}
