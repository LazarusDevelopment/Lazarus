package me.qiooip.lazarus.handlers.block;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;

public class SubclaimHandler extends Handler implements Listener {

    private final BlockFace[] signFaces;

    public SubclaimHandler() {
        this.signFaces = new BlockFace[] { BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    }

    private boolean isSubclaimable(Block block) {
        return (block.getType() == Material.CHEST) || (block.getType() == Material.TRAPPED_CHEST);
    }

    private Sign getSign(Block block) {
        for(BlockFace face : this.signFaces) {
            Block relative = block.getRelative(face);
            if(relative.getType() != Material.SIGN_POST && relative.getType() != Material.WALL_SIGN) continue;

            BlockState state = relative.getState();

            Attachable attachable = (Attachable) state.getData();
            if(!relative.getRelative(attachable.getAttachedFace()).equals(block)) continue;

            return (Sign) state;
        }

        return null;
    }

    private Block getBlockAttachedTo(Block block) {
        MaterialData data = block.getState().getData();
        if(!(data instanceof Attachable)) return block.getRelative(BlockFace.DOWN);

        return block.getRelative(((Attachable) data).getAttachedFace());
    }

    private String shortenName(Player player) {
        return player.getName().length() > 15 ? player.getName().substring(0, 15) : player.getName();
    }

    private String[] getSubclaim(Block block) {
        if(block.getType() != Material.CHEST && block.getType() != Material.TRAPPED_CHEST) return null;

        Chest chest = (Chest) block.getState();

        if(chest.getInventory().getHolder() instanceof DoubleChest) {
            DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();

            Sign sign1 = this.getSign(((BlockState) doubleChest.getLeftSide()).getBlock());
            Sign sign2 = this.getSign(((BlockState) doubleChest.getRightSide()).getBlock());

            if(sign1 != null && sign1.getLine(0).equalsIgnoreCase(Config.SUBCLAIMS_SIGN_TITLE)) return sign1.getLines();
            if(sign2 != null && sign2.getLine(0).equalsIgnoreCase(Config.SUBCLAIMS_SIGN_TITLE)) return sign2.getLines();

            return null;
        }

        Sign sign = this.getSign(chest.getBlock());
        if(sign != null && sign.getLine(0).equalsIgnoreCase(Config.SUBCLAIMS_SIGN_TITLE)) return sign.getLines();

        return null;
    }

    private void sendCreationMessage(PlayerFaction faction, Player player, Block block) {
        Location loc = block.getLocation();

        faction.sendMessage(Language.PREFIX + Language.SUBCLAIMS_SUCCESSFULLY_CREATED
            .replace("<player>", player.getName())
            .replace("<x>", String.valueOf(loc.getBlockX()))
            .replace("<y>", String.valueOf(loc.getBlockY()))
            .replace("<z>", String.valueOf(loc.getBlockZ())));
    }

    private boolean containsName(Player player, String[] lines) {
        return lines[1].equalsIgnoreCase(this.shortenName(player))
            || lines[2].equalsIgnoreCase(this.shortenName(player))
            || lines[3].equalsIgnoreCase(this.shortenName(player));
    }

    private void createSubclaim(SignChangeEvent event, PlayerFaction faction, Block block, Player player, String denyMessage, String signLine, boolean condition) {
        if(!condition) {
            event.setCancelled(true);
            player.sendMessage(Language.PREFIX + denyMessage);
            return;
        }

        event.setLine(0, Config.SUBCLAIMS_SIGN_TITLE);
        event.setLine(1, signLine);
        this.sendCreationMessage(faction, player, block);
    }

    private void checkBreak(BlockBreakEvent event, Player player, Block block, boolean chest, boolean condition) {
        if(condition) return;

        event.setCancelled(true);
        player.sendMessage(Language.PREFIX + (chest ? Language.SUBCLAIMS_CAN_NOT_DESTROY_SUBCLAIM : Language.SUBCLAIMS_CAN_NOT_DESTROY_SIGN));
    }

    private void checkOpen(PlayerInteractEvent event, Player player, Block block, boolean condition) {
        if(condition) return;

        event.setCancelled(true);
        player.sendMessage(Language.PREFIX + Language.SUBCLAIMS_CAN_NOT_OPEN);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        Block block = this.getBlockAttachedTo(event.getBlock());
        if(!this.isSubclaimable(block)) return;

        String[] lines = event.getLines();
        if(!lines[0].equalsIgnoreCase("[subclaim]") && !lines[0].equalsIgnoreCase("[s]")) return;

        Player player = event.getPlayer();

        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);

        if(playerFaction == null) {
            player.sendMessage(Language.PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        Faction factionAt = ClaimManager.getInstance().getFactionAt(block);

        if(playerFaction != factionAt) {
            event.setCancelled(true);
            player.sendMessage(Language.PREFIX + Language.SUBCLAIMS_NOT_IN_OWN_CLAIM);
            return;
        }

        if(this.getSubclaim(block) != null) {
            event.setCancelled(true);
            player.sendMessage(Language.PREFIX + Language.SUBCLAIMS_ALREADY_EXISTS);
            return;
        }

        FactionPlayer fplayer = playerFaction.getMember(player);

        switch(lines[1].toLowerCase()) {
            case "leader": {
                this.createSubclaim(event, playerFaction, block, player, Language.SUBCLAIMS_MUST_BE_LEADER,
                Config.SUBCLAIMS_LEADER_ONLY, fplayer.getRole() == Role.LEADER);
                break;
            }
            case "coleader": {
                this.createSubclaim(event, playerFaction, block, player, Language.SUBCLAIMS_MUST_BE_CO_LEADER,
                Config.SUBCLAIMS_CO_LEADERS_ONLY, fplayer.getRole().isAtLeast(Role.CO_LEADER));
                break;
            }
            case "officer":
            case "captain": {
                this.createSubclaim(event, playerFaction, block, player, Language.SUBCLAIMS_MUST_BE_CAPTAIN,
                Config.SUBCLAIMS_CAPTAINS_ONLY, fplayer.getRole().isAtLeast(Role.CAPTAIN));
                break;
            }
            default: {
                if(!fplayer.getRole().isAtLeast(Role.CAPTAIN)) return;

                event.setLine(0, Config.SUBCLAIMS_SIGN_TITLE);

                if(StringUtils.isNullOrEmpty(lines[1])) {
                    event.setLine(1, player.getName());
                }

                this.sendCreationMessage(playerFaction, player, block);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if(!this.isSubclaimable(block)) return;

        String[] lines = this.getSubclaim(block);
        if(lines == null) return;

        Player player = event.getPlayer();

        if(player.hasPermission("lazarus.subclaim.bypass")) return;
        if(Lazarus.getInstance().getEotwHandler().isActive()) return;

        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
        if(playerFaction == null) return;

        Faction factionAt = ClaimManager.getInstance().getFactionAt(block);
        if(playerFaction != factionAt || ((PlayerFaction) factionAt).isRaidable()) return;

        FactionPlayer fplayer = playerFaction.getMember(player);

        if(lines[1].equalsIgnoreCase(Config.SUBCLAIMS_LEADER_ONLY)) {
            this.checkOpen(event, player, block, fplayer.getRole() == Role.LEADER);
        } else if(lines[1].equalsIgnoreCase(Config.SUBCLAIMS_CO_LEADERS_ONLY)) {
            this.checkOpen(event, player, block, fplayer.getRole().isAtLeast(Role.CO_LEADER));
        } else if(lines[1].equalsIgnoreCase(Config.SUBCLAIMS_CAPTAINS_ONLY)) {
            this.checkOpen(event, player, block, fplayer.getRole().isAtLeast(Role.CAPTAIN));
        } else if(!this.containsName(player, lines)) {
            this.checkOpen(event, player, block, fplayer.getRole().isAtLeast(Role.CAPTAIN));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(!this.isSubclaimable(block)) return;

        String[] lines = this.getSubclaim(block);
        if(lines == null) return;

        Player player = event.getPlayer();

        if(player.hasPermission("lazarus.subclaim.bypass")) return;
        if(Lazarus.getInstance().getEotwHandler().isActive()) return;

        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
        if(playerFaction == null) return;

        Faction factionAt = ClaimManager.getInstance().getFactionAt(block);
        if(playerFaction != factionAt || ((PlayerFaction) factionAt).isRaidable()) return;

        FactionPlayer fplayer = playerFaction.getMember(player);

        if(lines[1].equalsIgnoreCase(Config.SUBCLAIMS_LEADER_ONLY)) {
            this.checkBreak(event, player, block, true, fplayer.getRole() == Role.LEADER);
        } else if(lines[1].equalsIgnoreCase(Config.SUBCLAIMS_CO_LEADERS_ONLY)) {
            this.checkBreak(event, player, block, true, fplayer.getRole().isAtLeast(Role.CO_LEADER));
        } else if(lines[1].equalsIgnoreCase(Config.SUBCLAIMS_CAPTAINS_ONLY)) {
            this.checkBreak(event, player, block, true, fplayer.getRole().isAtLeast(Role.CAPTAIN));
        } else {
            this.checkBreak(event, player, block, true, fplayer.getRole().isAtLeast(Role.CAPTAIN));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignBreakEvent(BlockBreakEvent event) {
        if(Lazarus.getInstance().getEotwHandler().isActive()) return;
        if(event.getPlayer().hasPermission("lazarus.subclaim.bypass")) return;

        Block block = event.getBlock();
        if(block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) return;

        Sign sign = (Sign) block.getState();
        if(!sign.getLine(0).equalsIgnoreCase(Config.SUBCLAIMS_SIGN_TITLE)) return;

        Player player = event.getPlayer();

        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
        if(playerFaction == null) return;

        Faction factionAt = ClaimManager.getInstance().getFactionAt(block);
        if(playerFaction != factionAt || ((PlayerFaction) factionAt).isRaidable()) return;

        String[] lines = sign.getLines();

        FactionPlayer fplayer = playerFaction.getMember(player);

        if(lines[1].equalsIgnoreCase(Config.SUBCLAIMS_LEADER_ONLY)) {
            this.checkBreak(event, player, block, false, fplayer.getRole() == Role.LEADER);
        } else if(lines[1].equalsIgnoreCase(Config.SUBCLAIMS_CO_LEADERS_ONLY)) {
            this.checkBreak(event, player, block, false, fplayer.getRole().isAtLeast(Role.CO_LEADER));
        } else if(lines[1].equalsIgnoreCase(Config.SUBCLAIMS_CAPTAINS_ONLY)) {
            this.checkBreak(event, player, block, false, fplayer.getRole().isAtLeast(Role.CAPTAIN));
        } else {
            this.checkBreak(event, player, block, false, fplayer.getRole().isAtLeast(Role.CAPTAIN));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        InventoryHolder holder = event.getSource().getHolder();
        Location location;

        if(holder instanceof Chest) {
            location = ((Chest) holder).getLocation();
        } else if(holder instanceof DoubleChest) {
            location = ((DoubleChest) holder).getLocation();
        } else {
            return;
        }

        Inventory destination = event.getDestination();
        if(destination.getType() != InventoryType.HOPPER) return;

        if(this.getSubclaim(location.getBlock()) != null) {
            event.setCancelled(true);
        }
    }
}
