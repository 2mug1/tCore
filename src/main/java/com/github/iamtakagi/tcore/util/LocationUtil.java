package com.github.iamtakagi.tcore.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

public class LocationUtil {

	public static Location[] getFaces(Location start) {
		Location[] faces = new Location[4];
		faces[0] = new Location(start.getWorld(), start.getX() + 1, start.getY(), start.getZ());
		faces[1] = new Location(start.getWorld(), start.getX() - 1, start.getY(), start.getZ());
		faces[2] = new Location(start.getWorld(), start.getX(), start.getY() + 1, start.getZ());
		faces[3] = new Location(start.getWorld(), start.getX(), start.getY() - 1, start.getZ());
		return faces;
	}

	public static String serialize(Location location) {
		if (location == null) {
			return "null";
		}

		return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() +
		       ":" + location.getYaw() + ":" + location.getPitch();
	}

	public static Location deserialize(String worldName, String source) {
		if (source == null) {
			return null;
		}

		String[] split = source.split(":");
		World world = Bukkit.getServer().getWorld(worldName);

		if (world == null) {
			return null;
		}

		return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
	}

	public static Location deserialize(String source) {
		if (source == null) {
			return null;
		}

		String[] split = source.split(":");
		World world = Bukkit.getServer().getWorld(split[0]);

		if (world == null) {
			return null;
		}

		return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
	}

	public static String serializeWithBlock(Location location, BlockFace face) {
		if (location == null) {
			return "null";
		}

		return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + face.name();
	}

	public static Block deserializeWithBlock(String source) {
		if (source == null) {
			return null;
		}

		String[] split = source.split(":");
		World world = Bukkit.getServer().getWorld(split[0]);

		if (world == null) {
			return null;
		}

		return setFacingDirection(
				new Location(world, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])).getBlock(),
				BlockFace.valueOf(split[4]));
	}

	private static Block setFacingDirection(final Block block, final BlockFace face) {
		final BlockState state = block.getState();
		final MaterialData materialData = state.getData();
		if (materialData instanceof Directional) {
			final Directional directional = (Directional) materialData;
			directional.setFacingDirection(face);
			state.update();
		}

		return block;
	}
}
