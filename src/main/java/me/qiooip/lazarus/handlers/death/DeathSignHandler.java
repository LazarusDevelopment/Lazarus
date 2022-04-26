package me.qiooip.lazarus.handlers.death;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DeathSignHandler extends Handler implements Listener {

    private boolean isDeathSign(Block block) {
        if(!(block.getState() instanceof Sign)) return false;

        Sign sign = (Sign) block.getState();
        String[] lines = sign.getLines();

        return lines.length > 0
            && lines[1] != null
            && lines[1].equals(Config.DEATH_SIGN_LORE.get(1));
    }

    private ItemStack getDeathSign(Player player, Player killer) {
        FastDateFormat dateFormat = FastDateFormat.getInstance(Config
            .DATE_FORMAT, Config.TIMEZONE, Locale.ENGLISH);

        ItemBuilder deathSign = new ItemBuilder(Material.SIGN, 1)
            .setName(Config.DEATH_SIGN_NAME)
            .setLore(Config.DEATH_SIGN_LORE.stream().map(line -> line
            .replace("<killer>", killer.getName())
            .replace("<victim>", player.getName())
            .replace("<date>", dateFormat.format(System.currentTimeMillis())))
            .collect(Collectors.toList()));

        return deathSign.build();
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if(!this.isDeathSign(event.getBlock())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(!Config.DEATH_SIGN_ENABLED) return;

        Player player = event.getEntity();
        Player killer = player.getKiller();

        if(killer != null && killer != player) {
            event.getDrops().add(this.getDeathSign(player, killer));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(!this.isDeathSign(block)) return;

        event.setCancelled(true);

        Sign sign = (Sign) block.getState();

        block.getWorld().dropItemNaturally(block.getLocation(), new ItemBuilder(Material.SIGN, 1)
        .setName(Config.DEATH_SIGN_NAME).setLore(Arrays.asList(sign.getLines())).build());

        block.setType(Material.AIR);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        ItemStack item = event.getItemInHand();

        if(!(block.getState() instanceof Sign)) return;
        if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        if(!item.getItemMeta().getDisplayName().equals(Config.DEATH_SIGN_NAME)) return;

        Sign sign = (Sign) block.getState();

        List<String> lore = item.getItemMeta().getLore();

        IntStream.range(0, 4).forEach(i -> sign.setLine(i, lore.get(i)));

        sign.update();
        event.getPlayer().closeInventory();
    }
}
