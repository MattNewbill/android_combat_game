package combatgame.units;

import java.io.Serializable;

import combatgame.graphics.GPoint;

public class AttackedTile implements Serializable {

	private static final long serialVersionUID = 1L;
	public GPoint tile;
	public int damageTaken;
	
	public AttackedTile(GPoint tile, int damageTaken) {
		this.tile = tile;
		this.damageTaken = damageTaken;
	}
	
}
