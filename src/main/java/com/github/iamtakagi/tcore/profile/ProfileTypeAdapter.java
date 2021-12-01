package com.github.iamtakagi.tcore.profile;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ProfileTypeAdapter implements CommandTypeAdapter {

	public <T> T convert(String string, Class<T> type) {
		return type.cast(Profile.getByUsername(string));
	}

	@Override
	public boolean onException(Exception exception, CommandSender sender, String input) {
		return false;
	}
	@Override
	public List<String> onTabComplete(String string) {
		List<String> completed = new ArrayList<>();

		for (Profile profile : Profile.getProfiles().values()) {
			if (profile.getUsername() == null) continue;

			if (profile.getUsername().toLowerCase().startsWith(string.toLowerCase())) {
				completed.add(profile.getUsername());
			}
		}

		return completed;
	}
}
