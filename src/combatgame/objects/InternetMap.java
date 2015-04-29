package combatgame.objects;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.res.AssetManager;
import android.util.Log;
import combatgame.gamemode.GameMode;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.network.Internet;
import combatgame.network.JSONHelper;
import combatgame.state.GameState;

public class InternetMap extends Map {

	private static final long serialVersionUID = 1L;

	public long gameID;
	public boolean isPlayerOne;
	public int turnNumber = 1;
	
	public final String turnPostURL = "http://www.newbillity.com/android_combat_game_web/public/games/set_turn";
	public final String turnGetURL = "http://www.newbillity.com/android_combat_game_web/public/games/get_turn";
	
	public final long TIME_BETWEEN_TURN_CHECK = 10000;
	private long startTime;
	
	public InternetMap(GameState gamestate, AssetManager am, String filePath, GameMode gm, long gameID, boolean isPlayerOne) {
		super(gamestate, am, filePath, gm);
		
		this.gameID = gameID;
		this.isPlayerOne = isPlayerOne;
	}
	
	@Override
	public void update(List<TouchEvent> events) {
		if(isGameOver) {
			if(startTime == 0)
				startTime = System.currentTimeMillis();
			if(isWaitingToFade) {
				if(System.currentTimeMillis() - startTime > TIME_TILL_FADE) {
					isWaitingToFade = false;
					isFading = true;
					startTime = System.currentTimeMillis();
				}
			}
			else if(isFading) {
				if(fadePaintAlpha >= 255) {
					gamestate.gameover();
				}
				else {
					fadePaintAlpha += ALPHA_STEP;
					if(fadePaintAlpha > 255)
						fadePaintAlpha = 255;
					fadePaint.setAlpha(fadePaintAlpha);
				}
			}
		}
		else {
			//update current player's turn
			if((isPlayerOne && thisPlayersTurn == player1) || (!isPlayerOne && thisPlayersTurn == player2)) {
				events = thisPlayersTurn.update(events);
			}
			else if(player1 == thisPlayersTurn) {
				events = player2.notMyTurnUpdate(events);
				turnTimeCheck();
			}
			else if(player2 == thisPlayersTurn) {
				events = player1.notMyTurnUpdate(events);
				turnTimeCheck();
			}
			
			//check to see if someone has won yet
			checkWinConditions(); //TODO: check gamemode for win conditions
			
			//update map scroll
			updateMap(events);
			
			//get new lightmap
			clearLightMap();
			if(isPlayerOne)
				lightmap = player1.constructLightMap(lightmap);
			else
				lightmap = player2.constructLightMap(lightmap);
		}
	}
	
	private void turnTimeCheck() {
		if(startTime == 0)
			startTime = System.currentTimeMillis();
		
		if((System.currentTimeMillis() - startTime) > TIME_BETWEEN_TURN_CHECK) {
			getTurnAsJSON();
			startTime = 0;
		}
	}
	
	@Override
	protected void renderPlayers(Graphics2D g) {
		//render "enemy" units if they are visible
		if(isPlayerOne) {
			player2.renderVisibleUnits(g, lightmap);
			player1.render(g);
		}
		else {
			player1.renderVisibleUnits(g, lightmap);
			player2.render(g);
		}
	}
	
	@Override
	public void switchTurn() {
		if(thisPlayersTurn == player1) {
			thisPlayersTurn = player2;
			if(isPlayerOne)
				sendTurnAsJSON();
		}
		else {
			thisPlayersTurn = player1;
			if(!isPlayerOne)
				sendTurnAsJSON();
		}
		
		if((isPlayerOne && thisPlayersTurn == player1) || (!isPlayerOne && thisPlayersTurn == player2))
			thisPlayersTurn.newTurn();
	}
	
	private void sendTurnAsJSON() {
		turnNumber++;
		//if(thisPlayersTurn == player1)
		//	isP1Turn = true;
		
		JSONObject turn = JSONHelper.turnToJSON(gameID, this, turnNumber);
		String response = Internet.postJSON(turnPostURL, turn);
		Log.i("combatgame", response);
	}
	
	private void getTurnAsJSON() {
		try {
			JSONObject getTurn = new JSONObject();
			getTurn.put("game_id", gameID);
			String response = Internet.postJSON(turnGetURL, getTurn);
			
			JSONObject turn = new JSONObject(response);
			//Log.i("combatgame", turn.toString(3));
			int number = turn.getInt("turn_number");
			
			if(number == (turnNumber+1)) {
				turnNumber++;
				JSONArray p1Units = turn.getJSONArray("host_units");
				JSONArray p1Indicators = turn.getJSONArray("host_hit_indicators");
				JSONArray p2Units = turn.getJSONArray("client_units");
				JSONArray p2Indicators = turn.getJSONArray("client_hit_indicators");
				
				player1.injectTurn(p1Units, p1Indicators);
				player2.injectTurn(p2Units, p2Indicators);
				
				switchTurn();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
