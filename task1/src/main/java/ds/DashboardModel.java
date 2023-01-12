package ds;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Sorts.descending;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is model part of getting all required information for dashboard.
 * @author Olivia Wu, Keqing Xing
 * andrew id: jingyiw2, kxing
 */
public class DashboardModel {
    /**
     * Get top search cocktail names based on search history.
     * @return search terms and their frequency
     */
    ArrayList<Document> getTopSearch(){
        // connect to mongodb
        String uri = "mongodb+srv://keqingxing:kq123456@cluster0.od4bwm4.mongodb.net/?retryWrites=true&w=majority";

        try (MongoClient mongoClient = MongoClients.create(uri)) {

            //create database
            MongoDatabase database = mongoClient.getDatabase("cocktailSearch");
            MongoCollection<Document> collection=database.getCollection("cocktail");

            Bson group = group("$searchWord", Accumulators.sum("count", 1));
            Bson sort = sort(descending("count"));
            Bson limit = limit(10);

            ArrayList<Document> results = collection.aggregate(Arrays.asList( group, sort, limit))
                    .into(new ArrayList<Document>());

            return results;

        }
    }

    /**
     * Get all log information from database.
     * @return log information array list.
     */
    ArrayList<Document> getFullLog(){
        // connect to mongodb
        String uri = "mongodb+srv://keqingxing:kq123456@cluster0.od4bwm4.mongodb.net/?retryWrites=true&w=majority";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("cocktailSearch");
            MongoCollection<Document> collection=database.getCollection("cocktail");
            ArrayList<Document> results = collection.find().into(new ArrayList<Document>());
            return results;
        }
    }

    /**
     * Compute average response time for our web service.
     * @return average latency time.
     */
    long getAvgLatency(){
        // connect to mongodb
        String uri = "mongodb+srv://keqingxing:kq123456@cluster0.od4bwm4.mongodb.net/?retryWrites=true&w=majority";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("cocktailSearch");
            MongoCollection<Document> collection=database.getCollection("cocktail");
//            Bson group = group("", Accumulators.avg("avgLatency","$latency"));
//
//            Document results = collection.aggregate(Arrays.asList(group))
//                    .into(new ArrayList<Document>()).get(0);
            ArrayList<Document> results = collection.find().into(new ArrayList<Document>());
            long sum= 0;
            int count=0;
            for(Document d:results){
                sum+=(long)d.get("latency");
                count++;
            }
            long result=sum/count;
            return result;
        }
    }

    /**
     * Get what type of device is most frequently used by our users.
     * @return all device models and their frequency.
     */
    ArrayList<Document> getTopDevice(){
        // connect to mongodb
        String uri = "mongodb+srv://keqingxing:kq123456@cluster0.od4bwm4.mongodb.net/?retryWrites=true&w=majority";

        try (MongoClient mongoClient = MongoClients.create(uri)) {

            //create database
            MongoDatabase database = mongoClient.getDatabase("cocktailSearch");
            MongoCollection<Document> collection=database.getCollection("cocktail");

            Bson group = group("$device model", Accumulators.sum("count", 1));
            Bson sort = sort(descending("count"));
            Bson limit = limit(10);

            ArrayList<Document> results = collection.aggregate(Arrays.asList( group, sort, limit))
                    .into(new ArrayList<Document>());

            return results;

        }
    }




}
