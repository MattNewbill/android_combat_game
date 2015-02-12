package combatgame.units.cqc;

import combatgame.assets.GameplayAssets;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.util.Util;

public class CQC extends Unit {
	
	private static final long serialVersionUID = 1L;
	
	public CQC(int player_id, String name, boolean isPlayerOne) {
		this.name = name;
		this.player_id = player_id;
		this.isPlayerOne = isPlayerOne;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO: id system....
		this.movementCost = 3;
		this.visionRadius = 3;
		this.health = 100;
		this.armor = 75;
		this.isDead=false;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.unit_type = UnitType.CQC;
		this.abilities = new Ability[2];
		abilities[0] = new WideShot();
		abilities[1] = new ConcentratedShot();
		loadSprites();
	}
	
	public void loadSprites() {
		if(isPlayerOne)
			this.sprites = GameplayAssets.cqcIconsP1;
		else
			this.sprites = GameplayAssets.cqcIconsP2;
		
		abilities[0].loadButton();
		abilities[1].loadButton();
	}
}
