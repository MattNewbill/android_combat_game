package combatgame.units.medic;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
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
		GPoint tileOfUnit = unit.getXYCoordinate();
		GPoint tileInFrontofUnit = null;
		GPoint diagonalLeftTile = null;
		GPoint diagonalRightTile = null;
		
		switch (unit.getDirectionFacing()) {
		case Unit.FACING_UP: 
			tileInFrontofUnit = new GPoint(tileOfUnit.row - 1, tileOfUnit.col);//up
			diagonalLeftTile = new GPoint(tileOfUnit.row - 1, tileOfUnit.col - 1);//upper left
			diagonalRightTile = new GPoint(tileOfUnit.row - 1, tileOfUnit.col + 1);//upper right
			break;
		case Unit.FACING_DOWN:
			tileInFrontofUnit = new GPoint(tileOfUnit.row + 1, tileOfUnit.col);//down
			diagonalLeftTile = new GPoint(tileOfUnit.row + 1, tileOfUnit.col + 1);//bottom right
			diagonalRightTile = new GPoint(tileOfUnit.row + 1, tileOfUnit.col - 1);//bottom left
			break;
		case Unit.FACING_RIGHT:
			tileInFrontofUnit = new GPoint(tileOfUnit.row, tileOfUnit.col + 1);//right
			diagonalLeftTile = new GPoint(tileOfUnit.row - 1, tileOfUnit.col + 1);//upper right
			diagonalRightTile = new GPoint(tileOfUnit.row + 1, tileOfUnit.col + 1);//bottom right
			break;
		case Unit.FACING_LEFT:
			tileInFrontofUnit = new GPoint(tileOfUnit.row, tileOfUnit.col - 1);//left
			diagonalLeftTile = new GPoint(tileOfUnit.row + 1, tileOfUnit.col - 1);//bottom left
			diagonalRightTile = new GPoint(tileOfUnit.row - 1, tileOfUnit.col - 1);//top left
			break;
		}
		if(tileInFrontofUnit != null && Map.isValidTile(tileInFrontofUnit.row, tileInFrontofUnit.col, map)) {
			tilesAttackable.add(tileInFrontofUnit);
		}
		
		if(diagonalLeftTile != null && Map.isValidTile(diagonalLeftTile.row, diagonalLeftTile.col, map)) {
			tilesAttackable.add(diagonalLeftTile);
		}
		
		if(diagonalRightTile != null && Map.isValidTile(diagonalRightTile.row, diagonalRightTile.col, map)){
			tilesAttackable.add(diagonalRightTile);
		}
		return tilesAttackable;
	}

	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, GPoint tile, Map map) {
		attackedTiles.clear();
		attackedTiles.add(new AttackedTile(tile, this.damage));
		return attackedTiles;
	}
	
	//heal one tile for 40 damage
	//cost = 5;
	//range = 1;
	
}
