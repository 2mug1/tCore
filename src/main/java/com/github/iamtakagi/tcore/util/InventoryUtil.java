package com.github.iamtakagi.tcore.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InventoryUtil {

    public static ItemStack[] fixInventoryOrder(ItemStack[] source) {
        ItemStack[] fixed = new ItemStack[36];

        System.arraycopy(source, 0, fixed, 27, 9);
        System.arraycopy(source, 9, fixed, 0, 27);

        return fixed;
    }

    public static String serializeInventory(ItemStack[] source) {
        StringBuilder builder = new StringBuilder();

        for (ItemStack itemStack : source) {
            builder.append(serializeItemStack(itemStack));
            builder.append(";");
        }

        return builder.toString();
    }

    public static ItemStack[] deserializeInventory(String source) {
        List<ItemStack> items = new ArrayList<>();
        String[] split = source.split(";");

        for (String piece : split) {
            items.add(deserializeItemStack(piece));
        }

        return items.toArray(new ItemStack[items.size()]);
    }

    public static String serializeItemStack(ItemStack item) {
        StringBuilder builder = new StringBuilder();

        if (item == null) {
            return "null";
        }

        String isType = String.valueOf(item.getType().getId());
        builder.append("t@").append(isType);

        if (item.getDurability() != 0) {
            String isDurability = String.valueOf(item.getDurability());
            builder.append(":d@").append(isDurability);
        }

        if (item.getAmount() != 1) {
            String isAmount = String.valueOf(item.getAmount());
            builder.append(":a@").append(isAmount);
        }

        Map<Enchantment, Integer> isEnch = item.getEnchantments();

        if (isEnch.size() > 0) {
            for (Map.Entry<Enchantment, Integer> ench : isEnch.entrySet()) {
                builder.append(":e@").append(ench.getKey().getId()).append("@").append(ench.getValue());
            }
        }

        if (item.hasItemMeta()) {
            ItemMeta imeta = item.getItemMeta();

            if (imeta.hasDisplayName()) {
                builder.append(":dn@").append(imeta.getDisplayName());
            }

            if (imeta.hasLore()) {
                builder.append(":l@").append(imeta.getLore());
            }
        }

        return builder.toString();
    }

    public static ItemStack deserializeItemStack(String in) {
        ItemStack item = null;
        ItemMeta meta = null;

        if (in.equals("null")) {
            return new ItemStack(Material.AIR);
        }

        String[] split = in.split(":");

        for (String itemInfo : split) {
            String[] itemAttribute = itemInfo.split("@");
            String s2 = itemAttribute[0];

            switch (s2) {
                case "t": {
                    item = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
                    meta = item.getItemMeta();
                    break;
                }
                case "d": {
                    if (item != null) {
                        item.setDurability(Short.valueOf(itemAttribute[1]));
                        break;
                    }
                    break;
                }
                case "a": {
                    if (item != null) {
                        item.setAmount(Integer.valueOf(itemAttribute[1]));
                        break;
                    }
                    break;
                }
                case "e": {
                    if (item != null) {
                        item.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
                        break;
                    }
                    break;
                }
                case "dn": {
                    if (meta != null) {
                        meta.setDisplayName(itemAttribute[1]);
                        break;
                    }
                    break;
                }
                case "l": {
                    itemAttribute[1] = itemAttribute[1].replace("[", "");
                    itemAttribute[1] = itemAttribute[1].replace("]", "");
                    List<String> lore = Arrays.asList(itemAttribute[1].split(","));

                    for (int x = 0; x < lore.size(); ++x) {
                        String s = lore.get(x);

                        if (s != null) {
                            if (s.toCharArray().length != 0) {
                                if (s.charAt(0) == ' ') {
                                    s = s.replaceFirst(" ", "");
                                }

                                lore.set(x, s);
                            }
                        }
                    }

                    if (meta != null) {
                        meta.setLore(lore);
                        break;
                    }

                    break;
                }
            }
        }

        if (meta != null && (meta.hasDisplayName() || meta.hasLore())) {
            item.setItemMeta(meta);
        }

        return item;
    }

}
