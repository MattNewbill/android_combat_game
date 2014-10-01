package combatgame.objects;

public class MapFeature {
	private boolean isPassable;
	private boolean isSeethrough;
	private boolean isSlowing;
	private int featureType;

	public final static int TERRAIN = 0;
	public final static int HEDGEHOG = 1;
	public final static int TREE = 2;
	public final static int WATER = 3;

	public MapFeature(int mapFeature_int) {

		if (mapFeature_int == 0) { // normal terrain
			isPassable = true;
			isSeethrough = true;
			isSlowing = false;
			featureType = TERRAIN;
		} else if (mapFeature_int == HEDGEHOG) { // 1 is a hedgehog
			isPassable = false;
			isSeethrough = true;
			isSlowing = false;
			featureType = HEDGEHOG;
		} else if (mapFeature_int == TREE) { // 2 is a tree
			isPassable = true;
			isSeethrough = false;
			isSlowing = false;
			featureType = TREE;
		} else if (mapFeature_int == 3) { // 3 is water/mud
			isPassable = true;
			isSeethrough = true;
			isSlowing = true;
			featureType = WATER;
		} else {
			throw new IllegalArgumentException(
					"Error: Map feature not defined.");
		}
	}

	public int getFeatureType() {
		return featureType;
	}
	
}
