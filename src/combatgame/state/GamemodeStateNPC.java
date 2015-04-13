package combatgame.state;

import java.util.List;

import combatgame.input.TouchEvent;
import combatgame.main.StateManager;
import combatgame.objects.PartialMap;
import combatgame.widgets.Button;


public class GamemodeStateNPC extends GamemodeSelectionState {
	private static final long serialVersionUID = 1L;

	public GamemodeStateNPC(StateManager stateManager, PartialMap map) {
		super(stateManager, map);
	}

	@Override
	public int getStateID() {
		return State.NPC_GAMEMODE;
	}
	
	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		
		events = startButton.update(events);
		events = backButton.update(events);
		listview.update(events);
		selectedGamemode = listview.getSelectedGamemode();
		
		if(startButton.state == Button.ACTIVATED) {
			startButton.disarm();
			stateManager.setState(new NpcState(stateManager, map.getPath(), map.getTileset(), selectedGamemode));
		}
		else if(backButton.state == Button.ACTIVATED) {
			backButton.disarm();
			stateManager.setState(new MapStateNPC(stateManager));
		}
		else if(stateManager.isBackPressed()) {
			stateManager.setState(new MapSelectionState(stateManager));
		}
	}
}