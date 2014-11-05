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
		//passed a list of events and check to see if what attack what state what tile
	}
	
	@Override
	public void render(Graphics2D g) {
		//render overlay fo red outline
		//
	}
	
}
