package combatgame.units.medic;

import combatgame.assets.GameplayAssets;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.util.Util;

public class Medic extends Unit {

	public Medic(int player_id, String name) {
		this.name = name;
		this.player_id = player_id;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO
		this.movementCost = 1;
		this.visionRadius = 4;
		this.health = 100;
		this.isDead=false;
		this.sprites = GameplayAssets.medicIcons;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.abilities = new Ability[3];
		abilities[0] = new PistolShot();
		abilities[1] = new SingleHeal();
		abilities[2] = new AreaHeal();
	}
	
}
