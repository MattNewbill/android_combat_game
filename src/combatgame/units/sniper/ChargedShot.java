package combatgame.units.sniper;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class ChargedShot extends BasicAttack {
	
	private static final long serialVersionUID = 1L;

	public ChargedShot() {
		super();
		this.damage = 85;
		this.abilityCost = 9;
	}
	
	public void loadButton() {
		this.abilityButton = new Button(GameplayAssets.chargedShotIcon, null, 0, 0);
	}
	
}
