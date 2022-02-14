package me.qiooip.lazarus.handlers.logger.nms;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.games.conquest.ConquestManager;
import me.qiooip.lazarus.handlers.death.DeathMessageHandler;
import me.qiooip.lazarus.handlers.logger.CombatLogger;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;
import java.util.stream.IntStream;

public class VillagerCombatLogger_1_8 extends EntityVillager implements CombatLogger {

    @Getter private Player player;
    private final int deathban;
    private final ItemStack[] contents;
    private final ItemStack[] armor;

    private final BukkitTask removeTask;

    public VillagerCombatLogger_1_8(World world, Player player) {
        super(((CraftWorld) world).getHandle());

        this.setHealth((float) player.getHealth());
        this.setProfession(Config.COMBAT_LOGGER_VILLAGER_PROFESSION);
        this.fireProof = true;
        this.persistent = true;

        this.player = player;
        this.deathban = Lazarus.getInstance().getDeathbanManager().getBanTime(player);
        this.contents = player.getInventory().getContents();
        this.armor = player.getInventory().getArmorContents();

        this.setEquipment(0, CraftItemStack.asNMSCopy(player.getItemInHand()));
        IntStream.rangeClosed(0, 3).forEach(i -> this.setEquipment(i + 1, CraftItemStack.asNMSCopy(this.armor[i])));

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        player.getActivePotionEffects().forEach(effect -> {
            if(effect.getDuration() > 12000) return;
            this.addEffect(entityPlayer.getEffect(MobEffectList.byId[effect.getType().getId()]));
        });

        this.setCustomName(Config.COMBAT_LOGGER_NAME_FORMAT.replace("<player>", player.getName()));
        this.setCustomNameVisible(true);

        Location loc = player.getLocation();
        double locY = loc.getBlock().getType() == Material.AIR ? loc.getY() : Math.floor(loc.getY()) + 1;

        this.setPositionRotation(loc.getX(), locY, loc.getZ(), loc.getYaw(), loc.getPitch());

        ((CraftWorld) world).getHandle().addEntity(this, SpawnReason.CUSTOM);

        this.removeTask = Tasks.syncLater(() -> {
            Lazarus.getInstance().getCombatLoggerHandler().removeCombatLogger(this.player.getUniqueId());
            this.bukkitEntity.remove();
            this.player = null;
        }, Config.COMBAT_LOGGER_TIME * 20L);
    }

    @Override
    public void removeCombatLogger() {
        this.removeTask.cancel();
        this.bukkitEntity.remove();
        this.player = null;
    }

    @Override
    public float getCombatLoggerHealth() {
        return this.getHealth();
    }

    @Override
    public Location getCombatLoggerLocation() {
        return this.getBukkitEntity().getLocation();
    }

    @Override
    public void move(double d0, double d1, double d2) {
        super.move(0, d1, 0);
    }

    @Override
    public EntityHuman v_() {
        return null;
    }

    @Override
    public void c(int i) {

    }

    @Override
    public EntityLiving getGoalTarget() {
        return null;
    }

    @Override
    public void collide(Entity entity) {

    }

    @Override
    protected void dropDeathLoot(boolean flag, int i) {

    }

    @Override
    public EntityAgeable createChild(EntityAgeable entityAgeable) {
        return null;
    }

    @Override
    public void handleEffectChanges(Player player) {
        player.getActivePotionEffects().forEach(effect -> {
            MobEffect nmsEffect = this.getEffect(MobEffectList.byId[effect.getType().getId()]);

            if(nmsEffect == null) {
                Lazarus.getInstance().getPvpClassManager().getPotionEffectRestorer()
                    .removePlayerEffect(player, effect.getType());
                return;
            }

            NmsUtils.getInstance().addPotionEffect(player, new PotionEffect(PotionEffectType.getById(nmsEffect
                .getEffectId()), nmsEffect.getDuration(), nmsEffect.getAmplifier(), nmsEffect.isAmbient()));
        });
    }

    @Override
    public boolean damageEntity(DamageSource damageSource, float amount) {
        if(!this.isAlive()) return false;

        if(damageSource instanceof EntityDamageSourceIndirect && damageSource.isMagic()) {
            return false;
        }

        Entity damageSourceEntity = damageSource.getEntity();
        if(damageSourceEntity != null && this.player == damageSourceEntity.getBukkitEntity()) return false;

        if(damageSourceEntity instanceof EntityPlayer) {
            Player damager = (Player) damageSourceEntity.getBukkitEntity();

            if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(damager)) {
                return false;
            }

            if(TimerManager.getInstance().getPvpProtTimer().isActive(damager)) {
                damager.sendMessage(Language.PREFIX + Language.PVP_PROT_PVP_DENY_VICTIM);
                return false;
            }

            if(Lazarus.getInstance().getStaffModeManager().isInStaffMode(damager)) {
                damager.sendMessage(Language.PREFIX + Language.STAFF_MODE_DAMAGE_DENY);
                return false;
            }

            if(Lazarus.getInstance().getVanishManager().isVanished(damager)) {
                damager.sendMessage(Language.PREFIX + Language.VANISH_DAMAGE_DENY);
                return false;
            }

            PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(this.player);

            if(playerFaction != null) {
                PlayerFaction damagerFaction = FactionsManager.getInstance().getPlayerFaction(damager);

                if(playerFaction == damagerFaction) {
                    return false;
                }

                if(!Config.FACTION_ALLY_FRIENDLY_FIRE && playerFaction.isAlly(damagerFaction)) {
                    damager.sendMessage(Language.FACTIONS_DENY_DAMAGE_ALLIES
                        .replace("<player>", Config.ALLY_COLOR + this.player.getName()));
                    return false;
                }
            }

            TimerManager.getInstance().getCombatTagTimer().activate(damager.getUniqueId());
        }

        return super.damageEntity(damageSource, amount);
    }

    @Override
    public void die(DamageSource damageSource) {
        this.dropPlayerItems(this.contents, this.armor, this.getCombatLoggerLocation());

        String reason;
        Entity damager = damageSource.getEntity();

        if(damager instanceof EntityPlayer) {
            Player killer = ((EntityPlayer) damager).getBukkitEntity();
            Lazarus.getInstance().getKillstreakHandler().checkKillerKillstreak(killer);

            PlayerFaction killerFaction = FactionsManager.getInstance().getPlayerFaction(killer);

            if(killerFaction != null) {
                killerFaction.incrementPoints(Config.FACTION_TOP_KILL);
            }

            DeathMessageHandler deathMessageHandler = Lazarus.getInstance().getDeathMessageHandler();

            reason = Language.DEATHMESSAGE_REASON_COMBATLOGGER_KILLER
                .replace("<player>", deathMessageHandler.getPlayerName(this.player))
                .replace("<killer>", deathMessageHandler.getKillerName(killer));

            Lazarus.getInstance().getUserdataManager().getUserdata(killer).updateKillStats(reason);
        } else {
            reason = Language.DEATHMESSAGE_REASON_COMBATLOGGER.replace("<player>",
                Lazarus.getInstance().getDeathMessageHandler().getPlayerName(this.player));
        }

        Bukkit.getOnlinePlayers().forEach(online -> {
            if((damager != null && damager.getBukkitEntity() != online) && !Lazarus.getInstance()
                .getUserdataManager().getUserdata(online).getSettings().isDeathMessages()) return;

            online.sendMessage(reason);
        });

        UUID playerUUID = this.player.getUniqueId();

        Tasks.async(() -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            Userdata userdata = Lazarus.getInstance().getUserdataManager().getUserdata(offlinePlayer);

            userdata.updateDeathStats(reason);
            userdata.resetKillstreak();
        });

        Lazarus.getInstance().getDeathbanManager().deathbanPlayer(this.player,
        this.bukkitEntity.getLocation(), this.deathban, reason);

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(this.player);

        if(faction != null) {
            faction.onDeath(this.player);

            ConquestManager conquestManager = Lazarus.getInstance().getConquestManager();

            if(conquestManager.isActive()) {
                conquestManager.onDeath(this.player, faction);
            }
        }

        super.die(damageSource);

        EntityPlayer entityPlayer = ((CraftPlayer) this.player).getHandle();

        entityPlayer.getBukkitEntity().getInventory().clear();
        entityPlayer.getBukkitEntity().getInventory().setArmorContents(null);
        entityPlayer.setPosition(this.locX, this.locY, this.locZ);
        entityPlayer.setHealth(0);
        Tasks.async(() -> entityPlayer.getBukkitEntity().saveData());

        Lazarus.getInstance().getCombatLoggerHandler().removeCombatLogger(this.player.getUniqueId());

        this.removeTask.cancel();
        this.player = null;
    }
}
