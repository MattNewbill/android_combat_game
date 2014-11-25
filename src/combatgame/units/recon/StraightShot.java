package combatgame.units.recon;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class StraightShot extends BasicAttack {

	public StraightShot() {
		this.damage = 15;
		this.abilityCost = 3;
		this.abilityButton = new Button(GameplayAssets.straightShotIcon, null, 0, 0);
	}
}
