package me.qiooip.lazarus.abilities.commands;

import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.abilities.AbilitiesTimer;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbilityTimerCommand extends BaseCommand {

    public AbilityTimerCommand() {
        super("abilitytimer", Collections.singletonList("abilitiestimer"), "lazarus.abilitytimer");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            sender.sendMessage(Language.PREFIX + Language.ABILITIES_ABILITY_TIMER_COMMAND_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        AbilityType type = AbilityType.getByName(args[1]);
        if(type == null) {
            sender.sendMessage(Language.PREFIX + Language.ABILITIES_ABILITY_TIMER_COMMAND_NOT_FOUND.replace("<name>", args[1]));
            return;
        }

        AbilityItem ability = AbilitiesManager.getInstance().getEnabledAbilities().get(type);
        if(ability == null) {
            sender.sendMessage(Language.PREFIX + Language.ABILITIES_ABILITY_TIMER_COMMAND_NOT_ENABLED.replace("<name>", type.getName()));
            return;
        }

        int duration = StringUtils.parseSeconds(args[2]);

        if(duration == -1) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_DURATION);
            return;
        }

        AbilitiesTimer abilitiesTimer = TimerManager.getInstance().getAbilitiesTimer();
        if(abilitiesTimer.isActive(target, type)) abilitiesTimer.cancel(target, type);

        if(duration > 0) {
            abilitiesTimer.activate(target, ability.getType(), duration, Language.ABILITIES_PREFIX
                + Language.ABILITIES_ABILITY_COOLDOWN_EXPIRED.replace("<ability>", ability.getDisplayName()));
        }

        sender.sendMessage(Language.PREFIX + Language.ABILITIES_ABILITY_TIMER_COMMAND_CHANGED_SENDER
            .replace("<player>", target.getName())
            .replace("<abilityName>", ability.getDisplayName())
            .replace("<time>", args[2].toLowerCase()));

        if(!Language.ABILITIES_ABILITY_TIMER_COMMAND_CHANGED.isEmpty()) {
            target.sendMessage(Language.PREFIX + Language.ABILITIES_ABILITY_TIMER_COMMAND_CHANGED
                .replace("<abilityName>", ability.getDisplayName())
                .replace("<time>", args[2].toLowerCase())
                .replace("<sender>", sender.getName()));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if(args.length != 2 || !sender.hasPermission("lazarus.abilitytimer")) {
            return super.tabComplete(sender, alias, args);
        }

        List<String> completions = new ArrayList<>();

        for(AbilityType timer : AbilitiesManager.getInstance().getEnabledAbilities().keySet()) {
            String name = timer.getName().toUpperCase();

            if(!name.startsWith(args[1].toUpperCase())) continue;

            completions.add(name);
        }

        return completions;
    }
}
