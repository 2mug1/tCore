package com.github.iamtakagi.tcore.server;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerStatusAPI {

    @Getter
    private final static Map<String, ServerStatus> servers = new HashMap<>();

    private static int totalOnline = 0;

    public static void init(JavaPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            servers.values().forEach(ServerStatus::refresh);
            int count = servers.values().stream().filter(ServerStatus::isServerUp).mapToInt(ServerStatus::getCurrentPlayers).sum();
            count += Bukkit.getOnlinePlayers().size();
            totalOnline = count;
        }, 0L, 60L);
    }

    public static void register(String name, String address, int port) {
        servers.put(name, new ServerStatus(address, port));
    }

    public static ServerStatus getServerStatusByName(String name) {
        return servers.get(name);
    }

    public static int getTotalOnline() {
        return totalOnline;
    }

    public static Map<String, ServerStatus> getFilteredServers(String filterName) {
        return getFilteredServers(filterName).entrySet().stream().filter(entry -> entry.getValue().isServerUp()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, ServerStatus> getFilteredOnlineServers(String filterName) {
        return getFilteredServers(filterName).entrySet().stream().filter(entry -> entry.getValue().isServerUp()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static int getPlayersByFilteredOnlineServers(String filterName) {
        return getFilteredOnlineServers(filterName).values().stream().mapToInt(ServerStatus::getCurrentPlayers).sum();
    }
}
