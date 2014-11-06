package combatgame.units;

import combatgame.widgets.*;
import combatgame.graphics.*;
import combatgame.input.*;
import combatgame.input.Pool.PoolObjectFactory;
import combatgame.objects.Map;
import combatgame.objects.Unit;

import java.util.List;
import android.graphics.Point;

public abstract class Ability {

	protected int abilityCost;
	protected int damage;
	protected String name;
	
	protected Button abilityButton;
	
	protected static Pool<AttackedTile> pool;
	public static final int MAX_POOL_SIZE = 25;
	
	static {
		PoolObjectFactory<AttackedTile> factory = new PoolObjectFactory<AttackedTile>() {
			@Override
			public AttackedTile createObject() {
				return new AttackedTile(null, -1);
			}
		};
		pool = new Pool<AttackedTile>(factory, MAX_POOL_SIZE);
	}

	
	public Ability(int abilityCost) {
		this.abilityCost = abilityCost;
	}
	
	public abstract List<GPoint> getTilesAttackable(Unit unit, Map map);
	public abstract List<AttackedTile> getTilesAffected(GPoint tile);
	public void renderButton(Graphics2D g, int x, int y) {
		abilityButton.render(g, x, y);
	}
	
	public int getCost() {
		return abilityCost;
	}
	
	public String getName() {
		return name;
	}
	
	public Button getButton() {
		return abilityButton;
	}
	
	public int getDamage() {
		return damage;
	}
}
