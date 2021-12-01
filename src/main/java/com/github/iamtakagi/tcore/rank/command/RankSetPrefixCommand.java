package com.github.iamtakagi.tcore.rank.command;

import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setprefix", permission = "tCore.admin.rank", async = true)
public class RankSetPrefixCommand {

	public void execute(CommandSender sender, Rank rank, String prefix) {
		if (rank == null) {
			sender.sendMessage(Style.RED + "A packet with that name does not exist.");
			return;
		}

		rank.setPrefix(Style.translate(prefix));
		rank.save();

		sender.sendMessage(Style.GREEN + "You updated the packet's prefix.");
	}

}
