package combatgame.objects;

import android.graphics.Point;

public class Assault extends Unit {
	public Assault (int player_id, Point xyCoordinate) {
		this.player_id = player_id;
		this.xyCoordinate = xyCoordinate;
		this.shootingCost = 2;
		this.movementCost = 1;
		this.visionRadius = 4;
		this.damage = 25;
		this.health = 100;
		isDead=false;
	}
	
}
