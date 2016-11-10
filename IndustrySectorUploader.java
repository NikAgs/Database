import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import au.com.bytecode.opencsv.CSVReader;


public class IndustrySectorUploader {
	
	
	public void initSector () {
		
	}
	
	public void initIndustry(){
		
	}

	public void generateData() throws MalformedURLException {


		try {

			CSVReader reader = new CSVReader(new FileReader("/Users/TheNik/Documents/tempNasdaq.csv"));

		
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
					reader.readNext();
					int count = 1;
					//int count = 2906;
					
					while ((nextLine = reader.readNext()) != null) {

						stmt = conn.createStatement();
					    //stmt1 = conn.createStatement();
					    
						String sql = "";
						String ind = nextLine[3];
						
						//String sql = "insert into Stock(sector) SELECT sectorID FROM Sector WHERE name = '" + nextLine[2] + "' LIMIT 1";
					
							sql = "UPDATE StockData SET "
									+ "stockID = (SELECT stockID FROM Stock WHERE name = '" +
									nextLine[0] + "') WHERE stockID = " + count + ";";
							System.out.println(sql);
							stmt.executeUpdate(sql);			
						
						
						count++;
				
						
						
						
					}

					
					reader.close();
					
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
		IndustrySectorUploader n = new IndustrySectorUploader();
		n.generateData();
		

	}

}
