package combatgame.gamemode;

import android.content.res.AssetManager;
import combatgame.objects.Unit;
import combatgame.units.assault.Assault;
import combatgame.units.cqc.CQC;
import combatgame.units.medic.Medic;
import combatgame.units.recon.Recon;
import combatgame.units.sniper.Sniper;

public class Classic extends DeathMatch {

	private static final long serialVersionUID = 1L;

	public Classic(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "Classic";
		gamemodeLength = "Long";
		gamemodeDescription = "The classic Strategic Advantage experience.  Play at your own risk.";
	}
	
	@Override
	public Unit[] getPlayer1Units() {
		Unit[] units = new Unit[5];
		units[0] = new Assault(-1, "Assault \u03B1", true);
		units[1] = new Recon(-1, "Recon \u03B1", true);
		units[2] = new CQC(-1, "Juggernaut \u03B1", true);
		units[3] = new Sniper(-1, "Sniper \u03B1", true);
		units[4] = new Medic(-1, "Medic \u03B1", true);
		return units;
	}

	@Override
	public Unit[] getPlayer2Units() {
		Unit[] units = new Unit[5];
		units[0] = new Assault(-1, "Assault \u03B1", false);
		units[1] = new Recon(-1, "Recon \u03B1", false);
		units[2] = new CQC(-1, "Juggernaut \u03B1", false);
		units[3] = new Sniper(-1, "Sniper \u03B1", false);
		units[4] = new Medic(-1, "Medic \u03B1", false);
		return units;
	}

}
