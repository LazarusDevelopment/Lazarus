package me.qiooip.lazarus.lunarclient.cooldown;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.LCCooldown;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.timer.Timer;
import me.qiooip.lazarus.timer.event.TimerActivateEvent;
import me.qiooip.lazarus.timer.event.TimerCancelEvent;
import me.qiooip.lazarus.timer.event.TimerExpireEvent;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager implements Listener {

    private final Map<CooldownType, LunarClientCooldown> cooldowns;

    public CooldownManager() {
        this.cooldowns = new HashMap<>();

        this.setupCooldowns();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
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

    private void addCooldown(UUID uuid, CooldownType type, long duration) {
        if(!Lazarus.getInstance().getLunarClientManager().getPlayers().contains(uuid)) return;
        if(!this.cooldowns.containsKey(type)) return;

        this.sendCooldown(uuid, this.cooldowns.get(type).createCooldown(duration));
    }

    private void removeCooldown(UUID uuid, CooldownType type) {
        if(!Lazarus.getInstance().getLunarClientManager().getPlayers().contains(uuid)) return;
        if(!this.cooldowns.containsKey(type)) return;

        this.sendCooldown(uuid, this.cooldowns.get(type).createCooldown(0L));
    }

    private void sendCooldown(UUID uuid, LCCooldown cooldown) {
        LunarClientAPI.getInstance().sendCooldown(Bukkit.getPlayer(uuid), cooldown);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTimerActivate(TimerActivateEvent event) {
        CooldownType type = this.getCooldown(event.getTimer());
        if(type == null) return;

        this.addCooldown(event.getUuid(), type, event.getDelay());
    }

    @EventHandler
    public void onTimerCancel(TimerCancelEvent event) {
        CooldownType type = this.getCooldown(event.getTimer());
        if(type == null) return;

        this.removeCooldown(event.getUuid(), type);
    }

    @EventHandler
    public void onTimerExpire(TimerExpireEvent event) {
        CooldownType type = this.getCooldown(event.getTimer());
        if(type == null) return;

        this.removeCooldown(event.getUuid(), type);
    }

    private CooldownType getCooldown(Timer timer) {
        return !(timer instanceof PlayerTimer) ? null : timer.getLunarCooldownType();
    }
}
