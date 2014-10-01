package combatgame.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.res.AssetManager;

/*
 * Contains a 2D array of MapTiles
 */
public class Map {
	protected int tileWidthInPx;
	protected int tileHeightInPx;
	int map_width;
	int map_height;
	MapTile[][] board;
	int mapId;
	
	public Map (AssetManager am, String filePath) {
		try {
			//read in map
			BufferedReader reader = new BufferedReader(new InputStreamReader((am.open("maps/test_map.txt"))));
			
			//first line contains dimensions for map
			String[] dimensions = reader.readLine().split(" ");
			map_width = Integer.parseInt(dimensions[0]);
			map_height = Integer.parseInt(dimensions[1]);
			
			//parse map into tiles
			board = new MapTile[map_height][map_width];
			for(int row = 0; row < map_height; row++) {
				String[] tiles = reader.readLine().split(" ");
				for(int col = 0; col < map_width; col++) {
					board[row][col] = new MapTile(Integer.parseInt(tiles[col]));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
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

}
