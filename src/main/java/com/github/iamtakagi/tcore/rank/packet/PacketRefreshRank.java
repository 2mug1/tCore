package com.github.iamtakagi.tcore.rank.packet;

import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;
import com.google.gson.JsonObject;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PacketRefreshRank implements Packet {

	private UUID uuid;
	private String name;

	public PacketRefreshRank() {

	}

	@Override
	public int id() {
		return 5;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("uuid", uuid.toString())
				.addProperty("name", name)
				.get();
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
		name = jsonObject.get("name").getAsString();
	}

}