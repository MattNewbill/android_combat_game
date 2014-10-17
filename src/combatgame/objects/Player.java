package combatgame.objects;

import java.util.List;
import android.util.Log;
import combatgame.assets.GameplayAssets;
import combatgame.graphics.*;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.util.*;
import combatgame.widgets.Button;
import combatgame.widgets.UnitInfoDrawableButton;

/**
 * **NOT HAPPY**
 * Search for all the TODO's in this file, I've outlined what needs to be implemented
 * If you see something that looks stupid, it probably is and should be changed
 * 
 */

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//-- X ALWAYS CORRESPONDS TO COLUMNS AND Y ALWAYS CORRESPONDS TO ROWS
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Player {

	//used for determining what base to spawn in/attack
	boolean isPlayerOne;
	
	private int playerId;
	
	//action
	public static final int SELECTION = 0;
	public static final int MOVEMENT = 1;
	public static final int CHOOSE_ABILITY = 2;
	public static final int USE_ABILITY = 3;
	private int currentAction = SELECTION;
	
	//movement state
	private GPoint[][] movementPoints;
	
	//gameplay state
	private boolean isSetupPhase = true;
	
	//gameplay objects
	private Unit[] units;
	private int selectedUnitIndex = 0;
	private Map map;
	
	//hud icons
	private UnitInfoDrawableButton unitInfoButton;
	private Button moveButton;
	private Button abilityButton;
	private Button deselectButton;
	private Button endTurnButton;
	
	//determine presses vs scrolls
	private TouchEvent previousEvent;
	private GPoint previousTouchDownTile;
	
	public Player(boolean isPlayerOne, Map map, int numUnits) {
		this.isPlayerOne = isPlayerOne;
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
		unitInfoButton = new UnitInfoDrawableButton(GameplayAssets.unitInfoIcon, null, unitInfoButtonX, unitInfoButtonY);
		moveButton = new Button(GameplayAssets.moveIcon, null, moveButtonX, moveButtonY);
		abilityButton = new Button(GameplayAssets.abilityIcon, null, abilityButtonX, abilityButtonY);
		deselectButton = new Button(GameplayAssets.deselectIcon, null, deselectButtonX, deselectButtonY);
		endTurnButton = new Button(GameplayAssets.endTurnIcon, null, endTurnButtonX, endTurnButtonY);
	}
	
	public void update(List<TouchEvent> events) {
		
		//----------------------------------------
		//--UPDATE BUTTON STATES--
		//----------------------------------------
		
		//first thing we do is update all the hud buttons so the user can't select stuff through the hud
		events = unitInfoButton.update(events);
		events = moveButton.update(events);
		events = abilityButton.update(events);
		events = deselectButton.update(events);
		events = endTurnButton.update(events);
		
		//----------------------------------------
		//--SET UP PHASE--
		//----------------------------------------
		
		//if we are in the setup phase then only update the setup phase method
		if(isSetupPhase) {
			updateSetupPhase(events);
			return;
		}
		
		//now that the setup phase is over we have to start implementing the actual game logic
		//----------------------------------------
		//--TURN BASED GAME LOGIC--
		//----------------------------------------
		
		//update the unit info button's text
		if(selectedUnitIndex != -1)
			unitInfoButton.updateTextInfo(units[selectedUnitIndex]);
		
		//what action is the player currently doing
		switch(currentAction) {
			case SELECTION:
				selection(events);
				break;
			case MOVEMENT:
				movement(events);
				break;
			case CHOOSE_ABILITY:
				abilities(events);
				break;
			case USE_ABILITY:
				useAbility(events);
				break;
			default:
				throw new IllegalArgumentException("no such action");
		}
		
		//----------------------------------------
		//--CHECK BUTTON STATES--
		//----------------------------------------
		
		//check if hud icons were pressed
		if(unitInfoButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "unit info activated");
			map.scrollToTile(units[selectedUnitIndex].getXYCoordinate());
			unitInfoButton.disarm();
		}
		//TODO if a unit is selected this needs to call the Map to determine where this guy can move
		if(moveButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "move button activated");
			//if the movement button has already been selected, then we deselect it
			if(currentAction == MOVEMENT)
				currentAction = SELECTION;
			else
				currentAction = MOVEMENT;
			moveButton.disarm();
		}
		//TODO if a unit is selected this needs to pull up the "drop-up" menu to show the last of abilities
		if(abilityButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "ability button activated");
			//currentAction = ABILITY;
			abilityButton.disarm();
		}
		//deselected the current unit
		if(deselectButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "deselect button activated");
			deselectButton.disarm();
			selectedUnitIndex = -1; //deselect the unit
			currentAction = SELECTION;
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
			GPoint tile = getTileTouched(events);
			//only let player one spawn in player one's base
			if(isPlayerOne) {
				if(tile != null && map.getTile(tile).getFeatureType() == MapFeature.PLAYER_ONE_BASE && !map.getTile(tile).hasUnit()) {
					units[selectedUnitIndex].setXYCoordinate(tile, map);
					selectedUnitIndex++;
				}
			}
			//only let player two spawn in player two's base
			else {
				if(tile != null && map.getTile(tile).getFeatureType() == MapFeature.PLAYER_TWO_BASE && !map.getTile(tile).hasUnit()) {
					units[selectedUnitIndex].setXYCoordinate(tile, map);
					selectedUnitIndex++;
				}
			}
		}
	}
	
	/**
	 * Loop through the events to check if a tile was touched by the player
	 * @param events List of TouchEvents to loop through
	 * @return Return a point specifying what tile was touched
	 */
	public GPoint getTileTouched(List<TouchEvent> events) {
		GPoint pointTouched = null;
		boolean isUsed = false;
		//loop through our TouchEvents
		for(int i = 0; i < events.size(); i++) {
			//record the last time we touched down on the screen
			if(events.get(i).type == TouchEvent.TOUCH_DOWN)
				previousTouchDownTile = map.getTileTouched(events.get(i));
			//if our previousEvent pointer is null then copy the first TouchEvent into it
			if(previousEvent == null) {
				previousEvent = new TouchEvent();
				previousEvent.copy(events.get(0));
			}
			//if the previous touch event was TOUCH_DOWN and the current event is TOUCH_UP then the user clicked a tile
			if(events.get(i).type == TouchEvent.TOUCH_UP &&
			   previousEvent.type == TouchEvent.TOUCH_DOWN ||
			   (previousTouchDownTile != null && events.get(i).type == TouchEvent.TOUCH_UP &&
			   map.getTileTouched(events.get(i)).col == previousTouchDownTile.col &&
			   map.getTileTouched(events.get(i)).row == previousTouchDownTile.row)) {
				//call the map to get the exact tile that was touched
				pointTouched = map.getTileTouched(events.get(i));
				isUsed = true; //we used this touch event
				if(pointTouched != null) {
					Log.i("combatgame", "row: " + pointTouched.row);
					Log.i("combatgame", "col: " + pointTouched.col);
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
	
	////////////////////////////////////////////
	////PLAYER IS SELECTING UNIT
	////////////////////////////////////////////
	private void selection(List<TouchEvent> events) {
		//check what tile is pressed by the user
		GPoint tileTouched = getTileTouched(events); //get the tile touched by the user
		if(tileTouched != null) {
			GPoint tile = null;
			boolean isUnitSelected = false;
			for(int i = 0; i < units.length; i++) { //loop through our units to see if we touched one
				tile = units[i].getXYCoordinate();
				if(tile.col == tileTouched.col && tile.row == tileTouched.row) { //if we did touch it, set that unit as the currently selected unit
					movementPoints = null;
					selectedUnitIndex = i;
					isUnitSelected = true;
					enableButtons();
					break;
				}
			}
			//if none of our units were selected then we deselect the currently selected unit
			if(!isUnitSelected) {
				selectedUnitIndex = -1;
				disableButtons();
			}
		}
	}
	
	////////////////////////////////////////////
	////PLAYER IS MOVING A SELECTED UNIT
	////////////////////////////////////////////
	private void movement(List<TouchEvent> events) {
		if(units[selectedUnitIndex].getPointsLeft() == 0) {
			currentAction = SELECTION;
			return;
		}
		movementPoints = Movement.getMovement(map, units[selectedUnitIndex]);
		GPoint tileTouched = getTileTouched(events);
		if(tileTouched != null) {
			//check to see if we selected a movement tile
			for(int row = 1; row < movementPoints.length; row++) {
				for(int col = 0; col < movementPoints[row].length; col++) {
					if(tileTouched.col == movementPoints[row][col].col && tileTouched.row == movementPoints[row][col].row) {
						units[selectedUnitIndex].setXYCoordinate(tileTouched, map);
						units[selectedUnitIndex].usePoints(row * units[selectedUnitIndex].getMovementCost());
						return;
					}
				}
			}
			GPoint unitTile;
			//check to see if we selected one of our units instead
			for(int i = 0; i < units.length; i++) {
				//if we're talking about the currently selected unit, skip him
				if(i == selectedUnitIndex)
					continue;
				unitTile = units[i].getXYCoordinate();
				if(tileTouched.row == unitTile.row && tileTouched.col == unitTile.col) {
					selectedUnitIndex = i;
					return;
				}
			}
			currentAction = SELECTION;
		}
		
	}
	
	////////////////////////////////////////////
	////PLAYER IS SELECTING A UNIT'S ABILITY TO USE
	////////////////////////////////////////////
	private void abilities(List<TouchEvent> events) {
		Ability[] abilities = units[selectedUnitIndex].getAbilities();
		for(int i = 0; i < abilities.length; i++) {
			abilities[i].update(events);
		}
	}
	
	////////////////////////////////////////////
	////PLAYER IS USING A UNIT'S ABILITY
	////////////////////////////////////////////
	private void useAbility(List<TouchEvent> events) {
		
	}
	
	//TODO rendering needs to consult the map to see what can/can not be seen by the current player
	//get a "glow" effect going for selected players as well as positions they can move to and the action points that it costs to move them there
	public void render(Graphics2D g) {
		//---------------------------------------
		//--Render spawn overlays in our base, but only during setup phase
		//---------------------------------------
		if(isSetupPhase && selectedUnitIndex != units.length) {
			for(int row = 0; row < map.getNum_vertical_tiles(); row++) {
				for(int col = 0; col < map.getNum_horizontal_tiles(); col++) {
					if(isPlayerOne) {
						if(map.getTile(row, col).getFeatureType() == MapFeature.PLAYER_ONE_BASE && !map.getTile(row, col).hasUnit())
							g.drawBitmap(GameplayAssets.selectionOverlay, col * map.getTileWidthInPx() - map.getMapOffsetX(), row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
					}
					else {
						if(map.getTile(row, col).getFeatureType() == MapFeature.PLAYER_TWO_BASE && !map.getTile(row, col).hasUnit())
							g.drawBitmap(GameplayAssets.selectionOverlay, col * map.getTileWidthInPx() - map.getMapOffsetX(), row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
					}
				}
			}
		}
		
		//---------------------------------------
		//--Render movement overlays if we are trying to move a unit
		//---------------------------------------
		if(currentAction == MOVEMENT && movementPoints != null) {
			for(int row = 1; row < movementPoints.length; row++) {
				for(int col = 0; col < movementPoints[row].length; col++) {
					g.drawBitmap(GameplayAssets.selectionOverlay, movementPoints[row][col].col * map.getTileWidthInPx() - map.getMapOffsetX(), movementPoints[row][col].row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
				}
			}
		}
		
		//---------------------------------------
		//--Render ability buttons if the user has pressed the "ability" button
		//---------------------------------------
		if(currentAction == CHOOSE_ABILITY) {
			Ability[] abilities = units[selectedUnitIndex].getAbilities();
			for(int i = 0; i < abilities.length; i++) {
				//abilities.render(g, 50, Game.P_HEIGHT - abilityButton.getHeight() - (abilities.length * GameplayAssets.basicAttack.getHeight()));
			}
		}
		
		//---------------------------------------
		//--Render our units on the map
		//---------------------------------------
		for(int i = 0; i < units.length; i++) {
			GPoint coordinate = units[i].getXYCoordinate();
			if(!units[i].isDead() && coordinate != null) {
				//draw the selection overlay if we have this unit selected
				if(i == selectedUnitIndex)
					g.drawBitmap(GameplayAssets.selectionOverlay, coordinate.col * map.getTileWidthInPx() - map.getMapOffsetX(), coordinate.row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
				
				g.drawBitmap(units[i].getSprite(), coordinate.col * map.getTileWidthInPx() - map.getMapOffsetX(), coordinate.row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
			}
		}
		
		//---------------------------------------
		//--Render our HUD at the very end
		//---------------------------------------
		unitInfoButton.render(g);
		moveButton.render(g);
		abilityButton.render(g);
		deselectButton.render(g);
		endTurnButton.render(g);
	}
	
	public void dispose() {
		
	}
	
	//TODO construct a light map for this player determining which tiles are visible and which aren't
	public boolean[][] constructLightMap(boolean[][] lightmap) {
		for(int i = 0; i < units.length; i++) {
			if(units[i].getXYCoordinate() != null) {
				List<GPoint> lightPoints = Vision.getVision(map, units[i]);
				for(int k = 0; k < lightPoints.size(); k++) {
					lightmap[lightPoints.get(k).row][lightPoints.get(k).col] = true;
				}
			}
		}
		return lightmap;
	}
	
	//our turn has just now started
	public void newTurn() {
		//if it's the setup phase then scroll to our base
		if(isSetupPhase)
			for(int row = 0; row < map.getNum_vertical_tiles(); row++) {
				for(int col = 0; col < map.getNum_horizontal_tiles(); col++) {
					if(isPlayerOne) {
						if(map.getTile(row, col).getFeatureType() == MapFeature.PLAYER_ONE_BASE) {
							map.scrollToTile(new GPoint(row, col));
							return;
						}
					}
					else
						if(map.getTile(row, col).getFeatureType() == MapFeature.PLAYER_TWO_BASE) {
							Log.i("combatgame", "row: " + row + ", col: " + col);
							map.scrollToTile(new GPoint(row, col));
							return;
						}
				}
			}
		//otherwise scroll to the selected unit (if there is no selected unit, pick a new unit to be selected and scroll to him), reset action points
		else {
			for(int i = 0; i < units.length; i++)
				units[i].resetPoints();
			
			currentAction = SELECTION;
			if(selectedUnitIndex != -1 && !units[selectedUnitIndex].isDead()) {
				map.scrollToTile(units[selectedUnitIndex].getXYCoordinate());
			}
			else {
				for(int i = 0; i < units.length; i++) {
					if(!units[i].isDead()) {
						enableButtons();
						selectedUnitIndex = i;
						map.scrollToTile(units[selectedUnitIndex].getXYCoordinate());
					}
				}
			}
		}
	}
	
	public int getId() {
		return playerId;
	}
	
	//disable the hud buttons (generally if no unit is selected)
	private void disableButtons() {
		unitInfoButton.disable();
		moveButton.disable();
		abilityButton.disable();
		deselectButton.disable();
	}
	
	//enable the hud buttons (generally when a player has just selected one of their units)
	private void enableButtons() {
		unitInfoButton.enable();
		moveButton.enable();
		abilityButton.enable();
		deselectButton.enable();
	}
}
