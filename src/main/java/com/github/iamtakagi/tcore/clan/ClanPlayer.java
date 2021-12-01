package com.github.iamtakagi.tcore.clan;

import com.github.iamtakagi.tcore.player.PlayerInfo;
import lombok.Getter;
import lombok.Setter;
import com.github.iamtakagi.tcore.clan.json.ClanPlayerJsonDeserializer;
import com.github.iamtakagi.tcore.clan.json.ClanPlayerJsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
public class ClanPlayer extends PlayerInfo {

    public static ClanPlayerJsonSerializer SERIALIZER = new ClanPlayerJsonSerializer();
    public static ClanPlayerJsonDeserializer DESERIALIZER = new ClanPlayerJsonDeserializer();

    private ClanPlayerRole role;
    private ClanPlayerProcedureStage procedureStage;

    public ClanPlayer(@NotNull UUID uuid, @NotNull String name, ClanPlayerRole role, ClanPlayerProcedureStage procedureStage) {
        super(uuid, name);
        this.role = role;
        this.procedureStage = procedureStage;
    }

    public boolean isMoreThanLeader(){
        return role.getWeight() >= 1;
    }

    public boolean isOwner(){
        return role == ClanPlayerRole.Owner;
    }
}
