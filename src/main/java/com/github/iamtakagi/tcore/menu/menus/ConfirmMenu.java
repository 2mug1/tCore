package com.github.iamtakagi.tcore.menu.menus;

import com.github.iamtakagi.tcore.menu.Button;
import com.github.iamtakagi.tcore.menu.Menu;
import com.github.iamtakagi.tcore.menu.button.ConfirmationButton;
import com.github.iamtakagi.tcore.util.callback.TypeCallback;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class ConfirmMenu extends Menu {

	private String title;
	private TypeCallback<Boolean> response;
	private boolean closeAfterResponse;
	private Button[] centerButtons;
	private String confirmButtonName = "Confirm";
	private String cancelButtonName = "Cancel";

	public ConfirmMenu(String title, TypeCallback<Boolean> response, boolean closeAfter, Button... centerButtons) {
		this.title = title;
		this.response = response;
		this.closeAfterResponse = closeAfter;
		this.centerButtons = centerButtons;
	}

	public ConfirmMenu(String title, TypeCallback<Boolean> response, boolean closeAfter, String confirmButtonName, String cancelButtonName, Button... centerButtons) {
		this.title = title;
		this.response = response;
		this.closeAfterResponse = closeAfter;
		this.centerButtons = centerButtons;
		this.confirmButtonName = confirmButtonName;
		this.cancelButtonName = cancelButtonName;
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		HashMap<Integer, Button> buttons = new HashMap<>();

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				buttons.put(getSlot(x, y), new ConfirmationButton(true, response, closeAfterResponse, confirmButtonName, cancelButtonName));
				buttons.put(getSlot(8 - x, y), new ConfirmationButton(false, response, closeAfterResponse, confirmButtonName, cancelButtonName));
			}
		}

		if (centerButtons != null) {
			for (int i = 0; i < centerButtons.length; i++) {
				if (centerButtons[i] != null) {
					buttons.put(getSlot(4, i), centerButtons[i]);
				}
			}
		}

		return buttons;
	}

	@Override
	public String getTitle(Player player) {
		return title;
	}

}
