package combatgame.units.sniper;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class QuickShot extends BasicAttack {
	
	public QuickShot() {
		super();
		this.damage = 50;
		this.abilityCost = 6;
		this.abilityButton = new Button(GameplayAssets.quickShotIcon, null, 0, 0);
	}	
}
