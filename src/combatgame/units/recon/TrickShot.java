package combatgame.units.recon;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.MapTile;
import combatgame.objects.Unit;
import combatgame.units.AttackedTile;
import combatgame.units.BasicAttack;
import combatgame.units.Ability.AttackingDirection;
import combatgame.widgets.Button;

public class TrickShot extends BasicAttack {

	private static final long serialVersionUID = 1L;
	public final static String ABILITY_TYPE = "TRICKSHOT";
	
	public TrickShot() {
		this.type = ABILITY_TYPE;
		this.damage = 15;
		this.abilityCost = 4;
	}
	
	public void loadButton() {
		this.abilityButton = new Button(GameplayAssets.trickShotIcon, null, 0, 0);
	}
	
	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, GPoint tileTouched, Map map) {
		attackedTiles.clear();
		AttackedTile at = pool.newObject();
		
		double tempDamage = damage;
		
		//check which direction the attacking and defending units are facing and give bonus damage if flanking
		MapTile tile = map.getTile(tileTouched);
		if(tile.hasUnit()){ //there is a unit on the tile
			int unitId = tile.getUnit_id();
			Unit defender = map.getUnit(unitId);
			AttackingDirection dir = getAttackedDirection(UnitWhoIsAttacking, defender);
			
			int rando = (int)(Math.random()*3);
			
			if(rando == 0)
				tempDamage = damage * BACK_DAMAGE_MODIFIER + damage;
			else if(rando == 1)
				tempDamage = damage * SIDE_DAMAGE_MODIFIER + damage;
			else
				tempDamage = damage;
		}
		
		at.tile = tileTouched; at.damageTaken = (int)tempDamage;
		attackedTiles.add(at);
		return attackedTiles;
	}
}

