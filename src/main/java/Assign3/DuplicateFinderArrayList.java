package Assign3;

import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

public class DuplicateFinderArrayList {

    private static List<ArrayList<String>> input = new ArrayList<>();
    public static String sourcePath = "./src/main/output/csvOutput3.csv";

    public static void main(String[] args) throws IOException {

        long time1 = System.currentTimeMillis();
        findDuplicates();
        long time2 = System.currentTimeMillis();
        System.out.println(time2-time1 + " ms");

/*
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

*/



    }
    public static void findDuplicates() throws IOException {
        String line;
        ArrayList<String> tuples = new ArrayList<>();

        ArrayList<String> row = new ArrayList<>(2);

        MongeElkan mongeElkan = new MongeElkan();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(sourcePath));
        bufferedReader.readLine();

        while (bufferedReader.ready() && (line = bufferedReader.readLine()) != null){
            tuples.add(line);
        }


        ArrayList<String> clone = (ArrayList<String>)tuples.clone();

        String csvOutput2 = "./src/main/output/csvOutput5.csv";
        FileWriter fileWriter = new FileWriter(csvOutput2);

        fileWriter.write("");
        fileWriter.write("tuple_id_1, tuple_id_2\n");

        // clone the extracted HAshMap to iterate over and compare with each tuple
        for(int i = 0; i < tuples.size()-1; i++) {
            for(int j = i+1; j < clone.size(); j++){

           String[] array1 = tuples.get(i).split(",");
           String[] array2 = clone.get(j).split(",");

           if (!(array1[0].matches(array2[0]))) {
               if ((array1[1].equals("")||array2[1].equals(""))|| (mongeElkan.getSimilarity(array1[1], array2[1]) > 0.8)) {
                   if ((array1[7].equals("")||array2[7].equals(""))|| mongeElkan.getSimilarity(array1[7], array2[7]) > 0.8) {
                       if ((array1[3].equals("")||array2[3].equals(""))||mongeElkan.getSimilarity(array1[3], array2[3]) > 0.8) {
                           if ((array1[4].equals("")||array2[4].equals(""))|| mongeElkan.getSimilarity(array1[4], array2[4]) > 0.8) {
                                //row.add(s + ", " + s3);
                                //System.out.println(s + ", " + s3 + ", MongeElkan: " + mongeElkan.getSimilarity(s2, s4));
                                try {
                                    fileWriter.append(array1[0]).append(",").append(array2[0]).append("\n");
                                    fileWriter.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }


            //if ((mongeElkan.getSimilarity(s2,s4)) > 0.8){
            //    row.add(s +", " + s3 + ", MongeElkan: " + mongeElkan.getSimilarity(s2,s4));
           }
        }

        try {

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        input.add(row);

    }
}
