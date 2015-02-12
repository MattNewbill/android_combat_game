package combatgame.gamemode;

import android.content.res.AssetManager;

import combatgame.objects.Unit;
import combatgame.units.cqc.CQC;

public class MeatGrinder extends DeathMatch {

	private static final long serialVersionUID = 1L;

	public MeatGrinder(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "Meat Grinder";
		gamemodeLength = "Long";
		gamemodeDescription = "Prepare to shoot, and shoot, and shoot some more.";
	}
	
	@Override
	public Unit[] getPlayer1Units() {
		Unit[] units = new Unit[5];
		units[0] = new CQC(-1, "Juggernaut \u03B1", true);
		units[1] = new CQC(-1, "Juggernaut \u03B2", true);
		units[2] = new CQC(-1, "Juggernaut \u03B3", true);
		units[3] = new CQC(-1, "Juggernaut \u03B4", true);
		units[4] = new CQC(-1, "Juggernaut \u03B5", true);
		return units;
	}

	@Override
	public Unit[] getPlayer2Units() {
		Unit[] units = new Unit[5];
		units[0] = new CQC(-1, "Juggernaut \u03B1", false);
		units[1] = new CQC(-1, "Juggernaut \u03B2", false);
		units[2] = new CQC(-1, "Juggernaut \u03B3", false);
		units[3] = new CQC(-1, "Juggernaut \u03B4", false);
		units[4] = new CQC(-1, "Juggernaut \u03B5", false);
		return units;
	}
	
}
