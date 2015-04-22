package combatgame.gamemode;

import android.content.res.AssetManager;
import combatgame.network.GamemodeIDs;
import combatgame.objects.Unit;
import combatgame.units.recon.Recon;
import combatgame.units.cqc.CQC;
import combatgame.units.medic.Medic;

public class Convoy extends DeathMatch{

	private static final long serialVersionUID = 1L;

	public Convoy(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "Convoy";
		gamemodeLength = "Long";
		gamemodeDescription = "Scout ahead and follow up with the muscle!";
	}
	
	@Override
	public Unit[] getPlayer1Units() {
		Unit[] units = new Unit[3];
		units[0] = new Recon(-1, "Recon \u03B1", true);
		units[1] = new CQC(-1, "Juggernaut \u03B1", true);
		units[2] = new Medic(-1, "Medic \u03B1", true);
		return units;
	}

	@Override
	public Unit[] getPlayer2Units() {
		Unit[] units = new Unit[3];
		units[0] = new Recon(-1, "Recon \u03B1", false);
		units[1] = new CQC(-1, "Juggernaut \u03B1", false);
		units[2] = new Medic(-1, "Medic \u03B1", false);
		return units;
	}
	
	@Override
	public int getID() {
		return GamemodeIDs.CONVOY;
	}
	
}
