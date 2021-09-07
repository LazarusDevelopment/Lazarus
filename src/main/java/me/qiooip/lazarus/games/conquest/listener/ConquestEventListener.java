package me.qiooip.lazarus.games.conquest.listener;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.games.conquest.event.ConquestCappedEvent;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConquestEventListener implements Listener {

    public ConquestEventListener() {
        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    private boolean isConquestSign(Block block) {
        if(block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) return false;

        Sign sign = (Sign) block.getState();
        String[] lines = sign.getLines();
        return lines.length > 0 && lines[1] != null && lines[1].equals(Config.CONQUEST_CAPPED_SIGN_LORE.get(1));
    }

    private ItemStack getConquestCapSign(Player capper, PlayerFaction faction) {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance(Config
        .DATE_FORMAT, Config.TIMEZONE, Locale.ENGLISH);

        ItemBuilder builder = new ItemBuilder(Material.SIGN, 1).setName(Config.CONQUEST_CAPPED_SIGN_NAME)
        .setLore(Config.CONQUEST_CAPPED_SIGN_LORE.stream().map(line -> line.replace("<faction>", faction.getName())
        .replace("<date>", fastDateFormat.format(System.currentTimeMillis()))).collect(Collectors.toList()));

        return builder.build();
    }

    @EventHandler
    public void onConquestCapped(ConquestCappedEvent event) {
        Player capper = event.getCapper();
        event.getLoot().handleRewards(capper);

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(capper);
        faction.incrementPoints(Config.FACTION_TOP_CONQUEST_CAP);

        if(!Config.CONQUEST_CAPPED_SIGN_ENABLED) return;

        if(capper.getInventory().firstEmpty() == -1) {
            capper.getWorld().dropItemNaturally(capper.getLocation(), this.getConquestCapSign(capper, faction));
        } else {
            capper.getInventory().addItem(this.getConquestCapSign(capper, faction));
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if(!this.isConquestSign(event.getBlock())) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(!this.isConquestSign( event.getBlock())) return;

        event.setCancelled(true);

        Block block = event.getBlock();
        Sign sign = (Sign) block.getState();

        ItemBuilder builder = new ItemBuilder(Material.SIGN, 1).setName(Config
        .CONQUEST_CAPPED_SIGN_NAME).setLore(Arrays.asList(sign.getLines()));

        block.getWorld().dropItemNaturally(block.getLocation(), builder.build());
        block.setType(Material.AIR);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if(block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) return;

        ItemStack item = event.getPlayer().getItemInHand();
        if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName() || !item.getItemMeta().hasLore()) return;
        if(!item.getItemMeta().getDisplayName().equalsIgnoreCase(Config.CONQUEST_CAPPED_SIGN_NAME)) return;

        Sign sign = (Sign) block.getState();

        IntStream.range(0, 4).forEach(i -> sign.setLine(i, item.getItemMeta().getLore().get(i)));

        sign.update();
        event.getPlayer().closeInventory();
    }
}
