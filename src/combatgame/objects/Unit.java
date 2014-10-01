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
	protected boolean isDead;
	public static final int POINTS_PER_TURN = 5;
	
	public int getPlayer_id() {
		return this.player_id;	}
	
	public Point getXYCoordinate() {
		return this.xyCoordinate;	}
	
	public int getShootingCost() {
		return this.shootingCost;	}
	
	public int getMovementCost() {
		return this.movementCost;	}
	
	public int getVisionRadius() {
		return this.visionRadius;	}
	
	public int getDamage() {
		return this.damage;	}
	
	public int getHealth() {
		return this.health;	}
	
	public boolean getIsDead() {
		return this.isDead;	}
	
	public void takeDamage(int d) {
		this.health -= d;
		
		if(health<=0)
			this.isDead=true;
	}
	
}
