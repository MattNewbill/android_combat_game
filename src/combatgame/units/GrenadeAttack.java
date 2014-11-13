package combatgame.units;

import java.util.Arrays;
import java.util.List;

import android.util.Log;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.MapTile;
import combatgame.objects.Unit;
import combatgame.util.Vision;
import combatgame.widgets.Button;

public class GrenadeAttack extends Ability {

	public GrenadeAttack(int cost) {
		super(cost);
		this.name = "Shoot";
		this.damage = 35;
		this.abilityCost = 3;
		this.distance = 3;
		this.splash = 1;
		this.abilityButton = new Button(GameplayAssets.throwGrenadeIcon, null, 0, 0);
	}

	@Override
	public List<GPoint> getTilesAttackable(Unit unit, Map map) {
		if(unit.getUnit_id() == -1)//checks to make sure the space has a valid unit
			return null;
		tilesAttackable.clear();
		List<GPoint> tiles = Vision.getLaunchVision(map, unit, distance );
		for(int i = 0; i < tiles.size(); i++)
			tilesAttackable.add(tiles.get(i));
		//check player id of units
		return tilesAttackable;
	}

	@Override
	public List<AttackedTile> getTilesAffected(GPoint tile, Map map) {
		attackedTiles.clear();
		AttackedTile centerTile = pool.newObject();
		centerTile.tile = tile; centerTile.damageTaken = damage;
		attackedTiles.add(centerTile);
		
		if(isValidTile(tile.row - 1, tile.col - 1, map)) {
			GPoint topLeftTile = new GPoint(tile.row - 1, tile.col - 1);
			AttackedTile topLeftTileAttacked = pool.newObject();
			topLeftTileAttacked.tile = topLeftTile; topLeftTileAttacked.damageTaken = (int) (damage * .25);
			attackedTiles.add(topLeftTileAttacked);
		}
		if(isValidTile(tile.row - 1, tile.col, map)) {
			GPoint topCenterTile = new GPoint(tile.row - 1, tile.col);
			AttackedTile topCenterTileAttacked = pool.newObject();
			topCenterTileAttacked.tile = topCenterTile; topCenterTileAttacked.damageTaken = (int) (damage * .5);
			attackedTiles.add(topCenterTileAttacked);
		}
		
		if(isValidTile(tile.row - 1, tile.col + 1, map)) {
			GPoint topRightTile = new GPoint(tile.row - 1, tile.col + 1);
			AttackedTile topRightTileAttacked = pool.newObject();
			topRightTileAttacked.tile = topRightTile; topRightTileAttacked.damageTaken = (int) (damage * .25);
			attackedTiles.add(topRightTileAttacked);
		}
			
		if(isValidTile(tile.row, tile.col - 1, map)) {
			GPoint leftTile = new GPoint(tile.row, tile.col - 1);
			AttackedTile leftTileAttacked = pool.newObject();
			leftTileAttacked.tile = leftTile; leftTileAttacked.damageTaken = (int) (damage * .5);
			attackedTiles.add(leftTileAttacked);
		}
		
		if(isValidTile(tile.row, tile.col + 1, map)) {
			GPoint rightTile = new GPoint(tile.row, tile.col + 1);
			AttackedTile rightTileAttacked = pool.newObject();
			rightTileAttacked.tile = rightTile; rightTileAttacked.damageTaken = (int) (damage * .5);
			attackedTiles.add(rightTileAttacked);
		}
		
		if(isValidTile(tile.row + 1, tile.col - 1, map)) {
			GPoint bottomLeftTile = new GPoint(tile.row + 1, tile.col - 1);
			AttackedTile bottomLeftTileAttacked = pool.newObject();
			bottomLeftTileAttacked.tile = bottomLeftTile; bottomLeftTileAttacked.damageTaken = (int) (damage * .25);
			attackedTiles.add(bottomLeftTileAttacked);
		}
		
		if(isValidTile(tile.row + 1, tile.col, map)) {
			GPoint bottomCenterTile = new GPoint(tile.row + 1, tile.col);
			AttackedTile bottomCenterTileAttacked = pool.newObject();
			bottomCenterTileAttacked.tile = bottomCenterTile; bottomCenterTileAttacked.damageTaken = (int) (damage * .5);
			attackedTiles.add(bottomCenterTileAttacked);
		}
		
		if(isValidTile(tile.row + 1, tile.col + 1, map)) {
			GPoint bottomRightTile = new GPoint(tile.row + 1, tile.col + 1);
			AttackedTile bottomRightTileAttacked = pool.newObject();
			bottomRightTileAttacked.tile = bottomRightTile; bottomRightTileAttacked.damageTaken = (int) (damage * .25);
			attackedTiles.add(bottomRightTileAttacked);
		}
		
		return attackedTiles;
	}
	
	private boolean isValidTile(int row, int col, Map map) {
		if(row >= 0 && row < map.getNum_vertical_tiles() && col >= 0&& col < map.getNum_horizontal_tiles() && map.getTile(row, col).hasUnit() )
			return true;
		return false;
	}
}
