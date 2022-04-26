package me.qiooip.lazarus.handlers.death;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.Color;
import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StattrakHandler extends Handler implements Listener {

    private final Pattern pattern;

    public StattrakHandler() {
        this.pattern = Pattern.compile("\\d+");
    }

    private void setNewStattrakItem(Player player, Player killer, ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        meta.setLore(Arrays.asList("", Config.STATTRAK_KILLS_FORMAT
            .replace("<kills>", String.valueOf(1)), "", this.getKillLine(player, killer)));

        item.setItemMeta(meta);
    }

    private String getKillLine(Player player, Player killer) {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance(Config
            .DATE_FORMAT, Config.TIMEZONE, Locale.ENGLISH);

        return Config.STATTRAK_KILL_STRING
            .replace("<player>", player.getName()).replace("<killer>", killer.getName())
            .replace("<date>", fastDateFormat.format(System.currentTimeMillis()));
    }

    private int getKillCount(ItemStack item) {
        String kills = Config.STATTRAK_KILLS_FORMAT.replace("<kills>", "");

        String line = item.getItemMeta().getLore().stream().filter(l -> l.startsWith(kills)).findFirst().orElse("");
        Matcher matcher = pattern.matcher(Color.strip(line));

        return (matcher.find()) ? Integer.parseInt(matcher.group()) : 0;
    }

    private boolean hasStattrak(ItemStack item) {
        return this.getKillCount(item) != 0;
    }

    private void updateStattrakLore(Player player, Player killer, ItemStack item) {
        if(!Config.STATTRAK_ENABLED || !Config.STATTRAK_TRACKING_ITEMS.contains(item.getType().name())) return;

        if(!item.getItemMeta().hasLore() || !this.hasStattrak(item)) {
            this.setNewStattrakItem(player, killer, item);
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        int index = itemMeta.getLore().indexOf(Config.STATTRAK_KILLS_FORMAT.replace("<kills>", String.valueOf(this.getKillCount(item))));

        List<String> lore = itemMeta.getLore();

        switch(itemMeta.getLore().size() - (index + 1)) {
            case 2: {
                lore.set(index, Config.STATTRAK_KILLS_FORMAT.replace("<kills>", String.valueOf(this.getKillCount(item) + 1)));
                lore.add(lore.size() - 1, this.getKillLine(player, killer));
                break;
            }
            case 3: {
                lore.set(index, Config.STATTRAK_KILLS_FORMAT.replace("<kills>", String.valueOf(this.getKillCount(item) + 1)));
                lore.add(lore.size() - 2, this.getKillLine(player, killer));
                break;
            }
            case 4: {
                lore.set(index, Config.STATTRAK_KILLS_FORMAT.replace("<kills>", String.valueOf(this.getKillCount(item) + 1)));
                lore.add(lore.size() - 3, this.getKillLine(player, killer));
                lore.remove(lore.size() - 1);
            }
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Player killer = player.getKiller();
        if(killer == null || player == killer) return;

        ItemStack item = killer.getItemInHand();
        if(item == null || item.getType() == Material.AIR) return;

        this.updateStattrakLore(player, killer, item);
    }
}
