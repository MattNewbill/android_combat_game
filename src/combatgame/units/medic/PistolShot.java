package combatgame.units.medic;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class PistolShot extends BasicAttack {
	
	private static final long serialVersionUID = 1L;

	public PistolShot() {
		super();
		this.damage = 10;
		this.abilityCost = 3;
	}
	
	public void loadButton() {
		this.abilityButton = new Button(GameplayAssets.pistolShotIcon, null, 0, 0); //TODO: change sprites
	}
}
