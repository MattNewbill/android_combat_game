package combatgame.units.sniper;

import combatgame.assets.GameplayAssets;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.util.Util;

public class Sniper extends Unit {
	
	private static final long serialVersionUID = 1L;
	
	public Sniper (int player_id, String name, boolean isPlayerOne) {
		this.name = name;
		this.player_id = player_id;
		this.isPlayerOne = isPlayerOne;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO: id system...
		this.movementCost = 2;
		this.visionRadius = 6;
		this.health = 100;
		this.armor = 25;
		this.isDead=false;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.unit_type = UnitType.SNIPER;
		this.abilities = new Ability[2];
		abilities[0] = new QuickShot();
		abilities[1] = new ChargedShot();
		loadSprites();
	}
	
	public void loadSprites() {
		if(isPlayerOne)
			this.sprites = GameplayAssets.sniperIconsP1;
		else
			this.sprites = GameplayAssets.sniperIconsP2;
		
		abilities[0].loadButton();
		abilities[1].loadButton();
	}
	
}
