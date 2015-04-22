package combatgame.gamemode;

import android.content.res.AssetManager;
import combatgame.network.GamemodeIDs;
import combatgame.objects.Unit;
import combatgame.units.sniper.Sniper;
import combatgame.units.recon.Recon;

public class Spotter extends DeathMatch{

	private static final long serialVersionUID = 1L;

	public Spotter(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "Spotter";
		gamemodeLength = "Quick";
		gamemodeDescription = "Use your recon to spot targets for your snipers!";
	}
	
	@Override
	public Unit[] getPlayer1Units() {
		Unit[] units = new Unit[3];
		units[0] = new Sniper(-1, "Sniper \u03B1", true);
		units[1] = new Sniper(-1, "Sniper \u03B2", true);
		units[2] = new Recon(-1, "Recon \u03B1", true);
		return units;
	}

	@Override
	public Unit[] getPlayer2Units() {
		Unit[] units = new Unit[3];
		units[0] = new Sniper(-1, "Sniper \u03B1", false);
		units[1] = new Sniper(-1, "Sniper \u03B2", false);
		units[2] = new Recon(-1, "Recon \u03B1", false);
		return units;
	}
	
	@Override
	public int getID() {
		return GamemodeIDs.SPOTTER;
	}
	
}
