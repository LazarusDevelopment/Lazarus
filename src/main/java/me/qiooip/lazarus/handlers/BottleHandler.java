package me.qiooip.lazarus.handlers;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BottleHandler extends Handler implements Listener {

	private final Pattern pattern;
	
	public BottleHandler() {
		this.pattern = Pattern.compile("\\d+");
	}
	
	public ItemStack getBottle(Player player, int level) {
		if(player.getLevel() == 0 || player.getLevel() < level) return null;

		ItemBuilder builder = new ItemBuilder(Material.EXP_BOTTLE).setName(Config.BOTTLE_NAME);

		builder.setLore(Config.BOTTLE_LORE.stream()
			.map(line -> line.replace("<exp>", String.valueOf(level)))
			.collect(Collectors.toList()));

		return builder.build();
	}
	
	private int getExpLevel(List<String> lore) {
		AtomicInteger exp = new AtomicInteger();

		lore.forEach(line -> {
			Matcher matcher = pattern.matcher(ChatColor.stripColor(line));
			while(matcher.find()) {
				exp.set(Integer.parseInt(matcher.group()));
			}
		});

		return exp.get();
	}
	
	private boolean isExpBottle(ItemStack item) {
		return (item != null) && (item.getType().equals(Material.EXP_BOTTLE)) && (item.getItemMeta().hasDisplayName())
		&& (item.getItemMeta().getDisplayName().equals(Config.BOTTLE_NAME));
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if(!Config.BOTTLE_DROP_ON_DEATH) return;
		
		Player player = event.getEntity();
		
		if(this.getBottle(player, player.getLevel()) == null) return;

		player.getWorld().dropItemNaturally(player.getLocation(), this.getBottle(player, player.getLevel()));
		player.setLevel(0);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;
		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		Player player = event.getPlayer();
		if(!this.isExpBottle(player.getItemInHand())) return;

		int expLevel = this.getExpLevel(player.getItemInHand().getItemMeta().getLore());

		event.setCancelled(true);

		ItemUtils.removeOneItem(player);
		player.giveExpLevels(expLevel);
	}
}
