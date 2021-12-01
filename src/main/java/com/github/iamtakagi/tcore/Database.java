package com.github.iamtakagi.tcore;

import com.github.iamtakagi.tcore.io.file.type.BasicConfigurationFile;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import lombok.Getter;
import org.bson.Document;


public class Database {

    private MongoDatabase database;
    @Getter
    private MongoCollection<Document> profiles;
    @Getter
    private MongoCollection<Document> ranks;
    @Getter
    private MongoCollection<Document> clans;

    public Database(BasicConfigurationFile mainConfig) {
        if (mainConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
            ServerAddress serverAddress = new ServerAddress(mainConfig.getString("MONGO.HOST"),
                    mainConfig.getInteger("MONGO.PORT"));

            MongoCredential credential = MongoCredential.createCredential(
                    mainConfig.getString("MONGO.AUTHENTICATION.USERNAME"), "admin",
                    mainConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray());

            database = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build())
                    .getDatabase("tCore");
        } else {
            database = new MongoClient(mainConfig.getString("MONGO.HOST"),
                    mainConfig.getInteger("MONGO.PORT")).getDatabase("tCore");
        }

        this.profiles = this.database.getCollection("profiles");
        this.ranks = this.database.getCollection("ranks");
        this.clans = this.database.getCollection("clans");
    }
}