package combatgame.objects;

import android.graphics.Point;

public class Assault extends Unit {
	public Assault (int player_id, Point xyCoordinate) {
		this.player_id = player_id;
		this.xyCoordinate = xyCoordinate;
		this.shootingCost = 5;
		this.movementCost = 5;
		this.visionRadius = 5;
		this.damage = 9001;
		this.health = 100;
	}
	
}
