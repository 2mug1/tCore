package com.github.iamtakagi.tcore.profile.experience.packet;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.profile.experience.ExpBooster;
import com.github.iamtakagi.tcore.util.json.JsonChain;

import java.util.UUID;

public class PacketExpBoosterApply implements Packet {

    @Getter
    private UUID uuid;
    @Getter
    private ExpBooster expBooster;

    public PacketExpBoosterApply() {

    }

    public PacketExpBoosterApply(UUID uuid, ExpBooster expBooster) {
        this.uuid = uuid;
        this.expBooster = expBooster;
    }

    @Override
    public int id() {
        return 32;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("uuid", uuid.toString())
                .add("expBooster", ExpBooster.SERIALIZER.serialize(expBooster)).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.expBooster = ExpBooster.DESERIALIZER.deserialize(object.get("expBooster").getAsJsonObject());
    }
}
