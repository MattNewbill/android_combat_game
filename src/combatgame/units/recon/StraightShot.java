package combatgame.units.recon;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class StraightShot extends BasicAttack {

	private static final long serialVersionUID = 1L;

	public StraightShot() {
		super();
		this.damage = 15;
		this.abilityCost = 3;
	}
	
	public void loadButton() {
		this.abilityButton = new Button(GameplayAssets.straightShotIcon, null, 0, 0);
	}
}
