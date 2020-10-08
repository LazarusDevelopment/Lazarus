package me.qiooip.lazarus.utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.qiooip.lazarus.factions.FactionPlayer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerTypeAdapter implements JsonSerializer<Map<UUID, FactionPlayer>>, JsonDeserializer<Map<UUID, FactionPlayer>> {

    @Override
    public JsonElement serialize(Map<UUID, FactionPlayer> map, Type type, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        map.values().forEach(player -> array.add(context.serialize(player, player.getClass())));

        return array;
    }

    @Override
    public Map<UUID, FactionPlayer> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();
        Map<UUID, FactionPlayer> playerMap = new HashMap<>();

        FactionPlayer fplayer;

        for(JsonElement element : array) {
            fplayer = context.deserialize(element.getAsJsonObject(), FactionPlayer.class);
            playerMap.put(fplayer.getUuid(), fplayer);
        }

        return playerMap;
    }
}
