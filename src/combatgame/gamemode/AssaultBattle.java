package combatgame.gamemode;

import android.content.res.AssetManager;
import combatgame.network.GamemodeIDs;
import combatgame.objects.Unit;
import combatgame.units.assault.Assault;

public class AssaultBattle extends DeathMatch{

	private static final long serialVersionUID = 1L;

	public AssaultBattle(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "Assault Battle";
		gamemodeLength = "Quick";
		gamemodeDescription = "3v3 Assault battle to the death!";
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
		Unit[] units = new Unit[3];
		units[0] = new Assault(-1, "Assault \u03B1", false);
		units[1] = new Assault(-1, "Assault \u03B2", false);
		units[2] = new Assault(-1, "Assault \u03B3", false);
		return units;
	}
	
	@Override
	public int getID() {
		return GamemodeIDs.ASSAULT_BATTLE;
	}
	
}
