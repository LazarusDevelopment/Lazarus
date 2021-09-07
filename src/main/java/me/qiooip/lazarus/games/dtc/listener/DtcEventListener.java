package me.qiooip.lazarus.games.dtc.listener;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.games.dtc.event.DtcDestroyedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DtcEventListener implements Listener {

    public DtcEventListener() {
        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    @EventHandler
    public void onDtcDestroy(DtcDestroyedEvent event) {
        Player winner = event.getWinner();
        Lazarus.getInstance().getLootManager().getLootByName("DTC").handleRewards(winner);

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(winner);

        if(faction != null) {
            faction.incrementPoints(Config.FACTION_TOP_DTC_DESTROY);
        }
    }
}
