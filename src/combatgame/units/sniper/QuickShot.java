package combatgame.units.sniper;

import combatgame.widgets.Button;
import combatgame.assets.GameplayAssets;
import combatgame.graphics.*;
import combatgame.input.*;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.units.BasicAttack;
import combatgame.util.Vision;
import java.util.ArrayList;
import java.util.List;

public class QuickShot extends BasicAttack {
	
	public QuickShot() {
		super();
		this.damage = 50;
		this.abilityCost = 6;
		this.abilityButton = new Button(GameplayAssets.quickShotIcon, null, 0, 0);
	}	
}
