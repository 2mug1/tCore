package com.github.iamtakagi.tcore.convenient;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.convenient.event.SpawnTeleportEvent;
import com.github.iamtakagi.tcore.strap.Strapped;
import com.github.iamtakagi.tcore.util.LocationUtil;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Convenient extends Strapped {

	private Location spawn;

	public Convenient(Core instance) {
		super(instance);

		spawn = LocationUtil.deserialize(instance.getMainConfig().getStringOrDefault("ESSENTIAL.SPAWN_LOCATION", null));
	}

	public void setSpawn(Location location) {
		spawn = location;

		if (spawn == null) {
			instance.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", null);
		} else {
			instance.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", LocationUtil.serialize(this.spawn));
		}

		try {
			instance.getMainConfig().getConfiguration().save(instance.getMainConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Location getSpawn(){
		return spawn == null ? instance.getServer().getWorlds().get(0).getSpawnLocation() : spawn;
	}

	public void teleportToSpawn(Player player) {
		SpawnTeleportEvent event = new SpawnTeleportEvent(player, getSpawn());
		event.call();

		if (!event.isCancelled() && event.getLocation() != null) {
			player.teleport(event.getLocation());
		}
	}

	public int clearEntities(World world) {
		int removed = 0;

		for (Entity entity : world.getEntities()) {
			if (entity.getType() == EntityType.PLAYER) {
				continue;
			}

			removed++;
			entity.remove();
		}

		return removed;
	}

	public int clearEntities(World world, EntityType... excluded) {
		int removed = 0;

		entityLoop:
		for (Entity entity : world.getEntities()) {
			for (EntityType type : excluded) {
				if (entity.getType() == EntityType.PLAYER) {
					continue entityLoop;
				}

				if (entity.getType() == type) {
					continue entityLoop;
				}
			}

			removed++;
			entity.remove();
		}

		return removed;
	}

}
