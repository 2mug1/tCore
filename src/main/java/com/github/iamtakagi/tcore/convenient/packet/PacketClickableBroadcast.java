package com.github.iamtakagi.tcore.convenient.packet;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;

public class PacketClickableBroadcast implements Packet {

    @Getter private String message;
    @Getter private String clickableMessage;
    @Getter private String performCommand;
    @Getter private String hoverText;

    public PacketClickableBroadcast(){

    }

    public PacketClickableBroadcast(String message, String clickableMessage, String performCommand, String hoverText){
        this.message = message;
        this.clickableMessage = clickableMessage;
        this.performCommand = performCommand;
        this.hoverText = hoverText;
    }

    @Override
    public int id() {
        return 10;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().
                addProperty("message", message)
                .addProperty("clickableMessage", clickableMessage)
                .addProperty("performCommand", performCommand)
                .addProperty("hoverText", hoverText)
                .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        this.message = object.get("message").getAsString();
        this.clickableMessage = object.get("clickableMessage").getAsString();
        this.performCommand = object.get("performCommand").getAsString();
        this.hoverText = object.get("hoverText").getAsString();
    }
}
