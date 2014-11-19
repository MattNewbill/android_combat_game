package combatgame.units.sniper;

import combatgame.assets.*;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.assault.GrenadeAttack;
import combatgame.util.Util;

/**
 * **NOT HAPPY**
 * TODO: Potentially get a better unit id
 */

public class Sniper extends Unit {
	public Sniper (int player_id, String name) {
		this.name = name;
		this.player_id = player_id;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO
		this.movementCost = 2;
		this.visionRadius = 6;
		this.health = 100;
		this.isDead=false;
		this.sprites = GameplayAssets.sniperIcons;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.abilities = new Ability[2];
		abilities[0] = new QuickShot();
		abilities[2] = new ChargedShot();
		
	}
	
}
