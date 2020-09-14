package me.qiooip.lazarus.lunarclient.cooldown;

import com.lunarclient.bukkitapi.LunarClientAPI;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.lunarclient.LunarClientManager;
import me.qiooip.lazarus.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<CooldownType, LunarClientCooldown> cooldowns;

    public CooldownManager() {
        this.cooldowns = new HashMap<>();

        this.setupCooldowns();
    }

    public void disable() {
        this.cooldowns.clear();
    }

    private void setupCooldowns() {
        ConfigurationSection section = Lazarus.getInstance().getConfig().getConfigurationSection("COOLDOWNS");

        section.getKeys(false).forEach(cooldown -> {
            LunarClientCooldown lunarClientCooldown = new LunarClientCooldown();
            lunarClientCooldown.setName(section.getString(cooldown + ".NAME"));

            ItemStack itemStack = ItemUtils.parseItem(section.getString(cooldown + ".MATERIAL_ID"));
            if(itemStack == null) return;

            lunarClientCooldown.setMaterial(itemStack.getType());

            CooldownType type = CooldownType.valueOf(cooldown);
            this.cooldowns.put(type, lunarClientCooldown);
        });
    }

    public void addCooldown(UUID uuid, CooldownType type, long duration) {
        if(!LunarClientManager.getInstance().getPlayers().contains(uuid)) return;
        if(!this.cooldowns.containsKey(type)) return;

        LunarClientAPI.getInstance().sendCooldown(Bukkit.getPlayer(uuid), this.cooldowns.get(type).createCooldown(duration));
    }

    public void removeCooldown(UUID uuid, CooldownType type) {
        if(!LunarClientManager.getInstance().getPlayers().contains(uuid)) return;
        if(!this.cooldowns.containsKey(type)) return;

        LunarClientAPI.getInstance().sendCooldown(Bukkit.getPlayer(uuid), this.cooldowns.get(type).createCooldown(0L));
    }
}
