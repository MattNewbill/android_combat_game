package combatgame.objects;

/**
 * **HAPPY**
 */

public class MapFeature {
	private boolean isPassable;
	private boolean isSeethrough;
	private boolean isSlowing;
	private int featureType;

	public final static int TERRAIN = 0;
	public final static int BARRIER = 1;
	public final static int COVER = 2;
	public final static int WATER = 3;
	public final static int WALL = 4;
	public final static int PLAYER_ONE_BASE = 5;
	public final static int PLAYER_TWO_BASE = 6;

	public MapFeature(int mapFeature_int) {
		switch(mapFeature_int) {
			case TERRAIN:
				isPassable = true;
				isSeethrough = true;
				isSlowing = false;
				featureType = TERRAIN;
				break;
			case BARRIER:
				isPassable = false;
				isSeethrough = true;
				isSlowing = false;
				featureType = BARRIER;
				break;
			case COVER:
				isPassable = true;
				isSeethrough = false;
				isSlowing = false;
				featureType = COVER;
				break;
			case WATER:
				isPassable = true;
				isSeethrough = true;
				isSlowing = true;
				featureType = WATER;
				break;
			case WALL:
				isPassable = false;
				isSeethrough = false;
				isSlowing = true;
				featureType = WALL;
				break;
			case PLAYER_ONE_BASE:
				isPassable = true;
				isSeethrough = true;
				isSlowing = false;
				featureType = PLAYER_ONE_BASE;
				break;
			case PLAYER_TWO_BASE:
				isPassable = true;
				isSeethrough = true;
				isSlowing = false;
				featureType = PLAYER_TWO_BASE;
				break;
			default:
				throw new IllegalArgumentException("Error: Map feature not defined.");
		}
	}

	public int getFeatureType() {
		return featureType;
	}
	
	public boolean isPassable()
	{
		return isPassable;
	}
	
	public boolean isSeethrough()
	{
		return isSeethrough;
	}
	
	public boolean isSlowing()
	{
		return isSlowing;
	}
}
