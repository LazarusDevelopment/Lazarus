package me.qiooip.lazarus.utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.utils.GsonUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FactionTypeAdapter implements JsonSerializer<Map<UUID, Faction>>, JsonDeserializer<Map<UUID, Faction>> {

    @Override
    public JsonElement serialize(Map<UUID, Faction> map, Type type, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        JsonObject factionObject;

        for(Faction faction : map.values()) {
            factionObject = new JsonObject();

            factionObject.addProperty("type", GsonUtils.getFactionType(faction.getClass()));
            factionObject.add("data", context.serialize(faction, faction.getClass()));

            array.add(factionObject);
        }

        return array;
    }

    @Override
    public Map<UUID, Faction> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();
        Map<UUID, Faction> factionMap = new HashMap<>();

        Faction faction;
        Class<?> clazz;

        for(JsonElement element : array) {
            clazz = GsonUtils.getFactionClass(element.getAsJsonObject().get("type").getAsString());
            faction = context.deserialize(element.getAsJsonObject().get("data"), clazz);

            factionMap.put(faction.getId(), faction);
        }

        return factionMap;
    }
}
