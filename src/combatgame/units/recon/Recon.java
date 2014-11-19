package combatgame.units.recon;

import combatgame.assets.GameplayAssets;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.GrenadeAttack;
import combatgame.util.Util;

public class Recon extends Unit {

	public Recon(int player_id, String name) {
		this.name = name;
		this.player_id = player_id;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO
		this.movementCost = 1;
		this.visionRadius = 5;
		this.health = 100;
		this.isDead=false;
		this.sprites = GameplayAssets.reconIcons;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.abilities = new Ability[2];
		abilities[0] = new BasicAttack();
		abilities[1] = new GrenadeAttack();
	}
}
