package me.qiooip.lazarus.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

public class InventoryUtils {
    
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        if(items == null || items.length == 0) {
            return "";
        }

    	try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            
            dataOutput.writeInt(items.length);

            for(ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            return Base64Coder.encodeLines(outputStream.toByteArray());
    	} catch (IOException e) {
            throw new IllegalStateException("Unable to save items.", e);
        }
    }
    
    public static ItemStack[] itemStackArrayFromBase64(String data) {
        if(data == null || data.isEmpty()) {
            return new ItemStack[0];
        }

    	try(ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            ItemStack[] items = new ItemStack[dataInput.readInt()];
    
            for(int i = 0; i < items.length; i++) {
            	items[i] = (ItemStack) dataInput.readObject();
            }

            return items;
    	} catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Unable to load items.", e);
    	}
    }

    public static ItemStack[] getItemsFromInventory(Inventory inv) {
        return getRealItems(inv.getContents());
    }

    public static ItemStack[] getRealItems(ItemStack[] items) {
        return Stream.of(items).filter(item -> item != null && item.getType() != Material.AIR).toArray(ItemStack[]::new);
    }
    
    public static int getInventorySize(int listSize) {
    	if(listSize <= 9) {
    	    return 9;
        } else if(listSize <= 18) {
    	    return 18;
        } else if(listSize <= 27) {
    	    return 27;
        } else if(listSize <= 36) {
    	    return 36;
        } else if(listSize <= 45) {
    	    return 45;
        } else if(listSize <= 54) {
    	    return 54;
        } else {
    	    return 9;
        }
    }
}
