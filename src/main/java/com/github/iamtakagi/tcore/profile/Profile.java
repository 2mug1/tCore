package com.github.iamtakagi.tcore.profile;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.profile.conversation.ProfileConversations;
import com.github.iamtakagi.tcore.profile.experience.ExpBooster;
import com.github.iamtakagi.tcore.profile.experience.Experience;
import com.github.iamtakagi.tcore.profile.staff.ProfileStaffOptions;
import com.github.iamtakagi.tcore.punishment.Punishment;
import com.github.iamtakagi.tcore.punishment.PunishmentType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.github.iamtakagi.tcore.clan.Clan;
import com.github.iamtakagi.tcore.friend.Friend;
import com.github.iamtakagi.tcore.grant.event.GrantAppliedEvent;
import com.github.iamtakagi.tcore.grant.event.GrantExpireEvent;
import com.github.iamtakagi.tcore.profile.option.ProfileOptions;
import com.github.iamtakagi.tcore.grant.Grant;
import com.github.iamtakagi.tcore.rank.Rank;
import com.github.iamtakagi.tcore.util.Cooldown;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Profile {

	private static final JsonParser JSON_PARSER = new JsonParser();

	@Getter private static Map<UUID, Profile> profiles = new HashMap<>();

	@Getter @Setter private String username;
	@Getter private final UUID uuid;
	@Getter @Setter private Long firstSeen;
	@Getter @Setter private Long lastSeen;
	@Getter @Setter private String currentAddress;
	@Getter private List<String> ipAddresses;
	@Getter private final List<UUID> knownAlts;
	@Getter private final ProfileOptions options;
	@Getter private final ProfileStaffOptions staffOptions;
	@Getter private final ProfileConversations conversations;
	@Getter private Grant activeGrant;
	@Getter private final List<Grant> grants;
	@Getter private final List<Punishment> punishments;
	@Getter @Setter private boolean loaded;
	@Getter @Setter private Cooldown chatCooldown;
	@Getter private Experience experience;
	@Getter private Economy economy;
	@Getter @Setter private Long loginBonusTimestamp;
	@Getter private Friend friend;
	@Setter private String clanName;
	@Getter @Setter private long lastReported;
	@Getter @Setter private ExpBooster expBooster;

	@Getter @Setter private String prefix, suffix;

	public Profile(String username, UUID uuid) {
		this.username = username;
		this.uuid = uuid;
		this.grants = new ArrayList<>();
		this.punishments = new ArrayList<>();
		this.ipAddresses = new ArrayList<>();
		this.knownAlts = new ArrayList<>();
		this.options = new ProfileOptions();
		this.staffOptions = new ProfileStaffOptions();
		this.conversations = new ProfileConversations(this);
		this.chatCooldown = new Cooldown(0);
		this.experience = new Experience(this);
		this.economy = new Economy(this);
		this.friend = new Friend(this);
		load();
	}

	public void findAlternates() {
		if (this.currentAddress != null) {
			try (MongoCursor<Document> cursor = Core.get().getDatabase().getProfiles().find(Filters.eq("currentAddress", this.currentAddress)).iterator()) {
				cursor.forEachRemaining(document -> {
					final UUID uuid = UUID.fromString(document.getString("uuid"));

					if (!uuid.equals(this.getUuid())) {
						if (!this.knownAlts.contains(uuid)) {
							this.knownAlts.add(uuid);
						}
					}
				});
			}
		}
	}

	public Clan getClan(){
		if(clanName == null){
			return null;
		}

		Clan clan = Clan.getByName(clanName);
		if(clan != null && !clan.isDisbanded() && clan.isMember(uuid)){
			return clan;
		}

		return null;
	}

	public boolean isInClan(){
		return getClan() != null;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public String getColoredUsername() {
		return (isInClan() ? getClan().getStyleTag() + " " : "") + activeGrant.getRank().getColor() + username;
	}

	public Punishment getActivePunishmentByType(PunishmentType type) {
		for (Punishment punishment : punishments) {
			if (punishment.getType() == type && !punishment.isRemoved() && !punishment.hasExpired()) {
				return punishment;
			}
		}

		return null;
	}

	public int getPunishmentCountByType(PunishmentType type) {
		int i = 0;

		for (Punishment punishment : punishments) {
			if (punishment.getType() == type) i++;
		}

		return i;
	}

	public Rank getActiveRank() {
		return activeGrant.getRank();
	}

	public void activateNextGrant() {
		List<Grant> grants = new ArrayList<>(this.grants);

		grants.sort(Comparator.comparingInt(grant -> grant.getRank().getWeight()));
		Collections.reverse(grants);

		for (Grant grant : grants) {
			if (!grant.isRemoved() && !grant.hasExpired()) {
				if (!grant.equals(activeGrant)) {
					Core.get().debug("");
					activeGrant = grant;
					setupBukkitPlayer(getPlayer());
					return;
				}
			}
		}
	}

	public void checkGrants() {
		Player player = getPlayer();

		for (Grant grant : grants) {
			if (!grant.isRemoved() && grant.hasExpired()) {
				grant.setRemovedAt(System.currentTimeMillis());
				grant.setRemovedReason("Grant expired");
				grant.setRemoved(true);

				if (player != null) {
					new GrantExpireEvent(player, grant).call();
				}

				if (grant.equals(activeGrant)) {
					activateNextGrant();
				}
			}
		}

		if (activeGrant == null) {
			Grant defaultGrant = new Grant(UUID.randomUUID(), Rank.getDefaultRank(), null,
					System.currentTimeMillis(), "Default", Integer.MAX_VALUE);

			grants.add(defaultGrant);
			activeGrant = defaultGrant;

			if (player != null) {
				new GrantAppliedEvent(player, defaultGrant).call();
			}
		}
	}

	public void setupBukkitPlayer(Player player) {
		if (player == null) {
			return;
		}

		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			if (attachmentInfo.getAttachment() == null) {
				continue;
			}

			attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> {
				attachmentInfo.getAttachment().unsetPermission(permission);
			});
		}

		PermissionAttachment attachment = player.addAttachment(Core.get());

		for (String perm : activeGrant.getRank().getAllPermissions()) {
			attachment.setPermission(perm, true);
		}

		player.recalculatePermissions();
	}

	public boolean canReport(){
		return System.currentTimeMillis() >= lastReported + 1000*180;
	}

	public void load() {
		Document document = Core.get().getDatabase().getProfiles().find(Filters.eq("uuid", uuid.toString())).first();

		if (document != null) {
			if (username == null) {
				username = document.getString("username");
			}

			firstSeen = document.getLong("firstSeen");
			lastSeen = document.getLong("lastSeen");
			currentAddress = document.getString("currentAddress");
			ipAddresses = Core.GSON.fromJson(document.getString("ipAddresses"), Core.LIST_STRING_TYPE);
			experience.setLevel(document.getInteger("level"));
			experience.setExp(document.getInteger("exp"));
			experience.setTotalExp(document.getInteger("totalExp"));
			economy.setCoins(document.getInteger("coins"));
			loginBonusTimestamp = document.getLong("loginBonusTimestamp");
			clanName = document.getString("clanName");
			lastReported = document.getLong("lastReported");
			prefix = document.getString("prefix");
			suffix = document.getString("suffix");

			if(document.getString("expBooster") != null){
				expBooster = ExpBooster.DESERIALIZER.deserialize(JSON_PARSER.parse(document.getString("expBooster")).getAsJsonObject());
			}

			if(prefix == null){
				this.prefix = "";
			}

			if(suffix == null){
				this.suffix = "";
			}

			//Options
			Document optionsDocument = (Document) document.get("options");
			options.publicChatEnabled(optionsDocument.getBoolean("publicChatEnabled"));
			options.receivingNewConversations(optionsDocument.getBoolean("receivingNewConversations"));
			options.playingMessageSounds(optionsDocument.getBoolean("playingMessageSounds"));

			//Friends
			Document friendsDocument = (Document) document.get("friends");
			JsonArray receivingPlayersUUID = JSON_PARSER.parse(friendsDocument.getString("receivingPlayersUUID")).getAsJsonArray();
			for (JsonElement element : receivingPlayersUUID) {
				String uuid = element.getAsJsonObject().get("uuid").getAsString();
				if(uuid != null) {
					friend.getReceivingPlayersUUID().add(uuid);
				}
			}

			JsonArray requestingPlayersUUID = JSON_PARSER.parse(friendsDocument.getString("requestingPlayersUUID")).getAsJsonArray();
			for (JsonElement element : requestingPlayersUUID) {
				String uuid = element.getAsJsonObject().get("uuid").getAsString();
				if(uuid != null) {
					friend.getRequestingPlayersUUID().add(uuid);
				}
			}
			JsonArray acceptedPlayersUUID = JSON_PARSER.parse(friendsDocument.getString("acceptedPlayersUUID")).getAsJsonArray();
			for (JsonElement element : acceptedPlayersUUID) {
				String uuid = element.getAsJsonObject().get("uuid").getAsString();
				if(uuid != null) {
					friend.getAcceptedPlayersUUID().add(uuid);
				}
			}

			//Grants
			JsonArray grantList = JSON_PARSER.parse(document.getString("grants")).getAsJsonArray();
			for (JsonElement grantData : grantList) {
				// Transform into a Grant object
				Grant grant = Grant.DESERIALIZER.deserialize(grantData.getAsJsonObject());
				if (grant != null) {
					this.grants.add(grant);
				}
			}

			JsonArray punishmentList = JSON_PARSER.parse(document.getString("punishments")).getAsJsonArray();
			for (JsonElement punishmentData : punishmentList) {
				Punishment punishment = Punishment.DESERIALIZER.deserialize(punishmentData.getAsJsonObject());
				if (punishment != null) {
					this.punishments.add(punishment);
				}
			}
		}

		// Update active grants
		activateNextGrant();
		checkGrants();

		// Load Alts
		findAlternates();

		// Set loaded to true
		loaded = true;
	}

	public void save() {
		if(prefix == null){
			this.prefix = "";
		}

		if(suffix == null){
			this.suffix = "";
		}

		Document document = new Document();
		document.put("username", username);
		document.put("uuid", uuid.toString());
		document.put("firstSeen", firstSeen);
		document.put("lastSeen", lastSeen);
		document.put("currentAddress", currentAddress);
		document.put("ipAddresses", Core.GSON.toJson(ipAddresses, Core.LIST_STRING_TYPE));
		document.put("level", experience.getLevel());
		document.put("exp", experience.getExp());
		document.put("totalExp", experience.getTotalExp());
		document.put("coins", economy.getCoins());
		document.put("loginBonusTimestamp", loginBonusTimestamp);
		document.put("clanName", clanName);
		document.put("lastReported", lastReported);
		document.put("prefix", prefix);
		document.put("suffix", suffix);


		if(expBooster != null) {
			document.put("expBooster", ExpBooster.SERIALIZER.serialize(expBooster).toString());
		}else{
			document.put("expBooster", null);
		}

		Document optionsDocument = new Document();
		optionsDocument.put("publicChatEnabled", options.publicChatEnabled());
		optionsDocument.put("receivingNewConversations", options.receivingNewConversations());
		optionsDocument.put("playingMessageSounds", options.playingMessageSounds());
		document.put("options", optionsDocument);

		//Friends
		Document friendsDocument = new Document();

		JsonArray receivingPlayersUUID = new JsonArray();
		for (String uuid : this.friend.getReceivingPlayersUUID()) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("uuid", uuid);
			receivingPlayersUUID.add(jsonObject);
		}
		friendsDocument.put("receivingPlayersUUID", receivingPlayersUUID.toString());

		JsonArray requestingPlayersUUID = new JsonArray();
		for (String uuid : this.friend.getRequestingPlayersUUID()) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("uuid", uuid);
			requestingPlayersUUID.add(jsonObject);
		}
		friendsDocument.put("requestingPlayersUUID", requestingPlayersUUID.toString());

		JsonArray acceptedPlayersUUID = new JsonArray();
		for (String uuid : this.friend.getAcceptedPlayersUUID()) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("uuid", uuid);
			acceptedPlayersUUID.add(jsonObject);
		}
		friendsDocument.put("acceptedPlayersUUID", acceptedPlayersUUID.toString());

		document.put("friends", friendsDocument);

		//Grants
		JsonArray grantList = new JsonArray();

		for (Grant grant : this.grants) {
			grantList.add(Grant.SERIALIZER.serialize(grant));
		}

		document.put("grants", grantList.toString());

		JsonArray punishmentList = new JsonArray();

		for (Punishment punishment : this.punishments) {
			punishmentList.add(Punishment.SERIALIZER.serialize(punishment));
		}

		document.put("punishments", punishmentList.toString());

		Core.get().getDatabase().getProfiles().replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
	}

	public String getFormattedName(){
		return (getPrefix() == null ? "" : ChatColor.translateAlternateColorCodes('&', getPrefix())) +
				getActiveRank().getColor() + getPlayer().getName() + (getSuffix() == null ? "" : ChatColor.translateAlternateColorCodes('&', getSuffix()));
	}

	public static Profile getByUuid(UUID uuid) {
		if (profiles.containsKey(uuid)) {
			return profiles.get(uuid);
		}

		return new Profile(null, uuid);
	}

	public static Profile getByUsername(String username) {
		Player player = Bukkit.getPlayer(username);

		if (player != null) {
			return profiles.get(player.getUniqueId());
		}

		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);

		if (offlinePlayer.hasPlayedBefore()) {
			if (profiles.containsKey(offlinePlayer.getUniqueId())) {
				return profiles.get(offlinePlayer.getUniqueId());
			}

			return new Profile(offlinePlayer.getName(), offlinePlayer.getUniqueId());
		}

		UUID uuid = Core.get().getRedisCache().getUuid(username);

		if (uuid != null) {
			if (profiles.containsKey(uuid)) {
				return profiles.get(uuid);
			}

			return new Profile(username, uuid);
		}

		return null;
	}

	public static List<Profile> getByIpAddress(String ipAddress) {
		List<Profile> profiles = new ArrayList<>();
		Bson filter = Filters.eq("currentAddress", ipAddress);

		try (MongoCursor<Document> cursor = Core.get().getDatabase().getProfiles().find(filter).iterator()) {
			while (cursor.hasNext()) {
				Document document = cursor.next();
				profiles.add(new Profile(document.getString("username"),
						UUID.fromString(document.getString("uuid"))));
			}
		}

		return profiles;
	}
}
