package com.github.iamtakagi.tcore.punishment;

import com.google.gson.JsonObject;
import com.github.iamtakagi.tcore.util.json.JsonSerializer;

public class PunishmentJsonSerializer implements JsonSerializer<Punishment> {

	@Override
	public JsonObject serialize(Punishment punishment) {
		JsonObject object = new JsonObject();
		object.addProperty("id", punishment.getId());
		object.addProperty("type", punishment.getType().name());
		object.addProperty("addedBy", punishment.getAddedBy() == null ? null : punishment.getAddedBy().toString());
		object.addProperty("addedAt", punishment.getAddedAt());
		object.addProperty("addedReason", punishment.getAddedReason());
		object.addProperty("duration", punishment.getDuration());
		object.addProperty("removedBy", punishment.getRemovedBy() == null ? null : punishment.getRemovedBy().toString());
		object.addProperty("removedAt", punishment.getRemovedAt());
		object.addProperty("removedReason", punishment.getRemovedReason());
		object.addProperty("removed", punishment.isRemoved());
		return object;
	}

}
