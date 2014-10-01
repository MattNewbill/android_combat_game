package combatgame.objects;

public class MapFeature {
	private boolean isPassable;
	private boolean isSeethrough;
	private boolean isSlowing;
	private String featureType;
	
	public MapFeature (int mapFeature_int) {
		
		if(mapFeature_int == 0) {	//normal terrain
			isPassable=true;
			isSeethrough=true;
			isSlowing=false;
			featureType="normal terrain";
		}
		else if(mapFeature_int == 1) {	//1 is a hedgehog/rocks
			isPassable=false;
			isSeethrough=true;
			isSlowing=false;
			featureType="hedgehog/rocks";
		}
		else if(mapFeature_int == 2) {	//2 is a tree
			isPassable=true;
			isSeethrough=false;
			isSlowing=false;
			featureType="tree";
		}
		else if(mapFeature_int == 3) {	//3 is water/mud
			isPassable=true;
			isSeethrough=true;
			isSlowing=true;
			featureType="water/mud";
		}
		else {
			throw new IllegalArgumentException("Error: Map feature not defined.");
		}
	}
}
