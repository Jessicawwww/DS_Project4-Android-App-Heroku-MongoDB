import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class Request {



    private static String fetch(String urlString) {
//        normal fetch function
        String response = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                response += (str+"\n");
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Eeek, an exception");
        }
        return response;
    }
    public static void main(String[] args) throws ParseException {

        String response=fetch("https://api.coronatracker.com/v5/analytics/newcases/country?countryCode=AU&startDate=2022-02-01&endDate=2022-02-03");
//        JSONObject json;
//        JSONParser jsonParser=new JSONParser();
//        json= (JSONObject) jsonParser.parse(response);
        String []a =response.split(",");
        for (String curr:a){
            System.out.println(curr);
        }



    }
}
