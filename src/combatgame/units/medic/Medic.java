package combatgame.units.medic;

import combatgame.assets.GameplayAssets;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.util.Util;

public class Medic extends Unit {

	private static final long serialVersionUID = 1L;
	
	public Medic(int player_id, String name, boolean isPlayerOne) {
		this.name = name;
		this.player_id = player_id;
		this.isPlayerOne = isPlayerOne;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO: id system...
		this.movementCost = 1;
		this.visionRadius = 4;
		this.health = 100;
		this.armor = 25;
		this.isDead=false;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.unit_type = UnitType.MEDIC;
		this.abilities = new Ability[3];
		abilities[0] = new PistolShot();
		abilities[1] = new SingleHeal();
		abilities[2] = new AreaHeal();
		loadSprites();
	}
	
	public void loadSprites() {
		if(isPlayerOne)
			this.sprites = GameplayAssets.medicIconsP1;
		else
			this.sprites = GameplayAssets.medicIconsP2;
		
		abilities[0].loadButton();
		abilities[1].loadButton();
		abilities[2].loadButton();
	}
	
}
