package combatgame.units.sniper;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class ChargedShot extends BasicAttack {
	
	public ChargedShot() {
		super();
		this.damage = 85;
		this.abilityCost = 9;
		this.abilityButton = new Button(GameplayAssets.chargedShotIcon, null, 0, 0);
	}
	
}
