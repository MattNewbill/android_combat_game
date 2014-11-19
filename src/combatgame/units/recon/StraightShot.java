package combatgame.units.recon;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.units.BasicAttack;
import combatgame.util.Vision;
import combatgame.widgets.Button;

public class StraightShot extends BasicAttack {

	public StraightShot() {
		super();
		this.damage = 15;
		this.abilityCost = 3;
		this.abilityButton = new Button(GameplayAssets.basicAttackIcon, null, 0, 0);
	}
}
