package Assign2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ErrorDetection {

    private static List<ArrayList<String>> input = new ArrayList<ArrayList<String>>();
    static int numberOfCol = 0;

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
            int rowCount = 1;

            while ((sCurrentLine = br.readLine()) != null) {
                ArrayList<String> row = new ArrayList(12);
                String[] split = sCurrentLine.replace(", ", ";").replace(" ","").split(",");

                if(!header) {
                    for (int counter = 0; counter < numberOfCol; counter++) {
                        String attribute = "";
                        if(counter < split.length) attribute = split[counter].trim().replace('"', ' ').replaceAll(" ", "");
                        else row.add(counter, "E");
                        if(!attribute.equals("") && !attribute.equals(" ") && !attribute.matches("[a-z]+")) {

                                row.add(counter, attribute);
                            }
                            else{
                            // E is added for Error
                            row.add(counter, "E");
                        }
                    }
                    //state
                    if(!(row.get(6).length()==2) && !row.get(6).matches("[A-Z]*")){
                        row.set(6,"E");
                    }
                    // ZIP
                    if(!(row.get(7).length()==5)) row.set(7,"E");
                    //ssn
                    if(!(row.get(10).length()>=8 && row.get(10).length()<11 )) row.set(10,"E");

                    input.add(rowCount, row);

                    rowCount++;
                }
                else{
                    header = false;
                    createSchema(split);
                }

            }
        } catch (Exception e) {
            e.printStackTrace( );
        }



    }

    private static void createSchema(String[] split) {

        numberOfCol = split.length;
        ArrayList<String> row = new ArrayList();

        for(int i = 0; i < split.length; i++) {
            // first cleaning:
            // lower case and remove empty fields
            String attribute = split[i].trim( ).toLowerCase( ).replace('"', ' ').replace(" ", "");
            row.add(i,attribute);
        }
        //System.out.println(row.get(6));
        //System.out.println(row.get(7));
        //System.out.println(row.get(10));

                input.add(0,row);

        }


}
