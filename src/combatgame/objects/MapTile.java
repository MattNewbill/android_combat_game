package combatgame.objects;

/*
 * Individual tile of Map, contains properties to determine game flow
 */
public class MapTile {
	private MapFeature	feature;
	private boolean hasUnit;
	private Unit unit;
	
	public MapTile (int mapFeature_int ) {
		feature = new MapFeature (mapFeature_int);
		unit = null;
		hasUnit = false;
	}
	
	public void setUnit(Unit U) {
		hasUnit = true;
		this.unit = U;
	}
	
	public void clearUnit() {
		hasUnit = false;
		this.unit = null;
	}
	
	public int getFeatureType() {
		return feature.getFeatureType();
	}

}
