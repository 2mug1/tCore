package com.github.iamtakagi.tcore.clan.json;

import com.github.iamtakagi.tcore.clan.ClanPlayer;
import com.github.iamtakagi.tcore.clan.ClanPlayerProcedureStage;
import com.github.iamtakagi.tcore.clan.ClanPlayerRole;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.json.JsonDeserializer;
import com.google.gson.JsonObject;

import java.util.UUID;

public class ClanPlayerJsonDeserializer implements JsonDeserializer<ClanPlayer> {

    @Override
    public ClanPlayer deserialize(JsonObject object) {
        UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        return new ClanPlayer(
                uuid,
                Profile.getByUuid(uuid).getUsername(),
                ClanPlayerRole.valueOf(object.get("role").getAsString()),
                ClanPlayerProcedureStage.valueOf(object.get("procedureStage").getAsString())
        );
    }
}
