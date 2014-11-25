package combatgame.units.medic;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.widgets.Button;

public class SingleHeal extends Ability {

	public SingleHeal() {
		this.damage = -40;
		this.abilityCost = 5;
		this.abilityButton = new Button(GameplayAssets.singleHealIcon, null, 0, 0);
	}

	@Override
	public List<GPoint> getTilesAttackable(Unit unit, Map map) {
		tilesAttackable.clear();
		return null;
	}

	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, GPoint tile, Map map) {
		attackedTiles.clear();
		return null;
	}
	
	//heal one tile for 40 damage
	//cost = 5;
	//range = 1;
	
}
