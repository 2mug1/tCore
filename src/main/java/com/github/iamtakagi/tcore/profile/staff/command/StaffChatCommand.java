package com.github.iamtakagi.tcore.profile.staff.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.staff.packet.PacketStaffChat;
import com.github.iamtakagi.tcore.util.Style;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffchat", "sc" }, permission = "tCore.staff")
public class StaffChatCommand {

	public void execute(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.getStaffOptions().staffChatModeEnabled(!profile.getStaffOptions().staffChatModeEnabled());

		player.sendMessage(profile.getStaffOptions().staffChatModeEnabled() ?
				Style.GREEN + "You are now talking in staff chat." : Style.RED + "You are no longer talking in staff chat.");
	}

	public void execute(Player player, String message) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		if (!profile.getStaffOptions().staffModeEnabled()) {
			player.sendMessage(Style.RED + "You are not in staff mode.");
			return;
		}

		Core.get().getPidgin().sendPacket(new PacketStaffChat(player.getDisplayName(),
				Core.get().getMainConfig().getString("SERVER_NAME"), message));
	}

}
