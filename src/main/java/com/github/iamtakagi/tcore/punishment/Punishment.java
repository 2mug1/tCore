package com.github.iamtakagi.tcore.punishment;

import com.github.iamtakagi.tcore.Core;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.TimeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;

public class Punishment {

	public static PunishmentJsonSerializer SERIALIZER = new PunishmentJsonSerializer();
	public static PunishmentJsonDeserializer DESERIALIZER = new PunishmentJsonDeserializer();

	@Getter private final long id;
	@Getter private final PunishmentType type;
	@Getter @Setter private UUID addedBy;
	@Getter final private long addedAt;
	@Getter private final String addedReason;
	@Getter final private long duration;
	@Getter @Setter private UUID removedBy;
	@Getter @Setter private long removedAt;
	@Getter @Setter private String removedReason;
	@Getter @Setter private boolean removed;

	public Punishment(long id, PunishmentType type, long addedAt, String addedReason, long duration) {
		this.id = id;
		this.type = type;
		this.addedAt = addedAt;
		this.addedReason = addedReason;
		this.duration = duration;
	}

	public boolean isPermanent() {
		return type == PunishmentType.BLACKLIST || duration == Integer.MAX_VALUE;
	}

	public boolean hasExpired() {
		return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
	}

	public String getDurationText() {
		if (removed) {
			return "Removed";
		}

		if (isPermanent()) {
			return "Permanent";
		}

		return TimeUtil.millisToRoundedTime(duration);
	}

	public String getTimeRemaining() {
		if (removed) {
			return "Removed";
		}

		if (isPermanent()) {
			return "Permanent";
		}

		if (hasExpired()) {
			return "Expired";
		}

		return TimeUtil.millisToRoundedTime((addedAt + duration) - System.currentTimeMillis());
	}

	public String getContext() {
		if (!(type == PunishmentType.BAN || type == PunishmentType.MUTE)) {
			return removed ? type.getUndoContext() : type.getContext();
		}

		if (isPermanent()) {
			return (removed ? type.getUndoContext() : "permanently " + type.getContext());
		} else {
			return (removed ? type.getUndoContext() : "temporarily " + type.getContext());
		}
	}

	public void broadcast(String sender, String target, boolean silent, boolean removed) {
		if (silent) {
			Bukkit.getOnlinePlayers().forEach(player -> {
				if (player.hasPermission("tCore.staff")) {
					player.sendMessage(Core.get().getMainConfig().getString("PUNISHMENTS.BROADCAST_SILENT")
					                       .replace("{context}", getContext())
					                       .replace("{target}", target)
					                       .replace("{sender}", sender)
							               .replace("{reason}",  removed ? removedReason : addedReason));
				}
			});
		} else {
			Bukkit.broadcastMessage(Core.get().getMainConfig().getString("PUNISHMENTS.BROADCAST")
			                            .replace("{context}", getContext())
			                            .replace("{target}", target)
			                            .replace("{sender}", sender)
					                    .replace("{reason}",  removed ? removedReason : addedReason));
		}
	}

	public String getKickMessage() {
		String kickMessage;

		if (type == PunishmentType.BAN) {
			kickMessage = Core.get().getMainConfig().getString("PUNISHMENTS.BAN.KICK");
			String temporary = "";

			if (!isPermanent()) {
				temporary = Core.get().getMainConfig().getString("PUNISHMENTS.BAN.TEMPORARY");
				temporary = temporary.replace("{time-remaining}", getTimeRemaining());
			}

			kickMessage = kickMessage.replace("{context}", getContext())
			                         .replace("{temporary}", temporary)
					                 .replace("{reason}", addedReason)
									 .replace("{id}", Long.toString(id));

		} else if (type == PunishmentType.KICK) {
			kickMessage = Core.get().getMainConfig().getString("PUNISHMENTS.KICK.KICK")
			                  .replace("{context}", getContext())
			                  .replace("{reason}", addedReason)
					          .replace("{id}", Long.toString(id));
		} else {
			kickMessage = null;
		}

		return Style.translate(kickMessage);
	}

	@Override
	public boolean equals(Object object) {
		return object != null && object instanceof Punishment && ((Punishment) object).id == id;
	}

	public static Map<Punishment, Profile> getPunishments(){
		Map<Punishment, Profile> punishments = new HashMap<>();

		JsonParser jsonParser = new JsonParser();

		for (Document document : Core.get().getDatabase().getProfiles().find()) {
			JsonArray punishmentList = jsonParser.parse(document.getString("punishments")).getAsJsonArray();

			for (JsonElement punishmentData : punishmentList) {

				Punishment punishment = Punishment.DESERIALIZER.deserialize(punishmentData.getAsJsonObject());

				if (punishment != null) {
					punishments.put(punishment, Profile.getByUuid(UUID.fromString(document.getString("uuid"))));
				}
			}
		}

		return punishments;
	}

	public static long getCurrentPunishmentSize(){
		int size = 0;

		JsonParser jsonParser = new JsonParser();

		for (Document document : Core.get().getDatabase().getProfiles().find()) {
			size+=jsonParser.parse(document.getString("punishments")).getAsJsonArray().size();
		}

		return size;
	}
}
