package combatgame.state;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.main.StateManager;
import combatgame.network.GamemodeIDs;
import combatgame.network.Internet;
import combatgame.network.MapIDs;
import combatgame.widgets.Button;

public class LobbyState extends State {

	private static final long serialVersionUID = 1L;

	private final String IS_ACTIVE_URL = "http://www.newbillity.com/android_combat_game_web/public/games/set_is_active";
	private final String GET_GAME_URL = "http://www.newbillity.com/android_combat_game_web/public/games/get_game";
	
	private final long TIME_BETWEEN_GETS = 5000;
	private long timer = 0;
	
	private long gameID;
	private int mapID, gamemodeID;
	
	private transient Paint textPaint;
	
	private transient Button backButton;
	
	public LobbyState(StateManager stateManager, long gameID, int mapID, int gamemodeID) {
		super(stateManager);
		this.gameID = gameID;
		this.mapID = mapID;
		this.gamemodeID = gamemodeID;
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		
		if((System.nanoTime() - timer) / 1000000 > TIME_BETWEEN_GETS) {
			timer = System.nanoTime();
			checkGameStatus();
		}
		else {
		
			events = backButton.update(events);
		
			if(backButton.state == Button.ACTIVATED || stateManager.isBackPressed()) {
				stateManager.setState(new HostJoinState(stateManager));
			}
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		g.drawText("Waiting for player to join", Game.G_WIDTH / 2, Game.G_HEIGHT / 2 - 100, textPaint);
		
		backButton.render(g);
	}

	@Override
	public void pause(Context context, boolean saveData) {
		try {
			JSONObject object = new JSONObject();
			object.put("is_active", false);
			object.put("game_id", gameID);
			
			String response = Internet.postJSON(IS_ACTIVE_URL, object);
			Log.i("combatgame", "Set game to non-active result: " + response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void checkGameStatus() {
		try {
			JSONObject object = new JSONObject();
			object.put("game_id", gameID);
			
			String response = Internet.postJSON(GET_GAME_URL, object);
			//Log.i("combatgame", response);
			
			JSONObject jsonResponse = new JSONObject(response);
			JSONObject game = jsonResponse.getJSONObject("game");
			if(game.getLong("client_player_id") != 0)
				stateManager.setState(new InternetGameState(stateManager, gameID, MapIDs.getMapNameFromID(mapID)+".txt", GamemodeIDs.getGamemodeFromID(gamemodeID), true));
		} catch(Exception e) {
			stateManager.setState(new HostJoinState(stateManager, true));
			e.printStackTrace();
		}
	}
	
	@Override
	public void resume(StateManager stateManager) {
		this.stateManager = stateManager;
		Game.shouldScale(true);
		
		AssetManager am = stateManager.getAssetManager();
		try {
			Bitmap backButtonDisarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backButtonArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			
			int backButtonX = 40;
			int backButtonY = 555;
			
			backButton = new Button(backButtonDisarmed, backButtonArmed, backButtonX, backButtonY);
			
			JSONObject object = new JSONObject();
			object.put("is_active", true);
			object.put("game_id", gameID);
			
			String response = Internet.postJSON(IS_ACTIVE_URL, object);
			Log.i("combatgame", "Set game to active result: " + response);
			
			timer = System.nanoTime();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(42);
		
		
	}

	@Override
	public void dispose() {
		if(backButton != null) {
			backButton.recycle();
			backButton = null;
		}
		textPaint = null;
	}
	
	@Override
	public int getStateID() {
		return LOBBY;
	}

}
