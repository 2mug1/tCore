package com.github.iamtakagi.tcore.io.file;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigValidation {

	private final File file;
	private final FileConfiguration fileConfiguration;
	private final int version;

	public ConfigValidation(File file, FileConfiguration fileConfiguration, int version) {
		this.file = file;
		this.fileConfiguration = fileConfiguration;
		this.version = version;
	}

	public void check() {
		if (fileConfiguration.contains("CONFIG_VERSION")) {
			if (fileConfiguration.getInt("CONFIG_VERSION") != version) {
				for (ConfigVersion version : ConfigVersion.values()) {
					if (fileConfiguration.getInt("CONFIG_VERSION") < version.getNumber()) {
						System.out.println("Converting current configuration into version " + version.name());
						version.getConversion().convert(file, fileConfiguration);
					}
				}
			}
		} else {
			for (ConfigVersion version : ConfigVersion.values()) {
				System.out.println("Converting current configuration into version " + version.name());
				version.getConversion().convert(file, fileConfiguration);
			}
		}
	}

}
