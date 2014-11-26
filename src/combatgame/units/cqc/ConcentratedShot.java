package combatgame.units.cqc;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class ConcentratedShot extends BasicAttack {

	public ConcentratedShot() {
		super();
		this.damage = 90;
		this.abilityCost = 6;
		this.abilityButton = new Button(GameplayAssets.concentratedShotIcon, null, 0, 0);
	}
}
