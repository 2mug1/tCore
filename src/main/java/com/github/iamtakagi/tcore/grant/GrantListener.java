package com.github.iamtakagi.tcore.grant;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.grant.event.GrantAppliedEvent;
import com.github.iamtakagi.tcore.grant.event.GrantExpireEvent;
import com.github.iamtakagi.tcore.grant.packet.PacketDeleteGrant;
import com.github.iamtakagi.tcore.grant.procedure.GrantProcedure;
import com.github.iamtakagi.tcore.grant.procedure.GrantProcedureStage;
import com.github.iamtakagi.tcore.grant.procedure.GrantProcedureType;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.strap.StrappedListener;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.TimeUtil;
import com.github.iamtakagi.tcore.util.callback.TypeCallback;
import com.github.iamtakagi.tcore.menu.menus.ConfirmMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GrantListener extends StrappedListener {

	public GrantListener(Core instance) {
		super(instance);
	}

	@EventHandler
	public void onGrantAppliedEvent(GrantAppliedEvent event) {
		Player player = event.getPlayer();
		Grant grant = event.getGrant();

		player.sendMessage(Style.GREEN + ("A `{packet}` packet has been applied to you for {time-remaining}.")
				.replace("{packet}", grant.getRank().getDisplayName())
				.replace("{time-remaining}", grant.getDuration() == Integer.MAX_VALUE ?
						"forever" : TimeUtil.millisToRoundedTime((grant.getAddedAt() + grant.getDuration()) -
						                                         System.currentTimeMillis())));

		Profile profile = Profile.getByUuid(player.getUniqueId());
		profile.setupBukkitPlayer(player);
	}

	@EventHandler
	public void onGrantExpireEvent(GrantExpireEvent event) {
		Player player = event.getPlayer();
		Grant grant = event.getGrant();

		player.sendMessage(Style.RED + ("Your `{packet}` packet has expired.")
				.replace("{packet}", grant.getRank().getDisplayName()));

		Profile profile = Profile.getByUuid(player.getUniqueId());
		profile.setupBukkitPlayer(player);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		if (!event.getPlayer().hasPermission("tCore.staff.packet")) {
			return;
		}

		GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());

		if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
			event.setCancelled(true);

			if (event.getMessage().equalsIgnoreCase("cancel")) {
				GrantProcedure.getProcedures().remove(procedure);
				event.getPlayer().sendMessage(Style.RED + "You have cancelled the packet procedure.");
				return;
			}

			if (procedure.getType() == GrantProcedureType.REMOVE) {
				new ConfirmMenu(Style.YELLOW + "Delete this packet?", new TypeCallback<Boolean>() {
					@Override
					public void callback(Boolean data) {
						if (data) {
							procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
							procedure.getGrant().setRemovedAt(System.currentTimeMillis());
							procedure.getGrant().setRemovedReason(event.getMessage());
							procedure.getGrant().setRemoved(true);
							procedure.finish();
							event.getPlayer().sendMessage(Style.GREEN + "The packet has been removed.");

							Core.get().getPidgin().sendPacket(new PacketDeleteGrant(procedure.getRecipient().getUuid(),
									procedure.getGrant()));
						} else {
							procedure.cancel();
							event.getPlayer().sendMessage(Style.RED + "You did not confirm to remove the packet.");
						}
					}
				}, true) {
					@Override
					public void onClose(Player player) {
						if (!isClosedByMenu()) {
							procedure.cancel();
							event.getPlayer().sendMessage(Style.RED + "You did not confirm to remove the packet.");
						}
					}
				}.openMenu(event.getPlayer());
			}
		}
	}

}
