package com.github.iamtakagi.tcore.grant.procedure;

import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.grant.Grant;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class GrantProcedure {

	@Getter private static final Set<GrantProcedure> procedures = new HashSet<>();

	@Getter private final Player issuer;
	@Getter private final Profile recipient;
	@Getter private final GrantProcedureType type;
	@Getter private GrantProcedureStage stage;
	@Getter @Setter private Grant grant;

	public GrantProcedure(Player issuer, Profile recipient, GrantProcedureType type, GrantProcedureStage stage) {
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

	public static GrantProcedure getByPlayer(Player player) {
		for (GrantProcedure procedure : procedures) {
			if (procedure.issuer.equals(player)) {
				return procedure;
			}
		}

		return null;
	}

}
