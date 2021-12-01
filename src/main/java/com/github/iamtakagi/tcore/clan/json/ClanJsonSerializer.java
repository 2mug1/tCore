package com.github.iamtakagi.tcore.clan.json;

import com.github.iamtakagi.tcore.clan.Clan;
import com.github.iamtakagi.tcore.clan.ClanPlayer;
import com.github.iamtakagi.tcore.util.json.JsonSerializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ClanJsonSerializer implements JsonSerializer<Clan> {

    @Override
    public JsonObject serialize(Clan clan) {
        JsonObject object = new JsonObject();

        object.addProperty("name", clan.getName());
        object.addProperty("tag", clan.getTag());
        object.addProperty("createdAt", clan.getCreatedAt());

        JsonArray array = new JsonArray();

        for(ClanPlayer clanPlayer : clan.getPlayers()){
            array.add(ClanPlayer.SERIALIZER.serialize(clanPlayer));
        }

        object.addProperty("players", array.toString());

        return object;
    }
}
