package combatgame.main;

import combatgame.input.TouchHandler;
import combatgame.main.*;
import combatgame.state.State;

public interface StateManager {

	public void setState(State state);
	public State getCurrentState();
	public State getInitialState();
	public android.content.res.AssetManager getAssetManager();
	public TouchHandler getTouchHandler();
	public void shouldScale(boolean shouldScale);
	public boolean isScaled();
	
}
