package combatgame.gamemode;

import android.content.res.AssetManager;
import combatgame.network.GamemodeIDs;
import combatgame.objects.Unit;
import combatgame.units.recon.Recon;
import combatgame.units.medic.Medic;

public class Reconnaissance extends DeathMatch{

	private static final long serialVersionUID = 1L;

	public Reconnaissance(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "Reconnaissance";
		gamemodeLength = "Quick";
		gamemodeDescription = "Rush your recon team in for the quick kill!";
	}
	
	@Override
	public Unit[] getPlayer1Units() {
		Unit[] units = new Unit[3];
		units[0] = new Recon(-1, "Recon \u03B1", true);
		units[1] = new Recon(-1, "Recon \u03B2", true);
		units[2] = new Medic(-1, "Medic \u03B1", true);
		return units;
	}

	@Override
	public Unit[] getPlayer2Units() {
		Unit[] units = new Unit[3];
		units[0] = new Recon(-1, "Recon \u03B1", false);
		units[1] = new Recon(-1, "Recon \u03B2", false);
		units[2] = new Medic(-1, "Medic \u03B1", false);
		return units;
	}
	
	@Override
	public int getID() {
		return GamemodeIDs.RECONNAISSANCE;
	}
	
}
