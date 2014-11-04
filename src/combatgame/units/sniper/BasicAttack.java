package combatgame.units.sniper;

import combatgame.graphics.*;
import combatgame.input.*;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.Attack;

import java.util.List;

public class BasicAttack extends Ability {

	public BasicAttack(int cost) {
		super(cost);
		this.name = "Shoot";
	}

	@Override
	public List<GPoint> getTilesAttackable(Unit unit, Map map) {

		return null;
	}

	@Override
	public List<Attack> getTilesAffected(GPoint tile) {

		return null;
	}
	
}
