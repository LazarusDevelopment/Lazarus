package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopCommand extends SubCommand {

    public TopCommand() {
        super("top", true);

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        List<Faction> factions = FactionsManager.getInstance().getFactions().values().stream()
            .filter(faction -> faction instanceof PlayerFaction)
            .sorted(Comparator.comparing(faction -> ((PlayerFaction) faction).getPoints()).reversed())
            .limit(10).collect(Collectors.toList());

        if(factions.isEmpty()) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_TOP_NO_FACTIONS);
            return;
        }

        Language.FACTIONS_TOP_HEADER.forEach(sender::sendMessage);

        for(int i = 0; i < factions.size(); i++) {
            PlayerFaction faction = (PlayerFaction) factions.get(i);

            ComponentBuilder message = new ComponentBuilder(Language.FACTIONS_TOP_FACTION_FORMAT
            .replace("<number>", String.valueOf(i + 1)).replace("<name>", faction.getName(player))
            .replace("<points>", String.valueOf(faction.getPoints())));

            String hoverText = Language.FACTIONS_SHOW_HOVER_TEXT.replace("<faction>", faction.getName());

            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f show " + faction.getName()));

            player.spigot().sendMessage(message.create());
        }

        Language.FACTIONS_TOP_FOOTER.forEach(sender::sendMessage);
    }
}
