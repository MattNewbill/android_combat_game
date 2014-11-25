package combatgame.units.cqc;

import combatgame.assets.GameplayAssets;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.util.Util;

public class CQC extends Unit {
	
	public CQC(int player_id, String name) {
		this.name = name;
		this.player_id = player_id;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO
		this.movementCost = 3;
		this.visionRadius = 3;
		this.health = 100;
		this.isDead=false;
		this.sprites = GameplayAssets.cqbIcons;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.abilities = new Ability[2];
		abilities[0] = new WideShot();
		abilities[1] = new ConcentratedShot();
	}
}
