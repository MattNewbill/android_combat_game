package combatgame.state;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import combatgame.assets.GameplayAssets;
import combatgame.gamemode.GameMode;
import combatgame.graphics.Graphics2D;
import combatgame.main.Game;
import combatgame.main.StateManager;
import combatgame.objects.Map;

public class InternetGameState extends GameState {

	private static final long serialVersionUID = 1L;

	long gameID;
	
	Map map;
	String mapPath;
	String tileSet;
	GameMode gm;
	
	public InternetGameState(StateManager stateManager, long gameID, String mapPath, GameMode gm, boolean isPlayerOne) {
		super(stateManager);
		
		this.gameID = gameID;
		this.mapPath = mapPath;
		this.gm = gm;
		
		AssetManager am = stateManager.getAssetManager();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader((am.open(MapSelectionState.mapPath+"/"+mapPath))));
			String[] mapData = reader.readLine().split(" ");
			tileSet = mapData[2];
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameover() {
		
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(Graphics2D g, float delta) {
		
	}

	@Override
	public void pause(Context context, boolean saveData) {
		
	}

	@Override
	public void resume(StateManager stateManager) {
		this.stateManager = stateManager;
		Game.shouldScale(true);
		
		AssetManager am = this.stateManager.getAssetManager();
		
		//load gameplay assets and only the specific tiles for the map that the user chose
		GameplayAssets.loadGameplayAssets(am, tileSet);
		
		//create map
		if(map == null)
			map = new Map(this, am, mapPath, gm);
		else
			gm.resume();
		
		map.resume(this, am, gm);
	}

	@Override
	public int getStateID() {
		return INTERNET_GAME;
	}

}
