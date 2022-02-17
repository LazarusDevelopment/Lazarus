package me.qiooip.lazarus.utils.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.qiooip.lazarus.handlers.restore.InventoryData;
import me.qiooip.lazarus.utils.InventoryUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class RestoreTypeAdapter implements JsonSerializer<InventoryData>, JsonDeserializer<InventoryData> {

    @Override
    public JsonElement serialize(InventoryData data, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("contents", InventoryUtils.itemStackArrayToBase64(data.getContents()));
        object.addProperty("armor", InventoryUtils.itemStackArrayToBase64(data.getArmor()));

        return object;
    }

    @Override
    public InventoryData deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        JsonElement contentsJson = object.get("contents");
        JsonElement armorJson = object.get("armor");

        String contentsString = contentsJson != null ? contentsJson.getAsString() : null;
        String armorString = armorJson != null ? armorJson.getAsString() : null;

        ItemStack[] contents = InventoryUtils.itemStackArrayFromBase64(contentsString);
        ItemStack[] armor = InventoryUtils.itemStackArrayFromBase64(armorString);

        return new InventoryData(contents, armor);
    }
}
