package com.github.iamtakagi.tcore.clan.json;

import com.github.iamtakagi.tcore.clan.ClanPlayer;
import com.github.iamtakagi.tcore.util.json.JsonSerializer;
import com.google.gson.JsonObject;

public class ClanPlayerJsonSerializer implements JsonSerializer<ClanPlayer> {

    @Override
    public JsonObject serialize(ClanPlayer clanPlayer) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", clanPlayer.getUuid().toString());
        jsonObject.addProperty("role", clanPlayer.getRole().name());
        jsonObject.addProperty("procedureStage", clanPlayer.getProcedureStage().name());
        return jsonObject;
    }
}
