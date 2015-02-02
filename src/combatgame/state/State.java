package combatgame.state;

import java.io.Serializable;

import android.content.Context;
import combatgame.main.*;
import combatgame.graphics.*;

public abstract class State implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final int MAIN_MENU = 0;
	public static final int ABOUT = 1;
	public static final int CONNECTION = 2;
	public static final int BLUETOOTH = 3;
	public static final int INTERNET = 4;
	public static final int HOT_SEAT = 5;
	public static final int MAP_SELECTION = 6;
	public static final int GAMEMODE_SELECTION = 7;
	
	transient StateManager stateManager;
	
	public State(StateManager stateManager) {
		this.stateManager = stateManager;
	}
	
	public abstract void update(float delta);
	public abstract void render(Graphics2D g, float delta);
	public abstract void pause(Context context, boolean saveData);
	public abstract void resume(StateManager stateManager);
	public void dispose() {
		stateManager = null;
	}
	public abstract int getStateID();
	
}
