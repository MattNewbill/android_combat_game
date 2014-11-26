package combatgame.units.assault;

import combatgame.assets.*;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.util.Util;

public class Assault extends Unit {
	public Assault (int player_id, String name) {
		this.name = name;
		this.player_id = player_id;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO: id system...
		this.movementCost = 2;
		this.visionRadius = 4;
		this.health = 100;
		this.isDead=false;
		this.sprites = GameplayAssets.assaultIcons;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.abilities = new Ability[2];
		abilities[0] = new RifleFire();
		abilities[1] = new GrenadeAttack();
	}
	
}
