package combatgame.units.cqc;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.util.Vision;
import combatgame.widgets.Button;

public class ConcentratedShot extends Ability {

	public ConcentratedShot() {
		this.name = "Concentrated";
		this.damage = 90;
		this.abilityCost = 6;
		this.abilityButton = new Button(GameplayAssets.basicAttackIcon, null, 0, 0); //TODO: change sprite
	}
	
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
