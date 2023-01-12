package ds;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ds.CocktailModel;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the controller part of our web service.
 * @author Olivia(Jingyi Wu), Keqing Xing
 * Andrew id: jingyiw2, kxing
 */

@WebServlet(name = "CocktailServlet",urlPatterns = {"/getCocktailInfo"})
public class CocktailServlet extends HttpServlet {
    CocktailModel cm = null;
    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        cm = new CocktailModel();
    }

    /**
     * This servlet will reply to HTTP GET requests via this doGet method
     * @param request an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String search = request.getParameter("searchWord");
        String cocktailURL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + search;
        String start = String.valueOf(new Timestamp(System.currentTimeMillis()));
        long starttime = System.currentTimeMillis();
        // determine what type of device our user is
        String ua = request.getHeader("User-Agent");
        System.out.println("=================================");
        System.out.println(ua);
        Pattern pattern = Pattern.compile( ";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/" );
        Matcher matcher = pattern.matcher(ua);
        String model = "" ;
        if (matcher.find()) {
            model = matcher.group( 1 ).trim();
        }
        System.out.println("device here is "+model);
        boolean mobile;
        // prepare the appropriate DOCTYPE for the view pages
        if (ua != null && ((ua.indexOf("Android") != -1) || (ua.indexOf("iPhone") != -1))) {
            mobile = true;
            request.setAttribute("doctype", "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">");
        } else {
            mobile = false;
            request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        }
        System.out.println("search work is "+search);
        System.out.println("start fetch data using api");

        String nextView;
        String drinks = "";
        /*
         * Check if the search parameter is present.
         * If not, then give the user instructions and prompt for a search string.
         * If there is a search parameter, then do the search and return the result.
         */
        if (search != null) {
            String picSize = (mobile) ? "mobile" : "desktop";
            try {
                drinks = cm.doCocktailSearch(search, picSize); //return all information about cocktail
                System.out.println("finished search.");
            } catch (ParseException e) {
                System.out.println("servlet failed here.");
//                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                System.out.println("Unsupported Encoding Exception here.");
//                throw new RuntimeException(e);
            }
            /*
             * Attributes on the request object can be used to pass data to
             * the view.  These attributes are name/value pairs, where the name
             * is a String object.  Here the pictureURL is passed to the view
             * after it is returned from the model interestingPictureSize method.
             */

            System.out.println(drinks);
            request.setAttribute("data",drinks);
            String end = String.valueOf(new Timestamp(System.currentTimeMillis()));
            long endtime = System.currentTimeMillis();
            long latency = endtime - starttime;
            //write into mongodb
            // connect to mongodb
            String uri = "mongodb+srv://keqingxing:kq123456@cluster0.od4bwm4.mongodb.net/?retryWrites=true&w=majority";
            String status = drinks.equals("Oops! No result found. Invalid Input here.")? "failure":"success";
            try (MongoClient mongoClient = MongoClients.create(uri)) {
                MongoDatabase database = mongoClient.getDatabase("cocktailSearch");
                MongoCollection<Document> collection=database.getCollection("cocktail");

                Document doc = new Document();
                doc.put("searchWord",search);
                doc.put("user-agent",ua);
                doc.put("device model", model);
                doc.put("request time",start);
                doc.put("response time", end);
                doc.put("latency", latency);
                doc.put("response status", status);
                doc.put("request from app", request);
                doc.put("request to 3rd party API", cocktailURL);
                doc.put("response from 3rd party API", drinks);
                collection.insertOne(doc);
            }

                nextView = "result.jsp";
        } else {
            // no search parameter so choose the prompt view
            System.out.println("display prompt jsp");
            nextView = "prompt.jsp";
        }
        // Transfer control over the correct "view"
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        try {
            view.forward(request, response);
        } catch (ServletException e) {
            System.out.println("servlet failed here.");
//            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Unsupported Encoding Exception here.");
//            throw new RuntimeException(e);
        }
    }
}