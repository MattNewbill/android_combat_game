package combatgame.units.medic;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.widgets.Button;

public class AreaHeal extends Ability {

	public AreaHeal() {
		this.damage = -15;
		this.abilityCost = 5;
		this.abilityButton = new Button(GameplayAssets.areaHealIcon, null, 0, 0);
	}
	
	@Override
	public List<GPoint> getTilesAttackable(Unit unit, Map map) {
		tilesAttackable.clear();
		tilesAttackable.add(unit.getXYCoordinate());
		return tilesAttackable;
	}

	@Override
	public List<AttackedTile> getTilesAffected(GPoint tile, Map map) {
		attackedTiles.clear();
		return null;
	}

	//15 heal to 8 surrounding tiles
	//cost = 5
	//only be able to select themselves
	
}
