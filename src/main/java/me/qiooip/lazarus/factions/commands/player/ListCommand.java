package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ListCommand extends SubCommand {

    public ListCommand() {
        super("list", true);

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        int page = args.length == 0 ? 1 : StringUtils.isInteger(args[0]) ? Math.max(1, Integer.parseInt(args[0])) : 1;

        Map<PlayerFaction, Integer> factions = new HashMap<>();
        PlayerFaction lazyFaction;

        for(Player online : Bukkit.getOnlinePlayers()) {
            if((lazyFaction = FactionsManager.getInstance().getPlayerFaction(online)) == null) continue;
            if(!player.canSee(online) && Lazarus.getInstance().getVanishManager().isVanished(online)) continue;

            factions.put(lazyFaction, factions.getOrDefault(lazyFaction, 0) + 1);
        }

        int pageTotal = Math.max(1, (int) Math.ceil(factions.size() / 10d));

        if(page > pageTotal) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_LIST_PAGE_NOT_FOUND
            .replace("<number>", String.valueOf(pageTotal)));
            return;
        }

        List<Entry<PlayerFaction, Integer>> sortedFactions = new ArrayList<>(factions.entrySet());
        sortedFactions.sort(Entry.<PlayerFaction, Integer>comparingByValue().reversed());

        Language.FACTIONS_LIST_HEADER.forEach(line -> sender.sendMessage(line.replace("<page>",
        String.valueOf(page)).replace("<pageTotal>", String.valueOf(pageTotal))));

        for(int i = (page - 1) * 10; i < (page - 1) * 10 + 10; i++) {
            if(factions.size() <= i) break;

            Entry<PlayerFaction, Integer> entry = sortedFactions.get(i);

            ComponentBuilder message = new ComponentBuilder(Language.FACTIONS_LIST_FACTION_FORMAT
                .replace("<number>", String.valueOf(i + 1))
                .replace("<name>", entry.getKey().getName(sender))
                .replace("<online-count>", String.valueOf(entry.getValue()))
                .replace("<faction-size>", String.valueOf(entry.getKey().getMembers().size()))
                .replace("<dtr>", entry.getKey().getDtrString()).replace("<maxDtr>", entry.getKey().getMaxDtrString()));

            String hoverText = Language.FACTIONS_SHOW_HOVER_TEXT.replace("<faction>", entry.getKey().getName());

            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f show " + entry.getKey().getName()));

            player.spigot().sendMessage(message.create());
        }

        Language.FACTIONS_LIST_FOOTER.forEach(sender::sendMessage);
    }
}
