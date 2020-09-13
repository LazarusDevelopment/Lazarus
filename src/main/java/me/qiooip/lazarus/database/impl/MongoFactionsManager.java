package me.qiooip.lazarus.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.ChatType;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.event.FactionDisbandEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent.LeaveReason;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.RoadFaction;
import me.qiooip.lazarus.factions.type.SpawnFaction;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.GsonUtils;
import me.qiooip.lazarus.utils.LocationUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bson.Document;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class MongoFactionsManager extends FactionsManager {

    private MongoCollection<Document> getFactionsRepo() {
        return Lazarus.getInstance().getMongoManager().getFactionsRepo();
    }

    private MongoCollection<Document> getPlayersRepo() {
        return Lazarus.getInstance().getMongoManager().getPlayersRepo();
    }

    @Override
    public void loadFactions() {
        super.factions = new HashMap<>();

        try(MongoCursor<Document> cursor = this.getFactionsRepo().find().iterator()) {
            while(cursor.hasNext()) {
                Faction faction = this.factionFromDocument(cursor.next());
                super.factions.put(faction.getId(), faction);
                super.factionNames.put(faction.getName(), faction.getId());

                if(faction instanceof RoadFaction) {
                    ((RoadFaction) faction).setupDisplayName();
                }

                if(faction instanceof SpawnFaction) {
                    SpawnFaction spawnFaction = (SpawnFaction) faction;

                    spawnFaction.setSafezone(true);
                    spawnFaction.setDeathban(false);
                }
            }
        }

        Lazarus.getInstance().log("- &7Loaded &a" + super.factions.size() + " &7factions.");
    }

    @Override
    public void saveFactions(boolean log) {
        if(super.factions == null || super.factions.isEmpty()) return;

        ReplaceOptions options = new ReplaceOptions().upsert(true);
        List<WriteModel<Document>> toWrite = new ArrayList<>();

        super.factions.values().forEach(faction -> {
            Document data = this.factionToDocument(faction);
            toWrite.add(new ReplaceOneModel<>(Filters.eq("_id", data.get("_id")), data, options));
        });

        this.getFactionsRepo().bulkWrite(toWrite, new BulkWriteOptions().ordered(false));

        if(log) {
            Lazarus.getInstance().log("- &7Saved &a" + super.factions.size() + " &7factions.");
        }
    }

    @Override
    public void loadPlayers() {
        super.players = new HashMap<>();

        try(MongoCursor<Document> cursor = this.getPlayersRepo().find().iterator()) {
            while(cursor.hasNext()) {
                FactionPlayer fplayer = this.playerFromDocument(cursor.next());
                PlayerFaction faction = fplayer.getFaction();

                if(faction != null) {
                    super.players.put(fplayer.getUuid(), fplayer);
                    faction.addMember(fplayer);
                }
            }
        }

        Lazarus.getInstance().log("- &7Loaded &a" + super.players.size() + " &7players.");
    }

    @Override
    public void savePlayers(boolean log) {
        if(super.players == null || super.players.isEmpty()) return;

        ReplaceOptions options = new ReplaceOptions().upsert(true);
        List<WriteModel<Document>> toWrite = new ArrayList<>();

        super.players.values().forEach(fplayer -> {
            Document data = this.playerToDocument(fplayer);
            toWrite.add(new ReplaceOneModel<>(Filters.eq("_id", data.get("_id")), data, options));
        });

        this.getPlayersRepo().bulkWrite(toWrite, new BulkWriteOptions().ordered(false));

        if(log) {
            Lazarus.getInstance().log("- &7Saved &a" + this.players.size() + " &7players.");
        }
    }

    @Override
    public void deleteAllPlayerFactions() {
        this.getPlayersRepo().drop();
        this.players.clear();

        int factionsSize = super.factions.size();
        Iterator<Faction> iterator = super.factions.values().iterator();

        while(iterator.hasNext()) {
            Faction faction = iterator.next();
            if(faction instanceof SystemFaction) continue;

            super.factionNames.remove(faction.getName());
            iterator.remove();
        }

        this.getFactionsRepo().drop();
        this.saveFactions(false);

        Lazarus.getInstance().log("- &cDeleted &e" + (factionsSize - super.factions.size()) + " &cplayer factions.");
    }

    private Document factionToDocument(Faction faction) {
        if(faction instanceof PlayerFaction) {
            PlayerFaction playerFaction = (PlayerFaction) faction;

            return new Document("_id", faction.getId())
                .append("name", faction.getName())
                .append("deathban", faction.isDeathban())
                .append("dtr", playerFaction.getDtr())
                .append("lastDtrUpdate", playerFaction.getLastDtrUpdate())
                .append("announcement", playerFaction.getAnnouncement())
                .append("balance", playerFaction.getBalance())
                .append("lives", playerFaction.getLives())
                .append("points", playerFaction.getPoints())
                .append("home", LocationUtils.locationToString(playerFaction.getHome()))
                .append("open", playerFaction.isOpen())
                .append("autoRevive", playerFaction.isAutoRevive())
                .append("friendlyFire", playerFaction.isFriendlyFire())
                .append("allies", playerFaction.getAllies());
        } else {
            SystemFaction systemFaction = (SystemFaction) faction;

            return new Document("_id", faction.getId())
                .append("type", GsonUtils.getFactionType(faction.getClass()))
                .append("name", faction.getName())
                .append("deathban", faction.isDeathban())
                .append("safezone", systemFaction.isSafezone())
                .append("enderpearls", systemFaction.isEnderpearls())
                .append("color", systemFaction.getColor().replace('§', '&'));
        }
    }

    @SuppressWarnings("unchecked")
    private Faction factionFromDocument(Document document) {
        if(document.getString("type") == null) {
            PlayerFaction faction = new PlayerFaction();

            faction.setId((UUID) document.get("_id"));
            faction.setName(document.getString("name"));
            faction.setDeathban(document.getBoolean("deathban"));
            faction.setDtr(document.getDouble("dtr"));
            faction.setLastDtrUpdate(document.getLong("lastDtrUpdate"));
            faction.setAnnouncement(document.getString("announcement"));
            faction.setBalance(document.getInteger("balance"));
            faction.setLives(document.getInteger("lives"));
            faction.setPoints(document.getInteger("points"));
            faction.setHome(LocationUtils.stringToLocation(document.getString("home")));
            faction.setOpen(document.getBoolean("open"));
            faction.setAutoRevive(document.getBoolean("autoRevive"));
            faction.setFriendlyFire(document.getBoolean("friendlyFire"));
            faction.setAllies((List<UUID>) document.get("allies"));

            return faction;
        } else {
            SystemFaction faction = null;

            try {
                faction = (SystemFaction) GsonUtils.getFactionClass(document.getString("type")).newInstance();

                faction.setId((UUID) document.get("_id"));
                faction.setName(document.getString("name"));
                faction.setDeathban(document.getBoolean("deathban"));
                faction.setSafezone(document.getBoolean("safezone"));
                faction.setEnderpearls(document.getBoolean("enderpearls"));
                faction.setColor(Color.translate(document.getString("color")));

            } catch(InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return faction;
        }
    }

    private Document playerToDocument(FactionPlayer fplayer) {
        return new Document("_id", fplayer.getUuid())
            .append("factionId", fplayer.getFactionId())
            .append("role", fplayer.getRole().name())
            .append("chatType", fplayer.getChatType().name());
    }

    private FactionPlayer playerFromDocument(Document document) {
        FactionPlayer fplayer = new FactionPlayer();

        fplayer.setUuid((UUID) document.get("_id"));
        fplayer.setFactionId((UUID) document.get("factionId"));
        fplayer.setRole(Role.valueOf(document.getString("role")));
        fplayer.setChatType(ChatType.valueOf(document.getString("chatType")));

        return fplayer;
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionDisband(FactionDisbandEvent event) {
        Faction faction = event.getFaction();

        Tasks.async(() -> {
            if(faction instanceof PlayerFaction) {
                PlayerFaction playerFaction = (PlayerFaction) faction;

                playerFaction.getMembers().keySet().forEach(uuid -> this
                .getPlayersRepo().deleteOne(Filters.eq("_id", uuid)));
            }

            this.getFactionsRepo().deleteOne(Filters.eq("_id", faction.getId()));
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeaveFaction(PlayerLeaveFactionEvent event) {
        if(event.getReason() == LeaveReason.DISBAND) return;

        Tasks.async(() -> this.getPlayersRepo().deleteOne(Filters.eq("_id", event.getFactionPlayer().getUuid())));
    }
}
