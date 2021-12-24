package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class InviteCommand extends SubCommand {

    public InviteCommand() {
        super("invite", Collections.singletonList("inv"), true);

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_INVITE_USAGE);
            return;
        }

        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        if(!faction.getMember(player).getRole().isAtLeast(Role.CAPTAIN)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NO_PERMISSION.replace("<role>", Role.CAPTAIN.getName()));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, target, args[0])) return;

        FactionPlayer targetPlayer = faction.getMember(target);

        if(targetPlayer != null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALREADY_IN_FACTION_OTHERS.replace("<player>", target.getName()));
            return;
        }

        if(faction.getPlayerInvitations().contains(target.getName())) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_INVITE_ALREADY_INVITED.replace("<player>", target.getName()));
            return;
        }

        if(faction.getMembers().size() >= Config.FACTION_PLAYER_LIMIT) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_INVITE_FACTION_FULL);
            return;
        }

        faction.getPlayerInvitations().add(target.getName());

        String hoverText = Language.FACTIONS_INVITE_HOVER_TEXT.replace("<faction>", faction.getName());

        ComponentBuilder message = new ComponentBuilder(Language.FACTION_PREFIX)
            .append(Language.FACTIONS_INVITED_SELF.replace("<name>", faction.getName()))
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f join " + faction.getName()));

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_INVITED_OTHERS.replace("<player>", target.getName()));

        if(target.isOnline()) {
            target.getPlayer().spigot().sendMessage(message.create());
        }
    }
}
