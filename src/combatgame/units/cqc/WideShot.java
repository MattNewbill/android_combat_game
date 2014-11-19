package combatgame.units.cqc;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class WideShot extends BasicAttack {

	public WideShot() {
		this.damage = 50;
		this.abilityCost = 4;
		this.abilityButton = new Button(GameplayAssets.basicAttackIcon, null, 0, 0);
	}
	
	//cost = 4
	//50 damage to tile attacked
	//30 damage to direct left and right tiles
}
