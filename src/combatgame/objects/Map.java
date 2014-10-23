package combatgame.objects;

import combatgame.graphics.*;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * **NOT HAPPY**
 * TODO: implement scrollToTile() method
 */

public class Map {
	
	//players
	Player player1, player2;
	Player thisPlayersTurn;
	
	//map data
	protected int tileWidthInPx = 96;
	protected int tileHeightInPx = 96;
	protected int num_horizontal_tiles;
	protected int num_vertical_tiles;
	protected MapTile[][] board;
	protected int mapId;
	
	//lightmap
	protected boolean[][] lightmap;
	
	//scrolling data
	TouchEvent previousEvent; //used to calculate which direction we are scrolling
	public static final int MAX_OUT_OF_BOUNDS = 125; //max distance you can scroll past the edge of the map
	public static final int SCROLL_DISTANCE = 10; //how far we scroll due to a single touch event
	public static final int FLING_DISTANCE = 70; //how far we scroll due to a single "fling" event
	public static final int FLING_THRESHOLD = 17; //the distance apart to scroll events have to be to determine a "fling"
	int mapOffsetX = 0, mapOffsetY = 0;
	
	//auto scrolling data
	protected boolean isAutoScrolling = false;
	protected GPoint tileToScrollTo;
	public static final int LEEWAY = 30;
	public static final int FAST_SCROLL_DISTANCE = 20;
	
	//////////////////////////////////
	//--Temp color values for rendering tilemap
	//////////////////////////////////
	private int shadedYELLOW = Color.parseColor("#646400");
	private int shadedDKGRAY = Color.parseColor("#000000");
	private int shadedGREEN = Color.parseColor("#006400");
	private int shadedRED = Color.parseColor("#640000");
	private int shadedMAGENTA = Color.parseColor("#640064");
	
	public Map (AssetManager am, String filePath) {
		//create players
		player1 = new Player(true, this, 3);
		player2 = new Player(false, this, 3);
		thisPlayersTurn = player1;
		
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
		thisPlayersTurn.newTurn();
	}
	
	public void update(List<TouchEvent> events) {
		//update current player's turn
		thisPlayersTurn.update(events);
		
		//update map scroll
		updateMap(events);
		
		//get new lightmap
		clearLightMap();
		lightmap = thisPlayersTurn.constructLightMap(lightmap);
	}
	
	private void clearLightMap() {
		for(int row= 0; row < num_vertical_tiles; row++) {
			for(int col = 0; col < num_horizontal_tiles; col++) {
				lightmap[row][col] = false;
			}
		}
	}
	
	/**
	 * Updates the scroll offset for the map depending on the user input
	 * @param events List of TouchEvents to determine scroll distance
	 */
	private void updateMap(List<TouchEvent> events) {
		//update scroll data
		for(int i = 0; i < events.size(); i++) {
			//stop auto scrolling if the user presses down
			if(events.get(i).type == TouchEvent.TOUCH_DOWN)
				isAutoScrolling = false;
			
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
		
		//update our auto scrolling
		if(isAutoScrolling) {
			if(mapOffsetX < (tileToScrollTo.col * tileWidthInPx) - (Game.P_WIDTH / 2) - LEEWAY)
				mapOffsetX += FAST_SCROLL_DISTANCE;
			else if(mapOffsetX > (tileToScrollTo.col * tileWidthInPx) - (Game.P_WIDTH / 2) + LEEWAY) {
				mapOffsetX -= FAST_SCROLL_DISTANCE;
			}
			
			if(mapOffsetY < (tileToScrollTo.row * tileHeightInPx) - (Game.P_HEIGHT / 2) - LEEWAY)
				mapOffsetY += FAST_SCROLL_DISTANCE;
			else if(mapOffsetY > (tileToScrollTo.row * tileHeightInPx) - (Game.P_HEIGHT / 2) + LEEWAY)
				mapOffsetY -= FAST_SCROLL_DISTANCE;
			
		}
		
		//make sure we don't scroll too far past the edge of the map
		if(mapOffsetX < -MAX_OUT_OF_BOUNDS) {
			mapOffsetX = -MAX_OUT_OF_BOUNDS;
		}
		else if(mapOffsetX > ((getNum_horizontal_tiles() * getTileWidthInPx()) + MAX_OUT_OF_BOUNDS) - Game.P_WIDTH) {
			mapOffsetX = ((getNum_horizontal_tiles() * getTileWidthInPx()) + MAX_OUT_OF_BOUNDS) - Game.P_WIDTH;
		}
		if(mapOffsetY < -MAX_OUT_OF_BOUNDS) {
			mapOffsetY = -MAX_OUT_OF_BOUNDS;
		}
		else if(mapOffsetY > ((getNum_vertical_tiles() * getTileHeightInPx()) + MAX_OUT_OF_BOUNDS) - Game.P_HEIGHT) {
			mapOffsetY = ((getNum_vertical_tiles() * getTileHeightInPx()) + MAX_OUT_OF_BOUNDS) - Game.P_HEIGHT;
		}
	}
	
	/**
	 * Renders the map based on the current scroll offsets
	 * @param g Graphics object to render to
	 * @param paint Paint object to use for colors
	 */
	public void render(Graphics2D g, Paint paint) {
		
		//render map
		for(int row = 0; row < num_vertical_tiles; row++) {
			for(int col = 0; col < num_horizontal_tiles; col++) {
				//if we can't see the tile, make it black (eventually we'll replace this with our shaded sprite of the particular tile, but this works for now)
				if(!lightmap[row][col]) {
					if(getFeatureType(row, col) == MapFeature.TERRAIN) {
						paint.setColor(shadedYELLOW);
					}
					else if(getFeatureType(row, col) == MapFeature.HEDGEHOG) {
						paint.setColor(shadedDKGRAY);
					}
					else if(getFeatureType(row, col) == MapFeature.TREE) {
						paint.setColor(shadedGREEN);
					}
					else if(getFeatureType(row, col) == MapFeature.PLAYER_ONE_BASE) {
						paint.setColor(shadedMAGENTA);
					}
					else if(getFeatureType(row, col) == MapFeature.PLAYER_TWO_BASE) {
						paint.setColor(shadedRED);
					}
				}
				else if(getFeatureType(row, col) == MapFeature.TERRAIN) {
					paint.setColor(Color.YELLOW);
				}
				else if(getFeatureType(row, col) == MapFeature.HEDGEHOG) {
					paint.setColor(Color.DKGRAY);
				}
				else if(getFeatureType(row, col) == MapFeature.TREE) {
					paint.setColor(Color.GREEN);
				}
				else if(getFeatureType(row, col) == MapFeature.PLAYER_ONE_BASE) {
					paint.setColor(Color.MAGENTA);
				}
				else if(getFeatureType(row, col) == MapFeature.PLAYER_TWO_BASE) {
					paint.setColor(Color.RED);
				}
				g.drawRect((col * tileWidthInPx) - mapOffsetX, (row * tileHeightInPx) - mapOffsetY, (col * tileWidthInPx + tileWidthInPx) - mapOffsetX, (row * tileHeightInPx + tileHeightInPx) - mapOffsetY, paint);
			}
		}
		
		//render current player
		thisPlayersTurn.render(g);
		
		//draw player id text top, center of screen
		g.drawText(""+thisPlayersTurn.getId(), Game.P_WIDTH / 2, 20, new Paint());
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
	
	//TODO scroll the map to the selected tile
	public void scrollToTile(GPoint tile) {
		tileToScrollTo = tile;
		isAutoScrolling = true;
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

}
