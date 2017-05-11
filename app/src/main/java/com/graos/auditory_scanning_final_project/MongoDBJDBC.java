package com.graos.auditory_scanning_final_project;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import java.util.ArrayList;
import java.util.List;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

public class MongoDBJDBC {
    private MongoClient mongoClient;
    private MongoDatabase db;
    private boolean auth; // true - if connaction successfully ,else false if not
    public MongoDBJDBC(){
        MongoClient mongoClient = null;
        /*mongoClient = new MongoClient("localhost",27017);
        DB db = mongoClient.getDB("patients_db");
        auth = db.authenticate("yerson28890@gmail.com", "auditoryMongo1!".toCharArray());*/

        MongoClientURI uri  = new MongoClientURI("mongodb://yerson28890:auditoryMongo1!@ds133398.mlab.com:33398/patients_db");
        MongoClient client = new MongoClient(uri);
        db = client.getDatabase(uri.getDatabase());

        //us table names (collections):
            /*
                optionals_requests_of_patient
                patients
                statistics_of_patient
                therapists
            */

            /* source link: http://stackoverflow.com/questions/42708540/android-studio-connecting-to-mongodb-server-with-mongo-java-driver
            MongoCollection<Document> coll = db.getCollection("newDB");
            Document doc = new Document("username", username);
            coll.insertOne(doc);
            client.close();
            */

    }
    public MongoDatabase getDB(){
        return db;
    }
    public boolean getAuth(){
        return auth;
    }

    //https://docs.mongodb.com/manual/reference/operator/query/
    public BasicDBObject queryBuilder(ArrayList<String> where_columns, ArrayList<String> where_operators, ArrayList<Object> where_values) {
        if(where_columns.size() != where_operators.size() || where_columns.size() != where_values.size())
            return null;
        BasicDBObject Query = new BasicDBObject();
        for (int i=0;i<where_columns.size();i++) {
            Object o = where_values.get(i);
            if (o instanceof Integer) {
                Query.put(where_columns.get(i), new BasicDBObject(where_operators.get(i), (Integer) o));
            }else if (o instanceof String){
                Query.put(where_columns.get(i), new BasicDBObject(where_operators.get(i), (String)o));
            }else if (o instanceof Date){
                Query.put(where_columns.get(i), new BasicDBObject(where_operators.get(i), (Date)o));
            }
        }
        return Query;
    }

    /*
    //http://www.unityjdbc.com/doc/mongo/mongo_java_jdbc.php
    public FindIterable<Document> selectQuery(String collection_name, ArrayList<String> where_columns, ArrayList<String> where_operators, ArrayList<Object> where_values) {
        MongoCollection coll = db.getCollection(collection_name);
        if(coll==null)
            return null;
        BasicDBObject Query = queryBuilder(where_columns,where_operators,where_values);
        return coll.find(Query);
    }
    */

}
