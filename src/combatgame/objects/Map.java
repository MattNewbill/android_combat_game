package combatgame.objects;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.*;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.state.GameState;

import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.content.res.AssetManager;
import android.graphics.PorterDuffColorFilter;
import android.graphics.ColorFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

/**
 * **NOT HAPPY**
 * TODO: get a better rendering system down for the fog of war overlays...
 */

public class Map {
	
	//gamestate that created us
	GameState gamestate;
	
	//players
	Player player1, player2;
	Player thisPlayersTurn;
	
	//gamertag font
	Paint gamertagFont;
	
	//game over
	protected boolean isGameOver = false;
	protected Player winningPlayer = null;
	protected String gameoverString = null;
	protected Paint gameoverFont;
	protected Paint fadePaint;
	protected int fadePaintAlpha = 0;
	protected final int TIME_TILL_FADE = 5000;
	protected final int ALPHA_STEP = 3;
	protected long startTime = 0;
	protected boolean isWaitingToFade = true;
	protected boolean isFading = false;
	
	//map data
	protected int tileWidthInPx = 96;
	protected int tileHeightInPx = 96;
	protected int num_horizontal_tiles;
	protected int num_vertical_tiles;
	protected MapTile[][] board;
	protected int mapId;
	
	//are we just now switching turns
	protected boolean isSwitchingTurns = false;
	
	//lightmap
	protected boolean[][] lightmap;
	
	//scrolling data
	TouchEvent previousEvent; //used to calculate which direction we are scrolling
	public static final int MAX_OUT_OF_BOUNDS = 125; //max distance you can scroll past the edge of the map
	public static final int SCROLL_DISTANCE = 10;
	public static final int FLING_DISTANCE = 70; //how far we scroll due to a single "fling" event
	public static final int FLING_THRESHOLD = 17; //the distance apart to scroll events have to be to determine a "fling"
	int mapOffsetX = 0, mapOffsetY = 0;
	
	//shaded portions of the map that we can't see
	private int fogOfWarColor = Color.parseColor("#E64A3F3F");
	
	public Map (GameState gamestate, AssetManager am, String filePath) {
		this.gamestate = gamestate;
		
		//create players
		player1 = new Player("Player 1", true, this, 5);
		player2 = new Player("Player 2", false, this, 5);
		thisPlayersTurn = player1;
		
		gamertagFont = new Paint();
		gamertagFont.setColor(Color.BLACK);
		gamertagFont.setTextSize(20);
		gamertagFont.setTextAlign(Align.CENTER);
		
		gameoverFont = new Paint();
		gameoverFont.setColor(Color.WHITE);
		gameoverFont.setTextSize(60); //TODO: scale text for larger devices
		gameoverFont.setTextAlign(Align.CENTER);
		
		fadePaint = new Paint();
		fadePaint.setColor(Color.BLACK);
		fadePaint.setAlpha(0);
		try {
			//read in map
			BufferedReader reader = new BufferedReader(new InputStreamReader((am.open("maps/test_map.txt"))));
			
			//first line contains dimensions for map
			String[] dimensions = reader.readLine().split(" ");
			num_horizontal_tiles = Integer.parseInt(dimensions[0]);
			num_vertical_tiles = Integer.parseInt(dimensions[1]);
			
			//parse map into tiles
			board = new MapTile[num_vertical_tiles][num_horizontal_tiles];
			for(int row = 0; row < num_vertical_tiles; row++) {
				String[] tiles = reader.readLine().split(" ");
				for(int col = 0; col < num_horizontal_tiles; col++) {
					board[row][col] = new MapTile(Integer.parseInt(tiles[col]));
				}
			}
			
			//create lightmap object
			lightmap = new boolean[num_vertical_tiles][num_horizontal_tiles];
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		thisPlayersTurn.newTurn();
	}
	
	public void switchTurn() {
		if(thisPlayersTurn == player1)
			thisPlayersTurn = player2;
		else
			thisPlayersTurn = player1;
		
		isSwitchingTurns = true;
		thisPlayersTurn.newTurn();
	}
	
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
			thisPlayersTurn.update(events);
			
			//check to see if someone has won yet
			checkWinConditions();
			
			//update map scroll
			updateMap(events);
			
			//get new lightmap
			clearLightMap();
			lightmap = thisPlayersTurn.constructLightMap(lightmap);
		}
	}
	
	private void clearLightMap() {
		for(int row= 0; row < num_vertical_tiles; row++) {
			for(int col = 0; col < num_horizontal_tiles; col++) {
				lightmap[row][col] = false;
			}
		}
	}
	
	private void checkWinConditions() {
		//TODO: if we have different gamemodes then the victory conditions will be dependent on the gamemode
		//just using standard deathmatch victory condition
		Unit[] player1Units = player1.getUnits();
		Unit[] player2Units = player2.getUnits();
		boolean isPlayer1Destroyed = true;
		boolean isPlayer2Destroyed = true;
		for(int i = 0; i < player1Units.length; i++) {
			if(!player1Units[i].isDead())
				isPlayer1Destroyed = false;
		}
		for(int i = 0; i < player2Units.length; i++) {
			if(!player2Units[i].isDead())
				isPlayer2Destroyed = false;
		}
		//both teams wiped out, stalemate
		if(isPlayer1Destroyed && isPlayer2Destroyed) {
			isGameOver = true;
		}
		//player 2 wins
		else if(isPlayer1Destroyed && !isPlayer2Destroyed) {
			isGameOver = true;
			winningPlayer = player2;
		}
		//player 1 wins
		else if(!isPlayer1Destroyed && isPlayer2Destroyed) {
			isGameOver = true;
			winningPlayer = player1;
		}
	}
	
	/**
	 * Updates the scroll offset for the map depending on the user input
	 * @param events List of TouchEvents to determine scroll distance
	 */
	private void updateMap(List<TouchEvent> events) {
		//update scroll data
		for(int i = 0; i < events.size(); i++) {
			if(events.get(i).type == TouchEvent.TOUCH_DRAGGED) {
				//check for map scrolling
				if(previousEvent.type == TouchEvent.TOUCH_DRAGGED) {
					//scroll on the x axis
					if(Math.abs(events.get(i).x - previousEvent.x) > Math.abs(events.get(i).y - previousEvent.y)) {
						//fling scrolling, x axis
						if(Math.abs(events.get(i).x - previousEvent.x) > FLING_THRESHOLD) {
							if(events.get(i).x < previousEvent.x)
								mapOffsetX += FLING_DISTANCE;
							else
								mapOffsetX -= FLING_DISTANCE;
						}
						//regular scrolling, x axis
						else {
							if(events.get(i).x < previousEvent.x)
								mapOffsetX += SCROLL_DISTANCE;
							else
								mapOffsetX -= SCROLL_DISTANCE;
						}
					}
					//scroll on the y axis
					else {
						//fling scrolling, y axis
						if(Math.abs(events.get(i).y - previousEvent.y) > FLING_THRESHOLD) {
							if(events.get(i).y < previousEvent.y)
								mapOffsetY += FLING_DISTANCE;
							else
								mapOffsetY -= FLING_DISTANCE;
						}
						//regular scrolling, y axis
						else {
							if(events.get(i).y < previousEvent.y)
								mapOffsetY += SCROLL_DISTANCE;
							else
								mapOffsetY -= SCROLL_DISTANCE;
						}
					}
				}
			}
			//set the recently checked touch event as the previous touch event in preparation for the next event
			if(previousEvent == null) {
				previousEvent = new TouchEvent();
				previousEvent.copy(events.get(0));
			}
			previousEvent.copy(events.get(i));
		}
		
		//make sure we don't scroll too far past the edge of the map
		checkOffsetBounds();
	}
	
	/**
	 * Renders the map based on the current scroll offsets
	 * @param g Graphics object to render to
	 * @param paint Paint object to use for colors
	 */
	public void render(Graphics2D g, Paint paint) {
		//set our fog of war color to overlay the tiles we can't see
		paint.setColor(fogOfWarColor);
		
		//render map
		for(int row = 0; row < num_vertical_tiles; row++) {
			for(int col = 0; col < num_horizontal_tiles; col++) {
				//check our map to see which tile we need to draw
				Bitmap tile = null;
				Bitmap shadedTile = null;
				if(getFeatureType(row, col) == MapFeature.TERRAIN) {
					tile = GameplayAssets.dirtSprite;
					shadedTile = GameplayAssets.dirtSpriteShaded;
				}
				else if(getFeatureType(row, col) == MapFeature.HEDGEHOG) {
					tile = GameplayAssets.hedgehogSprite;
					shadedTile = GameplayAssets.hedgehogSpriteShaded;
				}
				else if(getFeatureType(row, col) == MapFeature.TREE) {
					tile = GameplayAssets.bushSprite;
					shadedTile = GameplayAssets.bushSpriteShaded;
				}
				else if(getFeatureType(row, col) == MapFeature.PLAYER_ONE_BASE) {
					tile = GameplayAssets.player1BaseSprite;
					shadedTile = GameplayAssets.player1BaseSpriteShaded;
				}
				else if(getFeatureType(row, col) == MapFeature.PLAYER_TWO_BASE) {
					tile = GameplayAssets.player2BaseSprite;
					shadedTile = GameplayAssets.player2BaseSpriteShaded;
				}
				
				//if the tile is going to be within our screen's dimensions, whether it's from scaling or not, we need to draw it
				if(isTileOnScreen(row, col)) {					
					//if we can't see the tile then draw the fog of war on top of the tile
					if(!lightmap[row][col]) {
						g.drawBitmap(shadedTile, (col * tileWidthInPx) - mapOffsetX, (row * tileHeightInPx) - mapOffsetY, null);
						//g.drawRect((col * tileWidthInPx) - mapOffsetX, (row * tileHeightInPx) - mapOffsetY, (col * tileWidthInPx + tileWidthInPx) - mapOffsetX, (row * tileHeightInPx + tileHeightInPx) - mapOffsetY, paint);
					}
					else {
						g.drawBitmap(tile, (col * tileWidthInPx) - mapOffsetX, (row * tileHeightInPx) - mapOffsetY, null);
					}
				}
			}
		}
		
		//render "enemy" units if they are visible
		if(thisPlayersTurn == player1) //if it's player1's turn then the "enemy" is player2
			player2.renderVisibleUnits(g, lightmap);
		else //vice versa
			player1.renderVisibleUnits(g, lightmap);
		
		//render current player
		thisPlayersTurn.render(g);
		
		//draw player gamer tag top, center of screen
		if(Game.isScaled()) { //TODO: scale player font for devices
			g.drawBitmap(GameplayAssets.playerBanner, Game.G_WIDTH / 2 - GameplayAssets.playerBanner.getWidth() / 2, 0, null);
			g.drawText(thisPlayersTurn.getGamertag()+"'s turn", Game.G_WIDTH / 2, 20, gamertagFont);
		}
		else {
			g.drawBitmap(GameplayAssets.playerBanner, Game.P_WIDTH / 2 - GameplayAssets.playerBanner.getWidth() / 2, 0, null);
			g.drawText(thisPlayersTurn.getGamertag()+"'s turn", Game.P_WIDTH / 2, 20, gamertagFont);
		}
		
		//draw the game over text
		if(isGameOver) {
			if(gameoverString == null) {
				if(winningPlayer == null)
					gameoverString = "Stalemate";
				else
					gameoverString = winningPlayer.getGamertag() + " wins!";
			}
			if(Game.isScaled()) {
				g.drawText(gameoverString, Game.G_WIDTH / 2, Game.G_HEIGHT / 2, gameoverFont);
				g.drawRect(0, 0, Game.G_WIDTH, Game.G_HEIGHT, fadePaint);
			}
			else {
				g.drawText(gameoverString, Game.P_WIDTH / 2, Game.P_HEIGHT / 2, gameoverFont);
				g.drawRect(0, 0, Game.P_WIDTH, Game.P_HEIGHT, fadePaint);
			}
		}
	}
	
	public boolean isTileOnScreen(int row, int col) {
		if(Game.isScaled()) {
			return (col * tileWidthInPx) - mapOffsetX >=  -tileWidthInPx &&
			       (col * tileWidthInPx) - mapOffsetX <= Game.G_WIDTH + tileWidthInPx &&
			       (row * tileHeightInPx) - mapOffsetY >= -tileHeightInPx &&
			       (row * tileHeightInPx) - mapOffsetY <= Game.G_WIDTH + tileHeightInPx ? true : false;
		}
		else {
			return (col * tileWidthInPx) - mapOffsetX >= -tileWidthInPx &&
				       (col * tileWidthInPx) - mapOffsetX <= Game.P_WIDTH + tileWidthInPx &&
				       (row * tileHeightInPx) - mapOffsetY >= -tileHeightInPx &&
				       (row * tileHeightInPx) - mapOffsetY <= Game.P_WIDTH + tileHeightInPx ? true : false;
		}
	}
	
	/**
	 * Takes a TouchEvent and returns the map tile that the event touched
	 * @param event The event to be tested with
	 * @return null if the event is out of bounds and a Point with the coordinates of the tile if it's in bounds
	 */
	public GPoint getTileTouched(TouchEvent event) {
		double x = (event.x + mapOffsetX) / (double)tileWidthInPx;
		double y = (event.y + mapOffsetY) / (double)tileHeightInPx;
		if(x < 0 || x > num_horizontal_tiles || y < 0 || y > num_vertical_tiles)
			return null;
		return new GPoint((int)y, (int)x);
	}
	
	public void dispose() {
		if(player1 != null)
			player1.dispose();
		if(player2 != null)
			player2.dispose();
	}
	
	public MapTile getTile(GPoint tile) {
		if(tile.col < 0 || tile.row < 0 ||
		   tile.col >= num_horizontal_tiles || tile.row >= num_vertical_tiles)
			return null;
		return board[tile.row][tile.col];
	}
	
	public void moveToTile(GPoint tile) { //TODO:  check whether this needs to be G_WIDTH or P_WIDTH depending on whether things are scaled or not
		mapOffsetX = (tile.col * tileWidthInPx) - (Game.P_WIDTH / 2);
		mapOffsetY = (tile.row * tileHeightInPx) - (Game.P_HEIGHT / 2);
		
		checkOffsetBounds();
	}
	
	private void checkOffsetBounds() { //TODO: see if we need to change this to P_WIDTH depending on whether things are scaled or not
		if(mapOffsetX < -MAX_OUT_OF_BOUNDS) {
			mapOffsetX = -MAX_OUT_OF_BOUNDS;
		}
		else if(mapOffsetX > ((getNum_horizontal_tiles() * getTileWidthInPx()) + MAX_OUT_OF_BOUNDS) - Game.G_WIDTH) {
			mapOffsetX = ((getNum_horizontal_tiles() * getTileWidthInPx()) + MAX_OUT_OF_BOUNDS) - Game.G_WIDTH;
		}
		if(mapOffsetY < -MAX_OUT_OF_BOUNDS) {
			mapOffsetY = -MAX_OUT_OF_BOUNDS;
		}
		else if(mapOffsetY > ((getNum_vertical_tiles() * getTileHeightInPx()) + MAX_OUT_OF_BOUNDS) - Game.G_HEIGHT) {
			mapOffsetY = ((getNum_vertical_tiles() * getTileHeightInPx()) + MAX_OUT_OF_BOUNDS) - Game.G_HEIGHT;
		}
	}
	
	public int getTileWidthInPx() {
		return tileWidthInPx;
	}

	public void setTileWidthInPx(int tileWidthInPx) {
		this.tileWidthInPx = tileWidthInPx;
	}

	public int getTileHeightInPx() {
		return tileHeightInPx;
	}

	public void setTileHeightInPx(int tileHeightInPx) {
		this.tileHeightInPx = tileHeightInPx;
	}

	public int getNum_horizontal_tiles() {
		return num_horizontal_tiles;
	}

	public void setNum_horizontal_tiles(int num_horizontal_tiles) {
		this.num_horizontal_tiles = num_horizontal_tiles;
	}

	public int getNum_vertical_tiles() {
		return num_vertical_tiles;
	}

	public void setNum_vertical_tiles(int num_vertical_tiles) {
		this.num_vertical_tiles = num_vertical_tiles;
	}
	
	public int getMapOffsetX() {
		return mapOffsetX;
	}
	
	public int getMapOffsetY() {
		return mapOffsetY;
	}

	public int getFeatureType(int row, int col) {
		return board[row][col].getFeatureType();
		}
	
	public MapFeature getFeature(int row, int col) {
		return board[row][col].getFeature();
	}
	
	public MapTile getTile(int row, int col) {
		return board[row][col];
	}
	
	public boolean isSwitchingTurns() {
		return isSwitchingTurns;
	}
	
	public void doneSwitchingTurns() {
		isSwitchingTurns = false;
	}
	
	public Player getCurrentPlayersTurn() {
		return thisPlayersTurn;
	}
	
	public void sendHitIndicator(HitIndicator indicator, Unit unit) {
		Player player = getPlayerByID(unit.getPlayer_id());
		if(player == null) //invalid player, oops
			return;
		
		player.addHitIndicator(indicator);
	}
	
	public Player getPlayerByID(int playerID) {
		int player1ID = player1.getId();
		int player2ID = player2.getId();
		
		if(playerID == player1ID)
			return player1;
		else if(playerID == player2ID)
			return player2;
		else
			return null;
	}
	
	public Unit getUnit(int unitId) {
		Unit p1Unit = player1.getUnit(unitId);
		Unit p2Unit = player2.getUnit(unitId);
		if(p1Unit != null)
			return p1Unit;
		else if(p2Unit != null)
			return p2Unit;
		else
			return null;
	}

}
