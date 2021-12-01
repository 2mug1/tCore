package com.github.iamtakagi.tcore.rank.command;

import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank delete", permission = "tCore.admin.rank", async = true)
public class RankDeleteCommand {

	public void execute(CommandSender sender, Rank rank) {
		if (rank == null) {
			sender.sendMessage(Locale.RANK_NOT_FOUND.format());
			return;
		}

		rank.delete();

		sender.sendMessage(Style.GREEN + "You deleted the rank.");
	}

}
