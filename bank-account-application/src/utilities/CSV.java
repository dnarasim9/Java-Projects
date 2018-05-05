package utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CSV {

  /**
   * Function to read a CSV file and return it as a list.
   *
   * @param file: file path
   * @return List of data in the csv file
   */
  public static List<String[]> read(String file) {

    List<String[]> data = new LinkedList<String[]>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line;

      while ((line = reader.readLine()) != null) {
        String[] dataArray = line.split(",");
        data.add(dataArray);
      }

    } catch (FileNotFoundException e) {
      System.err.println("Could not open file: " + file);
      e.printStackTrace();
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Could not read file: " + file);
      e.printStackTrace();
      System.exit(1);
    }

    return data;

  }

}
