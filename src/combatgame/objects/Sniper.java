package combatgame.objects;

import combatgame.assets.*;

/**
 * **HAPPY**
 */

public class Sniper extends Unit {
	public Sniper (int player_id) {
		this.player_id = player_id;
		this.shootingCost = 3;
		this.movementCost = 1;
		this.visionRadius = 5;
		this.damage = 50;
		this.health = 100;
		this.isDead=false;
		this.sprites = GameplayAssets.sniperIcons;
	}
	
}
