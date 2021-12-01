package com.github.iamtakagi.tcore.clan.packet;

import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;
import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.clan.Clan;
import com.github.iamtakagi.tcore.clan.ClanPlayer;

@Getter
public class PacketClanInvite implements Packet {

    private Clan clan;

    private ClanPlayer target;

    public PacketClanInvite(){

    }

    public PacketClanInvite(Clan clan, ClanPlayer clanPlayer){
        this.clan = clan;
        this.target = clanPlayer;
    }

    @Override
    public int id() {
        return 18;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .add("clan", Clan.SERIALIZER.serialize(clan))
                .add("target", ClanPlayer.SERIALIZER.serialize(target)).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        clan = Clan.DESERIALIZER.deserialize(object.get("clan").getAsJsonObject());
        target = ClanPlayer.DESERIALIZER.deserialize(object.get("target").getAsJsonObject());
    }
}
