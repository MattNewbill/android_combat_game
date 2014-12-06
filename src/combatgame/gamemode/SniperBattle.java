package combatgame.gamemode;

import android.content.res.AssetManager;
import combatgame.objects.Unit;
import combatgame.units.sniper.Sniper;

public class SniperBattle extends DeathMatch{

	public SniperBattle(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "Sniper Battle";
		gamemodeLength = "Quick";
		gamemodeDescription = "3v3 Sniper battle to the death!";
	}
	
	@Override
	public Unit[] getPlayer1Units() {
		Unit[] units = new Unit[3];
		units[0] = new Sniper(-1, "Sniper \u03B1", true);
		units[1] = new Sniper(-1, "Sniper \u03B2", true);
		units[2] = new Sniper(-1, "Sniper \u03B3", true);
		return units;
	}

	@Override
	public Unit[] getPlayer2Units() {
		Unit[] units = new Unit[3];
		units[0] = new Sniper(-1, "Sniper \u03B1", false);
		units[1] = new Sniper(-1, "Sniper \u03B2", false);
		units[2] = new Sniper(-1, "Sniper \u03B3", false);
		return units;
	}
	
}
