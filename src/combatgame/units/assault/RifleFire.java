package combatgame.units.assault;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class RifleFire extends BasicAttack {

	public RifleFire() {
		super();
		this.damage = 25;
		this.abilityCost = 4;
		this.abilityButton = new Button(GameplayAssets.rifleFireIcon, null, 0, 0);
	}
}
