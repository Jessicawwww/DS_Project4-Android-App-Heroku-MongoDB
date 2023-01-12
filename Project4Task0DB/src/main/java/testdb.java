import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.*;
import org.bson.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class testdb {
    public static void main( String[] args ) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb+srv://tianyiwang:oliverwang@cluster0.emjut.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("myFirstDatabase");
//            database.createCollection("students");
            String name,age,city;
            Scanner scan=new Scanner(System.in);
            System.out.println("Input a country code:");
            name=scan.nextLine();
            System.out.println("Input a start date:");
            age=scan.nextLine();
            System.out.println("Input a end date:");
            city=scan.nextLine();
            //Preparing a document
            Document document = new Document();
            document.append("Country", name);
            document.append("start_date", age);
            document.append("end_date", city);
            //Inserting the document into the collection
            database.getCollection("students").insertOne(document);
            for (String cName :database.listCollectionNames()){
                MongoCollection curr=database.getCollection(cName);
                FindIterable<Document> iterDoc = curr.find();
                Iterator it = iterDoc.iterator();
                while (it.hasNext()) {
                    System.out.println(it.next());
                }
            }
        }
    }
}
