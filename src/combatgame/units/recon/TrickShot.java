package combatgame.units.recon;

import java.util.List;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.widgets.Button;

public class TrickShot extends Ability {

	public TrickShot() {
		this.damage = 15;
		this.abilityCost = 4;
		this.abilityButton = new Button(GameplayAssets.trickShotIcon, null, 0, 0);
	}
	
	@Override
	public List<GPoint> getTilesAttackable(Unit unit, Map map) {
		tilesAttackable.clear();
		return null;
	}

	@Override
	public List<AttackedTile> getTilesAffected(Unit UnitWhoIsAttacking,  GPoint tile, Map map) {
		attackedTiles.clear();
		return null;
	}
}
