package combatgame.objects;

import combatgame.util.Util;
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
 * Search for all the TODO's in this file, I've outlined what needs to be implemented
 * If you see something that looks stupid, it probably is and should be changed
 * 
 */
public class Player {

	private int playerId;
	
	//gameplay state
	private boolean isSetupPhase = true;
	
	//gameplay objects
	private Unit[] units;
	private int selectedUnitIndex = 0;
	private Map map;
	
	//hud icons
	private Button unitInfoButton;
	private Button moveButton;
	private Button abilityButton;
	private Button deselectButton;
	private Button endTurnButton;
	
	//determine presses vs scrolls
	private TouchEvent previousEvent;
	
	public Player(Map map, int numUnits) {
		playerId = (int)System.currentTimeMillis() + Util.getRand(); //TODO potentially get a better ID system going...
		
		units = new Unit[numUnits];
		this.map = map;
		
		//just adding stock units for test purposes
		units[0] = new Assault(playerId);
		units[1] = new Assault(playerId);
		units[2] = new Sniper(playerId);
		
		//coordinates of each hud button
		int unitInfoButtonX = (Game.P_WIDTH / 2) - (2 * GameplayAssets.unitInfoIcon.getWidth()) - GameplayAssets.unitInfoIcon.getWidth() / 2;
		int unitInfoButtonY = Game.P_HEIGHT - GameplayAssets.unitInfoIcon.getHeight();
		int moveButtonX = (Game.P_WIDTH / 2) - GameplayAssets.moveIcon.getWidth() - GameplayAssets.moveIcon.getWidth() / 2;
		int moveButtonY = Game.P_HEIGHT - GameplayAssets.moveIcon.getHeight();
		int abilityButtonX = (Game.P_WIDTH / 2) - (GameplayAssets.abilityIcon.getWidth() / 2);
		int abilityButtonY = Game.P_HEIGHT - GameplayAssets.abilityIcon.getHeight();
		int deselectButtonX = (Game.P_WIDTH / 2) + (GameplayAssets.deselectIcon.getWidth() / 2);
		int deselectButtonY = Game.P_HEIGHT - GameplayAssets.deselectIcon.getHeight();
		int endTurnButtonX = (Game.P_WIDTH / 2) + (GameplayAssets.endTurnIcon.getWidth() / 2) + GameplayAssets.endTurnIcon.getWidth();
		int endTurnButtonY = Game.P_HEIGHT - GameplayAssets.endTurnIcon.getHeight();
		
		//create buttons
		unitInfoButton = new Button(GameplayAssets.unitInfoIcon, null, unitInfoButtonX, unitInfoButtonY);
		moveButton = new Button(GameplayAssets.moveIcon, null, moveButtonX, moveButtonY);
		abilityButton = new Button(GameplayAssets.abilityIcon, null, abilityButtonX, abilityButtonY);
		deselectButton = new Button(GameplayAssets.deselectIcon, null, deselectButtonX, deselectButtonY);
		endTurnButton = new Button(GameplayAssets.endTurnIcon, null, endTurnButtonX, endTurnButtonY);
	}
	
	public void update(List<TouchEvent> events) {
		//first thing we do is update all the hud buttons so the user can't select stuff through the hud
		events = unitInfoButton.update(events);
		events = moveButton.update(events);
		events = abilityButton.update(events);
		events = deselectButton.update(events);
		events = endTurnButton.update(events);
		
		//if we are in the setup phase then only update the setup phase method
		if(isSetupPhase) {
			updateSetupPhase(events);
			return;
		}
		
		//TODO
		//now that the setup phase is over we have to start implementing the actual game logic
		//
		//check what tile is pressed by the user, using the getTile(events)
		//method will give you the tile that the player pressed (if they in fact pressed a tile)
		//if that tile has a unit, check if it's ours, if it's ours, enable the hud buttons
		
		
		//check if hud icons were pressed
		//TODO if a unit is selected this needs to scroll to that unit
		if(unitInfoButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "unit info activated");
			unitInfoButton.disarm();
		}
		//TODO if a unit is selected this needs to call the Map to determine where this guy can move
		if(moveButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "move button activated");
			moveButton.disarm();
		}
		//TODO if a unit is selected this needs to pull up the "drop-up" menu to show the last of abilities
		if(abilityButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "ability button activated");
			abilityButton.disarm();
		}
		//deselected the current unit
		if(deselectButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "deselect button activated");
			deselectButton.disarm();
			selectedUnitIndex = -1; //deselect the unit
			disableButtons(); //disable hud buttons
		}
		//ends the turn
		//TODO add a check to see if all units have used up all their action points, if they aren't used up then give a warning to the player
		if(endTurnButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "end turn activated");
			endTurnButton.disarm();
			map.switchTurn();
		}
	}
	
	//only runs during the setup phase
	//setup phase consists of unit placement
	private void updateSetupPhase(List<TouchEvent> events) {
		if(selectedUnitIndex == units.length) {
			events = endTurnButton.update(events);
			if(endTurnButton.state == Button.ACTIVATED) {
				endTurnButton.disarm();
				isSetupPhase = false;
				selectedUnitIndex = 0;
				map.switchTurn();
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
	
	/**
	 * Loop through the events to check if a tile was touched by the player
	 * @param events List of TouchEvents to loop through
	 * @return Return a point specifying what tile was touched
	 */
	public Point getTileTouched(List<TouchEvent> events) {
		Point pointTouched = null;
		boolean isUsed = false;
		//loop through our TouchEvents
		for(int i = 0; i < events.size(); i++) {
			//if our previousEvent pointer is null then copy the first TouchEvent into it
			if(previousEvent == null) {
				previousEvent = new TouchEvent();
				previousEvent.copy(events.get(0));
			}
			//if the previous touch event was TOUCH_DOWN and the current event is TOUCH_UP then the user clicked a tile
			if(events.get(i).type == TouchEvent.TOUCH_UP &&
			   previousEvent.type == TouchEvent.TOUCH_DOWN) {
				//call the map to get the exact tile that was touched
				pointTouched = map.getTile(events.get(i));
				isUsed = true; //we used this touch event
				if(pointTouched != null) {
					Log.i("combatgame", "x: " + pointTouched.x);
					Log.i("combatgame", "y: " + pointTouched.y);
				}
				//the point is null if the point touched was out of bounds
				else {
					Log.i("combatgame", "out of bounds");
				}
			}
			//we did not use this particular touch event
			else {
				isUsed = false;
			}
			//copy the current event into the previous event pointer
			previousEvent.copy(events.get(i));
			 //we used the touch event, now remove it from the list
			if(isUsed)
				events.remove(i);
		}
		return pointTouched;
	}
	
	//TODO rendering needs to consult the map to see what can/can not be seen by the current player
	//get a "glow" effect going for selected players as well as positions they can move to and the action points that it costs to move them there
	public void render(Graphics2D g) {
		//render units
		for(int i = 0; i < units.length; i++) {
			Point coordinate = units[i].getXYCoordinate();
			if(!units[i].isDead() && coordinate != null) {
				g.drawBitmap(units[i].getSprite(), coordinate.x * map.getTileWidthInPx() - map.getMapOffsetX(), coordinate.y * map.getTileHeightInPx() - map.getMapOffsetY(), null);
			}
		}
		
		//draw the hud buttons
		unitInfoButton.render(g);
		moveButton.render(g);
		abilityButton.render(g);
		deselectButton.render(g);
		endTurnButton.render(g);
	}
	
	public void dispose() {
		
	}
	
	public int getId() {
		return playerId;
	}
	
	private void disableButtons() {
		unitInfoButton.disable();
		moveButton.disable();
		abilityButton.disable();
		deselectButton.disable();
	}
	
	private void enableButtons() {
		unitInfoButton.enable();
		moveButton.enable();
		abilityButton.enable();
		deselectButton.enable();
	}
}
