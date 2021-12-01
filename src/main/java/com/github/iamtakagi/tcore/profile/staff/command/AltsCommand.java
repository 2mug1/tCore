package com.github.iamtakagi.tcore.profile.staff.command;

import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.github.iamtakagi.tcore.util.Style;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "alts", async = true, permission = "tCore.staff.alts")
public class AltsCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		List<Profile> alts = new ArrayList<>();

		for (UUID altUuid : profile.getKnownAlts()) {
			Profile altProfile = Profile.getByUuid(altUuid);

			if (altProfile != null && altProfile.isLoaded()) {
				alts.add(altProfile);
			}
		}

		if (alts.isEmpty()) {
			sender.sendMessage(Style.RED + "This player has no known alt accounts.");
		} else {
			StringBuilder builder = new StringBuilder();

			for (Profile altProfile : alts) {
				builder.append(altProfile.getUsername());
				builder.append(", ");
			}

			sender.sendMessage(Style.GOLD + "Alts: " + Style.RESET + builder.toString());
		}
	}

}
