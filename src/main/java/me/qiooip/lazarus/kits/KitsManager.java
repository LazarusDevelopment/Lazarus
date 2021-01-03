package me.qiooip.lazarus.kits;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.kits.kit.KitData;
import me.qiooip.lazarus.kits.kit.KitType;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.InventoryUtils;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.ManagerEnabler;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.IntStream;

public class KitsManager implements Listener, ManagerEnabler {

	private final ConfigFile kitsFile;

	private final List<KitData> kits;
	private final Map<UUID, String> editingKits;
	private final Map<Location, KitData> kitSignCache;
	
	public KitsManager() {
		this.kitsFile = new ConfigFile("kits.yml");

		this.kits = new ArrayList<>();
		this.editingKits = new HashMap<>();
		this.kitSignCache = new HashMap<>();

		this.loadKits();
		
		Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
	}

	public void disable() {
		this.saveKits();

		this.kits.clear();
		this.editingKits.clear();
		this.kitSignCache.clear();
	}
	
	private void loadKits() {
		this.kitsFile.getKeys(false).forEach(kitName -> {
			ConfigurationSection section = this.kitsFile.getSection(kitName);

			KitType kitType = KitType.fromName(section.getString("type"));
			if(kitType == null) return;

			KitData kit = new KitData();
			kit.setType(kitType);
			kit.setName(kitName);
			kit.setDelay(section.getInt("delay"));
			kit.setPermission("lazarus.kit." + kitName);
			kit.setContents(InventoryUtils.itemStackArrayFromBase64(section.getString("contents")));
			kit.setArmor(InventoryUtils.itemStackArrayFromBase64(section.getString("armor")));

            this.kits.add(kit);
		});
	}
	
	private void saveKits() {
		this.kits.forEach(kit -> {
			ConfigurationSection section = this.kitsFile.createSection(kit.getName());
			section.set("type", kit.getType().name());
			section.set("delay", kit.getDelay());
			section.set("contents", InventoryUtils.itemStackArrayToBase64(kit.getContents()));
			section.set("armor", InventoryUtils.itemStackArrayToBase64(kit.getArmor()));
		});

		this.kitsFile.save();
	}
	
	public void createKit(String name, int delay) {
		KitData kit = new KitData();
		kit.setName(name);
		kit.setDelay(delay);
		kit.setPermission("lazarus.kit." + name);
		kit.setContents(new ItemStack[36]);

        this.kits.add(kit);
	}
	
	public void removeKit(KitData kit) {
		this.kits.remove(kit);

		this.kitsFile.set(kit.getName(), null);
		this.kitsFile.save();
	}
	
	public KitData getKit(String name) {
		return this.kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	private boolean hasKitPermission(Player player, KitData kit) {
	    return player.hasPermission(kit.getPermission()) || player.hasPermission("lazarus.kit.*");
    }

	private boolean isEditingKit(Player player) {
		return this.editingKits.containsKey(player.getUniqueId());
	}

	private String getCooldownString(Userdata data, KitData kit) {
	    return DurationFormatUtils.formatDurationWords(data.getKitDelays().get(kit.getName()) - System.currentTimeMillis(), true, true);
    }

    private void applyKitCooldown(Userdata data, KitData kit) {
        data.getKitDelays().put(kit.getName(), kit.getDelay() == -1 ? -1 : System.currentTimeMillis() + (kit.getDelay() * 1000L));
    }
	
	public void editKit(Player player, KitData kit) {
		this.editingKits.put(player.getUniqueId(), kit.getName());

		Inventory inventory = Bukkit.createInventory(null, 54, Color.translate("&3Kit: &c" + kit.getName()));
		inventory.setContents(kit.getContents());
		IntStream.rangeClosed(45, 48).forEach(i -> inventory.setItem(i, kit.getArmor()[i-45]));

		ItemStack placeholder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).setName(ChatColor.RED + "Kit Editor").build();
		IntStream.rangeClosed(36, 44).forEach(i -> inventory.setItem(i, placeholder));
		IntStream.rangeClosed(49, 51).forEach(i -> inventory.setItem(i, placeholder));

		inventory.setItem(52, new ItemBuilder(Material.STAINED_CLAY, 1, 14).setName(ChatColor.RED + "Close Editor").build());
		inventory.setItem(53, new ItemBuilder(Material.STAINED_CLAY, 1, 5).setName(ChatColor.GREEN + "Save Kit").build());

		player.openInventory(inventory);
	}

	private boolean isOnCooldown(Player player, KitData kit) {
		if(kit.getDelay() == 0) return false;
		if(player.hasPermission("lazarus.kits.delay.bypass")) return false;

		Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);
		if(!data.getKitDelays().containsKey(kit.getName())) return false;

		long delay = data.getKitDelays().get(kit.getName());
		return delay == -1 || System.currentTimeMillis() < delay;
	}

	public void listKits(CommandSender sender) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Language.KIT_PREFIX + Language.COMMANDS_FOR_PLAYERS_ONLY);
			return;
		}

		Player player = (Player) sender;
		StringJoiner availableKits = new StringJoiner(Color.translate("&7, "));

		this.kits.stream().sorted(Comparator.comparing(KitData::getName)).forEach(kit -> {
			if(!player.hasPermission(kit.getPermission()) || kit.getType() == KitType.SPECIAL) return;

			if(this.isOnCooldown(player, kit)) {
				if(kit.getDelay() == -1) return;
				availableKits.add(Color.translate("&c" + kit.getName()));
				return;
			}

			availableKits.add(Color.translate("&a" + kit.getName()));
		});

		if(availableKits.length() == 0) {
			sender.sendMessage(Language.KIT_PREFIX + Language.KITS_LIST_NO_KITS);
			return;
		}

		sender.sendMessage(Language.KITS_LIST_FORMAT.replace("<kits>", availableKits.toString()));
	}

	public void giveKit(Player player, KitData kit, boolean sign) {
		if(kit.getType() == KitType.SPECIAL) {
			player.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_SPECIAL_EVENT_KIT);
			return;
		}

		if(kit.getType() != KitType.KITMAP && !this.hasKitPermission(player, kit) && kit.getDelay() != -1) {
			player.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_NO_PERMISSION.replace("<kit>", kit.getName()));
            return;
		}

		Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);

		if(this.isOnCooldown(player, kit)) {
			player.sendMessage(kit.getDelay() == -1
            	? Language.KIT_PREFIX + Language.KITS_EXCEPTION_ONE_TIME_ONLY
					.replace("<kit>", kit.getName())
				: Language.KIT_PREFIX + Language.KITS_EXCEPTION_COOLDOWN
					.replace("<kit>", kit.getName())
            		.replace("<time>", this.getCooldownString(data, kit)));
            return;
		}

		this.applyKitCooldown(data, kit);
		kit.applyKit(player);

		if(kit.getType() != KitType.KITMAP && player.getInventory().firstEmpty() == -1) {
			player.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_FULL_INVENTORY);
		}

		player.updateInventory();
        player.sendMessage(Language.KIT_PREFIX + Language.KITS_EQUIPPED.replace("<kit>", kit.getName()));
	}

	public void giveKitWithCommand(CommandSender sender, Player player, KitData kit) {
		if(kit.getType() == KitType.SPECIAL) {
			sender.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_SPECIAL_EVENT_KIT);
			return;
		}

		kit.applyKit(player);

        if(player.getInventory().firstEmpty() == -1) {
        	player.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_FULL_INVENTORY);
		}

		player.updateInventory();
        player.sendMessage(Language.KIT_PREFIX + Language.KITS_EQUIPPED.replace("<kit>", kit.getName()));

		sender.sendMessage(Language.KIT_PREFIX + Language.KITS_APPLIED_OTHERS
        	.replace("<kit>", kit.getName())
			.replace("<player>", player.getName()));
	}

	private void setKitmapSignFormat(String kit, String color, SignChangeEvent event) {
		event.setLine(0, "");
		event.setLine(1, color + kit);
		event.setLine(2, color + "Kit");
		event.setLine(3, "");
	}

	private String getColor(String kit) {
		switch(kit.toUpperCase()) {
			case "ARCHER": return Config.ARCHER_SIGN_COLOR;
			case "BARD": return Config.BARD_SIGN_COLOR;
			case "BUILDER": return Config.BUILDER_SIGN_COLOR;
			case "DIAMOND": return Config.DIAMOND_SIGN_COLOR;
			case "ROGUE": return Config.ROGUE_SIGN_COLOR;
			default: return null;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)) return;

		Player player = (Player) event.getWhoClicked();

		if(!this.isEditingKit(player)) return;
		if(event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;

		KitData kit = this.getKit(this.editingKits.get(player.getUniqueId()));
		if(kit == null) return;

		if(kit.shouldCancelEvent(event.getSlot())) {
			event.setCancelled(true);
		}

		ItemStack item = event.getCurrentItem();
		if(item == null || !item.hasItemMeta()) return;
		if(!item.getItemMeta().hasDisplayName()) return;

		if(item.getItemMeta().getDisplayName().contains("Close Editor")) {
			Tasks.sync(() -> player.closeInventory());
			return;
		}

		if(item.getItemMeta().getDisplayName().contains("Save Kit")) {
			if(kit.getType() == KitType.KITMAP || kit.getType() == KitType.SPECIAL) {
				IntStream.range(0, 36).forEach(i -> kit.getContents()[i] = event.getInventory().getItem(i));
			} else {
				ItemStack[] contents = new ItemStack[36];
				System.arraycopy(event.getInventory().getContents(), 0, contents, 0, contents.length);
				kit.setContents(InventoryUtils.getRealItems(contents));
			}

			ItemStack[] armor = kit.getArmor();
			IntStream.rangeClosed(45, 48).forEach(i -> armor[i-45] = event.getInventory().getItem(i));

			this.editingKits.remove(player.getUniqueId());
			player.sendMessage(Language.KIT_PREFIX + Language.KITS_EDIT_EDITED.replace("<kit>", kit.getName()));

			Tasks.sync(() -> player.closeInventory());
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
        if(!Config.KITS_FIRST_JOIN_KIT_ENABLED || event.getPlayer().hasPlayedBefore()) return;
			
		KitData kit = this.getKit(Config.KITS_FIRST_JOIN_KIT);
		if(kit == null) return;

		Tasks.syncLater(() -> this.giveKit(event.getPlayer(), kit, false), 5L);
	}

	@EventHandler
	public void onSighChange(SignChangeEvent event) {
		if(!event.getPlayer().hasPermission("lazarus.kits.sign.create")) return;

		int titleSignLine;

		if(StringUtils.containsIgnoreCase(event.getLine(0), "kit")) {
			titleSignLine = 1;
		} else if(StringUtils.containsIgnoreCase(event.getLine(1), "kit")) {
			titleSignLine = 2;
		} else if(StringUtils.containsIgnoreCase(event.getLine(2), "kit")) {
			titleSignLine = 3;
		} else {
			return;
		}

		String kitName = event.getLine(titleSignLine);

		switch(kitName.toLowerCase()) {
			case "archer": {
				this.setKitmapSignFormat("Archer", Config.ARCHER_SIGN_COLOR, event);
				break;
			}
			case "bard": {
				this.setKitmapSignFormat("Bard", Config.BARD_SIGN_COLOR, event);
				break;
			}
			case "builder": {
				this.setKitmapSignFormat("Builder", Config.BUILDER_SIGN_COLOR, event);
				break;
			}
			case "diamond": {
				this.setKitmapSignFormat("Diamond", Config.DIAMOND_SIGN_COLOR, event);
				break;
			}
			case "rogue": {
				this.setKitmapSignFormat("Rogue", Config.ROGUE_SIGN_COLOR, event);
				break;
			}
			default: {
				KitData regularKit = this.getKit(kitName);
				if(regularKit == null) return;

				for(int i = 0; i < 4; i++) {
					event.setLine(i, Config.REGULAR_KIT_SIGN_FORMAT.get(i).replace("<kitName>", regularKit.getName()));
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(event.getClickedBlock().getType() != Material.SIGN_POST && event.getClickedBlock().getType() != Material.WALL_SIGN) return;

		KitData kitData = this.kitSignCache.get(event.getClickedBlock().getLocation());

		if(kitData == null) {
			Sign sign = (Sign) event.getClickedBlock().getState();

			if(sign.getLine(1).equalsIgnoreCase(Config.REGULAR_KIT_SIGN_FORMAT.get(1))) {
				String kitNameLine = sign.getLine(2);
				String kitName = Color.strip(kitNameLine);

				kitData = this.getKit(kitName);
			} else {
				String kitNameLine = sign.getLine(1);
				String kitName = Color.strip(kitNameLine);

				String kitColor = this.getColor(kitName);
				if(kitColor == null || !kitNameLine.startsWith(Color.translate(kitColor))) return;

				kitData = this.getKit(kitName);
			}

			if(kitData == null) return;
			this.kitSignCache.put(sign.getLocation(), kitData);
		}

		if(Lazarus.getInstance().getStaffModeManager().isInStaffMode(event.getPlayer())) {
			event.getPlayer().sendMessage(Language.PREFIX + Language.STAFF_MODE_INTERACT_DENY);
			return;
		}

		event.setCancelled(true);
		this.giveKit(event.getPlayer(), kitData, true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.getBlock().getType() != Material.SIGN_POST && event.getBlock().getType() != Material.WALL_SIGN) return;

		this.kitSignCache.remove(event.getBlock().getLocation());
	}
}
