package me.qiooip.lazarus.utils.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.qiooip.lazarus.utils.LocationUtils;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class LocationTypeAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(LocationUtils.locationToString(location));
    }

    @Override
    public Location deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return LocationUtils.stringToLocation(json.getAsString());
    }
}
