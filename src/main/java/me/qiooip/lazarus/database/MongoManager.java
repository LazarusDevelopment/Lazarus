package me.qiooip.lazarus.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class MongoManager {

    private final MongoClient mongo;
    private MongoDatabase database;

    private final MongoCollection<Document> claimsRepo;
    private final MongoCollection<Document> factionsRepo;
    private final MongoCollection<Document> playersRepo;
    private final MongoCollection<Document> userdataRepo;

    public MongoManager() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        MongoClientSettings.Builder settings = MongoClientSettings.builder();
        settings.applyConnectionString(MongoConfig.getConnectionString());
        settings.applyToConnectionPoolSettings(builder -> builder.maxConnectionIdleTime(60, TimeUnit.SECONDS));
        settings.retryWrites(true);

        if(MongoConfig.AUTH_ENABLED && !MongoConfig.USE_CONNECTION_STRING) {
            settings.credential(MongoConfig.getCredentials());
        }

        this.mongo = MongoClients.create(settings.build());
        MongoDatabase database = this.mongo.getDatabase(MongoConfig.DATABASE_NAME);

        this.claimsRepo = database.getCollection("claims");
        this.factionsRepo = database.getCollection("factions");
        this.playersRepo = database.getCollection("players");
        this.userdataRepo = database.getCollection("userdata");
    }

    public void disable() {
        this.mongo.close();
    }
}
