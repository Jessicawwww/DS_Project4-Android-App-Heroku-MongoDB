package edu.heinz.ds.androidinterestingpicture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import androidx.annotation.RequiresApi;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
/**
 * This class is used for getting data from web service deployed on heroku.
 * @author Jingyi Wu, Keqing Xing
 * Andrew id: jingyiw2, kxing
 */

/**
 * This class will get data from web service we deployed on heroku based on user's input through url.
 */
public class GetData {
    CocktailInformation ci = null;   // for callback
    String searchTerm = null;       // search api for all information
    Map drink = null;
    String name = null;
    String cate = null;
    String instruction = null;
    Bitmap picture = null;
    List<String> ingredients = new ArrayList<>();

    public void search(String searchTerm, Activity activity, CocktailInformation ci) {
        this.ci = ci;
        this.searchTerm = searchTerm;
        new BackgroundTask(activity).execute();
    }

    private class BackgroundTask {
        private Activity activity; // The UI thread
        public BackgroundTask(Activity activity) {
            this.activity = activity;
        }
        private void startBackground() {
            new Thread(new Runnable() {
                public void run() {
                    doInBackground();
                    // This is magic: activity should be set to MainActivity.this then this method uses the UI thread
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            onPostExecute();
                        }
                    });
                }
            }).start();
        }
        private void execute(){
            // There could be more setup here, which is why startBackground is not called directly
            startBackground();
        }

        /**
         * doInBackground( ) implements whatever you need to do on the background thread.
         */
        private void doInBackground() {
            drink = getDrinkData(searchTerm);
            if (drink==null){
                return;
            }
            name = drink.get("strDrink").toString();
            cate = drink.get("strCategory").toString();
            instruction = drink.get("strInstructions").toString();
            picture = (Bitmap) drink.get("strDrinkThumb");
            ingredients.add(drink.get("strIngredient1").toString()+drink.get("strMeasure1").toString());
            ingredients.add(drink.get("strIngredient2").toString()+drink.get("strMeasure2").toString());
            if (drink.get("strIngredient3")!=null){
                ingredients.add(drink.get("strIngredient3").toString()+drink.get("strMeasure3").toString());
            }
            if (drink.get("strIngredient4")!=null && drink.get("strMeasure4")!=null){
                ingredients.add(drink.get("strIngredient4").toString()+drink.get("strMeasure4").toString());
            }
        }

        /**
         * onPostExecute( ) will run on the UI thread after the background thread completes. Implement this method to suit your needs
         */
        public void onPostExecute() {
            ci.infoReady(drink);
        }

        /**
         * Get drink information data from web serivce on heroku.
         * @param searchTerm
         * @return
         */
        private Map getDrinkData(String searchTerm){
            Map drink = null;
            StringBuilder sb = new StringBuilder();
            JSONObject json;
            JSONParser parser = new JSONParser();
            String jsonText = "";
            Document doc;
            Element p;
            try {
                URL oracle = new URL("https://frozen-reef-96952.herokuapp.com/getCocktailInfo?searchWord="+searchTerm); // URL to Parse
                URLConnection yc = oracle.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                System.out.println("=========================================");
                doc = Jsoup.parse(sb.toString());
                p =  doc.selectFirst("p");
                jsonText = Objects.requireNonNull(p).text();
                System.out.println(jsonText);
                json = (JSONObject) parser.parse(jsonText);
                drink = json;
                URL url = new URL((String) drink.get("strDrinkThumb"));
                Bitmap bm = getRemoteImage(url);
                drink.put("strDrinkThumb", bm);
                in.close();
            } catch (FileNotFoundException e) { e.printStackTrace();
            } catch (IOException e) {e.printStackTrace();
            } catch (ParseException e) {e.printStackTrace();
            }
            return drink;
        }

        /*
         * Given a URL referring to an image, return a bitmap of that image
         */
        @RequiresApi(api = Build.VERSION_CODES.P)
        private Bitmap getRemoteImage(final URL url) {
            try {
                final URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                return bm;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

