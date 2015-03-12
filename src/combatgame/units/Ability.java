package combatgame.units;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import combatgame.graphics.GPoint;
import combatgame.graphics.Graphics2D;
import combatgame.input.LazyPool;
import combatgame.input.LazyPool.LazyPoolObjectFactory;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.widgets.Button;

public abstract class Ability implements Serializable {

	public static final double SIDE_DAMAGE_MODIFIER = .10;
	public static final double BACK_DAMAGE_MODIFIER = .20;
	
	public static enum AttackingDirection {
		SIDE,
		BACK,
		FRONT
	}
	
	private static final long serialVersionUID = 1L;
	protected int abilityCost;
	protected int damage;
	protected String type = null;
	

	protected List<GPoint> tilesAttackable = new ArrayList<GPoint>();
	protected List<AttackedTile> attackedTiles = new ArrayList<AttackedTile>();
	
	protected transient Button abilityButton;
	
	
	protected static LazyPool<AttackedTile> pool;
	public static final int MAX_POOL_SIZE = 50;
	
	static {
		LazyPoolObjectFactory<AttackedTile> factory = new LazyPoolObjectFactory<AttackedTile>() {
			@Override
			public AttackedTile createObject() {
				return new AttackedTile(null, -1);
			}
		};
		pool = new LazyPool<AttackedTile>(factory, MAX_POOL_SIZE);
	}
	
	public Ability() {}
	
	public abstract void loadButton();
	public abstract List<GPoint> getTilesAttackable(Unit unit, Map map);
	public abstract List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, GPoint tileTouched, Map map);
	public abstract boolean isValidTileToAttack(Unit unitWhoIsAttacking, GPoint tileToCheck, Map map);
	public void renderButton(Graphics2D g, int x, int y) {
		abilityButton.render(g, x, y);
	}
	
	public int getCost() {
		return abilityCost;
	}
	
	public Button getButton() {
		return abilityButton;
	}
	
	public int getDamage() {
		return damage;
	}
	public String getType() {
		return type;
	}
	
	protected AttackingDirection getAttackedDirection(Unit attacker, Unit defender) {
		int defenderDir = defender.getDirectionFacing();
		switch(attacker.getDirectionFacing()) {
			case Unit.FACING_UP:
				if(defenderDir == Unit.FACING_LEFT || defenderDir == Unit.FACING_RIGHT)
					return AttackingDirection.SIDE;
				else if(defenderDir == Unit.FACING_UP)
					return AttackingDirection.BACK;
				break;
			case Unit.FACING_DOWN:
				if(defenderDir == Unit.FACING_LEFT || defenderDir == Unit.FACING_RIGHT)
					return AttackingDirection.SIDE;
				else if(defenderDir == Unit.FACING_DOWN)
					return AttackingDirection.BACK;
				break;
			case Unit.FACING_RIGHT:
				if(defenderDir == Unit.FACING_UP || defenderDir == Unit.FACING_DOWN)
					return AttackingDirection.SIDE;
				else if(defenderDir == Unit.FACING_RIGHT)
					return AttackingDirection.BACK;
				break;
			case Unit.FACING_LEFT:
				if(defenderDir == Unit.FACING_UP || defenderDir == Unit.FACING_DOWN)
					return AttackingDirection.SIDE;
				else if(defenderDir == Unit.FACING_LEFT)
					return AttackingDirection.BACK;
				break;
			default:
				throw new IllegalArgumentException("Invalid direction facing");
		}
		return AttackingDirection.FRONT;
	}
}
