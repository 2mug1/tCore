package com.github.iamtakagi.tcore.clan.json;

import com.github.iamtakagi.tcore.clan.Clan;
import com.github.iamtakagi.tcore.clan.ClanPlayer;
import com.github.iamtakagi.tcore.util.json.JsonDeserializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.LinkedList;
import java.util.List;

public class ClanJsonDeserializer implements JsonDeserializer<Clan> {

    @Override
    public Clan deserialize(JsonObject object) {
        List<ClanPlayer> players = new LinkedList<>();

        Clan clan = new Clan(object.get("name").getAsString(), object.get("tag").getAsString());

        JsonArray array = new JsonParser().parse(object.get("players").getAsString()).getAsJsonArray();
        for (JsonElement element : array) {
            players.add(ClanPlayer.DESERIALIZER.deserialize(element.getAsJsonObject()));
        }

        clan.setPlayers(players);

        return clan;
    }
}
