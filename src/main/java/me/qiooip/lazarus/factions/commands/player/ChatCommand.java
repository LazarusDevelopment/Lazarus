package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.ChatType;
import me.qiooip.lazarus.factions.event.FactionChatEvent;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ChatCommand extends SubCommand {

    public ChatCommand() {
        super("chat", Collections.singletonList("c"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        FactionPlayer fplayer = faction.getMember(player);

        if(args.length == 0) {
            this.changeChatType(player, fplayer, fplayer.getChatType().nextType());
            return;
        }

        ChatType chatType = ChatType.parseType(args[0]);

        if(chatType != null) {
            if(chatType == ChatType.ALLY && Config.FACTION_MAX_ALLIES <= 0) {
                sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLIES_DISABLED);
                return;
            }

            this.changeChatType(player, fplayer, chatType);
            return;
        }

        String message = fplayer.getChatType().getFormat()
            .replace("<player>", player.getName())
            .replace("<message>", StringUtils.joinArray(args, " ", 1));

        FactionChatEvent chatEvent = new FactionChatEvent(player, faction, message);

        if(chatEvent.isCancelled()) {
            return;
        }

        faction.getOnlinePlayers().forEach(recipient -> recipient.sendMessage(message));

        if(fplayer.getChatType() == ChatType.ALLY) {
            faction.getAlliesAsFactions().forEach(ally -> ally.getOnlinePlayers()
            .forEach(recipient -> recipient.sendMessage(message)));
        }
    }

    private void changeChatType(Player player, FactionPlayer fplayer, ChatType chatType) {
        fplayer.setChatType(chatType);

        player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CHAT_COMMAND_CHANGED
            .replace("<type>", chatType.getName()));
    }
}
