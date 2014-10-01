package combatgame.objects;

import android.graphics.Point;

public class Sniper extends Unit {
	public Sniper (int player_id, Point xyCoordinate) {
		this.player_id = player_id;
		this.xyCoordinate = xyCoordinate;
		this.shootingCost = 3;
		this.movementCost = 1;
		this.visionRadius = 5;
		this.damage = 50;
		this.health = 100;
		isDead=false;
	}
	
}
