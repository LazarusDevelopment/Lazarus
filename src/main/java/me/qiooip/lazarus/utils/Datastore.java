package me.qiooip.lazarus.utils;

import me.qiooip.lazarus.Lazarus;

public enum Datastore {

    JSON, MONGO;

    public static Datastore DATASTORE;

    static {
        String value = Lazarus.getInstance().getConfig().getString("DATASTORE");

        try {
            DATASTORE = Datastore.valueOf(value);
        } catch(IllegalArgumentException e) {
            DATASTORE = JSON;
        }
    }
}
