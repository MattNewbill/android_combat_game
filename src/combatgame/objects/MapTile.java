package combatgame.objects;

/**
 * **HAPPY**
 */

/*
 * Individual tile of Map, contains properties to determine game flow
 */
public class MapTile {
	private MapFeature	feature;
	private boolean hasUnit;
	private int player_id;
	private int unit_id;
	
	public MapTile (int mapFeature_int ) {
		feature = new MapFeature (mapFeature_int);
		player_id = -1;
		unit_id = -1;
		hasUnit = false;
	}
	
	public void setUnit(int unit_id, int player_id) {
		hasUnit = true;
		this.unit_id = unit_id;
		this.player_id = player_id;
	}
	
	public void clearUnit() {
		hasUnit = false;
		this.unit_id = -1;
		this.player_id = -1;
	}
	
	public boolean hasUnit() {
		return hasUnit;
	}
	
	public int getPlayer_id() {
		return player_id;
	}
	
	public int getUnit_id() {
		return unit_id;
	}
	
	public int getFeatureType() {
		return feature.getFeatureType();
	}

}
