package combatgame.state;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import combatgame.assets.GameplayAssets;
import combatgame.gamemode.GameMode;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.main.StateManager;
import combatgame.network.Internet;
import combatgame.objects.InternetMap;
import combatgame.objects.Map;
import combatgame.widgets.Button;

public class InternetGameState extends GameState {

	private static final long serialVersionUID = 1L;

	private final String IS_ACTIVE_URL = "http://www.newbillity.com/android_combat_game_web/public/games/set_is_active";
	long gameID;
	
	Map map;
	String mapPath;
	String tileSet;
	GameMode gm;
	
	boolean isPlayerOne;
	
	boolean isGameOver = false;
	
	boolean isExitDialogShowing = false;
	transient Paint exitDialogPaint;
	transient Button yesButton;
	transient Button noButton;
	
	public InternetGameState(StateManager stateManager, long gameID, String mapPath, GameMode gm, boolean isPlayerOne) {
		super(stateManager);
		
		this.gameID = gameID;
		this.mapPath = mapPath;
		this.gm = gm;
		this.isPlayerOne = isPlayerOne;
		
		AssetManager am = stateManager.getAssetManager();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader((am.open(MapSelectionState.mapPath+"/"+mapPath))));
			String[] mapData = reader.readLine().split(" ");
			tileSet = mapData[2];
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameover() {
		isGameOver = true;
	}

	@Override
	public void update(float delta) {
		if(!isGameOver) {
			List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
			if(!isExitDialogShowing)
				isExitDialogShowing = stateManager.isBackPressed();
			if(isExitDialogShowing) {
				events = yesButton.update(events);
				events = noButton.update(events);
				if(yesButton.state == Button.ACTIVATED) {
					yesButton.disarm();
					setGameInactive();
					stateManager.setState(new MainMenuState(stateManager));
				}
				if(noButton.state == Button.ACTIVATED) {
					noButton.disarm();
					isExitDialogShowing = false;
				}
			}
			else {
				map.update(events);
			}
		}
		else {
			setGameInactive();
			stateManager.setState(new MainMenuState(stateManager));
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		map.render(g);
		
		if(isExitDialogShowing) {
			g.drawRect(0, 0, Game.G_WIDTH, Game.G_HEIGHT, exitDialogPaint);
			g.drawBitmap(GameplayAssets.exitDialogIcon, Game.G_WIDTH / 2 - GameplayAssets.exitDialogIcon.getWidth() / 2, Game.G_HEIGHT / 2 - GameplayAssets.exitDialogIcon.getHeight() / 2, null); //TODO: scale for larger devices
			yesButton.render(g);
			noButton.render(g);
		}
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
			map = new InternetMap(this, am, MapSelectionState.mapPath+"/"+mapPath, gm, gameID, isPlayerOne);
		else
			gm.resume();
		
		map.resume(this, am, gm);
		
		yesButton = new Button(GameplayAssets.yesIcon, GameplayAssets.yesArmedIcon, Game.G_WIDTH / 2 - GameplayAssets.yesIcon.getWidth() - 10, Game.G_HEIGHT / 2 + GameplayAssets.yesIcon.getHeight() / 2 + 30);
		noButton = new Button(GameplayAssets.noIcon, GameplayAssets.noArmedIcon, Game.G_WIDTH / 2 + 10, Game.G_HEIGHT / 2 + GameplayAssets.noIcon.getHeight() / 2 + 30);
		exitDialogPaint = new Paint();
		exitDialogPaint.setColor(Color.BLACK);
		exitDialogPaint.setAlpha(125);
	}

	@Override
	public int getStateID() {
		return INTERNET_GAME;
	}
	
	@Override
	public boolean getCheckWin(){
		return true;
	}
	
	private void setGameInactive() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("game_id", gameID);
			obj.put("is_active", false);
			
			String response = Internet.postJSON(IS_ACTIVE_URL, obj);
			Log.i("combatgame", "Set game active response: " + response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
