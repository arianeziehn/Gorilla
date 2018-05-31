package Assign2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ErrorDetection {

    private static List<ArrayList<String>> input = new ArrayList<ArrayList<String>>();

    public static void main(String[] args) {


        String sourcePath = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\inputDB.csv";
        readData(sourcePath,true);

        for(ArrayList<String> row : input){

            if(!(row.get(6).length()==5)) row.set(6,"E");
        }
    }

    private static void readData(String filepath, boolean source){

        boolean header = true;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String sCurrentLine;
            int rowCount = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                ArrayList<String> row = new ArrayList();
                String[] split = sCurrentLine.replace(" ","").split(",");
                System.out.println(sCurrentLine);
                System.out.println(split.length);
                if(!header) {
                    for (int counter = 0; counter < split.length; counter++) {
                        String attribute = split[counter].trim().replace('"', ' ').replaceAll(" ", "");

                        if(!attribute.equals("") && !attribute.equals(" ") && !attribute.matches("[a-z]+")) {

                                row.add(counter, attribute);
                            }
                            else{
                            // E is added for Error
                            row.add(counter, "E");
                        }
                    }
                    System.out.println(rowCount+ " :" + row);
                    if(!(row.get(6).length()==2)) row.set(6,"E");
                    // ZIP
                    if(!(row.get(7).length()==5)) row.set(7,"E");
                    if(!(row.get(10).length()>=8 && row.get(10).length()<11 )) row.set(10,"E");

                    input.add(rowCount, row);
                    System.out.println(rowCount+ " :" + row);
                    rowCount++;
                }
                else{
                    header = false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace( );
        }



    }
}
