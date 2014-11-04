package combatgame.units;

import combatgame.widgets.*;
import combatgame.graphics.*;
import combatgame.input.*;
import combatgame.objects.Map;
import combatgame.objects.Unit;

import java.util.List;
import android.graphics.Point;

public abstract class Ability {

	protected int abilityCost;
	protected String name;
	
	protected Button abilityButton;
	
	public Ability(int abilityCost) {
		this.abilityCost = abilityCost;
	}
	
	public abstract List<GPoint> getTilesAttackable(Unit unit, Map map);
	public abstract List<Attack> getTilesAffected(GPoint tile);
	public void renderButton(Graphics2D g, int x, int y) {
		abilityButton.render(g, x, y);
	}
	
	public int getCost() {
		return abilityCost;
	}
	
	public String getName() {
		return name;
	}
	
	public Button getButton() {
		return abilityButton;
	}
}
