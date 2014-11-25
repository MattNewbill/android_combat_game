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
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, GPoint tile, Map map) {
		attackedTiles.clear();
		AttackedTile centerTile = pool.newObject();
		centerTile.tile = tile; centerTile.damageTaken = this.damage;
		attackedTiles.add(centerTile);
		
		if(Map.isValidTile(tile.row - 1, tile.col - 1, map)) {
			GPoint topLeftTile = new GPoint(tile.row - 1, tile.col - 1);
			AttackedTile topLeftTileAttacked = pool.newObject();
			topLeftTileAttacked.tile = topLeftTile; topLeftTileAttacked.damageTaken = this.damage;
			attackedTiles.add(topLeftTileAttacked);
		}
		if(Map.isValidTile(tile.row - 1, tile.col, map)) {
			GPoint topCenterTile = new GPoint(tile.row - 1, tile.col);
			AttackedTile topCenterTileAttacked = pool.newObject();
			topCenterTileAttacked.tile = topCenterTile; topCenterTileAttacked.damageTaken = this.damage;
			attackedTiles.add(topCenterTileAttacked);
		}
		
		if(Map.isValidTile(tile.row - 1, tile.col + 1, map)) {
			GPoint topRightTile = new GPoint(tile.row - 1, tile.col + 1);
			AttackedTile topRightTileAttacked = pool.newObject();
			topRightTileAttacked.tile = topRightTile; topRightTileAttacked.damageTaken = this.damage;
			attackedTiles.add(topRightTileAttacked);
		}
			
		if(Map.isValidTile(tile.row, tile.col - 1, map)) {
			GPoint leftTile = new GPoint(tile.row, tile.col - 1);
			AttackedTile leftTileAttacked = pool.newObject();
			leftTileAttacked.tile = leftTile; leftTileAttacked.damageTaken = this.damage;
			attackedTiles.add(leftTileAttacked);
		}
		
		if(Map.isValidTile(tile.row, tile.col + 1, map)) {
			GPoint rightTile = new GPoint(tile.row, tile.col + 1);
			AttackedTile rightTileAttacked = pool.newObject();
			rightTileAttacked.tile = rightTile; rightTileAttacked.damageTaken = this.damage;
			attackedTiles.add(rightTileAttacked);
		}
		
		if(Map.isValidTile(tile.row + 1, tile.col - 1, map)) {
			GPoint bottomLeftTile = new GPoint(tile.row + 1, tile.col - 1);
			AttackedTile bottomLeftTileAttacked = pool.newObject();
			bottomLeftTileAttacked.tile = bottomLeftTile; bottomLeftTileAttacked.damageTaken = this.damage;
			attackedTiles.add(bottomLeftTileAttacked);
		}
		
		if(Map.isValidTile(tile.row + 1, tile.col, map)) {
			GPoint bottomCenterTile = new GPoint(tile.row + 1, tile.col);
			AttackedTile bottomCenterTileAttacked = pool.newObject();
			bottomCenterTileAttacked.tile = bottomCenterTile; bottomCenterTileAttacked.damageTaken = this.damage;
			attackedTiles.add(bottomCenterTileAttacked);
		}
		
		if(Map.isValidTile(tile.row + 1, tile.col + 1, map)) {
			GPoint bottomRightTile = new GPoint(tile.row + 1, tile.col + 1);
			AttackedTile bottomRightTileAttacked = pool.newObject();
			bottomRightTileAttacked.tile = bottomRightTile; bottomRightTileAttacked.damageTaken = this.damage;
			attackedTiles.add(bottomRightTileAttacked);
		}
		
		return attackedTiles;
	}

	//15 heal to 8 surrounding tiles
	//cost = 5
	//only be able to select themselves
	
}
