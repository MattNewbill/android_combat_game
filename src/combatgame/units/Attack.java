package combatgame.units;

import combatgame.graphics.GPoint;

public class Attack {

	public GPoint tile;
	public int damageTaken;
	
	public Attack(GPoint tile, int damageTaken) {
		this.tile = tile;
		this.damageTaken = damageTaken;
	}
	
}
