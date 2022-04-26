package me.qiooip.lazarus.handlers.kitmap;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KillstreakHandler extends Handler implements Listener {

    private final Map<Integer, List<String>> killstreakRewards;

    public KillstreakHandler() {
        this.killstreakRewards = new HashMap<>();
        this.loadKillstreakRewards();
    }

    @Override
    public void disable() {
        this.killstreakRewards.clear();
    }

    private void loadKillstreakRewards() {
        ConfigurationSection section = Lazarus.getInstance().getConfig().getSection("KITMAP_KILLSTREAK.KILLS");

        section.getKeys(false).forEach(killCount -> {
            if(!StringUtils.isInteger(killCount)) return;

            this.killstreakRewards.put(Integer.parseInt(killCount), section.getStringList(killCount));
        });
    }

    public void checkKillerKillstreak(Player killer) {
        if(!Config.KITMAP_MODE_ENABLED || !Config.KITMAP_KILLSTREAK_ENABLED) return;

        Userdata killerUserdata = Lazarus.getInstance().getUserdataManager().getUserdata(killer);
        killerUserdata.addKillstreak();

        List<String> commands = this.killstreakRewards.get(killerUserdata.getKillstreak());
        if(commands == null) return;

        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
            command.replace("<player>", killer.getName())));

        Messages.sendMessage(Language.KITMAP_KILLSTREAK_MESSAGE
            .replace("<player>", killer.getName())
            .replace("<amount>", String.valueOf(killerUserdata.getKillstreak())));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(!Config.KITMAP_MODE_ENABLED || !Config.KITMAP_KILLSTREAK_ENABLED) return;

        Player victim = event.getEntity();
        int killstreak = Lazarus.getInstance().getUserdataManager().getUserdata(victim).resetKillstreak();

        if(killstreak > 0) {
            victim.sendMessage(Language.PREFIX + Language.KITMAP_KILLSTREAK_ON_DEATH
                .replace("<amount>", String.valueOf(killstreak)));
        }

        Player killer = victim.getKiller();

        if(killer != null && victim != killer) {
            this.checkKillerKillstreak(killer);
        }
    }
}
