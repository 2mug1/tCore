package com.github.iamtakagi.tcore.convenient.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.util.BukkitReflection;
import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "setslots", async = true, permission = "tCore.admin.setslots")
public class SetSlotsCommand {

	public void execute(CommandSender sender, int slots) {
		BukkitReflection.setMaxPlayers(Core.get().getServer(), slots);
		sender.sendMessage(Style.GOLD + "You set the max slots to " + slots + ".");
	}

}
