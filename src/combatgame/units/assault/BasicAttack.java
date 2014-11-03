package combatgame.units.assault;

import combatgame.graphics.*;
import combatgame.input.*;
import combatgame.units.Ability;

import java.util.List;

public class BasicAttack extends Ability {

	public BasicAttack(int cost) {
		super(cost);
		this.name = "Shoot";
	}
	
	@Override
	public void update(List<TouchEvent> eventst) {
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
	}
	
}
