package combatgame.units.sniper;

import combatgame.graphics.*;
import combatgame.input.*;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.util.Vision;

import java.util.ArrayList;
import java.util.List;

public class BasicAttack extends Ability {
	List<GPoint> tilesAttackable = new ArrayList<GPoint>();
	List<AttackedTile> attackedTiles = new ArrayList<AttackedTile>();	
	
	public BasicAttack(int cost) {
		super(cost);
		this.name = "Shoot";
		this.damage = 50;
	}

	@Override
	public List<GPoint> getTilesAttackable(Unit unit, Map map) {
		if(unit.getUnit_id() == -1)//checks to make sure the space has a valid unit
			return null;
		tilesAttackable.clear();
		List<GPoint> tilesVisable = Vision.getSprintVision(map, unit); 
		//check player id of units
		for(int i = 0; i < tilesVisable.size(); i++) {
			int currentMapTileUnitId = map.getTile(tilesVisable.get(i)).getUnit_id();
			if(currentMapTileUnitId != unit.getUnit_id())
				tilesAttackable.add(tilesVisable.get(i));
		}
		return tilesAttackable;
	}

	@Override
	public List<AttackedTile> getTilesAffected(GPoint tile) {
		attackedTiles.clear();
		AttackedTile at = new AttackedTile(tile, damage);
		attackedTiles.add(at);
		return attackedTiles;
	}
	
}
