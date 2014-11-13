package combatgame.units;

import java.util.List;

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
		List<GPoint> tilesAttackable = Vision.getLaunchVision(map, unit, distance ); 
		//check player id of units
		return tilesAttackable;
	}

	@Override
	public List<AttackedTile> getTilesAffected(GPoint tile, Map map) {
		attackedTiles.clear();
		AttackedTile centerTile = new AttackedTile(tile, damage);
		attackedTiles.add(centerTile);
		
		GPoint topLeftTile = new GPoint(tile.row - 1, tile.col - 1);
		AttackedTile topLeftTileAttacked = new AttackedTile(topLeftTile, (int) (damage * .5));
		addTile(topLeftTileAttacked, map);
		
		GPoint topCenterTile = new GPoint(tile.row - 1, tile.col);
		AttackedTile topCenterTileAttacked = new AttackedTile(topCenterTile, (int) (damage * .5));
		addTile(topCenterTileAttacked, map);
		
		GPoint topRightTile = new GPoint(tile.row - 1, tile.col + 1);
		AttackedTile topRightTileAttacked = new AttackedTile(topRightTile, (int) (damage * .5));
		addTile(topRightTileAttacked, map);
		
		GPoint leftTile = new GPoint(tile.row, tile.col - 1);
		AttackedTile leftTileAttacked = new AttackedTile(leftTile, (int) (damage * .5));
		addTile(leftTileAttacked, map);
		
		GPoint rightTile = new GPoint(tile.row, tile.col + 1);
		AttackedTile rightTileAttacked = new AttackedTile(rightTile, (int) (damage * .5));
		addTile(rightTileAttacked, map);
		
		GPoint bottomLeftTile = new GPoint(tile.row + 1, tile.col - 1);
		AttackedTile bottomLeftTileAttacked = new AttackedTile(bottomLeftTile, (int) (damage * .5));
		addTile(bottomLeftTileAttacked, map);
		
		GPoint bottomCenterTile = new GPoint(tile.row + 1, tile.col);
		AttackedTile bottomCenterTileAttacked = new AttackedTile(bottomCenterTile, (int) (damage * .5));
		addTile(bottomCenterTileAttacked, map);
		
		GPoint bottomRightTile = new GPoint(tile.row + 1, tile.col + 1);
		AttackedTile bottomRightTileAttacked = new AttackedTile(bottomRightTile, (int) (damage * .5));
		addTile(bottomRightTileAttacked, map);
		
		return attackedTiles;
	}
	
	private void addTile(AttackedTile attackedTile, Map map) {
		MapTile mapTile = map.getTile(attackedTile.tile);
		if(attackedTile.tile.row >= 0 && attackedTile.tile.col <= map.getNum_horizontal_tiles() && mapTile.hasUnit() )
			attackedTiles.add(attackedTile);
	}
}
