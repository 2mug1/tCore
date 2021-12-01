package com.github.iamtakagi.tcore.io.file.impl;

import com.github.iamtakagi.tcore.io.file.ConfigConversion;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigConversion2 implements ConfigConversion {

	@Override
	public void convert(File file, FileConfiguration fileConfiguration) {
		fileConfiguration.set("CONFIG_VERSION", 2);
		fileConfiguration.set("SETTINGS.UPDATE_PLAYER_LIST_NAME", true);

		try {
			fileConfiguration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
