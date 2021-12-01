package com.github.iamtakagi.tcore.io.file.impl;

import com.github.iamtakagi.tcore.io.file.ConfigConversion;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigConversion1 implements ConfigConversion {

	@Override
	public void convert(File file, FileConfiguration fileConfiguration) {
		fileConfiguration.set("CONFIG_VERSION", 1);
		fileConfiguration.set("CHAT.FORMAT", "%1$s&r: %2$s");
		fileConfiguration.set("CHAT.CLEAR_CHAT_BROADCAST", "&eThe chat has been cleared by &r{0}");
		fileConfiguration.set("CHAT.CLEAR_CHAT_FOR_STAFF", false);
		fileConfiguration.set("CHAT.MUTE_CHAT_BROADCAST", "&eThe chat has been {0} by &r{1}");

		try {
			fileConfiguration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
