package combatgame.objects;

import android.graphics.Point;
import android.graphics.Bitmap;

/**
 * **NOT HAPPY**
 * TODO: Need four sprites to represent each direction the unit can be facing,
 * currently just using one sprite.
 */

public abstract class Unit {
	protected int player_id;
	protected Point xyCoordinate;
	protected int shootingCost;
	protected int movementCost;
	protected int visionRadius;
	protected int damage;
	protected int health;
	protected boolean isDead;
	protected int directionFacing;
	protected Bitmap sprite; //TODO
	public static final int POINTS_PER_TURN = 5;
	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;
	
	public int getPlayer_id() {
		return this.player_id;	}
	
	public void setXYCoordinate(Point xyCoordinate) {
		this.xyCoordinate = xyCoordinate;	}
	
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
	
	public boolean isDead() {
		return this.isDead;	}
	
	public void takeDamage(int d) {
		this.health -= d;
		
		if(health<=0)
			this.isDead=true;
	}
	
	public void heal(int h) {
		this.health += h;
		
		if(health>100)
			this.health=100;
	}
	
	public int getDirectionFacing() {
		return directionFacing;
	}
	
	public void setDirectionFacing(int direction) {
		directionFacing = direction;
	}
	
	//TODO
	public Bitmap getSprite() {
		return this.sprite;
	}
	
}
