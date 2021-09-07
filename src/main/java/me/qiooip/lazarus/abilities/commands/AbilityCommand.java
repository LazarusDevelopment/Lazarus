package me.qiooip.lazarus.abilities.commands;

import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbilityCommand extends BaseCommand {

    public AbilityCommand() {
        super("ability", Collections.singletonList("abilities"), "lazarus.ability");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.PREFIX + Language.ABILITIES_ABILITY_COMMAND_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        if(target.getInventory().firstEmpty() == -1) {
            sender.sendMessage(Language.PREFIX + Language.ABILITIES_ABILITY_COMMAND_INVENTORY_FULL.replace("<player>", target.getName()));
            return;
        }

        AbilityType type = AbilityType.getByName(args[1]);
        if(type == null) {
            sender.sendMessage(Language.PREFIX + Language.ABILITIES_ABILITY_COMMAND_NOT_FOUND.replace("<name>", args[1]));
            return;
        }

        AbilityItem ability = AbilitiesManager.getInstance().getEnabledAbilities().get(type);
        if(ability == null) {
            sender.sendMessage(Language.PREFIX + Language.ABILITIES_ABILITY_COMMAND_NOT_ENABLED.replace("<name>", type.getName()));
            return;
        }

        int amount = 1;
        if(args.length == 3) {
            if(!StringUtils.isInteger(args[2])) {
                sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_NUMBER);
                return;
            }

            amount = Math.abs(Integer.parseInt(args[2]));
        }

        ItemStack item = ability.getItem().clone();
        item.setAmount(amount);

        target.getInventory().addItem(item);

        sender.sendMessage(Language.PREFIX + Language.ABILITIES_ABILITY_COMMAND_GIVE_ABILITY
            .replace("<amount>", String.valueOf(amount))
            .replace("<ability>", item.getItemMeta().getDisplayName())
            .replace("<player>", target.getName()));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if(args.length != 2 || !sender.hasPermission("lazarus.ability")) {
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