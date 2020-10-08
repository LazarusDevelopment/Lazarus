package me.qiooip.lazarus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.classes.manager.PvpClassManager;
import me.qiooip.lazarus.commands.manager.CommandManager;
import me.qiooip.lazarus.commands.manager.SubCommandExecutor;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.database.MongoManager;
import me.qiooip.lazarus.database.impl.MongoClaimManager;
import me.qiooip.lazarus.database.impl.MongoFactionsManager;
import me.qiooip.lazarus.database.impl.MongoUserdataManager;
import me.qiooip.lazarus.deathban.DeathbanManager;
import me.qiooip.lazarus.economy.EconomyManager;
import me.qiooip.lazarus.economy.SignShopManager;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.commands.FactionsCommandExecutor;
import me.qiooip.lazarus.games.conquest.ConquestManager;
import me.qiooip.lazarus.games.conquest.commands.ConquestCommandExecutor;
import me.qiooip.lazarus.games.dragon.EnderDragonManager;
import me.qiooip.lazarus.games.dragon.commands.EnderDragonCommandExecutor;
import me.qiooip.lazarus.games.dtc.DtcManager;
import me.qiooip.lazarus.games.dtc.commands.DtcCommandExecutor;
import me.qiooip.lazarus.games.king.KillTheKingManager;
import me.qiooip.lazarus.games.king.commands.KillTheKingCommandExecutor;
import me.qiooip.lazarus.games.koth.KothManager;
import me.qiooip.lazarus.games.koth.commands.KothCommandExecutor;
import me.qiooip.lazarus.games.loot.LootManager;
import me.qiooip.lazarus.games.loot.commands.LootCommandExecutor;
import me.qiooip.lazarus.games.mountain.MountainManager;
import me.qiooip.lazarus.games.mountain.commands.MountainCommandExecutor;
import me.qiooip.lazarus.games.schedule.ScheduleManager;
import me.qiooip.lazarus.games.schedule.commands.ScheduleCommandExecutor;
import me.qiooip.lazarus.glass.GlassManager;
import me.qiooip.lazarus.handlers.BottleHandler;
import me.qiooip.lazarus.handlers.InventoryHandler;
import me.qiooip.lazarus.handlers.MapkitHandler;
import me.qiooip.lazarus.handlers.StatsHandler;
import me.qiooip.lazarus.handlers.block.AutoSmeltHandler;
import me.qiooip.lazarus.handlers.block.CrowbarHandler;
import me.qiooip.lazarus.handlers.block.FilterHandler;
import me.qiooip.lazarus.handlers.chat.ChatControlHandler;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import me.qiooip.lazarus.handlers.chat.MessagingHandler;
import me.qiooip.lazarus.handlers.death.DeathMessageHandler;
import me.qiooip.lazarus.handlers.kitmap.KillstreakHandler;
import me.qiooip.lazarus.handlers.limiter.PotionLimiterHandler;
import me.qiooip.lazarus.handlers.logger.CombatLoggerHandler;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.handlers.manager.HandlerManager;
import me.qiooip.lazarus.handlers.portal.EndPortalHandler;
import me.qiooip.lazarus.handlers.rank.RankReviveHandler;
import me.qiooip.lazarus.handlers.rank.ReclaimHandler;
import me.qiooip.lazarus.handlers.restore.InventoryData;
import me.qiooip.lazarus.handlers.restore.InventoryRestoreManager;
import me.qiooip.lazarus.handlers.salvage.SalvageHandler;
import me.qiooip.lazarus.handlers.settings.SettingsHandler;
import me.qiooip.lazarus.handlers.staff.FreezeHandler;
import me.qiooip.lazarus.handlers.staff.NotesHandler;
import me.qiooip.lazarus.handlers.staff.RebootHandler;
import me.qiooip.lazarus.handlers.staff.StaffChatHandler;
import me.qiooip.lazarus.handlers.staff.WarpsHandler;
import me.qiooip.lazarus.handlers.timer.EotwHandler;
import me.qiooip.lazarus.handlers.timer.SotwHandler;
import me.qiooip.lazarus.integration.vault.Economy_Lazarus;
import me.qiooip.lazarus.kits.KitsManager;
import me.qiooip.lazarus.kits.commands.KitCommandExecutor;
import me.qiooip.lazarus.lunarclient.LunarClientManager;
import me.qiooip.lazarus.scoreboard.ScoreboardManager;
import me.qiooip.lazarus.selection.SelectionManager;
import me.qiooip.lazarus.staffmode.StaffModeManager;
import me.qiooip.lazarus.staffmode.VanishManager;
import me.qiooip.lazarus.tab.TabManager;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.userdata.UserdataManager;
import me.qiooip.lazarus.userdata.settings.Settings;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.Datastore;
import me.qiooip.lazarus.utils.GsonUtils;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.gson.FactionTypeAdapter;
import me.qiooip.lazarus.utils.gson.LocationTypeAdapter;
import me.qiooip.lazarus.utils.gson.LootTypeAdapter;
import me.qiooip.lazarus.utils.gson.PlayerTypeAdapter;
import me.qiooip.lazarus.utils.gson.RestoreTypeAdapter;
import me.qiooip.lazarus.utils.gson.SettingsTypeAdapter;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Getter
public class Lazarus extends JavaPlugin {

    @Getter private static Lazarus instance;
    @Setter private boolean fullyEnabled;

    private Gson gson;

    @Setter private ConfigFile config;
    @Setter private ConfigFile language;
    @Setter private ConfigFile scoreboardFile;
    @Setter private ConfigFile tabFile;
    @Setter private ConfigFile classesFile;
    @Setter private ConfigFile limitersFile;
    @Setter private ConfigFile abilitiesFile;
    @Setter private ConfigFile itemsFile;
    private ConfigFile utilitiesFile;

    @Setter private MongoManager mongoManager;

    @Setter private UserdataManager userdataManager;
    private DeathbanManager deathbanManager;
    private EconomyManager economyManager;
    private GlassManager glassManager;
    private InventoryRestoreManager inventoryRestoreManager;
    private KitsManager kitsManager;
    private LunarClientManager lunarClientManager;
    private PvpClassManager pvpClassManager;
    private SelectionManager selectionManager;
    private SignShopManager signShopManager;
    private StaffModeManager staffModeManager;
    private VanishManager vanishManager;
    private AbilitiesManager abilitiesManager;
    private ScoreboardManager scoreboardManager;
    @Setter private TabManager tabManager;
    private HandlerManager handlerManager;

    private ConquestManager conquestManager;
    private EnderDragonManager enderDragonManager;
    private DtcManager dtcManager;
    private KillTheKingManager killTheKingManager;
    private KothManager kothManager;
    private MountainManager mountainManager;
    private LootManager lootManager;
    private ScheduleManager scheduleManager;

    private CommandManager commandManager;
    private ConquestCommandExecutor conquestCommandExecutor;
    private EnderDragonCommandExecutor enderDragonCommandExecutor;
    private DtcCommandExecutor dtcCommandExecutor;
    private FactionsCommandExecutor factionsCommandExecutor;
    private KitCommandExecutor kitCommandExecutor;
    private KillTheKingCommandExecutor killTheKingCommandExecutor;
    private KothCommandExecutor kothCommandExecutor;
    private MountainCommandExecutor mountainCommandExecutor;
    private LootCommandExecutor lootCommandExecutor;
    private ScheduleCommandExecutor scheduleCommandExecutor;

    private AutoSmeltHandler autoSmeltHandler;
    private BottleHandler bottleHandler;
    private ChatControlHandler chatControlHandler;
    private CombatLoggerHandler combatLoggerHandler;
    private CrowbarHandler crowbarHandler;
    private DeathMessageHandler deathMessageHandler;
    private EndPortalHandler endPortalHandler;
    private EotwHandler eotwHandler;
    private FilterHandler filterHandler;
    private FreezeHandler freezeHandler;
    private InventoryHandler inventoryHandler;
    private KillstreakHandler killstreakHandler;
    private MapkitHandler mapkitHandler;
    private MessagingHandler messagingHandler;
    private NotesHandler notesHandler;
    private PotionLimiterHandler potionLimiterHandler;
    private RankReviveHandler rankReviveHandler;
    private RebootHandler rebootHandler;
    private ReclaimHandler reclaimHandler;
    private SalvageHandler salvageHandler;
    private SettingsHandler settingsHandler;
    private SotwHandler sotwHandler;
    private StaffChatHandler staffChatHandler;
    private StatsHandler statsHandler;
    private WarpsHandler warpsHandler;

    @Override
    public void onEnable() {
        instance = this;

        Config.START_TIME = System.currentTimeMillis();
        NmsUtils.init();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.log("&3===&b=============================================&3===");
        this.log("- &bName&7: Lazarus");
        this.log("- &bVersion&7: " + this.getDescription().getVersion());
        this.log("- &bAuthor&7: qIooIp");

        try {
            this.config = new ConfigFile("config.yml");
            this.language = new ConfigFile("language.yml");
            this.scoreboardFile = new ConfigFile("scoreboard.yml");
            this.tabFile = new ConfigFile("tab.yml");
            this.classesFile = new ConfigFile("classes.yml");
            this.limitersFile = new ConfigFile("limiters.yml");
            this.abilitiesFile = new ConfigFile("abilities.yml");
            this.itemsFile = new ConfigFile("items.yml");
            this.utilitiesFile = new ConfigFile("utilities.yml");
        } catch(RuntimeException e) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.log("");
        this.log("- &7Using &c" + Datastore.DATASTORE + " &7datastore.");
        this.log("");

        this.beforeInit();

        if(!this.checkLunarClientAPI()) return;

        try {
            this.setupDatastore();
            
            this.setupManagers();
            this.setupHandlers();
        } catch(Exception e) {
            this.log("&4===&c=============================================&4===");
            this.log("   &cError occurred while enabling Lazarus. Error:");
            this.log("");

            e.printStackTrace();

            this.log("");
            this.log("&4===&c=============================================&4===");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if(!this.fullyEnabled) return;

        this.log("&3===&b=============================================&3===");
        this.log("- &7Disabling Lazarus " + this.getDescription().getVersion());
        this.log("");

        this.scoreboardManager.disable();
        if(Config.TAB_ENABLED) this.tabManager.disable();

        ClaimManager.getInstance().disable();
        FactionsManager.getInstance().disable();
        TimerManager.getInstance().disable();
        this.userdataManager.disable();

        this.disableManagers();
        this.disableGameManagers();
        this.disableCommandManagers();

        if(Datastore.DATASTORE == Datastore.MONGO) {
            this.mongoManager.disable();
        }

        NmsUtils.getInstance().disable();
        Bukkit.getServicesManager().unregisterAll(this);

        this.log("&3===&b=============================================&3===");
    }

    private void disableManagers() {
        this.deathbanManager.disable();
        this.handlerManager.disable();
        this.glassManager.disable();
        this.inventoryRestoreManager.disable();
        this.kitsManager.disable();
        this.pvpClassManager.disable();
        this.selectionManager.disable();
        this.signShopManager.disable();
        this.staffModeManager.disable();
        this.vanishManager.disable();
        this.abilitiesManager.disable();

        if(this.lunarClientManager != null) {
            this.lunarClientManager.disable();
        }
    }

    private void disableGameManagers() {
        this.conquestManager.disable();
        this.enderDragonManager.disable();
        this.dtcManager.disable();
        this.killTheKingManager.disable();
        this.kothManager.disable();
        this.mountainManager.disable();
        this.lootManager.disable();
        this.scheduleManager.disable();
    }

    private void disableCommandManagers() {
        this.commandManager.disable();
        this.conquestCommandExecutor.disable();
        this.enderDragonCommandExecutor.disable();
        this.dtcCommandExecutor.disable();
        this.factionsCommandExecutor.disable();
        this.kitCommandExecutor.disable();
        this.killTheKingCommandExecutor.disable();
        this.kothCommandExecutor.disable();
        this.mountainCommandExecutor.disable();
        this.lootCommandExecutor.disable();
        this.scheduleCommandExecutor.disable();
    }

    private void beforeInit() {
        ChatHandler.setup();
        ItemUtils.setupItemStackMaps();
        PlayerUtils.setupMetadataValue();

        this.registerGson();
        this.registerVaultSupport();

        new Config();
        new Language();

        if(Config.TAB_ENABLED && Bukkit.getMaxPlayers() < 60) {
            this.log("&4===&c=============================================&4===");
            this.log("     &cIncrease your slot number to at least 60");
            this.log("          &cfor your tab to work properly!");
            this.log("&4===&c=============================================&4===");
            this.log("");
        }
    }
    
    private boolean checkLunarClientAPI() {
        if(Config.LUNAR_CLIENT_API_ENABLED && !Bukkit.getPluginManager().isPluginEnabled("LunarClient-API")) {
            this.log("&4===&c=============================================&4===");
            this.log("       &cLunar client support is enabled, but");
            this.log("            &cLunarClient-API is missing!");
            this.log("&4===&c=============================================&4===");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        
        return true;
    }

    private void setupDatastore() {
        if(Datastore.DATASTORE == Datastore.JSON) {
            new FactionsManager();
            new ClaimManager();
            this.userdataManager = new UserdataManager();
        } else {
            this.mongoManager = new MongoManager();

            new MongoFactionsManager();
            new MongoClaimManager();
            this.userdataManager = new MongoUserdataManager();
        }

        new TimerManager();
    }

    private void setupManagers() throws Exception {
        for(Field field : this.getClass().getDeclaredFields()) {
            if(!ManagerEnabler.class.isAssignableFrom(field.getType()) && field.getType().getSuperclass() != SubCommandExecutor.class) continue;

            field.setAccessible(true);

            Constructor<?> constructor = field.getType().getDeclaredConstructor();
            field.set(this, constructor.newInstance());
        }

        if(Config.TAB_ENABLED) {
            this.tabManager = new TabManager();
        }

        if(Config.LUNAR_CLIENT_API_ENABLED && Bukkit.getPluginManager().isPluginEnabled("LunarClient-API")) {
            this.lunarClientManager = new LunarClientManager();
        }
    }

    private void setupHandlers() throws Exception {
        for(Field field : this.getClass().getDeclaredFields()) {
            if(field.getType().getSuperclass() != Handler.class) continue;

            field.setAccessible(true);
            field.set(this, this.handlerManager.getHandler(field.getType()));
        }

        this.log("");
        this.log("- &bIssue tracker&7: github.com/qIooIp/Lazarus-Issues");
        this.log("- &bEnabled. Took &a" + (System.currentTimeMillis() - Config.START_TIME) + " &bms.");
        this.log("&3===&b=============================================&3===");

        this.fullyEnabled = true;
    }

    public void registerGson() {
        this.gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
            .enableComplexMapKeySerialization().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
            .registerTypeAdapter(GsonUtils.PLAYER_TYPE, new PlayerTypeAdapter())
            .registerTypeAdapter(GsonUtils.FACTION_TYPE, new FactionTypeAdapter())
            .registerTypeAdapter(GsonUtils.LOOT_TYPE, new LootTypeAdapter())
            .registerTypeAdapter(Location.class, new LocationTypeAdapter())
            .registerTypeAdapter(InventoryData.class, new RestoreTypeAdapter())
            .registerTypeAdapter(Settings.class, new SettingsTypeAdapter())
        .create();
    }

    public void registerVaultSupport() {
        Plugin vaultPlugin = Bukkit.getPluginManager().getPlugin("Vault");
        if(vaultPlugin == null || !vaultPlugin.isEnabled()) return;

        Bukkit.getServicesManager().register(Economy.class, new Economy_Lazarus(), this, ServicePriority.Highest);
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(Color.translate(message));
    }

    public ClassLoader getPluginClassLoader() {
        return this.getClassLoader();
    }
}
