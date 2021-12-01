package com.github.iamtakagi.tcore.io.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class FileConfig {
    private File file;
    private org.bukkit.configuration.file.FileConfiguration config;

    public File getFile() { return this.file; }
    public org.bukkit.configuration.file.FileConfiguration getConfig() { return this.config; }

    public FileConfig(JavaPlugin plugin, String fileName) {
        this.file = new File(plugin.getDataFolder(), fileName);

        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();

            if (plugin.getResource(fileName) == null) {
                try {
                    this.file.createNewFile();
                } catch (IOException e) {
                    plugin.getLogger().severe("Failed to create new file " + fileName);
                }
            } else {
                plugin.saveResource(fileName, false);
            }
        }

        this.config = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfig(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            org.bukkit.Bukkit.getLogger().severe("Could not save config file " + this.file.toString());
            e.printStackTrace();
        }
    }
}