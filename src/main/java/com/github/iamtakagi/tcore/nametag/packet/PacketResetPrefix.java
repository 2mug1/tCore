package com.github.iamtakagi.tcore.nametag.packet;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;

import java.util.UUID;

@Getter
public class PacketResetPrefix implements Packet {

    private UUID uuid;

    public PacketResetPrefix(){

    }

    public PacketResetPrefix(UUID uuid){
        this.uuid = uuid;
    }

    @Override
    public int id() {
        return 27;
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
