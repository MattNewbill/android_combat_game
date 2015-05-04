package combatgame.objects;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import combatgame.assets.GameplayAssets;
import combatgame.gamemode.GameMode;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.network.Internet;
import combatgame.network.JSONHelper;
import combatgame.network.OnCompletion;
import combatgame.network.PostAsync;
import combatgame.network.PostWrapper;
import combatgame.state.GameState;

public class InternetMap extends Map {

	private static final long serialVersionUID = 1L;

	public long gameID;
	public boolean isPlayerOne;
	public int turnNumber = 1;
	
	public final String turnPostURL = "http://www.newbillity.com/android_combat_game_web/public/games/set_turn";
	public final String turnGetURL = "http://www.newbillity.com/android_combat_game_web/public/games/get_turn";
	
	public final long TIMEOUT_PERIOD = 300000;
	private long timeoutStartTime;
	
	public final long TIME_BETWEEN_TURN_CHECK = 10000;
	private long startTime;
	
	public static final int MAX_GET_MISSES = 5;
	private int misses = 0;
	
	private transient AsyncTask<PostWrapper, Void, String> currentNetworkThread;
	
	public InternetMap(GameState gamestate, AssetManager am, String p1Name, String p2Name, String filePath, GameMode gm, long gameID, boolean isPlayerOne) {
		super(gamestate, am, filePath, gm, false);
		
		this.gameID = gameID;
		this.isPlayerOne = isPlayerOne;
		
		player1 = new Player(p1Name, true, this, gm.getPlayer1Units());
		player2 = new Player(p2Name, false, this, gm.getPlayer2Units());
		thisPlayersTurn = player1;
		
		thisPlayersTurn.newTurn();
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
				checkForTimeout();
			}
			else if(player2 == thisPlayersTurn) {
				events = player1.notMyTurnUpdate(events);
				turnTimeCheck();
				checkForTimeout();
			}
			
			//check to see if someone has won yet
			checkWinConditions();
			
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
	
	private void checkForTimeout() {
		if(timeoutStartTime == 0)
			timeoutStartTime = System.currentTimeMillis();
		
		if((System.currentTimeMillis() - timeoutStartTime) > TIMEOUT_PERIOD)
			gamestate.timeout();
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
	protected void renderPlayerBanners(Graphics2D g) {
		//draw player gamer tag top, center of screen
		if((isPlayerOne && thisPlayersTurn == player1) || (!isPlayerOne && thisPlayersTurn != player1))
			g.drawBitmap(GameplayAssets.playerBanner, Game.G_WIDTH / 2 - GameplayAssets.playerBanner.getWidth() / 2, 0, null);
		else if((isPlayerOne && thisPlayersTurn != player1) || (!isPlayerOne && thisPlayersTurn == player1))
			g.drawBitmap(GameplayAssets.playerBannerEnemy, Game.G_WIDTH / 2 - GameplayAssets.playerBanner.getWidth() / 2, 0, null);
		
		g.drawText(thisPlayersTurn.getGamertag()+"'s turn", Game.G_WIDTH / 2, 26, gamertagFont);
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
		
		timeoutStartTime = 0;
	}
	
	@Override
	protected void checkWinConditions() {
		int victory = gamemode.checkWinConditions(this);
		
		//both teams wiped out, stalemate
		if(victory == GameMode.STALEMATE) {
			isGameOver = true;
		}
		//player 2 wins
		else if(victory == GameMode.PLAYER_2_WINS) {
			isGameOver = true;
			winningPlayer = player2;
		}
		//player 1 wins
		else if(victory == GameMode.PLAYER_1_WINS) {
			isGameOver = true;
			winningPlayer = player1;
		}
		
		if(isGameOver)
			sendTurnAsJSON();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if(currentNetworkThread != null)
			currentNetworkThread.cancel(true);
	}
	
	private void sendTurnAsJSON() {
		turnNumber++;
		//if(thisPlayersTurn == player1)
		//	isP1Turn = true;
		
		JSONObject turn = JSONHelper.turnToJSON(gameID, this, turnNumber);
		String response = Internet.postJSON(turnPostURL, turn);
		
		//no internet
		if(response.equals(""))
			gamestate.lostConnection();
		Log.i("combatgame", response);
	}
	
	private void getTurnAsJSON() {
		try {
			final JSONObject getTurn = new JSONObject();
			getTurn.put("game_id", gameID);
			
			if(currentNetworkThread != null)
				currentNetworkThread.cancel(true);
			
			gamestate.getStateManager().getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					currentNetworkThread = new PostAsync(new OnCompletion() {
						@Override
						public void onComplete(String response) {
							//no internet
							if(response.equals("")) {
								misses++;
								if(misses >= MAX_GET_MISSES)
									gamestate.lostConnection();
								return;
							}
							try {
								//we have internet, reset our timeout period
								misses = 0;
								JSONObject turn = new JSONObject(response);
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
							}catch(Exception e) {
								e.printStackTrace();
							}
						}
					}).execute(new PostWrapper(turnGetURL, getTurn));
				}
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
