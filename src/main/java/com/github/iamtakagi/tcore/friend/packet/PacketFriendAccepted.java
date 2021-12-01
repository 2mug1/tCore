package com.github.iamtakagi.tcore.friend.packet;

import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.util.json.JsonChain;

@Getter
public class PacketFriendAccepted implements Packet {

    private String targetUUID;
    private String senderUUID;

    public PacketFriendAccepted(){

    }

    public PacketFriendAccepted(String targetUUID, String senderUUID){
        this.targetUUID = targetUUID;
        this.senderUUID = senderUUID;
    }

    @Override
    public int id() {
        return 13;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().
                addProperty("targetUUID", targetUUID)
                .addProperty("senderUUID", senderUUID)
                .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        targetUUID = object.get("targetUUID").getAsString();
        senderUUID = object.get("senderUUID").getAsString();
    }

}
