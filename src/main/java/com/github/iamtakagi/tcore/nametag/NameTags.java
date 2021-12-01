package com.github.iamtakagi.tcore.nametag;

import com.github.iamtakagi.tcore.Api;
import com.github.iamtakagi.tcore.util.Style;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NameTags {

	public static void handle(Player viewer, boolean showHealth) {
		for (Player target : Bukkit.getOnlinePlayers()) {
			handle(viewer, target, showHealth);
		}
	}

	public static void handle(Player viewer, Player target, boolean showHealth) {
		if (viewer == null || target == null) {
			return;
		}

		if (viewer.equals(target)) {
			NameTags.color(viewer, target, Api.getColorOfPlayer(viewer).toString(), null, showHealth);
			return;
		}

		NameTags.color(viewer, target, Api.getColorOfPlayer(target).toString(), null, showHealth);
	}

	public static void handle(Player viewer, String prefix, String suffix, boolean showHealth) {
		for (Player target : Bukkit.getOnlinePlayers()) {
			handle(viewer, target, prefix, suffix, showHealth);
		}
	}

	public static void handle(Player viewer, Player target,  String prefix, String suffix, boolean showHealth) {
		if (viewer == null || target == null) {
			return;
		}

		if (viewer.equals(target)) {
			NameTags.color(viewer, target, prefix, suffix, showHealth);
			return;
		}

		NameTags.color(viewer, target, prefix, suffix, showHealth);
	}

	public static void color(Player viewer, Player target, String prefix, String suffix, boolean showHealth) {
		if (viewer == null || target == null) {
			return;
		}

		Scoreboard scoreboard = viewer.getScoreboard();

		if (scoreboard.equals(Bukkit.getServer().getScoreboardManager().getMainScoreboard())) {
			scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		}

		Team team = viewer.getScoreboard().getTeam(getTeamName(target));

		if (team == null) {
			team = viewer.getScoreboard().registerNewTeam(getTeamName(target));
		}

		if (prefix != null && !prefix.isEmpty()) {
			team.setPrefix(prefix);
		} else {
			team.setPrefix("");
		}

		if (suffix != null && !suffix.isEmpty()) {
			team.setSuffix(suffix);
		} else {
			team.setSuffix("");
		}

		if (!team.hasEntry(target.getName())) {
			reset(viewer, target);

			team.addEntry(target.getName());
		}

		if (showHealth) {
			Objective objective = viewer.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

			if (objective == null) {
				objective = viewer.getScoreboard().registerNewObjective("showhealth", "health");
			}

			objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
			objective.setDisplayName(Style.RED + StringEscapeUtils.unescapeJava("\u2764"));
			objective.getScore(target.getName()).setScore((int) Math.floor(target.getHealth() / 2));
		}

		viewer.setScoreboard(scoreboard);
	}

	public static void reset(Player viewer, Player target) {
		if (viewer != null && target != null && !viewer.equals(target)) {
			Objective objective = viewer.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

			if (objective != null) {
				objective.unregister();
			}

			Team team = viewer.getScoreboard().getTeam(getTeamName(target));

			if (team != null) {
				team.removeEntry(target.getName());
			}
		}
	}

	private static String getTeamName(Player player) {
		return player.getName();
	}

}
