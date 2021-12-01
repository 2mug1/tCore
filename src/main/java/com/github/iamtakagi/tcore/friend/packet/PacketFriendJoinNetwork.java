package com.github.iamtakagi.tcore.friend.packet;

import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import com.github.iamtakagi.tcore.util.json.JsonChain;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PacketFriendJoinNetwork implements Packet {

    private String playerName;
    private List<String> friendsUUID;

    public PacketFriendJoinNetwork(){

    }

    public PacketFriendJoinNetwork(String playerName, List<String> friendsUUID){
        this.playerName = playerName;
        this.friendsUUID = friendsUUID;
    }

    @Override
    public int id() {
        return 14;
    }

    @Override
    public JsonObject serialize() {
        JsonArray friendsArray = new JsonArray();
        for (String uuid : friendsUUID) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("uuid", uuid);
            friendsArray.add(jsonObject);
        }

        return new JsonChain().
                addProperty("playerName", playerName)
                .addProperty("friendsUUID", friendsArray.toString())
                .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        playerName = object.get("playerName").getAsString();

        friendsUUID = new ArrayList<>();
        JsonArray array = new JsonParser().parse(object.get("friendsUUID").getAsString()).getAsJsonArray();
        for (JsonElement element : array) {
            String uuid = element.getAsJsonObject().get("uuid").getAsString();
            if(uuid != null) {
                friendsUUID.add(uuid);
            }
        }
    }
}
