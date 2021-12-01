package com.github.iamtakagi.tcore.pidgin.packet;

import com.google.gson.JsonObject;

public interface Packet {

	int id();

	JsonObject serialize();

	void deserialize(JsonObject object);

}
