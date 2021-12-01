package com.github.iamtakagi.tcore.io.config;


import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConfigCursor {
    private final FileConfig fileConfig;
    private String path;

    @java.beans.ConstructorProperties({"fileConfig", "path"})
    public ConfigCursor(FileConfig fileConfig, String path) {
        this.fileConfig = fileConfig;this.path = path;
    }


    public FileConfig getFileConfig() { return this.fileConfig; }
    public String getPath() { return this.path; } public void setPath(String path) { this.path = path; }

    public boolean exists() {
        /* 20 */     return exists(null);
        /*    */   }

    public boolean exists(String path) {
        return this.fileConfig.getConfig().contains(this.path + (path == null ? "" : new StringBuilder().append(".").append(path).toString()));
    }

    public java.util.Set<String> getKeys() {
        /* 28 */     return getKeys(null);
        /*    */   }

    public java.util.Set<String> getKeys(String path) {
        return
                this.fileConfig.getConfig().getConfigurationSection(this.path + (path == null ? "" : new StringBuilder().append(".").append(path).toString())).getKeys(false);
    }

    public String getString(String path) {
        return this.fileConfig.getConfig().getString((this.path == null ? "" : new StringBuilder().append(this.path).append(".").toString()) + path);
    }

    public void setItemStackList(String key, List<ItemStack> itemStackList) {
        this.fileConfig.getConfig().set(this.path + "." + key, itemStackList);
    }

    public List<ItemStack> getItemStackList(String key) {
        return (List<ItemStack>) this.fileConfig.getConfig().getList(path + "." + key);
    }

    public boolean getBoolean(String path) {
        return this.fileConfig.getConfig().getBoolean((this.path == null ? "" : new StringBuilder().append(this.path).append(".").toString()) + "." + path);
    }

    public int getInt(String path) {
        return this.fileConfig.getConfig().getInt((this.path == null ? "" : new StringBuilder().append(this.path).append(".").toString()) + "." + path);
    }

    public long getLong(String path) {
        return this.fileConfig.getConfig().getLong((this.path == null ? "" : new StringBuilder().append(this.path).append(".").toString()) + "." + path);
    }

    public java.util.List<String> getStringList(String path) {
        return this.fileConfig.getConfig().getStringList((this.path == null ? "" : new StringBuilder().append(this.path).append(".").toString()) + "." + path);
    }

    public java.util.UUID getUuid(String path) {
        return java.util.UUID.fromString(this.fileConfig.getConfig().getString(this.path + "." + path));
    }

    public org.bukkit.World getWorld(String path) {
        return org.bukkit.Bukkit.getWorld(this.fileConfig.getConfig().getString(this.path + "." + path));
    }

    public void set(Object value) {
        /* 65 */     set(null, value);
        /*    */   }

    public void set(String path, Object value) {
        this.fileConfig.getConfig().set(this.path + (path == null ? "" : new StringBuilder().append(".").append(path).toString()), value);
    }

    public void save() {
        /* 73 */     this.fileConfig.save();
        /*    */   }
}