package me.qiooip.lazarus.handlers.salvage;

import com.google.common.collect.Iterables;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

public class SalvageHandler extends Handler implements Listener {

    public void handleSalvage(Player player) {
        ItemStack handItem = player.getItemInHand();
        ItemStack recipeReference = new ItemBuilder(handItem.clone()).setDurability(0).build();

        ShapedRecipe recipe = (ShapedRecipe) Bukkit.getRecipesFor(recipeReference).stream()
            .filter(r -> r instanceof ShapedRecipe).findFirst().orElse(null);

        if(recipe == null) return;

        ItemUtils.removeOneItem(player);

        double salvagePercentage = (double) (handItem.getType().getMaxDurability()
            - handItem.getDurability()) / handItem.getType().getMaxDurability();

        Collection<ItemStack> ingredients = recipe.getIngredientMap().values();
        Map<Material, Integer> toAdd = new EnumMap<>(Material.class);

        ingredients.stream().filter(Objects::nonNull).forEach(ingredient -> toAdd.put(ingredient
            .getType(), toAdd.getOrDefault(ingredient.getType(), 0) + ingredient.getAmount()));

        StringJoiner message = new StringJoiner("\n");

        toAdd.forEach((material, amount) -> {
            int roundedAmount = (int) Math.round(Math.max(1, amount * salvagePercentage));
            ItemStack itemStack = new ItemBuilder(material, roundedAmount).build();

            PlayerUtils.addToInventoryOrDropToFloor(player, itemStack);

            message.add(Language.SALVAGE_INGREDIENT_FORMAT
                .replace("<amount>", String.valueOf(itemStack.getAmount()))
                .replace("<material>", ItemUtils.getItemName(itemStack)));
        });

        this.dropRandomEnchantedBook(player, handItem, message);

        player.sendMessage(Language.SALVAGE_SALVAGED.replace("<item>", ItemUtils.getItemName(handItem))
            .replace("<durability>", String.valueOf((double) Math.round(salvagePercentage * 100 * 100d) / 100d)));

        player.sendMessage(message.toString());
    }

    private void dropRandomEnchantedBook(Player player, ItemStack handItem, StringJoiner message) {
        Set<Entry<Enchantment, Integer>> enchantments = handItem.getEnchantments().entrySet();
        if(enchantments.isEmpty()) return;

        int randomEntry = ThreadLocalRandom.current().nextInt(enchantments.size());
        Entry<Enchantment, Integer> enchantment = Iterables.get(enchantments, randomEntry);

        ItemStack bookItemStack = new ItemBuilder(Material.ENCHANTED_BOOK)
            .addStoredEnchantment(enchantment.getKey(), enchantment.getValue()).build();

        PlayerUtils.addToInventoryOrDropToFloor(player, bookItemStack);

        message.add(Language.SALVAGE_INGREDIENT_FORMAT
            .replace("<amount>", "1")
            .replace("<material>", ItemUtils.getItemName(bookItemStack)));
    }
}
