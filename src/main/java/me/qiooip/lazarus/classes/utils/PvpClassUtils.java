package me.qiooip.lazarus.classes.utils;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.items.BardClickableItem;
import me.qiooip.lazarus.classes.items.BardHoldableItem;
import me.qiooip.lazarus.classes.items.ClickableItem;
import me.qiooip.lazarus.classes.manager.PvpClass;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PvpClassUtils {

    public static List<PotionEffect> loadPassiveEffects(PvpClass pvpClass, String value) {
        List<PotionEffect> effects = new ArrayList<>();

        String sectionName = pvpClass.getName().toUpperCase() + "_CLASS." + value;
        ConfigurationSection section = Lazarus.getInstance().getClassesFile().getSection(sectionName);

        section.getKeys(false).forEach(potion -> {
            if(PotionEffectType.getByName(potion) == null) return;

            effects.add(new PotionEffect(PotionEffectType.getByName(potion),
            Integer.MAX_VALUE, section.getInt(potion) - 1));
        });

        return effects;
    }

    public static List<ClickableItem> loadClickableItems(PvpClass pvpClass) {
        List<ClickableItem> clickables = new ArrayList<>();

        String sectionName = pvpClass.getName().toUpperCase() + "_CLASS.CLICKABLE_POTION_EFFECTS";
        ConfigurationSection potionSection = Lazarus.getInstance().getClassesFile().getSection(sectionName);

        potionSection.getKeys(false).forEach(potion -> {
            if(PotionEffectType.getByName(potion) == null) return;

            ClickableItem clickable = new ClickableItem();

            ItemStack itemStack = ItemUtils.parseItem(potionSection.getString(potion + ".MATERIAL_ID"));
            if(itemStack == null) return;

            clickable.setItem(itemStack);
            clickable.setCooldown(potionSection.getInt(potion + ".COOLDOWN"));
            clickable.setPotionEffect(new PotionEffect(PotionEffectType.getByName(potion), potionSection
            .getInt(potion + ".DURATION") * 20, potionSection.getInt(potion + ".LEVEL") - 1));

            clickables.add(clickable);
        });

        return clickables;
    }

    public static List<BardClickableItem> loadBardClickableItems() {
        List<BardClickableItem> bardItems = new ArrayList<>();

        String sectionName = "BARD_CLASS.CLICKABLE_ITEMS";
        ConfigurationSection potionSection = Lazarus.getInstance().getClassesFile().getSection(sectionName);

        potionSection.getKeys(false).forEach(potion -> {

            BardClickableItem bardItem = new BardClickableItem();

            ItemStack itemStack = ItemUtils.parseItem(potionSection.getString(potion + ".MATERIAL_ID"));
            if(itemStack == null) return;

            bardItem.setCooldown(0);
            bardItem.setItem(itemStack);
            bardItem.setApplyToEnemy(potionSection.getBoolean(potion + ".APPLY_TO_ENEMY"));
            bardItem.setDistance(potionSection.getInt(potion + ".DISTANCE"));
            bardItem.setCooldown(potionSection.getInt(potion + ".COOLDOWN"));
            bardItem.setCanBardHimself(potionSection.getBoolean(potion + ".CAN_BARD_HIMSELF"));
            bardItem.setEnergyNeeded(potionSection.getInt(potion + ".ENERGY_NEEDED"));
            bardItem.setChatColor(Color.translate(potionSection.getString(potion + ".CHAT_COLOR", "&b")));
            bardItem.setPotionEffect(new PotionEffect(PotionEffectType.getByName(potion), potionSection
            .getInt(potion + ".DURATION") * 20, potionSection.getInt(potion + ".LEVEL") - 1));

            bardItems.add(bardItem);
        });

        return bardItems;
    }

    public static List<BardHoldableItem> loadBardHoldableItems() {
        List<BardHoldableItem> bardItems = new ArrayList<>();

        String sectionName = "BARD_CLASS.HOLDABLE_ITEMS";
        ConfigurationSection potionSection = Lazarus.getInstance().getClassesFile().getSection(sectionName);

        potionSection.getKeys(false).forEach(potion -> {

            BardHoldableItem bardItem = new BardHoldableItem();

            ItemStack itemStack = ItemUtils.parseItem(potionSection.getString(potion + ".MATERIAL_ID"));
            if(itemStack == null) return;

            bardItem.setCooldown(0);
            bardItem.setItem(itemStack);
            bardItem.setCanBardHimself(potionSection.getBoolean(potion + ".CAN_BARD_HIMSELF"));
            bardItem.setDistance(potionSection.getInt(potion + ".DISTANCE"));
            bardItem.setPotionEffect(new PotionEffect(PotionEffectType.getByName(potion), potionSection
            .getInt(potion + ".DURATION") * 20, potionSection.getInt(potion + ".LEVEL") - 1));

            bardItems.add(bardItem);
        });

        return bardItems;
    }

    public static int getWarmup(String name) {
        switch(name) {
            case "Archer": return Config.ARCHER_WARMUP;
            case "Bard": return Config.BARD_WARMUP;
            case "Miner": return Config.MINER_WARMUP;
            case "Rogue": return Config.ROGUE_WARMUP;
            default: return 0;
        }
    }
}
