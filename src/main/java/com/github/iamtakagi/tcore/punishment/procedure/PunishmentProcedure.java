package com.github.iamtakagi.tcore.punishment.procedure;

import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.punishment.Punishment;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class PunishmentProcedure {

	@Getter private static final Set<PunishmentProcedure> procedures = new HashSet<>();

	@Getter private final Player issuer;
	@Getter private final Profile recipient;
	@Getter private final PunishmentProcedureType type;
	@Getter private PunishmentProcedureStage stage;
	@Getter @Setter private Punishment punishment;

	public PunishmentProcedure(Player issuer, Profile recipient, PunishmentProcedureType type, PunishmentProcedureStage stage) {
		this.issuer = issuer;
		this.recipient = recipient;
		this.type = type;
		this.stage = stage;

		procedures.add(this);
	}

	public void finish() {
		this.recipient.save();
		procedures.remove(this);
	}

	public void cancel() {
		procedures.remove(this);
	}

	public static PunishmentProcedure getByPlayer(Player player) {
		for (PunishmentProcedure procedure : procedures) {
			if (procedure.issuer.equals(player)) {
				return procedure;
			}
		}

		return null;
	}



}
