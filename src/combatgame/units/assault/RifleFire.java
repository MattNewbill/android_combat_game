package combatgame.units.assault;

import combatgame.assets.GameplayAssets;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class RifleFire extends BasicAttack {

	private static final long serialVersionUID = 1L;

	public RifleFire() {
		super();
		this.damage = 25;
		this.abilityCost = 4;
	}
	
	public void loadButton() {
		this.abilityButton = new Button(GameplayAssets.rifleFireIcon, null, 0, 0);
	}
}
