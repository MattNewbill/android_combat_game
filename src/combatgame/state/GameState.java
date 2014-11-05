package combatgame.state;

import combatgame.main.StateManager;
import combatgame.objects.Player;

public abstract class GameState extends State {

	public GameState(StateManager stateManager) {
		super(stateManager);
	}
	
	public abstract void gameover();

}
