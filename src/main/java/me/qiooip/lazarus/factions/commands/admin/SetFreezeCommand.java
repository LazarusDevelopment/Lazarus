package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.FactionFreezeTimer;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class SetFreezeCommand extends SubCommand {

    public SetFreezeCommand() {
        super("setfreeze", Collections.singletonList("setregen"), "lazarus.factions.setfreeze");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SET_FREEZE_USAGE);
            return;
        }

        PlayerFaction faction = FactionsManager.getInstance().searchForFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        int duration = StringUtils.parseSeconds(args[1]);

        if(duration == -1) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_DURATION);
            return;
        }

        FactionFreezeTimer timer = TimerManager.getInstance().getFactionFreezeTimer();
        timer.cancel(faction);

        if(duration == 0) {
            TimerManager.getInstance().getDtrRegenTimer().addFaction(faction, faction.getDtr());
        } else {
            timer.activate(faction, duration);
        }

        String time = StringUtils.formatDurationWords(duration * 1000L);

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SET_FREEZE_CHANGED_SENDER
            .replace("<faction>", faction.getName())
            .replace("<time>", time));

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SET_FREEZE_CHANGED_FACTION
            .replace("<player>", sender.getName())
            .replace("<time>", time));
    }
}
