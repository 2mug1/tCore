package com.github.iamtakagi.tcore.clan.packet;

import com.github.iamtakagi.tcore.clan.Clan;
import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;
import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public class PacketClanDisband implements Packet {

    private Clan clan;

    public PacketClanDisband(){

    }

    public PacketClanDisband(Clan clan){
        this.clan = clan;
    }

    @Override
    public int id() {
        return 17;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().add("clan", Clan.SERIALIZER.serialize(clan)).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        clan = Clan.DESERIALIZER.deserialize(object.get("clan").getAsJsonObject());
    }
}
