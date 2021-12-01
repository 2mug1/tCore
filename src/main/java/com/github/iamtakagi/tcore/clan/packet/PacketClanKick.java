package com.github.iamtakagi.tcore.clan.packet;

import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;
import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.clan.Clan;

import java.util.UUID;

@Getter
public class PacketClanKick implements Packet {

    private Clan clan;

    private UUID kickedPlayerUUID;

    private String kickedPlayerName;

    public PacketClanKick(){

    }

    public PacketClanKick(Clan clan, UUID kickedPlayerUUID, String kickedPlayerName){
        this.clan = clan;
        this.kickedPlayerUUID = kickedPlayerUUID;
        this.kickedPlayerName = kickedPlayerName;
    }

    @Override
    public int id() {
        return 21;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .add("clan", Clan.SERIALIZER.serialize(clan))
                .addProperty("kickedPlayerUUID", kickedPlayerUUID.toString())
                .addProperty("kickedPlayerName", kickedPlayerName).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        clan = Clan.DESERIALIZER.deserialize(object.get("clan").getAsJsonObject());
        kickedPlayerUUID = UUID.fromString(object.get("kickedPlayerUUID").getAsString());
        kickedPlayerName = object.get("kickedPlayerName").getAsString();
    }
}
