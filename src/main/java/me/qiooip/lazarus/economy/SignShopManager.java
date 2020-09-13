package me.qiooip.lazarus.economy;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.ManagerEnabler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class SignShopManager implements Listener, ManagerEnabler {

    private final Map<Location, ShopSign> signCache;
    private final Table<UUID, Sign, BukkitTask> signUpdate;

    private final int buyMaterialLine, buyAmountLine, buyPriceLine;
    private final int sellMaterialLine, sellAmountLine, sellPriceLine;

    public SignShopManager() {
        this.signCache = new HashMap<>();
        this.signUpdate = HashBasedTable.create();

        this.buyMaterialLine = this.getSignLineWithPlaceholder(Config.BUY_SIGN_LINES, "<material>", 1);
        this.buyAmountLine = this.getSignLineWithPlaceholder(Config.BUY_SIGN_LINES, "<amount>", 2);
        this.buyPriceLine = this.getSignLineWithPlaceholder(Config.BUY_SIGN_LINES, "<price>", 3);

        this.sellMaterialLine = this.getSignLineWithPlaceholder(Config.SELL_SIGN_LINES, "<material>", 1);
        this.sellAmountLine = this.getSignLineWithPlaceholder(Config.SELL_SIGN_LINES, "<amount>", 2);
        this.sellPriceLine = this.getSignLineWithPlaceholder(Config.SELL_SIGN_LINES, "<price>", 3);

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.signCache.clear();
        this.signUpdate.clear();
    }

    private ShopSign getShopSign(Player player, String[] lines, Location location, boolean create) {
        if(this.signCache.containsKey(location)) return this.signCache.get(location);

        ShopSignType type;

        if(lines[0].equalsIgnoreCase(create ? "[buy]" : Config.BUY_SIGN_LINES.get(0))) {
            type = ShopSignType.BUY;
        } else if(lines[0].equalsIgnoreCase(create ? "[sell]" : Config.SELL_SIGN_LINES.get(0))) {
            type = ShopSignType.SELL;
        } else {
            return null;
        }

        int materialLine = type == ShopSignType.BUY ? this.buyMaterialLine : this.sellMaterialLine;

        ItemStack item;
        String materialName;

        if(lines[materialLine].equalsIgnoreCase("Crowbar")) {
            item = Lazarus.getInstance().getCrowbarHandler().getNewCrowbar();
            materialName = "Crowbar";
        } else {
            item = ItemUtils.parseItem(lines[materialLine]);

            if(item == null) {
                player.sendMessage(Language.PREFIX + Language.ECONOMY_SIGNS_INVALID_MATERIAL);
                return null;
            }

            materialName = ItemUtils.getItemName(item);
            materialName = materialName.length() > 15 ? item.getTypeId() + ":" + item.getDurability() : materialName;
        }

        int amountLine = type == ShopSignType.BUY ? this.buyAmountLine : this.sellAmountLine;
        int amount;

        try {
            amount = Math.abs(Integer.parseInt(lines[amountLine].replaceAll("[^0-9]", "")));
        } catch(NumberFormatException e) {
            player.sendMessage(Language.PREFIX + Language.ECONOMY_SIGNS_INVALID_AMOUNT);
            return null;
        }

        int priceLine = type == ShopSignType.BUY ? this.buyPriceLine : this.sellPriceLine;
        int price;

        try {
            price = Math.abs(Integer.parseInt(lines[priceLine].replaceAll("[^0-9]", "")));
        } catch(NumberFormatException e) {
            player.sendMessage(Language.PREFIX + Language.ECONOMY_SIGNS_INVALID_PRICE);
            return null;
        }

        ShopSign shopSign = new ShopSign(type, materialName, item, amount, price);
        this.signCache.put(location, shopSign);

        return shopSign;
    }

    private int getSignLineWithPlaceholder(List<String> lines, String placeholder, int def) {
        for(int i = 0; i < 4; i++) {
            if(lines.get(i).contains(placeholder)) return i;
        }

        return def;
    }

    private void sendShopSignChange(Player player, Sign sign, String message) {
        player.sendSignChange(sign.getLocation(), new String[] { message, sign.getLine(1), sign.getLine(2), sign.getLine(3) });
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getType() != Material.WALL_SIGN && event.getBlock().getType() != Material.SIGN_POST) return;
        this.signCache.remove(event.getBlock().getLocation());
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if(!player.hasPermission("lazarus.economy.sign.create")) return;

        String[] lines = event.getLines();

        ShopSign shopSign = this.getShopSign(player, lines, event.getBlock().getLocation(), true);
        if(shopSign == null) return;

        IntStream.range(0, 4).forEach(i -> {
            List<String> signLines = shopSign.getType() == ShopSignType.BUY
                ? Config.BUY_SIGN_LINES
                : Config.SELL_SIGN_LINES;

            event.setLine(i, signLines.get(i)
                .replace("<material>", shopSign.getMaterialName())
                .replace("<amount>", String.valueOf(shopSign.getAmount()))
                .replace("<price>", String.valueOf(shopSign.getPrice())));
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if(block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN) return;

        Sign sign = (Sign) block.getState();
        if(!sign.getLine(0).equals(Config.BUY_SIGN_LINES.get(0)) && !sign.getLine(0).equals(Config.SELL_SIGN_LINES.get(0))) return;

        Player player = event.getPlayer();

        ShopSign shopSign = this.getShopSign(player, sign.getLines(), sign.getLocation(), false);
        if(shopSign == null) return;

        event.setUseInteractedBlock(Result.DENY);

        if(this.signUpdate.contains(player.getUniqueId(), sign)) this.signUpdate.get(player.getUniqueId(), sign).cancel();
        this.signUpdate.put(player.getUniqueId(), sign, Tasks.asyncLater(() -> player.sendSignChange(sign.getLocation(), sign.getLines()), 40L));

        if(shopSign.getType() == ShopSignType.BUY) {
            if(player.getInventory().firstEmpty() == -1) {
                this.sendShopSignChange(player, sign, Language.ECONOMY_SIGNS_NO_SPACE);
                return;
            }

            if(Lazarus.getInstance().getEconomyManager().getBalance(player) < shopSign.getPrice()) {
                this.sendShopSignChange(player, sign, Language.ECONOMY_SIGNS_CANNOT_AFFORD);
                return;
            }

            Lazarus.getInstance().getEconomyManager().removeBalance(player, shopSign.getPrice());
            this.sendShopSignChange(player, sign, Language.ECONOMY_SIGNS_BOUGHT);

            player.getInventory().addItem(new ItemBuilder(shopSign.getItem()).setAmount(shopSign.getAmount()).build());
            player.updateInventory();
            return;
        }

        if(shopSign.getType() == ShopSignType.SELL) {
            if(!player.getInventory().contains(shopSign.getItem().getType())) {
                this.sendShopSignChange(player, sign, Language.ECONOMY_SIGNS_NOT_CARRYING);
                return;
            }

            int amount = Math.min(ItemUtils.getItemAmount(player, shopSign.getItem().getData()), shopSign.getAmount());

            float price = amount > shopSign.getAmount() ? shopSign.getPrice()
            : ((amount / (float) shopSign.getAmount()) * shopSign.getPrice());

            Lazarus.getInstance().getEconomyManager().addBalance(player, (int) price);
            this.sendShopSignChange(player, sign, Language.ECONOMY_SIGNS_SOLD);

            ItemUtils.removeItem(player.getInventory(), shopSign.getItem().getData(), amount);
            player.updateInventory();
        }
    }

    @Getter
    @AllArgsConstructor
    private static class ShopSign {

        private final ShopSignType type;
        private final String materialName;
        private final ItemStack item;
        private final int amount;
        private final int price;
    }

    public enum ShopSignType {
        SELL, BUY
    }
}
