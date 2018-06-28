package Assign3;

import Assign2.USMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DuplicateDetection {

    private static List<ArrayList<String>> input = new ArrayList<>();
    private static USMap mapper = new USMap();
    private static int numberOfCol = 0;
    private int rowCount;

    public static void main(String[] args) throws IOException {


        String sourcePath = "./src/main/resources/inputDB.csv";
        readData(sourcePath);

        String csvOutput2 = "./src/main/output/csvOutput3.csv";
        FileWriter fileWriter = new FileWriter(csvOutput2);

        fileWriter.write("");


        for (ArrayList<String> anInput : input) {
            Iterator iterator = anInput.iterator();
            while (iterator.hasNext()) {
                fileWriter.append(iterator.next().toString());
                if(iterator.hasNext()) {
                    fileWriter.append(",");
                }
            }
            fileWriter.append("\n");
        }

        fileWriter.flush();
        fileWriter.close();

    }

    private static void readData(String filepath){

        boolean header = true;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String sCurrentLine;
            int rowCount = 1;

            while ((sCurrentLine = br.readLine()) != null) {

                String[] split = sCurrentLine.replace(", ", " ").split(",");

                if(!header) {
                    ArrayList<String> row = new ArrayList<>(numberOfCol);
                    for (int counter = 0; counter < numberOfCol; counter++) {
                        String attribute = "";
                        if(counter < split.length){
                            // to UpperCase for constrain 1
                            attribute = split[counter].replace('"', ' ').trim().toUpperCase();
                            //System.out.println(input.get(0).get(counter)+ "counter : "+ counter +": " + " " + attribute);
                        }
                        //else row.add(counter, "E");

                        if(!attribute.equals("") && !attribute.equals(" ") && !attribute.matches("[a-z]+")) {
                                row.add(counter, attribute);
                            }
                            else{
                            // E is added for Error
                            if((attribute.equals("") || !attribute.equals(" ")) && (counter == 2 || counter == 8 || counter == 9 ||counter == 11 || counter == 12)) row.add(counter, "");
                            //else row.add(counter, "E");
                            else row.add(counter, attribute);
                        }
                    }

                    // check zip code column for state characters and copy them in the right column
                    if (row.get(7).contains(" ")) {
                        if (mapper.containsKey((row.get(7)).split(" ")[1])) {
                            row.set(6, row.get(7).split(" ")[1]);
                        } else if (mapper.containsValue((row.get(7)).split(" ")[1])) {
                            row.set(6, row.get(7).split(" ")[1]);
                        } else if (mapper.containsKey((row.get(7)).split(" ")[0])) {
                            row.set(6, row.get(7).split(" ")[0]);
                        } else if (mapper.containsValue((row.get(7)).split(" ")[0])) {
                            row.set(6, row.get(7).split(" ")[0]);
                        }
                    }
                    if (row.get(9).contains(" ")) {
                        if (mapper.containsKey((row.get(9)).split(" ")[0])) {
                            row.set(6, row.get(9).split(" ")[0]);
                        } else if (mapper.containsKey((row.get(9)).split(" ")[1])) {
                            row.set(6, row.get(9).split(" ")[1]);
                        } else if (row.get(9).split(" ").length == 3) {
                            if (mapper.containsKey((row.get(9)).split(" ")[2]))
                                row.set(6, row.get(9).split(" ")[2]);
                        }
                    }

                    // ZIP
                    if(!(row.get(7).length()==5) && !row.get(7).matches("[0-9]*")){

                        //System.out.println(input.get(0).get(7) + " " + row.get(7));
                        String zip = row.get(7).replaceAll("[a-zA-Z-.]*", "");
                        zip = zip.replaceAll(" ", "");
                        String state = row.get(7).replaceAll("[0-9]*", "");
                        state = state.replaceAll(" ", "");



                        if((zip.length()==5) && zip.matches("[0-9]*")) {
                            //System.out.println(input.get(0).get(7) + "2 " + zip);
                            row.set(7, zip);
                        }

                        else row.set(7,"E");

                        if(mapper.containsValue(state)) row.set(6,state);
                        if(mapper.containsKey(state)) row.set(6,mapper.get(state));


                    }
                    //state
                    if(!mapper.containsValue(row.get(6))){
                        String state = row.get(6);
                        //System.out.println(input.get(0).get(6) + " " + state);
                        if(mapper.containsKey(state)){
                            state = mapper.get(state);
                            //System.out.println(input.get(0).get(6) + "2 " + state);
                            row.set(6,state);
                        }

                        else row.set(6,"E");
                    }
                    //ssn
                    if(!(row.get(10).length()>=8 && row.get(10).length()<11 ) && !row.get(10).matches("[0-9]*")){
                        String ssn = row.get(10).replace("-", "");
                        //System.out.println(input.get(0).get(10) + " " + row.get(10));
                        if((ssn.length() >= 8 && ssn.length() < 11 ) && ssn.matches("[0-9]*")){
                           // System.out.println(input.get(0).get(10) + "2 " + ssn);
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
        ArrayList<String> row = new ArrayList<>();

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
