package com.github.iamtakagi.tcore.rank.command;

import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setsuffix", permission = "tCore.admin.rank", async = true)
public class RankSetSuffixCommand {

	public void execute(CommandSender sender, Rank rank, String suffix) {
		if (rank == null) {
			sender.sendMessage(Style.RED + "A packet with that name does not exist.");
			return;
		}

		rank.setSuffix(Style.translate(suffix));
		rank.save();

		sender.sendMessage(Style.GREEN + "You updated the packet's suffix.");
	}

}
