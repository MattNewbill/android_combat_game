package combatgame.network;

import combatgame.state.MapSelectionState;

import android.util.Log;

public class MapIDs {
	
	public static final int ALPHA_MAP = 0;
	public static final int DANGER_CLOSE = 1;
	public static final int FOREST = 2;
	public static final int HIDE_AND_SEEK = 3;
	public static final int LONG_WAY_AROUND = 4;
	public static final int NINJA_COVER = 5;
	public static final int NO_MANS_LAND = 6;
	public static final int SCATTERED = 7;
	
	public static String getMapNameFromID(int id) {
		switch(id) {
			case ALPHA_MAP:
				return "Alpha Map";
			case DANGER_CLOSE:
				return "Danger Close";
			case FOREST:
				return "Forest";
			case HIDE_AND_SEEK:
				return "Hide and Seek";
			case LONG_WAY_AROUND:
				return "Long Way Around";
			case NINJA_COVER:
				return "Ninja Cover";	
			case NO_MANS_LAND:
				return "No Man's Land";
			case SCATTERED:
				return "Scattered";
			default:
				return "Alpha Map";
		}
	}
	
	public static int getIDFromMapName(String name) {
		String temp = name;
		if(name.endsWith(".txt")) {
			temp = temp.substring(0, temp.length()-4);
		}
		if(temp.startsWith(MapSelectionState.mapPath)) {
			temp = temp.substring(MapSelectionState.mapPath.length()+1, temp.length());
		}
		
		Log.i("combatgame", temp);
		
		if(temp.equals("Alpha Map"))
			return ALPHA_MAP;
		else if(temp.equals("Danger Close"))
			return DANGER_CLOSE;
		else if(temp.equals("Forest"))
			return FOREST;
		else if(temp.equals("Hide and Seek"))
			return HIDE_AND_SEEK;
		else if(temp.equals("Long Way Around"))
			return LONG_WAY_AROUND;
		else if(temp.equals("Ninja Cover"))
			return NINJA_COVER;
		else if(temp.equals("No Man's Land"))
			return NO_MANS_LAND;
		else if(temp.equals("Scattered"))
			return SCATTERED;
		else
			return ALPHA_MAP;
	}
	
}
