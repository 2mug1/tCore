package com.github.iamtakagi.tcore.util;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class BukkitUtil {

    public static String getName(PotionEffectType potionEffectType) {
        if (potionEffectType.getName().equalsIgnoreCase("fire_resistance")) {
            return "Fire Resistance";
        } else if (potionEffectType.getName().equalsIgnoreCase("speed")) {
            return "Speed";
        } else if (potionEffectType.getName().equalsIgnoreCase("weakness")) {
            return "Weakness";
        } else if (potionEffectType.getName().equalsIgnoreCase("slowness")) {
            return "Slowness";
        } else {
            return "Unknown";
        }
    }

    public static Player getDamager(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            return (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                return (Player) ((Projectile) event.getDamager()).getShooter();
            }
        }

        return null;
    }

}
