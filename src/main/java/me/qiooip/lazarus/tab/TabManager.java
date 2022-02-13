package me.qiooip.lazarus.tab;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.event.FactionDisbandEvent;
import me.qiooip.lazarus.factions.event.FactionRenameEvent;
import me.qiooip.lazarus.factions.event.PlayerJoinFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent.LeaveReason;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.tab.module.TabModule;
import me.qiooip.lazarus.tab.module.impl.PlayerListModule;
import me.qiooip.lazarus.tab.task.TabUpdater;
import me.qiooip.lazarus.tab.task.TabUpdaterImpl;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TabManager implements Listener {

    public static final String VALUE = "eyJ0aW1lc3RhbXAiOjE0MTEyNjg3OTI3NjUsInByb2ZpbGVJZCI6IjNmYmVjN2RkMGE1ZjQwYmY5ZDExODg1YTU0NTA3MTEyIiwicHJvZmlsZU5hbWUiOiJsYXN0X3VzZXJuYW1lIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg0N2I1Mjc5OTg0NjUxNTRhZDZjMjM4YTFlM2MyZGQzZTMyOTY1MzUyZTNhNjRmMzZlMTZhOTQwNWFiOCJ9fX0=";
    public static final String SIGNATURE = "u8sG8tlbmiekrfAdQjy4nXIcCfNdnUZzXSx9BE1X5K27NiUvE1dDNIeBBSPdZzQG1kHGijuokuHPdNi/KXHZkQM7OJ4aCu5JiUoOY28uz3wZhW4D+KG3dH4ei5ww2KwvjcqVL7LFKfr/ONU5Hvi7MIIty1eKpoGDYpWj3WjnbN4ye5Zo88I2ZEkP1wBw2eDDN4P3YEDYTumQndcbXFPuRRTntoGdZq3N5EBKfDZxlw4L3pgkcSLU5rWkd5UH4ZUOHAP/VaJ04mpFLsFXzzdU4xNZ5fthCwxwVBNLtHRWO26k/qcVBzvEXtKGFJmxfLGCzXScET/OjUBak/JEkkRG2m+kpmBMgFRNtjyZgQ1w08U6HHnLTiAiio3JswPlW5v56pGWRHQT5XWSkfnrXDalxtSmPnB5LmacpIImKgL8V9wLnWvBzI7SHjlyQbbgd+kUOkLlu7+717ySDEJwsFJekfuR6N/rpcYgNZYrxDwe4w57uDPlwNL6cJPfNUHV7WEbIU1pMgxsxaXe8WSvV87qLsR7H06xocl2C0JFfe2jZR4Zh3k9xzEnfCeFKBgGb4lrOWBu1eDWYgtKV67M2Y+B3W5pjuAjwAxn0waODtEn/3jKPbc/sxbPvljUCw65X+ok0UUN1eOwXV5l2EGzn05t3Yhwq19/GxARg63ISGE8CKw=";

    private final Map<UUID, PlayerTab> tabs;
    private final PlayerListModule playerListModule;
    private TabUpdater updater;

    public TabManager() {
        this.updater = new TabUpdaterImpl(this);
        this.tabs = new ConcurrentHashMap<>();

        this.playerListModule = new PlayerListModule();

        Bukkit.getOnlinePlayers().forEach(this::loadTab);
        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.updater.cancel();

        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerTab tab = this.getTab(player);
            if(tab != null) tab.unregister();
        });

        this.tabs.clear();
    }

    public void setUpdater(TabUpdater tabUpdater) {
        this.updater.cancel();
        this.updater = tabUpdater;
    }

    public void loadTab(Player player) {
        PlayerTab tab = NmsUtils.getInstance().getNewPlayerTab(player);

        this.tabs.put(player.getUniqueId(), tab);
        this.updater.initialSet(tab);

        if(NmsUtils.getInstance().getClientVersion(player) >= 47) {
            NmsUtils.getInstance().sendHeaderAndFooter(player);
        }

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);
        if(faction == null) return;

        Tasks.async(() -> {
            for(Player online : faction.getOnlinePlayers()) {
                PlayerTab onlineTab = this.getTab(online);

                if(onlineTab != null) {
                    this.playerListModule.apply(onlineTab, faction);
                }
            }
        });
    }

    public void removeTab(Player player) {
        PlayerTab playerTab = this.tabs.remove(player.getUniqueId());

        if(playerTab != null) {
            playerTab.unregister();
        }

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction != null) {
            for(Player online : faction.getOnlinePlayers()) {
                this.playerListModule.apply(this.getTab(online), faction);
            }
        }
    }

    public PlayerTab getTab(Player player) {
        return this.tabs.get(player.getUniqueId());
    }

    public void registerTabModule(TabModule tabModule) {
        this.updater.registerTabModule(tabModule);
    }

    public void updateFactionPlayerList(PlayerFaction faction) {
        for(Player online : faction.getOnlinePlayers()) {
            this.playerListModule.apply(this.getTab(online), faction);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionRename(FactionRenameEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;
        Tasks.async(() -> this.updateFactionPlayerList((PlayerFaction) event.getFaction()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoinFaction(PlayerJoinFactionEvent event) {
        Tasks.async(() -> this.updateFactionPlayerList(event.getFaction()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeaveFaction(PlayerLeaveFactionEvent event) {
        if(event.getReason() == LeaveReason.DISBAND) return;

        Tasks.async(() -> {
            this.updateFactionPlayerList(event.getFaction());
            Player player = event.getFactionPlayer().getPlayer();

            if(player != null) {
                this.playerListModule.clearFactionPlayerList(this.getTab(player));
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionDisband(FactionDisbandEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;
        PlayerFaction playerFaction = (PlayerFaction) event.getFaction();

        Tasks.async(() -> playerFaction.getOnlinePlayers().forEach(online ->
            this.playerListModule.clearFactionPlayerList(this.getTab(online))));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Tasks.syncLater(() -> this.loadTab(event.getPlayer()), 5L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Tasks.async(() -> this.removeTab(event.getPlayer()));
    }
}
