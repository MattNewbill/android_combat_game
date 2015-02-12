package combatgame.gamemode;

import android.content.res.AssetManager;

import combatgame.objects.Unit;
import combatgame.units.cqc.CQC;
import combatgame.units.medic.Medic;
import combatgame.units.recon.Recon;

public class ShootingFlies extends DeathMatch {

	private static final long serialVersionUID = 1L;
	
	public ShootingFlies(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "Shooting Flies";
		gamemodeLength = "Medium";
		gamemodeDescription = "Face the fury of a horde of angry recons.";
	}
	
	@Override
	public Unit[] getPlayer1Units() {
		Unit[] units = new Unit[2];
		units[0] = new CQC(-1, "Juggernaut \u03B1", true);
		units[1] = new Medic(-1, "Medic \u03B1", true);
		return units;
	}

	@Override
	public Unit[] getPlayer2Units() {
		Unit[] units = new Unit[5];
		units[0] = new Recon(-1, "Recon \u03B1", false);
		units[1] = new Recon(-1, "Recon \u03B2", false);
		units[2] = new Recon(-1, "Recon \u03B3", false);
		units[3] = new Recon(-1, "Recon \u03B4", false);
		units[4] = new Recon(-1, "Recon \u03B5", false);
		return units;
	}
	
}
