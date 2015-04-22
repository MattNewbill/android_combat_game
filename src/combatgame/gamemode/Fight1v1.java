package combatgame.gamemode;

import android.content.res.AssetManager;
import combatgame.network.GamemodeIDs;
import combatgame.objects.Unit;
import combatgame.units.assault.Assault;

public class Fight1v1 extends DeathMatch{

	private static final long serialVersionUID = 1L;

	public Fight1v1(AssetManager am, String path) {
		super(am, path);
		gamemodeName = "1v1";
		gamemodeLength = "Quick";
		gamemodeDescription = "1v1 me noob!";
	}
	
	@Override
	public Unit[] getPlayer1Units() {
		Unit[] units = new Unit[1];
		units[0] = new Assault(-1, "Assault \u03B1", true);
		return units;
	}

	@Override
	public Unit[] getPlayer2Units() {
		Unit[] units = new Unit[1];
		units[0] = new Assault(-1, "Assault \u03B1", false);
		return units;
	}
	
	@Override
	public int getID() {
		return GamemodeIDs.FIGHT_1V1;
	}
	
}
