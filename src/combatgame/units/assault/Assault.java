package combatgame.units.assault;

import combatgame.assets.*;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.util.Util;

public class Assault extends Unit {
	
	public Assault (int player_id, String name, boolean isPlayerOne) {
		this.name = name;
		this.player_id = player_id;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO: id system...
		this.movementCost = 2;
		this.visionRadius = 4;
		this.health = 100;
		this.armor = 50;
		this.isDead=false;
		if(isPlayerOne)
			this.sprites = GameplayAssets.assaultIconsP1;
		else
			this.sprites = GameplayAssets.assaultIconsP2;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.unit_type = UnitType.ASSAULT;
		this.abilities = new Ability[2];
		abilities[0] = new RifleFire();
		abilities[1] = new GrenadeAttack();
	}
	
}
