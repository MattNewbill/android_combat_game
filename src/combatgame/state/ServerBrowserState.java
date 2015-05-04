package combatgame.state;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.main.StateManager;
import combatgame.network.GameStub;
import combatgame.network.GamemodeIDs;
import combatgame.network.Internet;
import combatgame.network.MapIDs;
import combatgame.widgets.Button;
import combatgame.widgets.ServerListView;

public class ServerBrowserState extends State {

	private static final long serialVersionUID = 1L;

	private final String GET_OPEN_GAMES_URL = "http://www.newbillity.com/android_combat_game_web/public/games/get_open_games";
	private final String JOIN_GAME_URL = "http://www.newbillity.com/android_combat_game_web/public/games/join";
	
	private transient Button backButton, joinButton, refreshButton;
	
	private transient ServerListView serverList;
	private transient GameStub selectedServer;
	private transient Paint selectedServerPaint;
	
	private transient boolean firstRender; //render the scene one time, then get the list of open games
	private transient boolean hasRefreshedList;
	private String loading = "Loading list of games";
	private transient Paint loadingPaint;
	
	private boolean error = false;
	private boolean noServers = false;
	private String errorString = "Could not connect to server";
	private String noServersString = "No servers found";
	
	public ServerBrowserState(StateManager stateManager) {
		super(stateManager);
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		
		if(!firstRender) {
			if(!hasRefreshedList) {
				refreshList();
				hasRefreshedList = true;
			}
		}
		
		events = backButton.update(events);
		events = joinButton.update(events);
		events = refreshButton.update(events);
		if(serverList != null) {
			serverList.update(events);
			selectedServer = serverList.getSelectedGame();
		}
		
		if(joinButton.state == Button.ACTIVATED) {
			joinButton.disarm();
			joinGame();
		}
		else if(backButton.state == Button.ACTIVATED || stateManager.isBackPressed()) {
			stateManager.setState(new HostJoinState(stateManager));
		}
		else if(refreshButton.state == Button.ACTIVATED) {
			refreshButton.disarm();
			refreshList();
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		
		//render "loading" string
		if(firstRender) {
			g.drawText(loading, 320, Game.G_HEIGHT / 2 - 100, loadingPaint);
			firstRender = false;
		}
		
		if(selectedServer != null) {
			g.drawText("Host: " + selectedServer.hostName, 700, 55, selectedServerPaint);
			g.drawText("Map: " + MapIDs.getMapNameFromID(selectedServer.mapID), 700, 105, selectedServerPaint);
			g.drawText("Gamemode: " + GamemodeIDs.getGamemodeNameFromID(selectedServer.gamemodeID), 700, 155, selectedServerPaint);
		}
		
		//render error string if there is an error
		if(error)
			g.drawText(errorString, Game.G_WIDTH - 261, Game.G_HEIGHT / 2 + 50, loadingPaint);
		if(noServers)
			g.drawText(noServersString, Game.G_WIDTH - 261, Game.G_HEIGHT / 2 + 75, loadingPaint);
		
		//render list view
		if(serverList != null)
			serverList.render(g);
		
		//render join and render buttons
		joinButton.render(g);
		backButton.render(g);
		refreshButton.render(g);
	}

	@Override
	public void pause(Context context, boolean saveData) {
		
	}

	@Override
	public void resume(StateManager stateManager) {
		Game.shouldScale(true);
		this.stateManager = stateManager;
		
		AssetManager am = stateManager.getAssetManager();
		try {
			Bitmap join = BitmapFactory.decodeStream(am.open("images/interface_buttons/join_250.png"));
			Bitmap joinArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/join_250_armed.png"));
			Bitmap back = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			Bitmap refresh = BitmapFactory.decodeStream(am.open("images/menu/refresh_servers.png"));
			Bitmap refreshArmed = BitmapFactory.decodeStream(am.open("images/menu/refresh_servers_armed.png"));
			
			int joinButtonX = Game.G_WIDTH - join.getWidth() - 25;
			int joinButtonY = Game.G_HEIGHT - join.getHeight() - 15;
			int backButtonX = joinButtonX - back.getWidth() - 10;
			int backButtonY = joinButtonY;
			int refreshButtonX = backButtonX;
			int refreshButtonY = Game.G_HEIGHT - join.getHeight() - refresh.getHeight() - 30;
			
			joinButton = new Button(join, joinArmed, joinButtonX, joinButtonY);
			backButton = new Button(back, backArmed, backButtonX, backButtonY);
			refreshButton = new Button(refresh, refreshArmed, refreshButtonX, refreshButtonY);
			
			loadingPaint = new Paint();
			loadingPaint.setColor(Color.WHITE);
			loadingPaint.setTextSize(42);
			loadingPaint.setTextAlign(Paint.Align.CENTER);
			
			selectedServerPaint = new Paint();
			selectedServerPaint.setTextSize(42);
			selectedServerPaint.setColor(Color.WHITE);
			
			firstRender = true;
			hasRefreshedList = false;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		if(joinButton != null) {
			joinButton.recycle();
			joinButton = null;
		}
		if(backButton != null) {
			backButton.recycle();
			backButton = null;
		}
		if(refreshButton != null) {
			refreshButton.recycle();
			refreshButton = null;
		}
		if(serverList != null) {
			serverList.recycle();
			serverList = null;
		}
		loadingPaint = null;
		selectedServerPaint = null;
		selectedServer = null;
	}
	
	@Override
	public int getStateID() {
		return State.SERVER_BROWSER;
	}
	
	private void joinGame() {
		if(selectedServer != null) {
			try {
				JSONObject object = new JSONObject();
				object.put("client_player_id", Game.ID);
				object.put("game_id", selectedServer.gameID);
				
				String response = Internet.postJSON(JOIN_GAME_URL, object);
				
				JSONObject temp = new JSONObject(response);
				if(temp.getBoolean("success")) {
					stateManager.setState(new InternetGameState(stateManager, selectedServer.hostName, Game.NAME, selectedServer.gameID, MapIDs.getMapNameFromID(selectedServer.mapID)+".txt", GamemodeIDs.getGamemodeFromID(selectedServer.gamemodeID), false));
				}
				else {
					error = true;
				}
			} catch(Exception e) {
				error = true;
				e.printStackTrace();
			}
		}
	}
	
	private void refreshList() {
		String response = Internet.getJSON(GET_OPEN_GAMES_URL);
		
		if(serverList != null) {
			serverList.recycle();
			serverList = null;
		}
		Log.i("combatgame", response);
		//parse response
		try {
			JSONObject jsonResponse = new JSONObject(response);
			if(jsonResponse.getBoolean("success")) {
				JSONArray gamesArray = jsonResponse.getJSONArray("games");
				GameStub[] stubsArray = new GameStub[gamesArray.length()];
				for(int i = 0; i < gamesArray.length(); i++) {
					JSONObject temp = gamesArray.getJSONObject(i);
					JSONObject host = temp.getJSONObject("host_player");
					stubsArray[i] = new GameStub(host.getString("name"), temp.getLong("id"), temp.getInt("map_id"), temp.getInt("game_mode_id"));
				}
				serverList = new ServerListView(stubsArray);
				noServers = false;
			}
			else {
				noServers = true;
			}
		} catch(Exception e) {
			error = true;
			e.printStackTrace();
		}
	}

}
