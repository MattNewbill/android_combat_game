package combatgame.units.sniper;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class QuickShot extends BasicAttack {
	
	private static final long serialVersionUID = 1L;

	public QuickShot() {
		super();
		this.damage = 50;
		this.abilityCost = 6;
	}	
	
	public void loadButton() {
		this.abilityButton = new Button(GameplayAssets.quickShotIcon, null, 0, 0);
	}
}
