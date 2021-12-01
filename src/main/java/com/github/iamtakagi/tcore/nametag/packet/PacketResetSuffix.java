package com.github.iamtakagi.tcore.nametag.packet;

import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.util.json.JsonChain;

import java.util.UUID;

@Getter
public class PacketResetSuffix implements Packet {

    private UUID uuid;

    public PacketResetSuffix(){

    }

    public PacketResetSuffix(UUID uuid){
        this.uuid = uuid;
    }

    @Override
    public int id() {
        return 28;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().
                addProperty("uuid", uuid.toString()).
                get();
    }

    @Override
    public void deserialize(JsonObject object) {
        this.uuid =  UUID.fromString(object.get("uuid").getAsString());
    }
}
