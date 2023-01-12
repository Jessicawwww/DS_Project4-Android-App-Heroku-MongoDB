package ds;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * This class is used for fetch data with API.
 */
public class CocktailModel {
    /**
     * Based on user's input search term, use cocktail API to search corresponding cocktail and fetch details about it.
     * @param searchTag user's input cocktail name.
     * @param picSize size of user's device.
     * @return string data that API gets
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
    public String doCocktailSearch(String searchTag, String picSize) throws UnsupportedEncodingException, ParseException {
        searchTag = URLEncoder.encode(searchTag, "UTF-8");
        String response = "";
        String searchResult = "Oops! No result found. Invalid Input here.";
        JSONObject drink = null;
        // create data API url based on search term
        String cocktailURL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + searchTag;
        response = fetch(cocktailURL, "TLSV1.3");

        JSONObject json;
        JSONParser jsonParser=new JSONParser();
        json= (JSONObject) jsonParser.parse(response);
        JSONArray drinks = (JSONArray)json.get("drinks");
        if (drinks!=null){
            drink = (JSONObject)drinks.get(0);
            searchResult = drink.toJSONString();
        }
        return searchResult;
    }

    /**
     * Return a URL of an image of appropriate size.
     * @param pictureURL The URL of the image.
     * @param picSize The string "mobile" or "desktop" indicating the size of photo requested.
     * @return the url of the image with appropriate size.
     */
    private String interestingPictureSize(String pictureURL, String picSize) {
        int finalDot = pictureURL.lastIndexOf(".");
        /*
         * From the flickr online documentation, an underscore and a letter
         * before the final "." and file extension is a size indicator.
         * "_m" for small and "-z" for big.
         */
        String sizeLetter = (picSize.equals("mobile")) ? "m" : "z";
        if (pictureURL.indexOf("_", finalDot-2) == -1) {
            // If the URL currently did not have a _? size indicator, add it.
            return (pictureURL.substring(0, finalDot) + "_" + sizeLetter
                    + pictureURL.substring(finalDot));
        } else {
            // Else just change it
            return (pictureURL.substring(0, finalDot - 1) + sizeLetter
                    + pictureURL.substring(finalDot));
        }
    }

    /**
     * Handling Annoying SSLHandShakeException.
     * Reference from: http://www.nakov.com/blog/2009/07/16/disable-certificate-validation-in-java-ssl-connections/
     * @param certType we use TLSV1.3 here.
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    private static void createTrustManager(String certType) throws KeyManagementException, NoSuchAlgorithmException{
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance(certType);
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    /**
     * Establish connection and fetch data with API.
     * @param searchURL url to search for.
     * @param certType in this case we use TLSV1.3.
     * @return json string from API.
     */
    private static String fetch(String searchURL, String certType) {
        try {
            // Create trust manager, which lets you ignore SSLHandshakeExceptions
            createTrustManager(certType);
        } catch (KeyManagementException ex) {
            System.out.println("Shouldn't come here: ");
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Shouldn't come here: ");
            ex.printStackTrace();
        }

        String response = "";
        try {
            URL url = new URL(searchURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            while ((str = in.readLine()) != null) {
                // str is one line of text readLine() strips newline characters
                response += str;
            }
            in.close();
        } catch (IOException e) {
            System.err.println("Something wrong with URL");
            return null;
        }
        return response;
    }
}
