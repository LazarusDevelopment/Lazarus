package me.qiooip.lazarus.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.userdata.UserdataManager;
import me.qiooip.lazarus.utils.StringUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MongoUserdataManager extends UserdataManager {

    private MongoCollection<Document> getUserdataRepo() {
        return Lazarus.getInstance().getMongoManager().getUserdataRepo();
    }

    @Override
    public void loadUserdata(UUID uuid, String name) {
        if(super.userdata.containsKey(uuid)) return;

        Document document = this.getUserdataRepo().find(Filters.eq("_id", uuid)).first();

        if(document == null) {
            super.userdata.put(uuid, new Userdata(uuid, name));
            return;
        }

        super.userdata.put(uuid, this.userdataFromDocument(document));
    }

    @Override
    public void saveUserdata(UUID uuid, boolean remove) {
        Userdata userdata = this.getUserdata(uuid);
        if(userdata == null) return;

        Document document = this.userdataToDocument(userdata);

        this.getUserdataRepo().replaceOne(Filters.eq("_id", uuid), document, new ReplaceOptions().upsert(true));
        if(remove) super.userdata.remove(uuid);
    }

    @Override
    public void saveUserdata(Userdata userdata) {
        if(userdata == null) return;

        Document document = this.userdataToDocument(userdata);
        this.getUserdataRepo().replaceOne(Filters.eq("_id", userdata.getUuid()), document, new ReplaceOptions().upsert(true));
    }

    @Override
    public Userdata getUserdata(OfflinePlayer player) {
        if(super.userdata.containsKey(player.getUniqueId())) return this.getUserdata(player.getUniqueId());

        Document document = this.getUserdataRepo().find(Filters.eq("_id", player.getUniqueId())).first();
        if(document == null) return null;

        Userdata userdata = this.userdataFromDocument(document);
        super.userdata.put(player.getUniqueId(), userdata);

        return userdata;
    }

    @Override
    public void deleteAllUserdata() {
        int deleted = super.userdata.size();

        this.getUserdataRepo().drop();
        super.userdata.clear();

        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID uuid = player.getUniqueId();
            super.userdata.put(uuid, new Userdata(uuid, player.getName()));
        });

        Lazarus.getInstance().log("- &cDeleted &e" + deleted + " &cuserdata files.");
    }

    private Document userdataToDocument(Userdata data) {
        return new Document("_id", data.getUuid())
            .append("name", data.getName())
            .append("kills", data.getKills())
            .append("deaths", data.getDeaths())
            .append("killstreak", data.getKillstreak())
            .append("balance", data.getBalance())
            .append("lives", data.getLives())
            .append("reclaimUsed", data.isReclaimUsed())
            .append("settings", StringUtils.settingsToString(data.getSettings()))
            .append("ignoring", data.getIgnoring())
            .append("notes", data.getNotes())
            .append("lastKills", data.getLastKills())
            .append("lastDeaths", data.getLastDeaths())
            .append("kitDelays", data.getKitDelays());
    }

    @SuppressWarnings("unchecked")
    private Userdata userdataFromDocument(Document document) {
        Userdata data = new Userdata();

        data.setUuid((UUID) document.get("_id"));
        data.setName(document.getString("name"));
        data.setKills(document.getInteger("kills"));
        data.setDeaths(document.getInteger("deaths"));
        data.setKillstreak(document.getInteger("killstreak"));
        data.setBalance(document.getInteger("balance"));
        data.setLives(document.getInteger("lives"));
        data.setReclaimUsed(document.getBoolean("reclaimUsed"));
        data.setSettings(StringUtils.settingsFromString(document.getString("settings")));
        data.setIgnoring((List<UUID>) document.get("ignoring"));
        data.setNotes((List<String>) document.get("notes"));
        data.setLastKills(document.get("lastKills", new ArrayList<>()));
        data.setLastDeaths((List<String>) document.get("lastDeaths"));
        data.setKitDelays((Map<String, Long>) document.get("kitDelays"));

        return data;
    }
}
