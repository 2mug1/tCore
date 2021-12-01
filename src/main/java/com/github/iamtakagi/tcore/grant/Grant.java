package com.github.iamtakagi.tcore.grant;

import com.github.iamtakagi.tcore.rank.Rank;
import com.github.iamtakagi.tcore.util.TimeUtil;
import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

public class Grant {

	public static GrantJsonSerializer SERIALIZER = new GrantJsonSerializer();
	public static GrantJsonDeserializer DESERIALIZER = new GrantJsonDeserializer();

	@Getter private final UUID uuid;
	@Getter private final Rank rank;
	@Getter @Setter private UUID addedBy;
	@Getter private final long addedAt;
	@Getter private final String addedReason;
	@Getter private final long duration;
	@Getter @Setter private UUID removedBy;
	@Getter @Setter private long removedAt;
	@Getter @Setter private String removedReason;
	@Getter @Setter private boolean removed;

	public Grant(UUID uuid, Rank rank, UUID addedBy, long addedAt, String addedReason, long duration) {
		this.uuid = uuid;
		this.rank = rank;
		this.addedBy = addedBy;
		this.addedAt = addedAt;
		this.addedReason = addedReason;
		this.duration = duration;
	}

	public boolean isPermanent() {
		return duration == Integer.MAX_VALUE;
	}

	public boolean hasExpired() {
		return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
	}

	public String getExpiresAtDate() {
		if (duration == Integer.MAX_VALUE) {
			return "Never";
		}

		return TimeUtil.dateToString(new Date(addedAt + duration), "&7");
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

	@Override
	public boolean equals(Object object) {
		return object instanceof Grant && ((Grant) object).uuid.equals(uuid);
	}

}
