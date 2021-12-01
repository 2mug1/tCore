package com.github.iamtakagi.tcore.world;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldListener implements Listener {

    @EventHandler
    public void init(WorldInitEvent event){
        event.getWorld().setKeepSpawnInMemory(false);
    }

    @EventHandler
    public void onLoad(WorldLoadEvent event){
        World world = event.getWorld();
        world.setAutoSave(false);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("keepInventory", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setTime(12000L);
        world.setThundering(false);
        world.setStorm(false);
        world.setDifficulty(Difficulty.EASY);

        for(Entity entity : world.getEntities()){
            entity.remove();
        }
    }

    @EventHandler
    public void onChange(WeatherChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onChange(ThunderChangeEvent event){
        event.setCancelled(true);
    }
}
