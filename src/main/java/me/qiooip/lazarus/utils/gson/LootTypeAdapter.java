package me.qiooip.lazarus.utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.games.loot.LootData;
import me.qiooip.lazarus.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LootTypeAdapter implements JsonSerializer<List<LootData>>, JsonDeserializer<List<LootData>> {

    @Override
    public JsonElement serialize(List<LootData> list, Type type, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        JsonObject jsonObject;

        for(LootData loot : list) {
            jsonObject = context.serialize(loot, loot.getClass()).getAsJsonObject();
            jsonObject.addProperty("items", InventoryUtils.itemStackArrayToBase64(loot.getInventory().getContents()));

            array.add(jsonObject);
        }

        return array;
    }

    @Override
    public List<LootData> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();
        List<LootData> lootList = new ArrayList<>();

        LootData loot;
        ItemStack[] items;
        Inventory inventory;

        for(JsonElement element : array) {
            loot = context.deserialize(element.getAsJsonObject(), LootData.class);
            items = InventoryUtils.itemStackArrayFromBase64(element.getAsJsonObject().get("items").getAsString());

            if(items.length > Config.LOOT_INVENTORY_SIZE) {
                ItemStack[] subContents = new ItemStack[Config.LOOT_INVENTORY_SIZE];
                System.arraycopy(items, 0, subContents, 0, Config.LOOT_INVENTORY_SIZE);

                items = subContents;
            }

            inventory = Bukkit.createInventory(null, Config.LOOT_INVENTORY_SIZE,
                Config.LOOT_INVENTORY_NAME.replace("<event>", loot.getName()));

            inventory.setContents(items);

            loot.setInventory(inventory);
            loot.setItems(InventoryUtils.getItemsFromInventory(inventory));

            lootList.add(loot);
            this.connectKoth(loot);
        }

        return lootList;
    }

    private void connectKoth(LootData loot) {
        KothData koth = Lazarus.getInstance().getKothManager().getKoth(loot.getName());
        if(koth != null) koth.setLoot(loot);
    }
}
