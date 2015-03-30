package combatgame.state;

import java.util.List;

import org.json.JSONObject;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.main.StateManager;
import combatgame.network.Internet;
import combatgame.network.MapIDs;
import combatgame.objects.PartialMap;
import combatgame.widgets.Button;
import combatgame.widgets.GamemodeListView;

public class InternetGamemodeSelectionState extends GamemodeSelectionState {

	private static final long serialVersionUID = 1L;

	public final String CREATE_GAME_URL = "http://www.newbillity.com/android_combat_game_web/public/games/create_game";
	
	public InternetGamemodeSelectionState(StateManager stateManager, PartialMap map) {
		super(stateManager, map);
	}
	
	@Override
	public int getStateID() {
		return State.INTERNET_GAMEMODE_SELECTION;
	}
	
	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		
		events = startButton.update(events);
		events = backButton.update(events);
		listview.update(events);
		selectedGamemode = listview.getSelectedGamemode();
		
		if(startButton.state == Button.ACTIVATED) {
			startButton.disarm();
			host();
		}
		else if(backButton.state == Button.ACTIVATED) {
			backButton.disarm();
			stateManager.setState(new InternetMapSelectionState(stateManager));
		}
		else if(stateManager.isBackPressed()) {
			stateManager.setState(new InternetMapSelectionState(stateManager));
		}
	}
	
	@Override
	public void resume(StateManager stateManager) {
		Game.shouldScale(true);
		this.stateManager = stateManager;
		
		AssetManager am = stateManager.getAssetManager();
		this.listview = new GamemodeListView(am);
		try {
			Bitmap start = BitmapFactory.decodeStream(am.open("images/interface_buttons/host_250.png"));
			Bitmap startArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/host_250_armed.png"));
			Bitmap back = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			
			int okButtonX = Game.G_WIDTH - start.getWidth() - 25;
			int okButtonY = Game.G_HEIGHT - start.getHeight() - 15;
			int backButtonX = okButtonX - back.getWidth() - 10;
			int backButtonY = okButtonY;
			
			startButton = new Button(start, startArmed, okButtonX, okButtonY);
			backButton = new Button(back, backArmed, backButtonX, backButtonY);
			
			selectedGamemodeNamePaint = new Paint();
			selectedGamemodeNamePaint.setTextSize(36);
			selectedGamemodeNamePaint.setColor(Color.WHITE);
			
			selectedGamemodeLengthPaint = new Paint();
			selectedGamemodeLengthPaint.setTextSize(32);
			selectedGamemodeLengthPaint.setColor(Color.WHITE);
			
			selectedGamemodeDescriptionPaint = new Paint();
			selectedGamemodeDescriptionPaint.setTextSize(28);
			selectedGamemodeDescriptionPaint.setColor(Color.WHITE);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void host() {
		int mapID = MapIDs.getIDFromMapName(map.getPath());
		int gamemodeID = selectedGamemode.getID();
		
		try {
			JSONObject game = new JSONObject();
			game.put("host_player_id", Game.ID);
			game.put("game_mode_id", gamemodeID);
			game.put("map_id", mapID);
			//game.put("password", "");
			
			String response = Internet.postJSON(CREATE_GAME_URL, game);
			JSONObject temp = new JSONObject(response);
			Log.i("combatgame", response);
			if(temp.getLong("game_id") > 0)
				stateManager.setState(new LobbyState(stateManager, temp.getLong("game_id"), mapID, gamemodeID));
			else
				stateManager.setState(new HostJoinState(stateManager, true));
		} catch(Exception e) {
			stateManager.setState(new HostJoinState(stateManager, true));
			e.printStackTrace();
		}
	}

}
