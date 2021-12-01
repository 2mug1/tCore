package com.github.iamtakagi.tcore.board;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public interface BoardAdapter {

	String getTitle(Player player);

	List<String> getScoreboard(Player player, Board board);

	long getInterval();

	void onScoreboardCreate(Player player, Scoreboard board);

	void preLoop();

}