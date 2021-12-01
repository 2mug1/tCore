package com.github.iamtakagi.tcore.grant.menu;

import com.github.iamtakagi.tcore.grant.Grant;
import com.github.iamtakagi.tcore.grant.procedure.GrantProcedure;
import com.github.iamtakagi.tcore.grant.procedure.GrantProcedureStage;
import com.github.iamtakagi.tcore.grant.procedure.GrantProcedureType;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.ItemBuilder;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.TimeUtil;
import com.github.iamtakagi.tcore.menu.Button;
import com.github.iamtakagi.tcore.menu.pagination.PaginatedMenu;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class GrantsListMenu extends PaginatedMenu {

	private Profile profile;

	@Override
	public String getPrePaginatedTitle(Player player) {
		return "&6" + profile.getUsername() + "'s Grants (" + profile.getGrants().size() + ")";
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (Grant grant : profile.getGrants()) {
			buttons.put(buttons.size(), new GrantInfoButton(profile, grant));
		}

		return buttons;
	}

	@AllArgsConstructor
	private class GrantInfoButton extends Button {

		private Profile profile;
		private Grant grant;

		@Override
		public ItemStack getButtonItem(Player player) {
			int durability;

			if (grant.isRemoved()) {
				durability = 5;
			} else if (grant.hasExpired()) {
				durability = 4;
			} else {
				durability = 14;
			}

			String addedBy = "Console";

			if (grant.getAddedBy() != null) {
				addedBy = "Could not fetch...";

				Profile addedByProfile = Profile.getByUuid(grant.getAddedBy());

				if (addedByProfile != null && addedByProfile.isLoaded()) {
					addedBy = addedByProfile.getUsername();
				}
			}

			String removedBy = "Console";

			if (grant.getRemovedBy() != null) {
				removedBy = "Could not fetch...";

				Profile removedByProfile = Profile.getByUuid(grant.getRemovedBy());

				if (removedByProfile != null && removedByProfile.isLoaded()) {
					removedBy = removedByProfile.getUsername();
				}
			}

			List<String> lore = new ArrayList<>();

			lore.add(Style.MENU_BAR);
			lore.add("&3Rank: &e" + grant.getRank().getDisplayName());
			lore.add("&3Duration: &e" + grant.getDurationText());
			lore.add("&3Issued by: &e" + addedBy);
			lore.add("&3Reason: &e" + grant.getAddedReason());

			if (grant.isRemoved()) {
				lore.add(Style.MENU_BAR);
				lore.add("&a&lGrant Removed");
				lore.add("&a" + TimeUtil.dateToString(new Date(grant.getRemovedAt()), "&7"));
				lore.add("&aRemoved by: &7" + removedBy);
				lore.add("&aReason: &7&o\"" + grant.getRemovedReason() + "\"");
			} else {
				if (!grant.hasExpired()) {
					lore.add(Style.MENU_BAR);
					lore.add("&aRight click to remove this grant");
				}
			}

			lore.add(Style.MENU_BAR);

			return new ItemBuilder(Material.PAPER)
					.name("&3" + TimeUtil.dateToString(new Date(grant.getAddedAt()), "&7"))
					.durability(durability)
					.lore(lore)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			if (clickType == ClickType.RIGHT && !grant.isRemoved() && !grant.hasExpired()) {
				GrantProcedure procedure = new GrantProcedure(player, profile, GrantProcedureType.REMOVE, GrantProcedureStage.REQUIRE_TEXT);
				procedure.setGrant(grant);

				player.sendMessage(Style.GREEN + "Type a reason for removing this packet in chat...");
				player.closeInventory();
			}
		}
	}

}
