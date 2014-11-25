package combatgame.units.recon;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.AttackedTile;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class StraightShot extends BasicAttack {

	public StraightShot() {
		this.damage = 15;
		this.abilityCost = 3;
		this.abilityButton = new Button(GameplayAssets.straightShotIcon, null, 0, 0);
	}

	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, 
			GPoint tileTouched, Map map) {
		// TODO Auto-generated method stub
		return null;
	}
}
