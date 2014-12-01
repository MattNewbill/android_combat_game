package combatgame.units.medic;

import java.util.ArrayList;
import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.util.Vision;
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
		int RANGE_OF_SINGLE_HEAL = 1;
		tilesAttackable = Vision.getSprintVision(map, unit, RANGE_OF_SINGLE_HEAL);
		return tilesAttackable;
	}

	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, GPoint tile, Map map) {
		attackedTiles.clear();
		attackedTiles.add(new AttackedTile(tile, this.damage));
		return attackedTiles;
	}
	
	public boolean isValidTileToAttack(Unit unitWhoIsAttacking, GPoint tileToCheck, Map map) {
		//check player id of units
		if(map.getTile(tileToCheck).hasUnit()) {
			int currentMapTilePlayerId = map.getTile(tileToCheck).getPlayer_id();
			if(currentMapTilePlayerId == unitWhoIsAttacking.getPlayer_id())
				return true;
		}
		return false;
	}
	
}
