package com.github.iamtakagi.tcore.staff.packet;

import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.google.gson.JsonObject;
import com.github.iamtakagi.tcore.util.json.JsonChain;
import lombok.Getter;

@Getter
public class PacketStaffLeaveNetwork implements Packet {

	private String playerName;
	private String serverName;

	public PacketStaffLeaveNetwork() {

	}

	public PacketStaffLeaveNetwork(String playerName, String serverName) {
		this.playerName = playerName;
		this.serverName = serverName;
	}

	@Override
	public int id() {
		return 8;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("playerName", playerName)
				.addProperty("serverName", serverName)
				.get();
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		playerName = jsonObject.get("playerName").getAsString();
		serverName = jsonObject.get("serverName").getAsString();
	}

}
