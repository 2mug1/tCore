package com.github.iamtakagi.tcore.punishment.listener;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.punishment.procedure.PunishmentProcedure;
import com.github.iamtakagi.tcore.punishment.procedure.PunishmentProcedureStage;
import com.github.iamtakagi.tcore.punishment.procedure.PunishmentProcedureType;
import com.github.iamtakagi.tcore.strap.StrappedListener;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.callback.TypeCallback;
import com.github.iamtakagi.tcore.menu.menus.ConfirmMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PunishmentListener extends StrappedListener {

	public PunishmentListener(Core instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		if (!event.getPlayer().hasPermission("tCore.staff.punishment")) {
			return;
		}

		PunishmentProcedure procedure = PunishmentProcedure.getByPlayer(event.getPlayer());

		if (procedure != null && procedure.getStage() == PunishmentProcedureStage.REQUIRE_TEXT) {
			event.setCancelled(true);

			if (event.getMessage().equalsIgnoreCase("cancel")) {
				PunishmentProcedure.getProcedures().remove(procedure);
				event.getPlayer().sendMessage(Style.RED + "You have cancelled the punishment procedure.");
				return;
			}

			if (procedure.getType() == PunishmentProcedureType.PARDON) {
				new ConfirmMenu(Style.YELLOW + "Pardon this punishment?", new TypeCallback<Boolean>() {
					@Override
					public void callback(Boolean data) {
						if (data) {
							procedure.getPunishment().setRemovedBy(event.getPlayer().getUniqueId());
							procedure.getPunishment().setRemovedAt(System.currentTimeMillis());
							procedure.getPunishment().setRemovedReason(event.getMessage());
							procedure.getPunishment().setRemoved(true);
							procedure.finish();

							event.getPlayer().sendMessage(Style.GREEN + "The punishment has been removed.");
						} else {
							procedure.cancel();
							event.getPlayer().sendMessage(Style.RED + "You did not confirm to pardon the punishment.");
						}
					}
				}, true) {
					@Override
					public void onClose(Player player) {
						if (!isClosedByMenu()) {
							procedure.cancel();
							event.getPlayer().sendMessage(Style.RED + "You did not confirm to pardon the punishment.");
						}
					}
				}.openMenu(event.getPlayer());
			}
		}
	}

}
