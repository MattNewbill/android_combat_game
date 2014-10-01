package combatgame.objects;

import android.graphics.Point;

public abstract class Unit {
	protected int player_id;
	protected Point xyCoordinate;
	protected int shootingCost;
	protected int movementCost;
	protected int visionRadius;
	protected int damage;
	protected int health;
	
}
