package combatgame.units.medic;

import java.util.List;

import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;

public class AreaHeal extends Ability {

	public AreaHeal() {
		
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

	//15 heal to 8 surrounding tiles
	//cost = 5
	//only be able to select themselves
	
}
