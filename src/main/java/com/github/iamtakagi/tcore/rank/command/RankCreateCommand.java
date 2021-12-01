package com.github.iamtakagi.tcore.rank.command;

import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank create", permission = "tCore.admin.rank", async = true)
public class RankCreateCommand {

	public void execute(CommandSender sender, String name) {
		if (Rank.getRankByDisplayName(name) != null) {
			sender.sendMessage(Style.RED + "A packet with that name already exists.");
			return;
		}

		Rank rank = new Rank(name);
		rank.save();

		sender.sendMessage(Style.GREEN + "You created a new rank.");
	}

}
