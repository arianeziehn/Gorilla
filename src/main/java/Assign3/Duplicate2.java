package Assign3;

import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Duplicate2 {

    private static List<ArrayList<String>> input = new ArrayList<>();
    public static String sourcePath = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\output\\csvOutput3.csv";

    public static void main(String[] args) throws IOException {

        findDuplicates();



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
        HashMap<String, String> tuples = new HashMap<>();

        ArrayList<String> row = new ArrayList<>(2);

        MongeElkan mongeElkan = new MongeElkan();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(sourcePath));
        bufferedReader.readLine();

        while (bufferedReader.ready() && (line = bufferedReader.readLine()) != null){
            String[] splitLine  = line.split(",");
            String key = splitLine[7].trim().substring(0,1)+splitLine[3].substring(0,1);
            tuples.put((line.split(",")[0]),line+","+key);
        }
        HashMap<String, String> clone = new HashMap<>(tuples);

        String csvOutput2 = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\output\\csvOutput4.csv";
        FileWriter fileWriter = new FileWriter(csvOutput2);

        fileWriter.write("");
        fileWriter.write("tuple_id_1, tuple_id_2\n");

        // clone the extracted HAshMap to iterate over and compare with each tuple
        tuples.forEach((tupleID, tuple) -> clone.forEach((tupleIDClone, tupleClone) -> {

           String[] array1 = tuple.split(",");
           String[] array2 = tupleClone.split(",");

           if (!(array1[0].matches(array2[0])) && (array1[array1.length-1].matches(array2[array2.length-1]))) {
                if (mongeElkan.getSimilarity(array1[1], array2[1]) > 0.8) {
                    if (mongeElkan.getSimilarity(array1[2], array2[2]) > 0.8) {
                        if (mongeElkan.getSimilarity(array1[3], array2[3]) > 0.8) {
                            if (mongeElkan.getSimilarity(array1[4], array2[4]) > 0.8) {
                                //row.add(s + ", " + s3);
                                //System.out.println(s + ", " + s3 + ", MongeElkan: " + mongeElkan.getSimilarity(s2, s4));
                                try {
                                    fileWriter.append(tupleID).append(",").append(tupleIDClone).append("\n");
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
            //}
        }));

        try {

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        input.add(row);

    }
}
