package com.github.iamtakagi.tcore.nametag.packet;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;

import java.util.UUID;

@Getter
public class PacketAddSuffix implements Packet {

    private UUID uuid;
    private String suffix;

    public PacketAddSuffix(){

    }

    public PacketAddSuffix(UUID uuid, String suffix){
        this.uuid = uuid;
        this.suffix = suffix;
    }

    @Override
    public int id() {
        return 26;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().
                addProperty("uuid", uuid.toString()).
                addProperty("suffix", suffix).
                get();
    }

    @Override
    public void deserialize(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.suffix = object.get("suffix").getAsString();
    }
}
