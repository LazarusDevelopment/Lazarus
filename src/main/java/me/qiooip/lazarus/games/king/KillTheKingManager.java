package me.qiooip.lazarus.games.king;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.king.event.KingKilledEvent;
import me.qiooip.lazarus.kits.kit.KitData;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class KillTheKingManager implements Listener, ManagerEnabler {

    @Getter private boolean active;

    private long startTime;
    private Player kingPlayer;

    public KillTheKingManager() {
        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.active = false;
        this.kingPlayer = null;
    }

    public void startKillTheKing(Player kingPlayer) {
        this.active = true;

        this.startTime = System.currentTimeMillis();
        this.kingPlayer = kingPlayer;

        KitData kingKit = Lazarus.getInstance().getKitsManager().getKit("King");
        kingKit.applyKit(kingPlayer);

        Messages.sendMessage(Language.KING_PREFIX + Language.KING_START_STARTED.replace("<location>",
        StringUtils.getLocationNameWithWorld(kingPlayer.getLocation())));
    }

    public void stopKillTheKing(boolean death) {
        this.active = false;

        if(!death) {
            this.kingPlayer.getInventory().clear();
            this.kingPlayer.getInventory().setArmorContents(null);
        }

        this.kingPlayer = null;
    }

    public String getKingName() {
        return this.kingPlayer.getName();
    }

    public String getKingWorld() {
        return StringUtils.getWorldName(this.kingPlayer.getLocation());
    }

    public String getKingLocation() {
        return StringUtils.getLocationNameWithoutY(this.kingPlayer.getLocation());
    }

    public String getKingLocationString() {
        if(!this.isActive()) {
            return Language.KING_PREFIX + Language.KING_EXCEPTION_NOT_RUNNING;
        }

        return Language.KING_PREFIX + Language.KING_KINGS_LOCATION.replace("<location>",
        StringUtils.getLocationNameWithWorld(this.kingPlayer.getLocation()));
    }

    public String getTimeLasted() {
        long diff = System.currentTimeMillis() - this.startTime;

        if(diff < 60000L) {
            return StringUtils.formatTime(diff, FormatType.MILLIS_TO_SECONDS) + "s";
        } else if(diff < 3600000L) {
            return StringUtils.formatTime(diff, FormatType.MILLIS_TO_MINUTES);
        } else {
            return StringUtils.formatTime(diff, FormatType.MILLIS_TO_HOURS);
        }
    }

    private void handleReward(Player killer) {
        if(!Config.KILL_THE_KING_AUTO_REWARD_ENABLED) return;

        Config.KILL_THE_KING_REWARD.forEach(command -> Bukkit.dispatchCommand(Bukkit
        .getConsoleSender(), command.replace("<player>", killer.getName())));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(!this.isActive() || !event.getPlayer().getUniqueId().equals(this.kingPlayer.getUniqueId())) return;
        if(!Config.KILL_THE_KING_DENY_ITEM_DROP) return;

        event.setCancelled(true);

        event.getPlayer().updateInventory();
        event.getPlayer().sendMessage(Language.KING_PREFIX + Language.KING_EXCEPTION_ITEM_DROP_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(!this.isActive() || !player.getUniqueId().equals(this.kingPlayer.getUniqueId())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onKingKilled(KingKilledEvent event) {
        this.handleReward(event.getKiller());

        Language.KING_KING_SLAIN.forEach(line -> Messages.sendMessage(line
            .replace("<king>", event.getKing().getName())
            .replace("<killer>", event.getKiller().getName())));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(!this.isActive() || !event.getEntity().getUniqueId().equals(this.kingPlayer.getUniqueId())) return;

        event.getDrops().clear();
        this.stopKillTheKing(true);

        if(event.getEntity().getKiller() != null) {
            new KingKilledEvent(event.getEntity(), event.getEntity().getKiller());
        }
    }
}
