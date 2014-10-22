package combatgame.objects;

import combatgame.assets.*;
import combatgame.util.Util;

/**
 * **NOT HAPPY**
 * TODO: Potentially get a better unit id
 */

public class Assault extends Unit {
	public Assault (int player_id) {
		this.name = "Assault";
		this.player_id = player_id;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO
		this.shootingCost = 2;
		this.movementCost = 1;
		this.visionRadius = 4;
		this.damage = 25;
		this.health = 100;
		this.isDead=false;
		this.sprites = GameplayAssets.assaultIcons;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
	}
	
}
