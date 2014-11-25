package combatgame.units.assault;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.AttackedTile;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class RifleFire extends BasicAttack {

	public RifleFire() {
		this.damage = 25;
		this.abilityCost = 4;
		this.abilityButton = new Button(GameplayAssets.rifleFireIcon, null, 0, 0);
	}

	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, 
			GPoint tileTouched, Map map) {
		// TODO Auto-generated method stub
		return null;
	}
}
