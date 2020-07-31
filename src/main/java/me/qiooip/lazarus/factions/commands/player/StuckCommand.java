package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.WarzoneFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.StuckTimer;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StuckCommand extends SubCommand {

    public StuckCommand() {
        super("stuck", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_STUCK_ONLY_IN_OVERWORLD);
            return;
        }

        Faction faction = ClaimManager.getInstance().getFactionAt(player);

        if(!(faction instanceof PlayerFaction) && !(faction instanceof WarzoneFaction)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_STUCK_ONLY_FROM_ENEMY_CLAIMS);
            return;
        }

        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);

        if(playerFaction != null && playerFaction == faction) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_STUCK_IN_OWN_CLAIM);
            return;
        }

        StuckTimer timer = TimerManager.getInstance().getStuckTimer();

        if(timer.isActive(player)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_STUCK_ALREADY_TELEPORTING);
            return;
        }

        FactionsManager.getInstance().setStuckInitialLocation(player, player.getLocation());

        Location safeLocation = ClaimManager.getInstance().getSafeLocation(player);
        safeLocation.setPitch(player.getLocation().getPitch());
        safeLocation.setYaw(player.getLocation().getYaw());

        timer.activate(player, safeLocation);

        player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_STUCK_TASK_STARTED.replace("<time>",
            StringUtils.formatTime(Config.FACTION_STUCK_WARMUP, FormatType.SECONDS_TO_MINUTES)));
    }
}
