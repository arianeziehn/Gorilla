package Assign2;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ErrorDetection {

    private static List<ArrayList<String>> input = new ArrayList<>();
    private static USMap mapper = new USMap();

    private static int numberOfCol = 0;

    public static void main(String[] args) throws IOException {


        String sourcePath = "./src/main/resources/inputDB.csv";
        String csvOutput = "./src/main/output/csvOutput.csv";
        FileWriter fileWriter = new FileWriter(csvOutput);
        readData(sourcePath);


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


        /**
        BufferedReader br=new BufferedReader(new FileReader(csvOutput));
        String line="";
        String newLine = "";



        FileWriter replacer = new FileWriter(csvOutput2);

        while((line=br.readLine())!=null) {
            newLine = line.replaceAll("\\[", "");
            newLine = newLine.replaceAll("\\]", "");


            replacer.write(newLine);
            replacer.append("\n");

        }

        replacer.flush();
        replacer.close();
*/
    }

    private static void readData(String filepath){

        boolean header = true;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String sCurrentLine;
            int rowCount = 1;

            while ((sCurrentLine = br.readLine()) != null) {

                String[] split = sCurrentLine.replace(", ", ";").split(",");

                if(!header) {
                    ArrayList<String> row = new ArrayList<>(numberOfCol);
                    for (int counter = 0; counter < numberOfCol; counter++) {
                        String attribute = "";
                        if(counter < split.length) attribute = split[counter].replace('"', ' ').trim();
                        //else row.add(counter, "E");
                        if(!attribute.equals("") && !attribute.equals(" ") && !attribute.matches(".*[a-z]+.*")) {

                                row.add(counter, attribute);
                            }
                            else{
                            // E is added for Error
                            if((attribute.equals("") || !attribute.equals(" ")) && (counter == 2 || counter == 8 || counter == 9 ||counter == 11) || counter ==12) row.add(counter, "");
                            else row.add(counter, "E");
                            //else row.add(counter, attribute);
                        }
                    }
                    //state
                    if(!mapper.containsValue(row.get(6))){
                        row.set(6,"E");
                    }

                    // ZIP
                    if(!(row.get(7).length()==5) && !row.get(7).matches("[0-9]*")) row.set(7,"E");

                    //ssn
                    if(!(row.get(10).length()>=8 && row.get(10).length()<11 ) && !row.get(10).matches("[0-9]*")) row.set(10,"E");

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
