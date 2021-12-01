package com.github.iamtakagi.tcore.grant;

import com.google.gson.JsonObject;
import com.github.iamtakagi.tcore.util.json.JsonSerializer;

public class GrantJsonSerializer implements JsonSerializer<Grant> {

	@Override
	public JsonObject serialize(Grant grant) {
		JsonObject object = new JsonObject();
		object.addProperty("uuid", grant.getUuid().toString());
		object.addProperty("grant", grant.getRank().getUuid().toString());
		object.addProperty("addedBy", grant.getAddedBy() == null ? null : grant.getAddedBy().toString());
		object.addProperty("addedAt", grant.getAddedAt());
		object.addProperty("addedReason", grant.getAddedReason());
		object.addProperty("duration", grant.getDuration());
		object.addProperty("removedBy", grant.getRemovedBy() == null ? null : grant.getRemovedBy().toString());
		object.addProperty("removedAt", grant.getRemovedAt());
		object.addProperty("removedReason", grant.getRemovedReason());
		object.addProperty("removed", grant.isRemoved());
		return object;
	}

}
