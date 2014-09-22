package combatgame.main;

import combatgame.state.State;

public interface StateManager {

	public void setState(State state);
	public State getCurrentState();
	public State getInitialState();
	
}
