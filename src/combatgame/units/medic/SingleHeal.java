package combatgame.units.medic;

import java.util.List;

import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;

public class SingleHeal extends Ability {

	public SingleHeal() {
		
	}

	@Override
	public List<GPoint> getTilesAttackable(Unit unit, Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AttackedTile> getTilesAffected(GPoint tile, Map map) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//heal one tile for 40 damage
	//cost = 5;
	//range = 1;
	
}
