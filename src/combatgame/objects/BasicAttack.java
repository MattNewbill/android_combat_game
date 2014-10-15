package combatgame.objects;

import combatgame.graphics.*;
import combatgame.input.*;
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
