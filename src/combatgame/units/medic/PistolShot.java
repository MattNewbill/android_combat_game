package combatgame.units.medic;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class PistolShot extends BasicAttack {
	
	public PistolShot() {
		this.damage = 10;
		this.abilityCost = 3;
		this.abilityButton = new Button(GameplayAssets.pistolShotIcon, null, 0, 0); //TODO: change sprites
	}
}
