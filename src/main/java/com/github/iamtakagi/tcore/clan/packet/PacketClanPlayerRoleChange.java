package com.github.iamtakagi.tcore.clan.packet;

import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;
import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.clan.Clan;
import com.github.iamtakagi.tcore.clan.ClanPlayer;

@Getter
public class PacketClanPlayerRoleChange implements Packet {

    private Clan clan;

    private ClanPlayer changedPlayer;

    public PacketClanPlayerRoleChange(){

    }

    public PacketClanPlayerRoleChange(Clan clan, ClanPlayer changedPlayer){
        this.clan = clan;
        this.changedPlayer = changedPlayer;
    }

    @Override
    public int id() {
        return 23;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .add("clan", Clan.SERIALIZER.serialize(clan))
                .add("changedPlayer", ClanPlayer.SERIALIZER.serialize(changedPlayer)).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        clan = Clan.DESERIALIZER.deserialize(object.get("clan").getAsJsonObject());
        changedPlayer = ClanPlayer.DESERIALIZER.deserialize(object.get("changedPlayer").getAsJsonObject());
    }
}
