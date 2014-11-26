package combatgame.units.recon;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class TrickShot extends BasicAttack {
	public final static String ABILITY_TYPE = "TRICKSHOT";
	
	public TrickShot() {
		this.type = ABILITY_TYPE;
		this.damage = 15;
		this.abilityCost = 4;
		this.abilityButton = new Button(GameplayAssets.trickShotIcon, null, 0, 0);
	}
}
