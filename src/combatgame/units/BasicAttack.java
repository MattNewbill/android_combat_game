package combatgame.units;

import java.util.List;

import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.util.Vision;

public abstract class BasicAttack extends Ability {

	public BasicAttack() {}
	
	@Override
	public List<GPoint> getTilesAttackable(Unit unit, Map map) {
		if(unit.getUnit_id() == -1)//checks to make sure the space has a valid unit
			return null;
		tilesAttackable.clear();
		List<GPoint> tilesVisible = Vision.getSprintVision(map, unit); 
		//check player id of units
		for(int i = 0; i < tilesVisible.size(); i++) {
			if(map.getTile(tilesVisible.get(i)).hasUnit()) {
				int currentMapTilePlayerId = map.getTile(tilesVisible.get(i)).getPlayer_id();
				if(currentMapTilePlayerId != unit.getPlayer_id())
					tilesAttackable.add(tilesVisible.get(i));
			}
		}
		return tilesAttackable;
	}

	@Override
	public List<AttackedTile> getTilesAffected(GPoint tile, Map map) {
		attackedTiles.clear();
		AttackedTile at = pool.newObject();
		at.tile = tile; at.damageTaken = damage;
		attackedTiles.add(at);
		return attackedTiles;
	}

}
