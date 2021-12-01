package com.github.iamtakagi.tcore.nametag.packet;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;

import java.util.UUID;

@Getter
public class PacketAddPrefix implements Packet {

    private UUID uuid;
    private String prefix;

    public PacketAddPrefix(){

    }

    public PacketAddPrefix(UUID uuid, String prefix){
        this.uuid = uuid;
        this.prefix = prefix;
    }

    @Override
    public int id() {
        return 25;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().
                addProperty("uuid", uuid.toString()).
                addProperty("prefix", prefix).
                get();
    }

    @Override
    public void deserialize(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.prefix = object.get("prefix").getAsString();
    }
}
