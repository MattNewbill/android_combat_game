package combatgame.units.medic;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.util.Vision;
import combatgame.widgets.Button;

public class AreaHeal extends Ability {

	private static final long serialVersionUID = 1L;
	
	public AreaHeal() {
		this.damage = -15;
		this.abilityCost = 5;
	}
	
	public void loadButton() {
		this.abilityButton = new Button(GameplayAssets.areaHealIcon, null, 0, 0);
	}
	
	@Override
	public List<GPoint> getTilesAttackable(Unit unit, Map map) {
		tilesAttackable.clear();
		tilesAttackable.add(unit.getXYCoordinate());
		return tilesAttackable;
	}

	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, GPoint tile, Map map) {
		attackedTiles.clear();
		int RANGE_OF_AREA_HEAL = 1;
		List<GPoint> points_affected = Vision.getSlowVision(map, UnitWhoIsAttacking, RANGE_OF_AREA_HEAL);
		
		for(int i = 0; i < points_affected.size(); i++) {
			AttackedTile tileAffected = pool.newObject();
			tileAffected.tile = points_affected.get(i);
			tileAffected.damageTaken = this.damage;
			attackedTiles.add(tileAffected);
		}
		return attackedTiles;
	}
	/*What is the purpose of this method?*/
	public boolean isValidTileToAttack(Unit unitWhoIsAttacking, GPoint tileToCheck, Map map) {
		return true;
	}

	//15 heal to 8 surrounding tiles
	//cost = 5
	//only be able to select themselves
	
}
