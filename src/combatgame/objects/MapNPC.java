package combatgame.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import combatgame.gamemode.GameMode;
import combatgame.state.GameState;
import android.content.res.AssetManager;

public class MapNPC extends Map {
	private static final long serialVersionUID = 1L;
	
	public MapNPC(GameState gamestate, AssetManager am, String filePath, GameMode gm) {
		super(gamestate, am, filePath, gm);
		player2 = new NPC("Player 2", false, this, gm.getPlayer2Units());
	}
}