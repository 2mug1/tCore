package com.github.iamtakagi.tcore.clan;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.clan.packet.PacketClanBroadcast;
import com.github.iamtakagi.tcore.util.Style;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Data;
import com.github.iamtakagi.tcore.clan.json.ClanJsonDeserializer;
import com.github.iamtakagi.tcore.clan.json.ClanJsonSerializer;
import com.github.iamtakagi.tcore.clan.packet.PacketClanChat;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
public class Clan {

    public static ClanJsonSerializer SERIALIZER = new ClanJsonSerializer();
    public static ClanJsonDeserializer DESERIALIZER = new ClanJsonDeserializer();

    private boolean loaded = false;

    private String name, tag;
    private List<ClanPlayer> players = new LinkedList<>();

    private long createdAt = 0L;
    private boolean disbanded = false;
    private long disbandedAt = 0L;
    private String disbandedReason = "";

    public Clan(String name, String tag){
        this.name = name;
        this.tag = tag;

        load();
    }

    private void load() {
        Document document = Core.get().getDb().getClans().find(Filters.eq("name", name)).first();

        if (document == null) {
            save();
            loaded = true;
            return;
        }

        setName(document.getString("name"));
        setTag(document.getString("tag"));
        setCreatedAt(document.getLong("createdAt"));
        setDisbanded(document.getBoolean("disbanded"));
        setDisbandedAt(document.getLong("disbandedAt"));
        setDisbandedReason(document.getString("disbandedReason"));

        JsonArray array = new JsonParser().parse(document.getString("players")).getAsJsonArray();
        for (JsonElement element : array) {
            players.add(ClanPlayer.DESERIALIZER.deserialize(element.getAsJsonObject()));
        }

        sortPlayersByRole();

        loaded = true;
    }

    public void save(){
        Document document = new Document();

        document.put("name", name);
        document.put("tag", tag);
        document.put("createdAt", createdAt);
        document.put("disbanded", disbanded);
        document.put("disbandedAt", disbandedAt);
        document.put("disbandedReason", disbandedReason);

        JsonArray array = new JsonArray();

        for(ClanPlayer clanPlayer : players){
            array.add(ClanPlayer.SERIALIZER.serialize(clanPlayer));
        }

        document.put("players", array.toString());

        sortPlayersByRole();

        Core.get().getDb().getClans().replaceOne(Filters.eq("name", name), document, new ReplaceOptions().upsert(true));
    }

    public String getStyleTag(){
        return Style.GRAY + "[" + tag + "]";
    }

    public ClanPlayer getOwner(){
        for(ClanPlayer clanPlayer : players){
            if(clanPlayer.getRole() == ClanPlayerRole.Owner){
                return clanPlayer;
            }
        }
        return null;
    }

    public boolean isMember(UUID uuid){
        return getMemberByUuid(uuid) != null;
    }

    public ClanPlayer getMemberByUuid(UUID uuid){
        for(ClanPlayer clanPlayer : getMembers()){
            if(clanPlayer.getUuid().equals(uuid)){
                return clanPlayer;
            }
        }
        return null;
    }

    public List<ClanPlayer> getMembers(){
        List<ClanPlayer> toReturn = new LinkedList<>();
        for(ClanPlayer clanPlayer : players){
            if(clanPlayer.getProcedureStage() == ClanPlayerProcedureStage.ACCEPTED){
                toReturn.add(clanPlayer);
            }
        }
        return toReturn;
    }

    public List<ClanPlayer> getLeaders(){
        List<ClanPlayer> toReturn = new LinkedList<>();
        for(ClanPlayer clanPlayer : getMembers()){
            if(clanPlayer.getRole() == ClanPlayerRole.Leader){
                toReturn.add(clanPlayer);
            }
        }
        return toReturn;
    }

    public List<ClanPlayer> getMoreThanLeaders(){
        List<ClanPlayer> toReturn = new LinkedList<>();
        for(ClanPlayer clanPlayer : getMembers()){
            if(clanPlayer.getRole().getWeight() >= 1){
                toReturn.add(clanPlayer);
            }
        }
        return toReturn;
    }


    public List<ClanPlayer> getInvitingPlayers(){
        List<ClanPlayer> toReturn = new LinkedList<>();
        for(ClanPlayer clanPlayer : players){
            if(clanPlayer.getProcedureStage() == ClanPlayerProcedureStage.INVITING){
                toReturn.add(clanPlayer);
            }
        }
        return toReturn;
    }

    public ClanPlayer getClanPlayerByUuid(UUID uuid){
        for(ClanPlayer clanPlayer : players){
            if(clanPlayer.getUuid().equals(uuid)){
                return clanPlayer;
            }
        }
        return null;
    }

    public boolean isInviting(UUID uuid){
        for(ClanPlayer clanPlayer : getInvitingPlayers()){
            if(clanPlayer.getUuid().equals(uuid)){
                return true;
            }
        }
        return false;
    }

    public void broadcast(String message){
        Core.get().getPidgin().sendPacket(new PacketClanBroadcast(this, message));
    }

    public void chat(String message, ClanPlayer sender){
        Core.get().getPidgin().sendPacket(new PacketClanChat(this, sender, message, Core.get().getMainConfig().getString("SERVER_NAME")));
    }

    public void sortPlayersByRole(){
        players.sort((o1, o2) -> o1.getRole().getWeight() > o2.getRole().getWeight() ? -1 : 1);
    }

    public static Clan getByName(String clanName){
        Document document = Core.get().getDb().getClans().find(Filters.eq("name", clanName)).first();

        if(document == null){
            return null;
        }

        if(document.getBoolean("disbanded")){
            return null;
        }

        return new Clan(clanName, null);
    }
}
