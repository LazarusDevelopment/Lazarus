package me.qiooip.lazarus.tab.module.impl;

import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.module.TabModule;
import org.bukkit.configuration.ConfigurationSection;

public class FactionInfoModule extends TabModule {

    private String[][] factionInfo;

    public FactionInfoModule() {
        super("FACTION_INFO_MODULE");
    }

    @Override
    public void loadAdditionalData(ConfigurationSection section) {
        this.factionInfo = new String[2][2];

        this.factionInfo[0] = section.getStringList("NO_FACTION").toArray(new String[2]);
        this.factionInfo[1] = section.getStringList("IN_FACTION").toArray(new String[2]);

        for(int i = 0; i < 2; i++) {
            this.factionInfo[1][i] = this.factionInfo[1][i].replace("<dtr>", "").replace("<home>", "");
        }
    }

    @Override
    public void apply(PlayerTab tab) {
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(tab.getPlayer());

        if(faction == null) {
            tab.set(this.startSlot, this.factionInfo[0][0]);
            tab.set(this.startSlot + 1, this.factionInfo[0][1]);
            return;
        }

        tab.set(this.startSlot, this.factionInfo[1][0] + faction.getDtrString() + "&7/" + faction.getMaxDtrString());
        tab.set(this.startSlot + 1, this.factionInfo[1][1] + faction.getHomeString());
    }
}
