package com.graos.auditory_scanning_final_project;

import android.provider.DocumentsContract.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MongoDBJDBC {
    private MongoClient mongoClient;
    private DB db;
    private boolean auth; // true - if connaction successfully ,else false if not
    public MongoDBJDBC(){
        MongoClient mongoClient = null;
        try {
            /*mongoClient = new MongoClient("localhost",27017);
            DB db = mongoClient.getDB("patients_db");
            auth = db.authenticate("yerson28890@gmail.com", "auditoryMongo1!".toCharArray());*/

            MongoClientURI uri  = new MongoClientURI("mongodb://yerson28890:auditoryMongo1!@ds133398.mlab.com:33398/patients_db");
            MongoClient client = new MongoClient(uri);
            DB db = client.getDB(uri.getDatabase());

            /* source link: http://stackoverflow.com/questions/42708540/android-studio-connecting-to-mongodb-server-with-mongo-java-driver
            MongoCollection<Document> coll = db.getCollection("newDB");
            Document doc = new Document("username", username);
            coll.insertOne(doc);
            client.close();
            */

        } catch (UnknownHostException e) {
        }
    }
    public DB getDB(){
        return db;
    }
    public boolean getAuth(){
        return auth;
    }
}
