package combatgame.units.recon;

import combatgame.assets.GameplayAssets;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.util.Util;

public class Recon extends Unit {
	
	private static final long serialVersionUID = 1L;
	
	public Recon(int player_id, String name, boolean isPlayerOne) {
		this.name = name;
		this.player_id = player_id;
		this.isPlayerOne = isPlayerOne;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO: id system...
		this.movementCost = 1;
		this.visionRadius = 5;
		this.health = 100;
		this.armor = 25;
		this.isDead=false;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.unit_type = UnitType.RECON;
		this.abilities = new Ability[2];
		abilities[0] = new StraightShot();
		abilities[1] = new TrickShot();
		loadSprites();
	}
	
	public void loadSprites() {
		if(isPlayerOne)
			this.sprites = GameplayAssets.reconIconsP1;
		else
			this.sprites = GameplayAssets.reconIconsP2;
		
		abilities[0].loadButton();
		abilities[1].loadButton();
	}
}
