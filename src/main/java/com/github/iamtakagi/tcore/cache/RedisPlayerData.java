package com.github.iamtakagi.tcore.cache;

import com.google.gson.JsonObject;
import com.github.iamtakagi.tcore.util.TimeUtil;
import com.github.iamtakagi.tcore.util.json.JsonChain;
import java.util.UUID;
import lombok.Data;

@Data
public class RedisPlayerData {

	private UUID uuid;
	private String username;
	private LastAction lastAction;
	private String lastSeenServer;
	private long lastSeenAt;

	public RedisPlayerData(JsonObject object) {
		this.uuid = UUID.fromString(object.get("uuid").getAsString());
		this.username = object.get("username").getAsString();
		this.lastAction = LastAction.valueOf(object.get("lastAction").getAsString());
		this.lastSeenServer = object.get("lastSeenServer").getAsString();
		this.lastSeenAt = object.get("lastSeenAt").getAsLong();
	}

	public RedisPlayerData(UUID uuid, String username) {
		this.uuid = uuid;
		this.username = username;
	}

	public JsonObject getJson() {
		return new JsonChain()
				.addProperty("uuid", uuid.toString())
				.addProperty("username", username)
				.addProperty("lastAction", lastAction.name())
				.addProperty("lastSeenServer", lastSeenServer)
				.addProperty("lastSeenAt", lastSeenAt)
				.get();
	}

	public String getTimeAgo() {
		return TimeUtil.millisToRoundedTime(System.currentTimeMillis() - lastSeenAt) + " ago";
	}

	public enum LastAction {
		LEAVING_SERVER,
		JOINING_SERVER
	}

}
