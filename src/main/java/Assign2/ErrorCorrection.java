package Assign2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ErrorCorrection {

    private static List<ArrayList<String>> input = new ArrayList<ArrayList<String>>();
    private static USMap mapper = new USMap();
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
                        if(counter < split.length){
                            attribute = split[counter].trim().replace('"', ' ').toUpperCase();
                            System.out.println(input.get(0).get(counter)+ "counter : "+ counter +": " + " " + attribute);
                        }
                        else row.add(counter, "E");

                        if(!attribute.equals("") && !attribute.equals(" ") && !attribute.matches("[a-z]+")) {

                                row.add(counter, attribute);
                            }
                            else{
                            // E is added for Error
                            if((attribute.equals("") || !attribute.equals(" ")) && (counter == 2 || counter == 8 || counter == 9 ||counter == 11)) row.add(counter, "");
                            row.add(counter, "E");
                        }
                    }

                    // ZIP
                    if(!(row.get(7).length()==5) && !row.get(7).matches("[0-9]*")){

                        System.out.println(input.get(0).get(7) + " " + row.get(7));
                        String zip = row.get(7).replaceAll("[a-zA-Z-.]*", "");
                        String state = row.get(7).replaceAll("[0-9]*", "");
                        if((zip.length()==5) && zip.matches("[0-9]*")) {
                            System.out.println(input.get(0).get(7) + "2 " + zip);
                            row.set(7, zip);
                        }
                        else row.set(7,"E");
                        if(mapper.containsValue(state)) row.set(6,state);
                        if(mapper.containsKey(state)) row.set(6,mapper.get(state));


                    }
                    //state
                    if(!mapper.containsValue(row.get(6))){
                        String state = row.get(6);
                        System.out.println(input.get(0).get(6) + " " + state);
                        if(mapper.containsKey(state)){
                            state = mapper.get(state);
                            System.out.println(input.get(0).get(6) + "2 " + state);
                            row.set(6,state);
                        }
                        else row.set(6,"E");
                    }
                    //ssn
                    if(!(row.get(10).length()>=8 && row.get(10).length()<11 ) && !row.get(10).matches("[0-9]*")){
                        String ssn = row.get(10).replace("-", "");
                        System.out.println(input.get(0).get(10) + " " + row.get(10));
                        if((ssn.length() >= 8 && ssn.length() < 11 ) && ssn.matches("[0-9]*")){
                            System.out.println(input.get(0).get(10) + "2 " + ssn);
                            row.set(10,ssn);
                        }
                        else row.set(10,"E");
                    }

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
