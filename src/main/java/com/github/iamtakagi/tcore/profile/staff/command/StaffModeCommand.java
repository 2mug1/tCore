package com.github.iamtakagi.tcore.profile.staff.command;

import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.util.Style;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffmode", "sm" }, permission = "tCore.staff")
public class StaffModeCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().staffModeEnabled(!profile.getStaffOptions().staffModeEnabled());

        player.sendMessage(profile.getStaffOptions().staffModeEnabled() ?
                Style.GREEN + "You are now in staff mode." : Style.RED + "You are no longer in staff mode.");
    }

}
