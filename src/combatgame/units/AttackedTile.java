package combatgame.units;

import combatgame.graphics.GPoint;

public class AttackedTile {

	public GPoint tile;
	public int damageTaken;
	
	public AttackedTile(GPoint tile, int damageTaken) {
		this.tile = tile;
		this.damageTaken = damageTaken;
	}
	
}
