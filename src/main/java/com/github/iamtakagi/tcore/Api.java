package com.github.iamtakagi.tcore;

import com.github.iamtakagi.tcore.profile.Economy;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.profile.experience.Experience;
import com.github.iamtakagi.tcore.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Api {

	public static String getServerName(){
		return Core.get().getServerName();
	}

	public static ChatColor getColorOfPlayer(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		return profile == null ? ChatColor.WHITE : profile.getActiveRank().getColor();
	}

	public static String getColoredName(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		return (profile == null ? ChatColor.WHITE : profile.getActiveRank().getColor()) + player.getName();
	}

	public static Rank getRankOfPlayer(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		return profile == null ? Rank.getDefaultRank() : profile.getActiveRank();
	}

	public static Profile getProfileByPlayer(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		return profile == null ? new Profile(player.getName(), player.getUniqueId()) : profile;
	}

	public static Profile getProfileByUUID(UUID uuid) {
		Profile profile = Profile.getProfiles().get(uuid);
		return profile == null ? new Profile("", uuid) : profile;
	}

	public static Experience getExperienceOfPlayer(Player player){
		return getProfileByPlayer(player).getExperience();
	}

	public static Economy getEconomyOfPlayer(Player player){
		return getProfileByPlayer(player).getEconomy();
	}

	public static Rank getRankOfPlayerByUUID(UUID uuid) {
		return new Profile(null, uuid).getActiveRank();
	}

	public static boolean isInStaffMode(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		return profile != null && player.hasPermission("tCore.staff") && profile.getStaffOptions().staffModeEnabled();
	}

}
