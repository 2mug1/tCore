package com.github.iamtakagi.tcore.punishment;

import com.google.gson.JsonObject;
import com.github.iamtakagi.tcore.util.json.JsonDeserializer;
import java.util.UUID;

public class PunishmentJsonDeserializer implements JsonDeserializer<Punishment> {

	@Override
	public Punishment deserialize(JsonObject object) {
		Punishment punishment = new Punishment(
				object.get("id").getAsLong(),
				PunishmentType.valueOf(object.get("type").getAsString()),
				object.get("addedAt").getAsLong(),
				object.get("addedReason").getAsString(),
				object.get("duration").getAsLong()
		);

		if (!object.get("addedBy").isJsonNull()) {
			punishment.setAddedBy(UUID.fromString(object.get("addedBy").getAsString()));
		}

		if (!object.get("removedBy").isJsonNull()) {
			punishment.setRemovedBy(UUID.fromString(object.get("removedBy").getAsString()));
		}

		if (!object.get("removedAt").isJsonNull()) {
			punishment.setRemovedAt(object.get("removedAt").getAsLong());
		}

		if (!object.get("removedReason").isJsonNull()) {
			punishment.setRemovedReason(object.get("removedReason").getAsString());
		}

		if (!object.get("removed").isJsonNull()) {
			punishment.setRemoved(object.get("removed").getAsBoolean());
		}

		return punishment;
	}

}
