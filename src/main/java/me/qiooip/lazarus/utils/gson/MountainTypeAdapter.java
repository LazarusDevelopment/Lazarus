package me.qiooip.lazarus.utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.qiooip.lazarus.games.mountain.MountainData;
import org.bukkit.Bukkit;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MountainTypeAdapter implements JsonSerializer<List<MountainData>>, JsonDeserializer<List<MountainData>> {

    @Override
    public JsonElement serialize(List<MountainData> list, Type type, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        list.forEach(mountain -> array.add(context.serialize(mountain, mountain.getClass())));

        return array;
    }

    @Override
    public List<MountainData> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();
        List<MountainData> mountainList = new ArrayList<>();

        MountainData mountainData;
        String worldName;

        for(JsonElement element : array) {
            worldName = element.getAsJsonObject().get("cuboid").getAsJsonObject().get("worldName").getAsString();
            if(Bukkit.getWorld(worldName) == null) continue;

            mountainData = context.deserialize(element.getAsJsonObject(), MountainData.class);
            mountainList.add(mountainData);
        }

        return mountainList;
    }
}
