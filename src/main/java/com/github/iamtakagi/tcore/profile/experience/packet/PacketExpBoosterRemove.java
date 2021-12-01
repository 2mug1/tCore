package com.github.iamtakagi.tcore.profile.experience.packet;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;

import java.util.UUID;

public class PacketExpBoosterRemove implements Packet {

    @Getter
    private UUID target;
    @Getter @Setter
    private UUID removedBy;
    @Getter
    private long removedAt;
    @Setter
    @Getter
    private String removedReason;

    public PacketExpBoosterRemove() {

    }

    public PacketExpBoosterRemove(UUID target, long removedAt, String removedReason) {
        this.target = target;
        this.removedAt = removedAt;
        this.removedReason = removedReason;
    }

    @Override
    public int id() {
        return 33;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("target", target.toString())
                .addProperty("removedBy", removedBy.toString())
                .addProperty("removedAt", removedAt)
                .addProperty("removedReason", removedReason)
                .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        this.target = UUID.fromString(object.get("target").getAsString());
        this.removedBy = UUID.fromString(object.get("removedBy").getAsString());
        this.removedAt = object.get("removedAt").getAsLong();
        this.removedReason = object.get("removedReason").getAsString();
    }
}
