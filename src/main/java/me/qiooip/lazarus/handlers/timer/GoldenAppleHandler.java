package me.qiooip.lazarus.handlers.timer;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.AppleTimer;
import me.qiooip.lazarus.timer.scoreboard.GAppleTimer;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class GoldenAppleHandler extends Handler implements Listener {
	
	private void applyNormalCooldown(Player player) {
		if(Config.NORMAL_GOLDEN_APPLE_COOLDOWN == 0) return;
		
		TimerManager.getInstance().getAppleTimer().activate(player);
    	
    	player.sendMessage(Language.PREFIX + Language.NORMAL_APPLE_COOLDOWN_STARTED.replace("<time>",
		StringUtils.formatTime(Config.NORMAL_GOLDEN_APPLE_COOLDOWN, FormatType.SECONDS_TO_MINUTES)));
    }

	private void applyEnchantedCooldown(Player player) {
		if(Config.ENCHANTED_GOLDEN_APPLE_COOLDOWN == 0) return;

		TimerManager.getInstance().getGAppleTimer().activate(player);

		player.sendMessage(Language.PREFIX + Language.ENCHANTED_APPLE_COOLDOWN_STARTED.replace("<time>",
		StringUtils.formatTime(Config.ENCHANTED_GOLDEN_APPLE_COOLDOWN, FormatType.SECONDS_TO_HOURS)));
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		if(event.getItem().getType() != Material.GOLDEN_APPLE) return;

		Player player = event.getPlayer();

		switch(event.getItem().getDurability()) {
			case 0: {
				AppleTimer timer = TimerManager.getInstance().getAppleTimer();

				if(timer.isActive(player)) {
					event.setCancelled(true);
					player.sendMessage(Language.PREFIX + Language.NORMAL_APPLE_COOLDOWN_DENY.replace("<time>", timer.getDynamicTimeLeft(player)));
					player.updateInventory();
					return;
				}

				this.applyNormalCooldown(player);
				break;
			}
			case 1: {
                GAppleTimer timer = TimerManager.getInstance().getGAppleTimer();

                if(timer.isActive(player)) {
                    event.setCancelled(true);
                    player.sendMessage(Language.PREFIX + Language.ENCHANTED_APPLE_COOLDOWN_DENY.replace("<time>", timer.getDynamicTimeLeft(player)));
                    player.updateInventory();
                    return;
                }

                this.applyEnchantedCooldown(player);
			}
		}
	}
}
