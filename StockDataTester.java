import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class StockDataTester {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub


		CSVReader r = new CSVReader(new FileReader("/Users/TheNik/Documents/LaVeda/fixedNasdaqStock.csv"));
		CSVReader r1 = new CSVReader(new FileReader("/Users/TheNik/Documents/LaVeda/fixedNasdaqDataStock.csv"));
		//CSVWriter writer = new CSVWriter(new FileWriter("/Users/TheNik/Documents/LaVeda/fixedNasdaqStock.csv", false), ',', CSVWriter.NO_QUOTE_CHARACTER); 
		
		String[] nextLine;
		String[] nextLine1;
		String[] compare = new String[7];
		String[] compare1 = new String[7];

		while ((nextLine = r.readNext()) != null) {



			if ((nextLine1 =r1.readNext()) != null) {

				if (nextLine1.length < 8) {
					System.out.println(nextLine[0]);
				}
				compare[0] = nextLine[0];
				compare1[0] = nextLine1[0];
				
				for (int i = 1; i < 7; i++) {
					compare[i] = nextLine[i+1];
					compare1[i] = nextLine1[i+1];
				}

				if (compareArrays(compare,compare1) == false) {
					System.out.println("There is a problem somewhere around " + nextLine[0]);
					
					while (compareArrays(compare1,compare) == false) {
						nextLine1 = r1.readNext();
						for (int i = 0; i < 7; i++) {
							compare1[i] = nextLine1[i+1];
						}
					}
					
					
				} //else writer.writeNext(nextLine1);
				
			} else System.out.println("There is a problem most likely at the end");


		}
		
		System.out.println("THEY MATCH!!!!");
	}

	public static boolean compareArrays(String[] array1, String[] array2) {
        boolean b = true;
        if (array1 != null && array2 != null){
          if (array1.length != array2.length)
              b = false;
          else
              for (int i = 0; i < array2.length; i++) {
                  if (!array2[i].equals( array1[i])) {
                      b = false;    
                  }                 
            }
        }else{
          b = false;
        }
        return b;
    }

}
