package me.qiooip.lazarus.tab.module.impl;

import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.module.ParametrizedTabModule;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerListModule extends ParametrizedTabModule<PlayerFaction> {

    private static final Comparator<FactionPlayer> COMPARATOR
        = (m1, m2) -> m2.getRole().ordinal() - m1.getRole().ordinal();

    private int numberOfPlayers;
    private String factionColor;
    private String playerColor;

    public PlayerListModule() {
        super("PLAYER_LIST_MODULE");
    }

    @Override
    public void loadAdditionalData(ConfigurationSection section) {
        this.numberOfPlayers = section.getInt("NUMBER_OF_PLAYERS");
        this.factionColor = section.getString("FACTION_NAME_COLOR");
        this.playerColor = section.getString("PLAYER_COLOR");
    }

    @Override
    public void apply(PlayerTab tab) {
        this.apply(tab, FactionsManager.getInstance().getPlayerFaction(tab.getPlayer()));
    }

    @Override
    public void apply(PlayerTab tab, PlayerFaction faction) {
        this.clearFactionPlayerList(tab);

        tab.set(this.startSlot, this.factionColor + faction.getName());
        AtomicInteger count = new AtomicInteger(this.startSlot + 1);

        faction.getMembers().values().stream()
            .filter(member -> member.getPlayer() != null)
            .sorted(COMPARATOR).limit(this.numberOfPlayers)
        .forEach(member ->
            tab.set(count.getAndIncrement(), this.playerColor + member.getRole().getPrefix() + member.getName())
        );
    }

    public void clearFactionPlayerList(PlayerTab tab) {
        if(tab == null) return;

        for(int i = this.startSlot; i <= this.startSlot + this.numberOfPlayers + 1; i++) {
            tab.set(i, "");
        }
    }
}
