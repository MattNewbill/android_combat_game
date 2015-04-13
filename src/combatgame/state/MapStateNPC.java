package combatgame.state;

import java.util.List;

import combatgame.input.TouchEvent;
import combatgame.main.StateManager;
import combatgame.widgets.Button;



public class MapStateNPC extends MapSelectionState {
	private static final long serialVersionUID = 1L;
	
	public MapStateNPC(StateManager stateManager) {
		super(stateManager);
	}
	
	@Override
	public int getStateID() {
		return State.NPC_MAP;
	}
	
	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		events = nextButton.update(events);
		events = backButton.update(events);
		listView.update(events);
		
		String temp = listView.getSelectedItem();
		if(!temp.equals(selectedMap)) {
			selectedMap = temp;
			selectedPartialMap = listView.getSelectedMap();
		}
		
		if(nextButton.state == Button.ACTIVATED) {
			nextButton.disarm();
			stateManager.setState(new GamemodeStateNPC(stateManager, selectedPartialMap));
		}
		else if(backButton.state == Button.ACTIVATED) {
			backButton.disarm();
			stateManager.setState(new ConnectionState(stateManager));
		}
		else if(stateManager.isBackPressed()) {
			stateManager.setState(new ConnectionState(stateManager));
		}
	}
}