package combatgame.units.assault;

import java.util.ArrayList;
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

public class RifleFire extends BasicAttack {

	public RifleFire() {
		this.damage = 25;
		this.abilityCost = 4;
		this.abilityButton = new Button(GameplayAssets.basicAttackIcon, null, 0, 0);
	}
}
