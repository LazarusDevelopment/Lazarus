package me.qiooip.lazarus.handlers.death;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.userdata.UserdataManager;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathMessageHandler extends Handler implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        String deathMessage = this.getDeathMessage(player);
        event.setDeathMessage(deathMessage);

        UserdataManager userdataManager = Lazarus.getInstance().getUserdataManager();

        Bukkit.getOnlinePlayers().forEach(online -> {
            Userdata userdata = userdataManager.getUserdata(online);
            boolean deathMessages = userdata.getSettings().isDeathMessages();

            if(player == online || player.getKiller() == online || deathMessages) {
                online.sendMessage(deathMessage);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cancelDeathMessage(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    private String getDeathMessage(Player player) {
        DamageCause cause = player.getLastDamageCause() == null
            ? DamageCause.CUSTOM
            : player.getLastDamageCause().getCause();

        String message = "";

        if(player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) player.getLastDamageCause();

            switch(cause) {
                case ENTITY_ATTACK: {
                    message = this.getEntityAttackDeathMessage(player, damageEvent);
                    break;
                }
                case PROJECTILE: {
                    message = this.getProjectileDeathMessage(player, damageEvent);
                    break;
                }
                case ENTITY_EXPLOSION: {
                    message = this.getEntityExplosionDeathMessage(player, damageEvent);
                    break;
                }
                case FALLING_BLOCK: {
                    message = Language.DEATHMESSAGE_REASON_FALLING_BLOCK.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case LIGHTNING: {
                    message = Language.DEATHMESSAGE_REASON_LIGHTNING.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case FALL: {
                    message = Language.DEATHMESSAGE_REASON_FALL.replace("<player>", this.getPlayerName(player));
                    break;
                }
            }
        } else {
            switch(cause) {
                case BLOCK_EXPLOSION: {
                    message = Language.DEATHMESSAGE_REASON_BLOCK_EXPLOSION.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case CONTACT: {
                    message = Language.DEATHMESSAGE_REASON_CONTACT.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case DROWNING: {
                    message = Language.DEATHMESSAGE_REASON_DROWNING.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case FIRE: {
                    message = Language.DEATHMESSAGE_REASON_FIRE.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case FIRE_TICK: {
                    message = Language.DEATHMESSAGE_REASON_FIRE_TICK.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case LAVA: {
                    message = Language.DEATHMESSAGE_REASON_LAVA.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case MAGIC: {
                    message = Language.DEATHMESSAGE_REASON_MAGIC.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case MELTING: {
                    message = Language.DEATHMESSAGE_REASON_MELTING.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case POISON: {
                    message = Language.DEATHMESSAGE_REASON_POISON.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case STARVATION: {
                    message = Language.DEATHMESSAGE_REASON_STARVATION.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case SUFFOCATION: {
                    message = Language.DEATHMESSAGE_REASON_SUFFOCATION.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case SUICIDE: {
                    message = Language.DEATHMESSAGE_REASON_SUICIDE.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case THORNS: {
                    message = Language.DEATHMESSAGE_REASON_THORNS.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case WITHER: {
                    message = Language.DEATHMESSAGE_REASON_WITHER.replace("<player>", this.getPlayerName(player));
                    break;
                }
                case FALL: {
                    message = this.getFallDeathMessage(player);
                    break;
                }
                case VOID: {
                    message = this.getVoidDeathMessage(player);
                    break;
                }
                default: {
                    message = Language.DEATHMESSAGE_REASON_CUSTOM.replace("<player>", this.getPlayerName(player));
                    break;
                }
            }
        }

        return message;
    }

    public String getPlayerName(OfflinePlayer player) {
        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);

        return Language.DEATHMESSAGE_PLAYER_NAME_FORMAT
            .replace("<player>", player.getName())
            .replace("<kills>", String.valueOf(data.getKills()));
    }

    public String getKillerName(Player killer) {
        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(killer);

        return Language.DEATHMESSAGE_KILLER_NAME_FORMAT
            .replace("<killer>", killer.getName())
            .replace("<kills>", data == null ? "?" : String.valueOf(data.getKills() + 1));
    }

    private String getEntityAttackDeathMessage(Player player, EntityDamageByEntityEvent damageEvent) {
        Entity damager = damageEvent.getDamager();

        if(damager instanceof Player) {
            Player playerDamager = (Player) damager;
            ItemStack handItem = playerDamager.getItemInHand();

            if(handItem != null && handItem.getType() != Material.AIR) {
                return Language.DEATHMESSAGE_REASON_ENTITY_ATTACK_PLAYER_ITEM
                    .replace("<player>", this.getPlayerName(player))
                    .replace("<killer>", this.getKillerName(playerDamager))
                    .replace("<item>", NmsUtils.getInstance().getItemName(handItem));
            } else {
                return Language.DEATHMESSAGE_REASON_ENTITY_ATTACK_PLAYER_NO_ITEM
                    .replace("<player>", this.getPlayerName(player))
                    .replace("<killer>", this.getKillerName(playerDamager));
            }
        } else {
            return Language.DEATHMESSAGE_REASON_ENTITY_ATTACK_ENTITY
                .replace("<player>", this.getPlayerName(player))
                .replace("<entity>", StringUtils.getEntityName(damager.getType().name()));
        }
    }

    private String getProjectileDeathMessage(Player player, EntityDamageByEntityEvent damageEvent) {
        Projectile projectile = (Projectile) damageEvent.getDamager();

        if(projectile.getShooter() instanceof Player) {
            Player playerShooter = (Player) projectile.getShooter();
            ItemStack handItem = playerShooter.getItemInHand();

            if(handItem != null && handItem.getType() != Material.AIR) {
                return Language.DEATHMESSAGE_REASON_PROJECTILE_PLAYER_ITEM
                    .replace("<player>", this.getPlayerName(player))
                    .replace("<killer>", this.getKillerName(playerShooter))
                    .replace("<item>", NmsUtils.getInstance().getItemName(handItem));
            } else {
                return Language.DEATHMESSAGE_REASON_PROJECTILE_PLAYER_NO_ITEM
                    .replace("<player>", this.getPlayerName(player))
                    .replace("<killer>", this.getKillerName(playerShooter));
            }
        } else {
            Entity entityShooter = (Entity) projectile.getShooter();

            return Language.DEATHMESSAGE_REASON_PROJECTILE_ENTITY
                .replace("<player>", this.getPlayerName(player))
                .replace("<entity>", StringUtils.getEntityName(entityShooter.getType().name()));
        }
    }

    private String getEntityExplosionDeathMessage(Player player, EntityDamageByEntityEvent damageEvent) {
        Entity damager = damageEvent.getDamager();

        if(damager instanceof TNTPrimed) {
            return Language.DEATHMESSAGE_REASON_BLOCK_EXPLOSION
                .replace("<player>", this.getPlayerName(player));
        } else {
            return Language.DEATHMESSAGE_REASON_ENTITY_EXPLOSION
                .replace("<player>", this.getPlayerName(player));
        }
    }

    private String getFallDeathMessage(Player player) {
        Player killer = player.getKiller();

        if(killer != null && player != killer) {
            return Language.DEATHMESSAGE_REASON_FALL_KILLER
                .replace("<player>", this.getPlayerName(player))
                .replace("<killer>", this.getKillerName(killer));
        } else {
            return Language.DEATHMESSAGE_REASON_FALL
            .replace("<player>", this.getPlayerName(player));
        }
    }

    private String getVoidDeathMessage(Player player) {
        Player killer = player.getKiller();

        if(killer != null && player != killer) {
            return Language.DEATHMESSAGE_REASON_VOID_KILLER
                .replace("<player>", this.getPlayerName(player))
                .replace("<killer>", this.getKillerName(killer));
        } else {
            return Language.DEATHMESSAGE_REASON_VOID
                .replace("<player>", this.getPlayerName(player));
        }
    }
}
