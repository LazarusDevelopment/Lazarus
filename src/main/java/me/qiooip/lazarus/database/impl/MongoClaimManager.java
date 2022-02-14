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
import me.qiooip.lazarus.factions.claim.Claim;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.event.FactionClaimChangeEvent;
import me.qiooip.lazarus.factions.event.FactionClaimChangeEvent.ClaimChangeReason;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.utils.Tasks;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class MongoClaimManager extends ClaimManager {

    private MongoCollection<Document> getClaimsRepo() {
        return Lazarus.getInstance().getMongoManager().getClaimsRepo();
    }

    @Override
    public void loadClaims() {
        super.claims = new ArrayList<>();

        try(MongoCursor<Document> cursor = this.getClaimsRepo().find().iterator()) {
            while(cursor.hasNext()) {
                Claim claim = this.claimFromDocument(cursor.next());
                Faction claimOwner = claim.getOwner();

                if(claimOwner != null) {
                    super.claims.add(claim);
                    super.cacheClaim(claim);
                    claimOwner.addClaim(claim);
                }
            }
        }

        Lazarus.getInstance().log("- &7Loaded &a" + this.claims.size() + " &7claims.");
    }

    @Override
    public void saveClaims(boolean log) {
        if(super.claims == null || super.claims.isEmpty()) return;

        ReplaceOptions options = new ReplaceOptions().upsert(true);
        List<WriteModel<Document>> toWrite = new ArrayList<>();

        super.claims.forEach(claim -> {
            Document data = this.claimToDocument(claim);
            toWrite.add(new ReplaceOneModel<>(Filters.eq("_id", data.get("_id")), data, options));
        });

        this.getClaimsRepo().bulkWrite(toWrite, new BulkWriteOptions().ordered(false));

        if(log) {
            Lazarus.getInstance().log("- &7Saved &a" + super.claims.size() + " &7claims.");
        }
    }

    public void deleteAllPlayerFactionClaims() {
        int claimsSize = super.claims.size();
        Iterator<Claim> iterator = super.claims.iterator();

        while(iterator.hasNext()) {
            Claim claim = iterator.next();
            if(claim.getOwner() instanceof SystemFaction) continue;

            super.uncacheClaim(claim);
            iterator.remove();
        }

        this.getClaimsRepo().drop();
        this.saveClaims(false);

        Lazarus.getInstance().log("- &cDeleted &e" + (claimsSize - this.claims.size()) + " &cplayer faction claims.");
    }

    private Document claimToDocument(Claim claim) {
        ObjectId claimId = claim.getId() == null ? new ObjectId() : claim.getId();

        return new Document("_id", claimId)
            .append("ownerId", claim.getOwnerId())
            .append("worldName", claim.getWorldName())
            .append("minX", claim.getMinX())
            .append("maxX", claim.getMaxX())
            .append("minZ", claim.getMinZ())
            .append("maxZ", claim.getMaxZ());
    }

    private Claim claimFromDocument(Document document) {
        Claim claim = new Claim();

        claim.setId(document.getObjectId("_id"));
        claim.setOwnerId((UUID) document.get("ownerId"));
        claim.setWorldName(document.getString("worldName"));
        claim.setMinX(document.getInteger("minX"));
        claim.setMaxX(document.getInteger("maxX"));
        claim.setMinZ(document.getInteger("minZ"));
        claim.setMaxZ(document.getInteger("maxZ"));

        return claim;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClaimRemove(FactionClaimChangeEvent event) {
        if(event.getClaimChangeReason() == ClaimChangeReason.CLAIM) return;

        Tasks.async(() -> event.getClaims().forEach(claim -> this
            .getClaimsRepo().deleteOne(Filters.eq("_id", claim.getId()))));
    }
}
