package com.github.iamtakagi.tcore.player;

import java.util.UUID;

import com.github.iamtakagi.tcore.Core;
import lombok.Getter;
import lombok.Setter;
import com.github.iamtakagi.tcore.util.BukkitReflection;
import org.bukkit.entity.Player;

@Getter
public class PlayerInfo {

	private UUID uuid;
	@Setter
	private String name;

	public PlayerInfo(Player player) {
		this.uuid = player.getUniqueId();
		this.name = player.getName();
	}

	public PlayerInfo(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public Player toPlayer() {
		final Player player = Core.get().getServer().getPlayer(this.getUuid());

		if (player != null && player.isOnline()) {
			return player;
		} else {
			return null;
		}
	}

	public String getDisplayName() {
		final Player player = this.toPlayer();

		return player == null ? this.getName() : player.getDisplayName();
	}

	public int getPing() {
		final Player player = Core.get().getServer().getPlayer(this.getUuid());

		if (player == null) {
			return 0;
		} else {
			return BukkitReflection.getPing(player);
		}
	}

}
