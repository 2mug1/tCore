package com.github.iamtakagi.tcore.profile.experience;

import com.google.gson.JsonObject;
import com.github.iamtakagi.tcore.util.json.JsonDeserializer;

import java.util.UUID;

public class ExpBoosterJsonDeserializer implements JsonDeserializer<ExpBooster> {

	@Override
	public ExpBooster deserialize(JsonObject object) {
		ExpBooster expBooster = new ExpBooster(
				object.get("addedAt").getAsLong(),
				object.get("duration").getAsLong(),
				object.get("increaseRate").getAsInt(),
				object.get("addedReason").getAsString()
		);


		if (!object.get("addedBy").isJsonNull()) {
			expBooster.setAddedBy(UUID.fromString(object.get("addedBy").getAsString()));
		}

		if (!object.get("removed").isJsonNull()) {
			expBooster.setRemoved(object.get("removed").getAsBoolean());
		}

		if (!object.get("removedAt").isJsonNull()) {
			expBooster.setRemovedAt(object.get("removedAt").getAsLong());
		}

		if (!object.get("removedBy").isJsonNull()) {
			expBooster.setRemovedBy(UUID.fromString(object.get("removedBy").getAsString()));
		}

		if (!object.get("removedReason").isJsonNull()) {
			expBooster.setRemovedReason(object.get("removedReason").getAsString());
		}

		return expBooster;
	}

}
