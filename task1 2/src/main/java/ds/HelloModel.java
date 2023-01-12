package ds;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.*;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Aggregates.*;

import static com.mongodb.client.model.Sorts.descending;

import javax.print.Doc;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import java.util.Scanner;

import static com.mongodb.client.model.Sorts.descending;

public class HelloModel {
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
