package com.github.iamtakagi.tcore.punishment.menu;

import com.github.iamtakagi.tcore.punishment.Punishment;
import com.github.iamtakagi.tcore.punishment.procedure.PunishmentProcedure;
import com.github.iamtakagi.tcore.punishment.procedure.PunishmentProcedureStage;
import com.github.iamtakagi.tcore.punishment.procedure.PunishmentProcedureType;
import lombok.AllArgsConstructor;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.ItemBuilder;
import com.github.iamtakagi.tcore.util.TimeUtil;
import com.github.iamtakagi.tcore.menu.Button;
import com.github.iamtakagi.tcore.menu.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PunishmentLogsMenu extends PaginatedMenu {

    private Map<Punishment, Profile> punishments = new HashMap<>();

    public PunishmentLogsMenu(){
        load();
    }

    private void load() {
        punishments = Punishment.getPunishments();
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Style.GOLD + "Punishment Logs (" + punishments.size() + ")";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        //最新順
        final int[] slot = {0};
        punishments.entrySet().stream().sorted((o1, o2) -> o1.getKey().getAddedAt() > o2.getKey().getAddedAt() ? -1 : 1).forEach(a -> {
            buttons.put(slot[0], new PunishmentInfoButton(a.getKey(), punishments.get(a.getKey())));
            slot[0]++;
        });

        return buttons;
    }

    @AllArgsConstructor
    private class PunishmentInfoButton extends Button {

        private Punishment punishment;
        private Profile profile;

        @Override
        public ItemStack getButtonItem(Player player) {
            int durability;

            if (punishment.isRemoved()) {
                durability = 5;
            } else if (punishment.hasExpired()) {
                durability = 4;
            } else {
                durability = 14;
            }

            String addedBy = "Console";

            if (punishment.getAddedBy() != null) {
                try {
                    Profile addedByProfile = Profile.getByUuid(punishment.getAddedBy());
                    addedBy = addedByProfile.getUsername();
                } catch (Exception e) {
                    addedBy = "Could not fetch...";
                }
            }

            String removedBy = "Console";

            if (punishment.getRemovedBy() != null) {
                try {
                    Profile removedByProfile = Profile.getByUuid(punishment.getRemovedBy());
                    removedBy = removedByProfile.getUsername();
                } catch (Exception e) {
                    removedBy = "Could not fetch...";
                }
            }

            List<String> lore = new ArrayList<>();
            lore.add(Style.MENU_BAR);
            lore.add("&3Name: &e" + profile.getUsername());
            lore.add("&3Type: &e" + punishment.getType().getReadable());
            lore.add("&3Duration: &e" + punishment.getDurationText());
            lore.add("&3Issued by: &e" + addedBy);
            lore.add("&3Reason: &e&o\"" + punishment.getAddedReason() + "\"");

            if (punishment.isRemoved()) {
                lore.add(Style.MENU_BAR);
                lore.add("&a&lPunishment Removed");
                lore.add("&a" + TimeUtil.dateToString(new Date(punishment.getRemovedAt()), "&7"));
                lore.add("&aRemoved by: &7" + removedBy);
                lore.add("&aReason: &7&o\"" + punishment.getRemovedReason() + "\"");
            } else {
                if (!punishment.hasExpired() && punishment.getType().isRemovable()) {
                    lore.add(Style.MENU_BAR);
                    lore.add("&aRight click to remove this packet");
                }
            }

            lore.add(Style.MENU_BAR);

            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .durability(durability)
                    .name("&3" + TimeUtil.dateToString(new Date(punishment.getAddedAt()), "&7"))
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType == ClickType.RIGHT && !punishment.isRemoved() && !punishment.hasExpired() && punishment.getType().isRemovable()) {
                PunishmentProcedure procedure = new PunishmentProcedure(player, profile, PunishmentProcedureType.PARDON, PunishmentProcedureStage.REQUIRE_TEXT);
                procedure.setPunishment(punishment);

                player.sendMessage(Style.GREEN + "Enter a reason for removing this packet.");
                player.closeInventory();
            }
        }
    }
}