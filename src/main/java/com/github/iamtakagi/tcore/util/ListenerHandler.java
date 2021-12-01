package com.github.iamtakagi.tcore.util;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ListenerHandler {

    /**
     * Registers all listeners from the given package with the given plugin.
     *
     * @param plugin      The plugin responsible for these listeners. This is here
     *                    because the .getClassesInPackage method requires it (for no real reason)
     * @param packageName The package to load listeners from. Example: "me.joeleoli.nucleus.listeners"
     */
    public static void loadListenersFromPackage(Plugin plugin, String packageName) {
        for (Class<?> clazz : ClassUtil.getClassesInPackage(plugin, packageName)) {
            if (isListener(clazz)) {
                try {
                    plugin.getServer().getPluginManager().registerEvents((Listener) clazz.newInstance(), plugin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Check if the given class implements the {@link Listener} interface.
     *
     * @param clazz     The class to check
     * @return          If the class implements the {@link Listener} interface
     */
    public static boolean isListener(Class<?> clazz) {
        for (Class<?> interfaze : clazz.getInterfaces()) {
            if (interfaze == Listener.class) {
                return true;
            }
        }

        return false;
    }

}
