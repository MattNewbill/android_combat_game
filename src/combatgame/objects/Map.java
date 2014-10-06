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
import android.graphics.Point;
import android.util.Log;

/**
 * **HAPPY**
 */

public class Map {
	
	//map data
	protected int tileWidthInPx = 96;
	protected int tileHeightInPx = 96;
	protected int num_horizontal_tiles;
	protected int num_vertical_tiles;
	protected MapTile[][] board;
	protected int mapId;
	
	//scrolling data
	TouchEvent previousEvent; //used to calculate which direction we are scrolling
	public static final int MAX_OUT_OF_BOUNDS = 100; //max distance you can scroll past the edge of the map
	public static final int SCROLL_DISTANCE = 10; //how far we scroll due to a single touch event
	public static final int FLING_DISTANCE = 70; //how far we scroll due to a single "fling" event
	public static final int FLING_THRESHOLD = 17; //the distance apart to scroll events have to be to determine a "fling"
	int mapOffsetX = 0, mapOffsetY = 0;
	
	
	public Map (AssetManager am, String filePath) {
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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the scroll offset for the map depending on the user input
	 * @param events List of TouchEvents to determine scroll distance
	 */
	public void update(List<TouchEvent> events) {
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
		for(int row = 0; row < num_vertical_tiles; row++) {
			for(int col = 0; col < num_horizontal_tiles; col++) {
				if(getFeature(row, col) == MapFeature.TERRAIN) {
					//normal terrain
					paint.setColor(Color.YELLOW);
				}
				else if(getFeature(row, col) == MapFeature.HEDGEHOG) {
					//rock
					paint.setColor(Color.DKGRAY);
				}
				else {// == MapFeature.TREE
					//tree
					paint.setColor(Color.GREEN);
				}
				g.drawRect((col * tileWidthInPx) - mapOffsetX, (row * tileHeightInPx) - mapOffsetY, (col * tileWidthInPx + tileWidthInPx) - mapOffsetX, (row * tileHeightInPx + tileHeightInPx) - mapOffsetY, paint);
			}
		}
	}
	
	/**
	 * Takes a TouchEvent and returns the map tile that the event touched
	 * @param event The event to be tested with
	 * @return null if the event is out of bounds and a Point with the coordinates of the tile if it's in bounds
	 */
	public Point getTile(TouchEvent event) {
		double x = (event.x + mapOffsetX) / (double)tileWidthInPx;
		double y = (event.y + mapOffsetY) / (double)tileHeightInPx;
		if(x < 0 || x > num_horizontal_tiles || y < 0 || y > num_vertical_tiles)
			return null;
		return new Point((int)x, (int)y);
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

	/***
	 * Returns true if map  was initialized correctly
	 * @param mapId
	 * @return
	 */
	public boolean initializeMap (int mapId) {
		//set map based off mapId
		
		return true;
	}

	public int getFeature(int row, int col) {
		return board[row][col].getFeatureType();
	}

}
