package combatgame.units;

import combatgame.widgets.*;
import combatgame.graphics.*;
import combatgame.input.*;
import java.util.List;
import android.graphics.Point;

public abstract class Ability {

	protected int abilityCost;
	protected String name;
	
	protected Button abilityButton;
	
	public Ability(int abilityCost) {
		this.abilityCost = abilityCost;
	}
	
	public abstract void update(List<TouchEvent> events);
	public abstract void render(Graphics2D g);
	public void renderButton(Graphics2D g, int x, int y) {
		abilityButton.render(g, x, y);
	}
	
	public int getCost() {
		return abilityCost;
	}
	
	public String getName() {
		return name;
	}
}
