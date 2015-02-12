package combatgame.units.cqc;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class ConcentratedShot extends BasicAttack {

	private static final long serialVersionUID = 1L;

	public ConcentratedShot() {
		super();
		this.damage = 90;
		this.abilityCost = 6;
	}
	
	public void loadButton() {
		this.abilityButton = new Button(GameplayAssets.concentratedShotIcon, null, 0, 0);
	}
}
