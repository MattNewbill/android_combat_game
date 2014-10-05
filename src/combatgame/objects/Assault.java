package combatgame.objects;

import combatgame.assets.*;
import android.graphics.Point;

/**
 * **NOT HAPPY**
 * TODO: Change single sprite to array of sprites
 */

public class Assault extends Unit {
	public Assault (int player_id) {
		this.player_id = player_id;
		this.shootingCost = 2;
		this.movementCost = 1;
		this.visionRadius = 4;
		this.damage = 25;
		this.health = 100;
		this.isDead=false;
		this.sprite = GameplayAssets.assaultIcon; //TODO
	}
	
}
