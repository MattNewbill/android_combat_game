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

	private static final long serialVersionUID = 1L;
	private int indirect_damage;//damage to the left and right targets


	public WideShot() {
		this.damage = 50;
		this.abilityCost = 5;
		this.indirect_damage = 30;//damage to the left and right targets
	}
	
	public void loadButton() {
		this.abilityButton = new Button(GameplayAssets.wideShotIcon, null, 0, 0);
	}
	
	
	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, GPoint tile, Map map) {
		attackedTiles.clear();
		GPoint tileInFrontofUnit = tile;
		GPoint leftTile = null;
		GPoint rightTile = null;
		
		switch (UnitWhoIsAttacking.getDirectionFacing()) {
		case Unit.FACING_UP: 
			leftTile = new GPoint(tile.row, tile.col - 1);//upper left
			rightTile = new GPoint(tile.row, tile.col + 1);//upper right
			break;
		case Unit.FACING_DOWN:
			leftTile = new GPoint(tile.row, tile.col + 1);//bottom right
			rightTile = new GPoint(tile.row, tile.col - 1);//bottom left
			break;
		case Unit.FACING_RIGHT:
			leftTile = new GPoint(tile.row - 1, tile.col);//upper right
			rightTile = new GPoint(tile.row + 1, tile.col);//bottom right
			break;
		case Unit.FACING_LEFT:
			leftTile = new GPoint(tile.row + 1, tile.col);//bottom left
			rightTile = new GPoint(tile.row - 1, tile.col);//top left
			break;
		}
		
		AttackedTile tileInFrontofUnitAttacked = pool.newObject();
		tileInFrontofUnitAttacked.tile = tileInFrontofUnit; tileInFrontofUnitAttacked.damageTaken = this.damage;
		attackedTiles.add(tileInFrontofUnitAttacked);
		
		if(leftTile != null && Map.isValidTile(leftTile.row, leftTile.col, map)) {
			AttackedTile leftTileAttacked = pool.newObject();
			leftTileAttacked.tile = leftTile; leftTileAttacked.damageTaken = indirect_damage ;
			attackedTiles.add(leftTileAttacked);
		}
		
		if(rightTile != null && Map.isValidTile(rightTile.row, rightTile.col, map)){
			AttackedTile rightTileAttacked = pool.newObject();
			rightTileAttacked.tile = rightTile; rightTileAttacked.damageTaken = indirect_damage;
			attackedTiles.add(rightTileAttacked);
		}
		return attackedTiles;
	}
}
