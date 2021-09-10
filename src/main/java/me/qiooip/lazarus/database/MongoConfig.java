package me.qiooip.lazarus.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoCredential;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.ConfigFile;

class MongoConfig {

    static final String DATABASE_NAME;
    static final boolean AUTH_ENABLED;

    static final boolean USE_CONNECTION_STRING;
    private static final String CONNECTION_STRING;

    private static final String SERVER_IP;
    private static final int SERVER_PORT;
    private static final String USER;
    private static final String PASSWORD;

    static {
        ConfigFile config = Lazarus.getInstance().getConfig();

        DATABASE_NAME = config.getString("MONGO.DATABASE_NAME");
        AUTH_ENABLED = config.getBoolean("MONGO.AUTH.ENABLED");

        USE_CONNECTION_STRING = config.getBoolean("MONGO.USE_CONNECTION_STRING");
        CONNECTION_STRING = config.getString("MONGO.CONNECTION_STRING");

        SERVER_IP = config.getString("MONGO.SERVER_IP");
        SERVER_PORT = config.getInt("MONGO.SERVER_PORT");
        USER = config.getString("MONGO.AUTH.USER");
        PASSWORD = config.getString("MONGO.AUTH.PASSWORD");
    }

    static ConnectionString getConnectionString() {
        if(USE_CONNECTION_STRING) {
            return new ConnectionString(CONNECTION_STRING);
        } else {
            return new ConnectionString("mongodb://" + SERVER_IP + ":" + SERVER_PORT);
        }
    }

    static MongoCredential getCredentials() {
        return MongoCredential.createCredential(USER, DATABASE_NAME, PASSWORD.toCharArray());
    }
}
