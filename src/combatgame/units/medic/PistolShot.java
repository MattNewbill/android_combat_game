package combatgame.units.medic;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.AttackedTile;
import combatgame.units.BasicAttack;
import combatgame.widgets.Button;

public class PistolShot extends BasicAttack {
	
	public PistolShot() {
		this.damage = 10;
		this.abilityCost = 3;
		this.abilityButton = new Button(GameplayAssets.pistolShotIcon, null, 0, 0); //TODO: change sprites
	}

	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking, 
			GPoint tileTouched, Map map) {
		// TODO Auto-generated method stub
		return null;
	}
}
