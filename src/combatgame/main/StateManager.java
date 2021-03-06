package combatgame.main;

import combatgame.input.TouchHandler;
import combatgame.state.State;

public interface StateManager {

	public void setState(State state);
	public State getCurrentState();
	public State getInitialState();
	public android.content.res.AssetManager getAssetManager();
	public TouchHandler getTouchHandler();
	public boolean isBackPressed();
	public android.app.Activity getActivity();
	
}
