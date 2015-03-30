package combatgame.gamemode;

import android.content.res.AssetManager;
import combatgame.network.GamemodeIDs;
import combatgame.objects.Unit;
import combatgame.units.medic.Medic;
import combatgame.units.sniper.Sniper;

public class Mediccc extends DeathMatch {

	private static final long serialVersionUID = 1L;
	
	public Mediccc(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "Medic!!!!";
		gamemodeLength = "Long";
		gamemodeDescription = "MEDIC!! MEDIC!! MEDIC!! MEDIC!!  BOOM! HEADSHOT!";
	}
	
	@Override
	public Unit[] getPlayer1Units() {
		Unit[] units = new Unit[5];
		units[0] = new Sniper(-1, "Sniper \u03B1", true);
		units[1] = new Sniper(-1, "Sniper \u03B2", true);
		units[2] = new Medic(-1, "Medic \u03B1", true);
		units[3] = new Medic(-1, "Medic \u03B2", true);
		units[4] = new Medic(-1, "Medic \u03B3", true);
		return units;
	}

	@Override
	public Unit[] getPlayer2Units() {
		Unit[] units = new Unit[5];
		units[0] = new Sniper(-1, "Sniper \u03B1", false);
		units[1] = new Sniper(-1, "Sniper \u03B2", false);
		units[2] = new Medic(-1, "Medic \u03B1", false);
		units[3] = new Medic(-1, "Medic \u03B2", false);
		units[4] = new Medic(-1, "Medic \u03B3", false);
		return units;
	}
	
	@Override
	public int getID() {
		return GamemodeIDs.MEDICCC;
	}
	
}
