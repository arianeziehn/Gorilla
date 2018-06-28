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
        mapper.put("Cali".toUpperCase(), "CA");
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
        mapper.put("Maryland".toUpperCase(),"MD");
        mapper.put("Massachusetts".toUpperCase(),"MA");
        mapper.put("Michigan".toUpperCase(),"MI");
        mapper.put("Minnesota".toUpperCase(),"MN");
        mapper.put("Mississippi".toUpperCase(),"MS");
        mapper.put("Missouri".toUpperCase(),"MO");
        mapper.put("Montana".toUpperCase(),"MT");
        mapper.put("Nebraska".toUpperCase(),"NE");
        mapper.put("Nevada".toUpperCase(),"NV");
        mapper.put("New Hampshire".toUpperCase(),"NH");
        mapper.put("New Jersey".toUpperCase(),"NJ");
        mapper.put("New Mexico".toUpperCase(),"NM");
        mapper.put("New York".toUpperCase(),"NY");
        mapper.put("North Carolina".toUpperCase(),"NC");
        mapper.put("North Dakota".toUpperCase(),"ND");
        mapper.put("Ohio".toUpperCase(),"OH");
        mapper.put("Oklahoma".toUpperCase(),"OK");
        mapper.put("Oregon".toUpperCase(),"OR");
        mapper.put("Pennsylvania".toUpperCase(),"PA");
        mapper.put("Rhode Island".toUpperCase(),"RI");
        mapper.put("South Carolina".toUpperCase(),"SC");
        mapper.put("South Dakota".toUpperCase(),"SD");
        mapper.put("Tennessee".toUpperCase(),"TN");
        mapper.put("Utah".toUpperCase(),"UT");
        mapper.put("Vermont".toUpperCase(),"VT");
        mapper.put("Virginia".toUpperCase(),"VA");
        mapper.put("Washington".toUpperCase(),"WA");
        mapper.put("West Virginia".toUpperCase(),"WV");
        mapper.put("Wisconsin".toUpperCase(),"WI");
        mapper.put("Wyoming".toUpperCase(),"WY");

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
