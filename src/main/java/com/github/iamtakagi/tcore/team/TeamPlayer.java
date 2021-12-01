package com.github.iamtakagi.tcore.team;

import com.github.iamtakagi.tcore.player.PlayerInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class TeamPlayer extends PlayerInfo {

    @Setter
    private boolean alive;

    public TeamPlayer(UUID uuid, String name) {
        super(uuid, name);
    }

}
