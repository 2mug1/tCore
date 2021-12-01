package com.github.iamtakagi.tcore.staff.packet;

import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.util.json.JsonChain;

@Getter
public class PacketReportPlayer implements Packet {

    private String targetName, senderName, fromServer, reason;
    private Long sentAt;

    public PacketReportPlayer(){

    }

    public PacketReportPlayer(String targetName, String senderName, String fromServer, String reason, Long sentAt){
        this.targetName = targetName;
        this.senderName = senderName;
        this.fromServer = fromServer;
        this.reason = reason;
        this.sentAt = sentAt;
    }

    @Override
    public int id() {
        return 24;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().
                addProperty("targetName", targetName).
                addProperty("senderName", senderName).
                addProperty("fromServer", fromServer).
                addProperty("reason", reason).
                addProperty("sentAt", sentAt).
                get();
    }

    @Override
    public void deserialize(JsonObject object) {
        this.targetName = object.get("targetName").getAsString();
        this.senderName = object.get("senderName").getAsString();
        this.fromServer = object.get("fromServer").getAsString();
        this.reason = object.get("reason").getAsString();
        this.sentAt = object.get("sentAt").getAsLong();
    }
}
