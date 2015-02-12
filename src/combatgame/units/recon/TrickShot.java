package combatgame.units.recon;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class TrickShot extends BasicAttack {

	private static final long serialVersionUID = 1L;
	public final static String ABILITY_TYPE = "TRICKSHOT";
	
	public TrickShot() {
		this.type = ABILITY_TYPE;
		this.damage = 15;
		this.abilityCost = 4;
	}
	
	public void loadButton() {
		this.abilityButton = new Button(GameplayAssets.trickShotIcon, null, 0, 0);
	}
}
