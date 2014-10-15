package combatgame.objects;

import combatgame.assets.*;
import combatgame.util.Util;

/**
 * **NOT HAPPY**
 * TODO: Potentially get a better unit id
 */

public class Sniper extends Unit {
	public Sniper (int player_id) {
		this.name = "Sniper";
		this.player_id = player_id;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO
		this.shootingCost = 3;
		this.movementCost = 1;
		this.visionRadius = 5;
		this.damage = 50;
		this.health = 100;
		this.isDead=false;
		this.sprites = GameplayAssets.sniperIcons;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
	}
	
}
