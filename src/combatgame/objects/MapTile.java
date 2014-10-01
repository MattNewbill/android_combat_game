package combatgame.objects;

/*
 * Individual tile of Map, contains properties to determine game flow
 */
public class MapTile {
	private MapFeature	spaceType;
	private boolean hasUnit;
	private Unit unit;
	
	public MapTile (int mapFeature_int ) {
		spaceType = new MapFeature (mapFeature_int);
		unit = null;
		hasUnit = false;
	}

}
