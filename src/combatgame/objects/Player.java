package combatgame.objects;

import combatgame.state.*;
import combatgame.main.*;
import combatgame.assets.*;
import combatgame.input.*;
import combatgame.graphics.*;
import combatgame.widgets.*;
import java.util.List;
import android.util.Log;
import android.graphics.Point;

/**
 * **NOT HAPPY**
 * TODO: Not a fan of how the setup phase is handled or how units are created/added or how unit selection is handled.
 * Basically, I hate this class as it currently stands.  Definitely need feedback on how this is going to work out.
 * A lot of this stuff should be temporary stuff for the demo but I'd ultimately like to dump most of this code
 * and start over with input from the rest of the team.
 * @author Matt Hamersky
 *
 */
public class Player {

	private GameState state;
	private int playerId;
	
	//gameplay state
	private boolean isSetupPhase = true; //TODO
	
	//gameplay objects
	private Unit[] units;
	private int selectedUnitIndex = 0; //TODO
	private Map map;
	
	//hud icons
	private Button endTurnButton;
	
	//determine presses vs scrolls
	private TouchEvent previousEvent;
	
	public Player(GameState state, int numUnits, Map map) {
		this.state = state;
		playerId = (int)System.currentTimeMillis(); //id generated is for test purposes, eventually it will be better...
		
		units = new Unit[numUnits];
		this.map = map;
		
		//just adding stock units for test purposes
		units[0] = new Assault(playerId); //TODO
		units[1] = new Assault(playerId); //TODO
		units[2] = new Sniper(playerId); //TODO
		
		//create hud buttons
		int endTurnButtonX = Game.P_WIDTH - GameplayAssets.endTurnIcon.getWidth();
		int endTurnButtonY = Game.P_HEIGHT - GameplayAssets.endTurnIcon.getHeight();
		endTurnButton = new Button(GameplayAssets.endTurnIcon, null, endTurnButtonX, endTurnButtonY);
	}
	
	public void update(List<TouchEvent> events) {
		//TODO
		if(isSetupPhase) {
			updateSetupPhase(events);
			return;
		}
		
		//send touch events to hud icons
		events = endTurnButton.update(events);
		
		//check if hud icons were pressed
		if(endTurnButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "end turn activated");
			endTurnButton.disarm();
			state.switchTurn();
		}
	}
	
	//TODO
	private void updateSetupPhase(List<TouchEvent> events) {
		if(selectedUnitIndex == units.length) {
			events = endTurnButton.update(events);
			if(endTurnButton.state == Button.ACTIVATED) {
				endTurnButton.disarm();
				isSetupPhase = false;
				selectedUnitIndex = 0;
				state.switchTurn();
			}
		}
		else {
			Point tile = getTileTouched(events);
			if(tile != null) {
				units[selectedUnitIndex].setXYCoordinate(tile);
				selectedUnitIndex++;
			}
		}
	}
	
	//TODO
	public Point getTileTouched(List<TouchEvent> events) {
		Point pointTouched = null;
		for(int i = 0; i < events.size(); i++) {
			if(previousEvent == null) {
				previousEvent = new TouchEvent();
				previousEvent.copy(events.get(0));
			}
			if(events.get(i).type == TouchEvent.TOUCH_UP &&
			   previousEvent.type == TouchEvent.TOUCH_DOWN) {
				pointTouched = map.getTile(events.get(i));
				if(pointTouched != null) {
					Log.i("combatgame", "x: " + pointTouched.x);
					Log.i("combatgame", "y: " + pointTouched.y);
				}
				else {
					Log.i("combatgame", "out of bounds");
				}
			}
			previousEvent.copy(events.get(i));
		}
		return pointTouched;
	}
	
	//TODO
	public void render(Graphics2D g) {
		//render units
		for(int i = 0; i < units.length; i++) {
			Point coordinate = units[i].getXYCoordinate();
			if(!units[i].isDead() && coordinate != null) {
				g.drawBitmap(units[i].getSprite(), coordinate.x * map.getTileWidthInPx() - map.getMapOffsetX(), coordinate.y * map.getTileHeightInPx() - map.getMapOffsetY(), null);
			}
		}
		endTurnButton.render(g);
	}
	
	public int getId() {
		return playerId;
	}
}
