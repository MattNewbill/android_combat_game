package combatgame.units.cqc;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.MapTile;
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
		double tempDamage = damage;
		MapTile mt = map.getTile(tileInFrontofUnit);
		if(mt.hasUnit()){ //there is a unit on the tile
			int unitId = mt.getUnit_id();
			Unit defender = map.getUnit(unitId);
			AttackingDirection dir = getAttackedDirection(UnitWhoIsAttacking, defender);
			if(dir == AttackingDirection.BACK)
				tempDamage = damage * BACK_DAMAGE_MODIFIER + damage;
			else if(dir == AttackingDirection.SIDE)
				tempDamage = damage * SIDE_DAMAGE_MODIFIER + damage;
		}
		
		tileInFrontofUnitAttacked.tile = tileInFrontofUnit; tileInFrontofUnitAttacked.damageTaken = (int)tempDamage;
		attackedTiles.add(tileInFrontofUnitAttacked);
		
		double tempIndirectDamage = indirect_damage;
		
		if(leftTile != null && Map.isValidTile(leftTile.row, leftTile.col, map)) {
			AttackedTile leftTileAttacked = pool.newObject();
			
			MapTile lt = map.getTile(leftTile);
			if(lt.hasUnit()){ //there is a unit on the tile
				int unitId = lt.getUnit_id();
				Unit defender = map.getUnit(unitId);
				AttackingDirection dir = getAttackedDirection(UnitWhoIsAttacking, defender);
				if(dir == AttackingDirection.BACK)
					tempIndirectDamage = indirect_damage * BACK_DAMAGE_MODIFIER + indirect_damage;
				else if(dir == AttackingDirection.SIDE)
					tempIndirectDamage = indirect_damage * SIDE_DAMAGE_MODIFIER + indirect_damage;
			}
			
			leftTileAttacked.tile = leftTile; leftTileAttacked.damageTaken = (int)tempIndirectDamage ;
			attackedTiles.add(leftTileAttacked);
		}
		
		tempIndirectDamage = indirect_damage;
		
		if(rightTile != null && Map.isValidTile(rightTile.row, rightTile.col, map)){
			AttackedTile rightTileAttacked = pool.newObject();
			
			MapTile rt = map.getTile(rightTile);
			if(rt.hasUnit()){ //there is a unit on the tile
				int unitId = rt.getUnit_id();
				Unit defender = map.getUnit(unitId);
				AttackingDirection dir = getAttackedDirection(UnitWhoIsAttacking, defender);
				if(dir == AttackingDirection.BACK)
					tempIndirectDamage = indirect_damage * BACK_DAMAGE_MODIFIER + indirect_damage;
				else if(dir == AttackingDirection.SIDE)
					tempIndirectDamage = indirect_damage * SIDE_DAMAGE_MODIFIER + indirect_damage;
			}
			
			rightTileAttacked.tile = rightTile; rightTileAttacked.damageTaken = (int)tempIndirectDamage;
			attackedTiles.add(rightTileAttacked);
		}
		return attackedTiles;
	}
}
