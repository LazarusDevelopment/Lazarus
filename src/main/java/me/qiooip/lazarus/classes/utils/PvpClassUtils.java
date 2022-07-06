package me.qiooip.lazarus.classes.utils;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.items.BardClickableItem;
import me.qiooip.lazarus.classes.items.BardHoldableItem;
import me.qiooip.lazarus.classes.items.BomberTntGun;
import me.qiooip.lazarus.classes.items.ClickableItem;
import me.qiooip.lazarus.classes.items.MageClickableItem;
import me.qiooip.lazarus.classes.manager.PvpClass;
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

    public static List<MageClickableItem> loadMageClickableItems() {
        List<MageClickableItem> bardItems = new ArrayList<>();

        String sectionName = "MAGE_CLASS.CLICKABLE_ITEMS";
        ConfigurationSection potionSection = Lazarus.getInstance().getClassesFile().getSection(sectionName);

        potionSection.getKeys(false).forEach(potion -> {

            MageClickableItem mageItem = new MageClickableItem();

            ItemStack itemStack = ItemUtils.parseItem(potionSection.getString(potion + ".MATERIAL_ID"));
            if(itemStack == null) return;

            mageItem.setCooldown(0);
            mageItem.setItem(itemStack);
            mageItem.setCooldown(potionSection.getInt(potion + ".COOLDOWN"));
            mageItem.setApplyToHimself(potionSection.getBoolean(potion + ".APPLY_TO_HIMSELF"));
            mageItem.setEnergyNeeded(potionSection.getInt(potion + ".ENERGY_NEEDED"));
            mageItem.setChatColor(Color.translate(potionSection.getString(potion + ".CHAT_COLOR", "&b")));
            mageItem.setPotionEffect(new PotionEffect(PotionEffectType.getByName(potion), potionSection
                .getInt(potion + ".DURATION") * 20, potionSection.getInt(potion + ".LEVEL") - 1));

            int distance = potionSection.getInt(potion + ".DISTANCE");

            mageItem.setDistance(distance);
            mageItem.setDistanceSquared(distance * distance);

            bardItems.add(mageItem);
        });

        return bardItems;
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
            bardItem.setCooldown(potionSection.getInt(potion + ".COOLDOWN"));
            bardItem.setCanBardHimself(potionSection.getBoolean(potion + ".CAN_BARD_HIMSELF"));
            bardItem.setEnergyNeeded(potionSection.getInt(potion + ".ENERGY_NEEDED"));
            bardItem.setChatColor(Color.translate(potionSection.getString(potion + ".CHAT_COLOR", "&b")));
            bardItem.setPotionEffect(new PotionEffect(PotionEffectType.getByName(potion), potionSection
                .getInt(potion + ".DURATION") * 20, potionSection.getInt(potion + ".LEVEL") - 1));

            int distance = potionSection.getInt(potion + ".DISTANCE");

            bardItem.setDistance(distance);
            bardItem.setDistanceSquared(distance * distance);

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
            bardItem.setPotionEffect(new PotionEffect(PotionEffectType.getByName(potion), potionSection
                .getInt(potion + ".DURATION") * 20, potionSection.getInt(potion + ".LEVEL") - 1));

            int distance = potionSection.getInt(potion + ".DISTANCE");
            bardItem.setDistanceSquared(distance * distance);

            bardItems.add(bardItem);
        });

        return bardItems;
    }

    public static BomberTntGun loadBomberTntGun() {
        String sectionName = "BOMBER_CLASS.TNT_GUN";
        ConfigurationSection section = Lazarus.getInstance().getClassesFile().getSection(sectionName);

        ItemStack itemStack = ItemUtils.parseItem(section.getString("MATERIAL_ID"));

        if(itemStack == null) {
            Lazarus.getInstance().log("&cBomber TNT gun failed to load!");
            return null;
        }

        return new BomberTntGun()
            .setItem(itemStack)
            .setCooldown(section.getInt("COOLDOWN"))
            .setFuseTicks(section.getInt("FUSE_TICKS"))
            .setTntVelocity(section.getDouble("TNT_VELOCITY"))
            .setKbMaxY(section.getDouble("KNOCKBACK.MAX_Y_VELOCITY"))
            .setKbYMultiplier(section.getDouble("KNOCKBACK.Y_MULTIPLIER"));
    }
}
