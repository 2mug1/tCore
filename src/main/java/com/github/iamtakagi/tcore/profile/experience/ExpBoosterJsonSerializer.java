package com.github.iamtakagi.tcore.profile.experience;

import com.google.gson.JsonObject;
import com.github.iamtakagi.tcore.util.json.JsonSerializer;

public class ExpBoosterJsonSerializer implements JsonSerializer<ExpBooster> {

	@Override
	public JsonObject serialize(ExpBooster expBooster) {
		JsonObject object = new JsonObject();
		object.addProperty("addedAt", expBooster.getAddedAt());
		object.addProperty("duration", expBooster.getDuration());
		object.addProperty("increaseRate", expBooster.getIncreaseRate());
		object.addProperty("addedReason", expBooster.getAddedReason());
		object.addProperty("addedBy", expBooster.getAddedBy() == null ? null : expBooster.getAddedBy().toString());
		object.addProperty("removed", expBooster.isRemoved());
		object.addProperty("removedAt", expBooster.getRemovedAt());
		object.addProperty("removedBy", expBooster.getRemovedBy() == null ? null : expBooster.getRemovedBy().toString());
		object.addProperty("removedReason", expBooster.getRemovedReason());
		return object;
	}

}
