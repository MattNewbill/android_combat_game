package combatgame.state;

import combatgame.main.*;
import combatgame.graphics.*;
import combatgame.input.*;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.Log;
import android.content.res.AssetManager;

public class TestState extends State {

	TouchEvent previousEvent; //used to calculate which direction we are scrolling
	public static final int MAX_OUT_OF_BOUNDS = 100; //max distance you can scroll past the edge of the map
	public static final int SCROLL_DISTANCE = 5; //how far we scroll due to a single touch event
	int mapOffsetX = 0, mapOffsetY = 0;
	int mapWidth, mapHeight;
	int tileWidth = 96;
	int tileHeight = 96;
	int[][] map;
	
	Paint paint;
	
	public TestState(StateManager stateManager) {
		super(stateManager);
		this.stateManager.shouldScale(false);
		
		paint = new Paint();
		
		//load assets
		AssetManager am = this.stateManager.getAssetManager();
		try {
			//read in map
			BufferedReader reader = new BufferedReader(new InputStreamReader((am.open("maps/test_map.txt"))));
			
			//first line contains dimensions for map
			String[] dimensions = reader.readLine().split(" ");
			mapWidth = Integer.parseInt(dimensions[0]);
			mapHeight = Integer.parseInt(dimensions[1]);
			
			//parse map into tiles
			map = new int[mapHeight][mapWidth];
			for(int row = 0; row < mapHeight; row++) {
				String[] tiles = reader.readLine().split(" ");
				for(int col = 0; col < mapWidth; col++) {
					map[row][col] = Integer.parseInt(tiles[col]);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void update(float delta) {
		
		//time to explain exactly what the heck this is
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		for(int i = 0; i < events.size(); i++) {
			if(events.get(i).type == TouchEvent.TOUCH_DRAGGED) {
				//check for map scrolling
				if(previousEvent.type == TouchEvent.TOUCH_DRAGGED) {
					//if the x distance is greater than the y distance moved then we only change the x distance
					//and vice versa if y was greater than x.  This gives a smoother scroll than if we moved
					//both x and y at the same time.  Diagonal scrolling takes a hit because of this but I think
					//the hit is worth it
					if(Math.abs(events.get(i).x - previousEvent.x) > Math.abs(events.get(i).y - previousEvent.y)) {
						if(events.get(i).x < previousEvent.x) {
							mapOffsetX += SCROLL_DISTANCE;
						}
						else {
							mapOffsetX -= SCROLL_DISTANCE;
						}
					}
					else {
						if(events.get(i).y < previousEvent.y) {
							mapOffsetY += SCROLL_DISTANCE;
						}
						else {
							mapOffsetY -= SCROLL_DISTANCE;
						}
					}
				}
			}
			//set the recently checked touch event as the previous touch event in preparation for the next event
			previousEvent = events.get(i);
		}
		if(mapOffsetX < -MAX_OUT_OF_BOUNDS) {
			mapOffsetX = -MAX_OUT_OF_BOUNDS;
		}
		else if(mapOffsetX > ((mapWidth * tileWidth) + MAX_OUT_OF_BOUNDS) - Game.P_WIDTH) {
			mapOffsetX = ((mapWidth * tileWidth) + MAX_OUT_OF_BOUNDS) - Game.P_WIDTH;
		}
		if(mapOffsetY < -MAX_OUT_OF_BOUNDS) {
			mapOffsetY = -MAX_OUT_OF_BOUNDS;
		}
		else if(mapOffsetY > ((mapHeight * tileHeight) + MAX_OUT_OF_BOUNDS) - Game.P_HEIGHT) {
			mapOffsetY = ((mapHeight * tileHeight) + MAX_OUT_OF_BOUNDS) - Game.P_HEIGHT;
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		for(int row = 0; row < mapHeight; row++) {
			for(int col = 0; col < mapWidth; col++) {
				if(map[row][col] == 0) {
					//normal terrain
					paint.setColor(Color.YELLOW);
				}
				else if(map[row][col] == 1) {
					//rock
					paint.setColor(Color.DKGRAY);
				}
				else {
					//tree
					paint.setColor(Color.GREEN);
				}
				g.drawRect((col * tileWidth) - mapOffsetX, (row * tileHeight) - mapOffsetY, (col * tileWidth + tileWidth) - mapOffsetX, (row * tileHeight + tileHeight) - mapOffsetY, paint);
			}
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}
