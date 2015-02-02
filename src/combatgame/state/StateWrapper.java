package combatgame.state;

import java.io.Serializable;

public class StateWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	private int state;
	
	public StateWrapper(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
}
