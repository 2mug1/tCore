package com.github.iamtakagi.tcore.punishment.packet;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;

import java.util.UUID;

public class PacketClearPunishments implements Packet {

    @Getter
    private UUID uuid;

    public PacketClearPunishments() {

    }

    public PacketClearPunishments(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public int id() {
        return 31;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("uuid", uuid.toString()).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
    }
}
