package combatgame.units;

import java.util.List;

import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.MapTile;
import combatgame.objects.Unit;
import combatgame.util.Vision;

public abstract class BasicAttack extends Ability {

	private static final long serialVersionUID = 1L;

	public BasicAttack() {
		super();
	}
	
	@Override
	public List<GPoint> getTilesAttackable(Unit unit, Map map) {
		if(unit.getUnit_id() == -1)//checks to make sure the space has a valid unit
			return null;
		tilesAttackable.clear();
		List<GPoint> temp = Vision.getSprintVision(map, unit);
		for(int i = 1; i < temp.size(); i++) {
			tilesAttackable.add(temp.get(i));
		}
		return tilesAttackable;
	}

	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, GPoint tileTouched, Map map) {
		attackedTiles.clear();
		AttackedTile at = pool.newObject();
		
		double tempDamage = damage;
		
		//check which direction the attacking and defending units are facing and give bonus damage if flanking
		MapTile tile = map.getTile(tileTouched);
		if(tile.hasUnit()){ //there is a unit on the tile
			int unitId = tile.getUnit_id();
			Unit defender = map.getUnit(unitId);
			AttackingDirection dir = getAttackedDirection(UnitWhoIsAttacking, defender);
			if(dir == AttackingDirection.BACK)
				tempDamage = damage * BACK_DAMAGE_MODIFIER + damage;
			else if(dir == AttackingDirection.SIDE)
				tempDamage = damage * SIDE_DAMAGE_MODIFIER + damage;
		}
		
		at.tile = tileTouched; at.damageTaken = (int)tempDamage;
		attackedTiles.add(at);
		return attackedTiles;
	}
	
	@Override
	public boolean isValidTileToAttack(Unit unitWhoIsAttacking, GPoint tileToCheck, Map map) {
		//check player id of units
		if(map.getTile(tileToCheck).hasUnit()) {
			int currentMapTilePlayerId = map.getTile(tileToCheck).getPlayer_id();
			if(currentMapTilePlayerId != unitWhoIsAttacking.getPlayer_id())
				return true;
		}
		return false;
	}

}
