package me.qiooip.lazarus.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.manager.PvpClass;
import me.qiooip.lazarus.classes.manager.PvpClassManager;
import me.qiooip.lazarus.classes.utils.PvpClassUtils;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Miner extends PvpClass {

    private final Map<Integer, DiamondData> diamondData;

    public Miner(PvpClassManager manager) {
        super(manager, "Miner",
            Material.IRON_HELMET,
            Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS,
            Material.IRON_BOOTS
        );

        this.warmup = Config.MINER_WARMUP;

        this.diamondData = new HashMap<>();
        this.loadDiamondData();
    }

    @Override
    public String getDisplayName() {
        return Config.KITMAP_MODE_ENABLED ? "Builder" : "Miner";
    }

    private void loadDiamondData() {
        ConfigurationSection section = Lazarus.getInstance().getClassesFile()
            .getSection("MINER_CLASS.POTION_EFFECT_REWARDS");

        section.getKeys(false).forEach(diamondAmount -> {
            int diamonds = Integer.parseInt(diamondAmount);
            String name = section.getString(diamondAmount + ".NAME");
            List<PotionEffect> effects = PvpClassUtils.loadPassiveEffects(this, "POTION_EFFECT_REWARDS." + diamondAmount + ".EFFECTS");

            this.diamondData.put(diamonds, new DiamondData(name, diamonds, effects));
        });
    }

    public Collection<DiamondData> getDiamondData(int diamondAmount) {
        return this.diamondData.values().stream().filter(data -> data.getDiamonds() <= diamondAmount).collect(Collectors.toList());
    }

    public void sendEffectInfo(CommandSender sender) {
        sender.sendMessage(Language.MINER_EFFECT_HEADER);
        sender.sendMessage(Language.MINER_EFFECT_TITLE);

        this.diamondData.values().stream().sorted(Comparator.comparingInt(DiamondData::getDiamonds)).forEach(data -> {
            StringJoiner joiner = new StringJoiner(", ");
            data.getEffects().forEach(effect -> joiner.add(StringUtils.getPotionEffectName(effect)));

            Language.MINER_EFFECT_FORMAT.forEach(line -> sender.sendMessage(line
                .replace("<name>", data.getName())
                .replace("<diamonds>", String.valueOf(data.getDiamonds()))
                .replace("<effects>", joiner.toString())));
        });

        sender.sendMessage(Language.MINER_EFFECT_FOOTER);
    }

    @Override
    public void applyActiveScoreboardLines(Player player, PlayerScoreboard scoreboard) {
        super.applyActiveScoreboardLines(player, scoreboard);

        if(!Config.KITMAP_MODE_ENABLED) {
            int minedDiamonds = player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE);
            scoreboard.add(Config.MINER_DIAMOND_COUNT_PLACEHOLDER, minedDiamonds + "");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(block.getType() != Material.DIAMOND_ORE || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        Player player = event.getPlayer();
        int diamonds = player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE) + 1;

        DiamondData diamondData = this.diamondData.get(diamonds);
        if(diamondData == null) return;

        diamondData.getEffects().forEach(effect -> this.getManager().addPotionEffect(player, effect));

        Messages.sendMessage(Language.MINER_EFFECT_REWARD.replace("<player>",
            player.getName()).replace("<name>", diamondData.getName()));
    }

    @Getter
    @AllArgsConstructor
    public static class DiamondData {

        private final String name;
        private final int diamonds;
        private final List<PotionEffect> effects;
    }
}
