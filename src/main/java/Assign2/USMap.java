package Assign2;

import java.util.HashMap;
import java.util.Map;

public class USMap {


    private static Map<String, String> mapper = new HashMap<String, String>();

    public USMap(){
        createUSmapper();
    }

    private static void createUSmapper(){
        mapper.put("Alabama".toUpperCase() ,"AL");
        mapper.put("Alaska".toUpperCase(),"AK");
        mapper.put("Arizona".toUpperCase(),"AZ");
        mapper.put("Arkansas".toUpperCase(),"AR");
        mapper.put("California".toUpperCase(),"CA");
        mapper.put("Colorado".toUpperCase(),"CO");
        mapper.put("Connecticut".toUpperCase(), "CT");
        mapper.put("Delaware".toUpperCase(),"DE");
        mapper.put("Florida".toUpperCase(),"FL");
        mapper.put("Georgia".toUpperCase(),"GA");
        mapper.put("Hawaii".toUpperCase(),"HI");
        mapper.put("Idaho".toUpperCase(),"ID");
        mapper.put("Illinois".toUpperCase(),"IL");
        mapper.put("Indiana".toUpperCase(),"IN");
        mapper.put("Texas".toUpperCase(),"TX");
        mapper.put("Iowa".toUpperCase(),"IA");
        mapper.put("Kansas".toUpperCase(),"KS");
        mapper.put("Kentucky".toUpperCase(),"KY");
        mapper.put("Louisiana".toUpperCase(), "LA");
        mapper.put("Maine".toUpperCase(),"ME");
        mapper.put("Maryland","MD");
/*Massachusetts – MA
Michigan – MI
Minnesota – MN
Mississippi – MS
Missouri – MOMontana – MT
Nebraska – NE
Nevada – NV
New Hampshire – NH
New Jersey – NJ
New Mexico – NM
New York – NY
North Carolina – NC
North Dakota – ND
Ohio – OH
Oklahoma – OK
Oregon – OR
Pennsylvania – PA
Rhode Island – RI
South Carolina – SC
South Dakota – SD
Tennessee – TN

Utah – UT
Vermont – VT
Virginia – VA
Washington – WA
West Virginia – WV
Wisconsin – WI
Wyoming – WY*/

    }

    public boolean containsValue(String value){
        return mapper.containsValue(value);
    }

    public boolean containsKey(String key){
        return mapper.containsKey(key);
    }

    public String get(String key){
        return mapper.get(key);
    }
}