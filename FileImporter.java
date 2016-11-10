
import java.sql.*;

public class FileImporter {

	public static void main(String[] args) {
		
		String file = "'/Users/TheNik/Documents/LaVeda/fixedAMEX.csv'";
		String table = "StockData";
		String columns = "stock, date, open, high, low, close, volume, adjustedClose";
		
		Connection conn = null;
		Statement stmt = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");
			String URL = "jdbc:mysql://192.95.31.34/VedaBase";
			String USER = "Nik";
			String PASS = "Sc00ter!";

			try {
				conn = DriverManager.getConnection(URL, USER, PASS);
				stmt = conn.createStatement();
	
				String sql = "load data local infile " + file + " into table " + table + " fields terminated by ','" +
						" enclosed by '\"'" +
						" lines terminated by '\\n'" +
						" (" + columns + ");";
				
				System.out.println(sql);
				stmt.executeUpdate(sql);
				


			} catch (SQLException e) {
				System.out.println("Error: cannot establish connection to database.");
				e.printStackTrace();
			}


		}
		catch(ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		}



	}

}
