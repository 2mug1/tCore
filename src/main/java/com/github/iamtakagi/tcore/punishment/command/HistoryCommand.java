package com.github.iamtakagi.tcore.punishment.command;

import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.punishment.menu.PunishmentLogsMenu;
import org.bukkit.entity.Player;

@CommandMeta(label = "history",  permission = "tCore.staff.history", async = true)
public class HistoryCommand {

    public void execute(Player player) {
        new PunishmentLogsMenu().openMenu(player);
    }
}
