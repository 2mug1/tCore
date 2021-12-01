package com.github.iamtakagi.tcore.hotbar;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ClickActionItem {

    @Getter
    private ItemStack itemStack;

    public ClickActionItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemStack)) {
            return false;
        }
        if (itemStack.getItemMeta().getDisplayName().equals(((ItemStack) obj).getItemMeta().getDisplayName()) &&
                itemStack.getType() == ((ItemStack) obj).getType()) {
            return true;
        }
        return false;
    }

    public abstract void onClick(Player player);
}
