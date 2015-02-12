package combatgame.gamemode;

import android.content.res.AssetManager;

import combatgame.objects.Unit;
import combatgame.units.assault.Assault;
import combatgame.units.recon.Recon;
import combatgame.units.sniper.Sniper;

public class Hunted extends DeathMatch {

	private static final long serialVersionUID = 1L;

	public Hunted(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "Hunted";
		gamemodeLength = "Quick";
		gamemodeDescription = "Evade and take down the roving assault troopers with precision and trickery.";
	}
	
	@Override
	public Unit[] getPlayer1Units() {
		Unit[] units = new Unit[3];
		units[0] = new Assault(-1, "Assault \u03B1", true);
		units[1] = new Assault(-1, "Assault \u03B2", true);
		units[2] = new Assault(-1, "Assault \u03B3", true);
		return units;
	}

	@Override
	public Unit[] getPlayer2Units() {
		Unit[] units = new Unit[2];
		units[0] = new Recon(-1, "Recon \u03B1", false);
		units[1] = new Sniper(-1, "Sniper \u03B1", false);
		return units;
	}
	
}
