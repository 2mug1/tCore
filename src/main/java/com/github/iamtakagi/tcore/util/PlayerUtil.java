package com.github.iamtakagi.tcore.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerUtil {

    public static void reset(Player player) {
        player.setFireTicks(0);
        player.getInventory().setHeldItemSlot(0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setMaximumNoDamageTicks(20);
        player.setExp(0);
        player.setLevel(0);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setContents(new ItemStack[36]);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.updateInventory();
    }
}
