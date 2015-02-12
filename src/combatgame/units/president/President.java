package combatgame.units.president;

import combatgame.assets.GameplayAssets;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.recon.StraightShot;
import combatgame.units.recon.TrickShot;
import combatgame.util.Util;

public class President extends Unit {

	private static final long serialVersionUID = 1L;
	
	public President(int player_id, String name, boolean isPlayerOne) {
		this.name = name;
		this.player_id = player_id;
		this.isPlayerOne = isPlayerOne;
		this.unit_id = (int)System.currentTimeMillis() + Util.getRand(); //TODO: id system...
		this.movementCost = 2;
		this.visionRadius = 5;
		this.health = 100;
		this.armor = 5;
		this.isDead=false;
		this.maxHealth = 100;
		this.pointsLeft = POINTS_PER_TURN;
		this.rotationCost = 1;
		this.unit_type = UnitType.PRESIDENT;
		this.abilities = new Ability[2];
		abilities[0] = new StraightShot();
		abilities[1] = new TrickShot();
		loadSprites();
	}
	
	public void loadSprites() {
		if(isPlayerOne)
			this.sprites = GameplayAssets.presidentIconsP1;
		else
			this.sprites = GameplayAssets.presidentIconsP2;
		
		abilities[0].loadButton();
		abilities[1].loadButton();
	}
	
}
