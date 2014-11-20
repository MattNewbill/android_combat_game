package combatgame.objects;

import android.util.Log;

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
	private int spriteID;
	public static final int NO_UNIT = -1;
	public static final int MOD_SIZE = 10;
	
	public MapTile (int mapFeature_int ) {
		spriteID = mapFeature_int;
		feature = new MapFeature(Math.abs(mapFeature_int / MOD_SIZE));
		player_id = -1;
		unit_id = NO_UNIT;
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
	
	public MapFeature getFeature() {
		return feature;
	}
	
	public int getSpriteID() {
		return spriteID % MOD_SIZE;
	}
	

}
