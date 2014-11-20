package combatgame.units;

import combatgame.widgets.*;
import combatgame.graphics.*;
import combatgame.input.*;
import combatgame.input.LazyPool.LazyPoolObjectFactory;
import combatgame.input.Pool.PoolObjectFactory;
import combatgame.objects.Map;
import combatgame.objects.Unit;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Point;

public abstract class Ability {

	protected int abilityCost;
	protected int damage;
	protected List<GPoint> tilesAttackable = new ArrayList<GPoint>();
	protected List<AttackedTile> attackedTiles = new ArrayList<AttackedTile>();
	
	protected Button abilityButton;
	
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
	
	public Ability(){}
	
	public abstract List<GPoint> getTilesAttackable(Unit unit, Map map);
	public abstract List<AttackedTile> getTilesAffected(GPoint tile, Map map);
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
}
