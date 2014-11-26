package combatgame.objects;

import combatgame.graphics.*;
import combatgame.units.Ability;
import android.graphics.Bitmap;

public abstract class Unit {
	protected String name;
	protected int player_id = -1;
	protected int unit_id = -1;
	protected GPoint xyCoordinate;
	protected int movementCost;
	protected int visionRadius;
	protected int health;
	protected int armor;
	protected boolean isDead;
	protected int directionFacing;
	protected Bitmap[] sprites;
	protected int pointsLeft;
	protected int maxHealth;
	protected Ability[] abilities;
	protected int rotationCost;
	
	public static final int POINTS_PER_TURN = 9;
	
	public static final int FACING_UP = 0;
	public static final int FACING_RIGHT = 1;
	public static final int FACING_DOWN = 2;
	public static final int FACING_LEFT = 3;
	
	public String getName() {
		return name;	}
	
	public int getPlayer_id() {
		return this.player_id;	}
	
	public int getUnit_id() {
		return this.unit_id;	}
	
	public void setXYCoordinate(GPoint xyCoordinate, Map map) {
		if(this.xyCoordinate != null)
			map.getTile(this.xyCoordinate).clearUnit();
		this.xyCoordinate = xyCoordinate;
		map.getTile(xyCoordinate).setUnit(unit_id, player_id);;
	}
	
	public GPoint getXYCoordinate() {
		return this.xyCoordinate;	}
	
	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public int getMovementCost() {
		return this.movementCost;	}
	
	public int getVisionRadius() {
		return this.visionRadius;	}
	
	public int getArmor() {
		return armor;
	}
	
	public int getHealth() {
		return this.health;	}
	
	public boolean isDead() {
		return this.isDead;	}
	
	public DamageDealt takeDamage(int d, Map map) {
		int damageAbsorbedByArmor = 0;
		int damageToHealth = 0;
		boolean isAttack = false;
		if(d > 0) {//we are doing damage, so check our armor
			isAttack = true;
			if(armor > 0) {
				damageAbsorbedByArmor =  (int) (d * (armor / 100.0));
			}
			damageToHealth = d - damageAbsorbedByArmor;
		}
		else {
			damageToHealth = d;
		}
		this.health -= damageToHealth;
		this.armor -= damageAbsorbedByArmor;
		
		if(health<=0) {
			this.isDead=true;
			map.getTile(this.xyCoordinate).clearUnit();
		}
		if(health > maxHealth) {
			this.health = maxHealth;
		}
		return new DamageDealt(-damageToHealth, - damageAbsorbedByArmor, isAttack);
	}
	
	public void heal(int h) {
		this.health += h;
		
		if(health>maxHealth)
			this.health=maxHealth;
	}
	
	public int getDirectionFacing() {
		return directionFacing;
	}
	
	public void setDirectionFacing(int direction) {
		directionFacing = direction;
	}
	
	public Bitmap getSprite() {
		return this.sprites[directionFacing];
	}
	
	public void resetPoints() {
		pointsLeft = POINTS_PER_TURN;
	}
	
	public void usePoints( int num ) {
		pointsLeft -= num;
	}
	
	public int getPointsLeft() {
		return pointsLeft;
	}
	
	public Ability[] getAbilities() {
		return abilities;
	}
	
	public void rotateLeft() {
		directionFacing--;
		if(directionFacing < FACING_UP)
			directionFacing = FACING_LEFT;
	}
	
	public void rotateRight() {
		directionFacing++;
		if(directionFacing > FACING_LEFT)
			directionFacing = FACING_UP;
	}
	
	public int getRotationCost() {
		return rotationCost;
	}
}
