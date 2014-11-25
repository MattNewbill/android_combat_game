package combatgame.units.cqc;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.AttackedTile;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class WideShot extends BasicAttack {

	private int indirect_damage;//damage to the left and right targets


	public WideShot() {
		this.damage = 50;
		this.abilityCost = 4;
		this.indirect_damage = 30;//damage to the left and right targets
		this.abilityButton = new Button(GameplayAssets.wideShotIcon, null, 0, 0);
	}
	
	
	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, GPoint tile, Map map) {
		attackedTiles.clear();
		GPoint tileInFrontofUnit = tile;
		GPoint diagonalLeftTile = null;
		GPoint diagonalRightTile = null;
		
		switch (UnitWhoIsAttacking.getDirectionFacing()) {
		case Unit.FACING_UP: 
			diagonalLeftTile = new GPoint(tile.row - 1, tile.col - 1);//upper left
			diagonalRightTile = new GPoint(tile.row - 1, tile.col + 1);//upper right
			break;
		case Unit.FACING_DOWN:
			diagonalLeftTile = new GPoint(tile.row + 1, tile.col + 1);//bottom right
			diagonalRightTile = new GPoint(tile.row + 1, tile.col - 1);//bottom left
			break;
		case Unit.FACING_RIGHT:
			diagonalLeftTile = new GPoint(tile.row - 1, tile.col + 1);//upper right
			diagonalRightTile = new GPoint(tile.row + 1, tile.col + 1);//bottom right
			break;
		case Unit.FACING_LEFT:
			diagonalLeftTile = new GPoint(tile.row + 1, tile.col - 1);//bottom left
			diagonalRightTile = new GPoint(tile.row - 1, tile.col - 1);//top left
			break;
		}
		
		AttackedTile tileInFrontofUnitAttacked = pool.newObject();
		tileInFrontofUnitAttacked.tile = tileInFrontofUnit; tileInFrontofUnitAttacked.damageTaken = this.damage;
		attackedTiles.add(tileInFrontofUnitAttacked);
		
		if(diagonalLeftTile != null && Map.isValidTile(diagonalLeftTile.row, diagonalLeftTile.col, map)) {
			AttackedTile diagonalLeftTileAttacked = pool.newObject();
			diagonalLeftTileAttacked.tile = diagonalLeftTile; diagonalLeftTileAttacked.damageTaken = indirect_damage ;
			attackedTiles.add(diagonalLeftTileAttacked);
		}
		
		if(diagonalRightTile != null && Map.isValidTile(diagonalRightTile.row, diagonalRightTile.col, map)){
			AttackedTile diagonalRightTileAttacked = pool.newObject();
			diagonalRightTileAttacked.tile = diagonalRightTile; diagonalRightTileAttacked.damageTaken = indirect_damage;
			attackedTiles.add(diagonalRightTileAttacked);
		}
		return attackedTiles;
	}
}
