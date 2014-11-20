package combatgame.units.cqc;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.units.AttackedTile;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class WideShot extends BasicAttack {

	public WideShot() {
		this.damage = 50;
		this.abilityCost = 4;
		this.abilityButton = new Button(GameplayAssets.wideShotIcon, null, 0, 0);
	}
	
	
	@Override
	public List<AttackedTile> getTilesAffected(GPoint tile, Map map) {
		attackedTiles.clear();
		return null;
	}
	//50 damage to tile attacked
	//30 damage to direct left and right tiles
}
