package me.qiooip.lazarus.classes;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.items.ClickableItem;
import me.qiooip.lazarus.classes.manager.PvpClass;
import me.qiooip.lazarus.classes.manager.PvpClassManager;
import me.qiooip.lazarus.classes.utils.PvpClassUtils;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.scoreboard.ScoreboardManager;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class Archer extends PvpClass implements Listener {

    private final static String ARROW_FORCE_METADATA = "arrowForce";

    private final List<ClickableItem> clickables;

    public Archer(PvpClassManager manager) {
        super(manager, "Archer",
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS
        );

        this.warmup = Config.ARCHER_WARMUP;
        this.clickables = PvpClassUtils.loadClickableItems(this);
    }

    @Override
    public void disable() {
        super.disable();

        this.clickables.clear();
    }

    private ClickableItem getClickableItem(ItemStack item) {
        return this.clickables.stream().filter(clickable -> clickable.getItem().getType() == item.getType()
            && clickable.getItem().getDurability() == item.getDurability()).findFirst().orElse(null);
    }

    private float getArrowForceMetadataValue(Entity arrow) {
        List<MetadataValue> metadataValues = arrow.getMetadata(ARROW_FORCE_METADATA);

        if(metadataValues.isEmpty()) {
            return 0f;
        } else {
            return metadataValues.get(0).asFloat();
        }
    }

    private void archerTagPlayer(Player tagger, Player player) {
        TimerManager.getInstance().getArcherTagTimer().activate(player.getUniqueId());
        ScoreboardManager.getInstance().updateTabRelations(player, false);

        player.sendMessage(Language.PREFIX + Language.ARCHER_TAG_TAGGED_VICTIM
            .replace("<player>", tagger.getName())
            .replace("<seconds>", String.valueOf(Config.ARCHER_TAG_DURATION)));

        tagger.sendMessage(Language.PREFIX + Language.ARCHER_TAG_TAGGED_TAGGER
            .replace("<victim>", player.getName())
            .replace("<seconds>", String.valueOf(Config.ARCHER_TAG_DURATION)));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;

        if(!this.isActive(event.getPlayer()) || !event.hasItem()) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ClickableItem clickable = this.getClickableItem(event.getItem());
        if(clickable == null) return;

        CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

        String cooldown = clickable.getItem().getType().name();
        String potionName = StringUtils.getPotionEffectName(clickable.getPotionEffect());

        Player player = event.getPlayer();

        if(timer.isActive(player, cooldown)) {
            player.sendMessage(Language.PREFIX + Language.ARCHER_CLICKABLE_COOLDOWN
                .replace("<effect>", potionName)
                .replace("<seconds>", timer.getTimeLeft(player, cooldown)));
            return;
        }

        this.applyClickableEffect(player, clickable, true);

        timer.activate(player, cooldown, clickable.getCooldown(), Language.PREFIX
            + Language.ARCHER_COOLDOWN_EXPIRED.replace("<effect>", potionName));
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        MetadataValue metadata = new FixedMetadataValue(Lazarus.getInstance(), event.getForce());
        event.getProjectile().setMetadata(ARROW_FORCE_METADATA, metadata);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player shooter = PlayerUtils.getArrowShooter(event);
        if(shooter == null || shooter == event.getEntity() || !this.isActive(shooter)) return;

        Player player = (Player) event.getEntity();
        if(this.isActive(player) && !Config.ARCHER_TAG_CAN_TAG_OTHER_ARCHERS) return;

        float arrowForce = this.getArrowForceMetadataValue(event.getDamager());
        if(Config.ARCHER_TAG_REQUIRE_FULL_FORCE && arrowForce < 0.5) return;

        this.archerTagPlayer(shooter, player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if(!(entity instanceof Player)) return;

        Player player = (Player) entity;
        if(!TimerManager.getInstance().getArcherTagTimer().isActive(player)) return;

        event.setDamage(event.getDamage() * Config.ARCHER_TAG_DAMAGE_MULTIPLIER);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        TimerManager.getInstance().getArcherTagTimer().cancel(event.getPlayer());
        ScoreboardManager.getInstance().updateTabRelations(event.getPlayer(), false);
    }
}
