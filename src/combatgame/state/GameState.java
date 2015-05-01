package combatgame.state;

import combatgame.main.StateManager;

public abstract class GameState extends State {

	private static final long serialVersionUID = 1L;

	public GameState(StateManager stateManager) {
		super(stateManager);
	}
	
	public abstract void gameover();

	public abstract boolean getCheckWin();
	
	public StateManager getStateManager() {
		return stateManager;
	}

}
