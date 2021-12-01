package com.github.iamtakagi.tcore.punishment.packet;

import com.github.iamtakagi.tcore.punishment.Punishment;
import com.google.gson.JsonObject;
import com.github.iamtakagi.tcore.pidgin.packet.Packet;
import com.github.iamtakagi.tcore.util.json.JsonChain;
import java.util.UUID;
import lombok.Getter;

public class PacketBroadcastPunishment implements Packet {

	@Getter private Punishment punishment;
	@Getter private String staff;
	@Getter private String target;
	@Getter private UUID targetUuid;
	@Getter private boolean silent;
	@Getter private boolean removed;

	public PacketBroadcastPunishment() {

	}

	public PacketBroadcastPunishment(Punishment punishment, String staff, String target, UUID targetUuid, boolean silent, boolean removed) {
		this.punishment = punishment;
		this.staff = staff;
		this.target = target;
		this.targetUuid = targetUuid;
		this.silent = silent;
		this.removed = removed;
	}

	@Override
	public int id() {
		return 2;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.add("punishment", Punishment.SERIALIZER.serialize(punishment))
				.addProperty("staff", staff)
				.addProperty("target", target)
				.addProperty("targetUuid", targetUuid.toString())
				.addProperty("silent", silent)
				.addProperty("removed", removed)
				.get();
	}

	@Override
	public void deserialize(JsonObject object) {
		punishment = Punishment.DESERIALIZER.deserialize(object.get("punishment").getAsJsonObject());
		staff = object.get("staff").getAsString();
		target = object.get("target").getAsString();
		targetUuid = UUID.fromString(object.get("targetUuid").getAsString());
		silent = object.get("silent").getAsBoolean();
		removed = object.get("removed").getAsBoolean();
	}

}
