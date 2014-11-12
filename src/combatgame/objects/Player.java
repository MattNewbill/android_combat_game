package combatgame.objects;

import java.util.LinkedList;
import java.util.List;
import android.util.Log;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import combatgame.assets.GameplayAssets;
import combatgame.graphics.*;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.units.assault.Assault;
import combatgame.units.sniper.Sniper;
import combatgame.util.*;
import combatgame.widgets.Button;
import combatgame.widgets.UnitInfoDrawableButton;

/**
 * **NOT HAPPY**
 * If you see something that looks stupid, it probably is and should be changed
 * 
 */

public class Player {

	//used for determining what base to spawn in/attack
	boolean isPlayerOne;
	
	private String gamertag;
	private int playerId;
	
	//action
	public static final int SELECTION = 0;
	public static final int CHOOSE_MOVEMENT = 1;
	public static final int USE_MOVEMENT = 2;
	public static final int CHOOSE_ABILITY = 3;
	public static final int USE_ABILITY = 4;
	public static final int SPAWN_UNIT = 5;
	private int currentAction = SELECTION;
	
	//movement state
	private GPoint[][] movementPoints;
	private List<GPoint> attackableTiles;
	
	//gameplay state
	private boolean isSetupPhase = true;
	
	//if the player has selected an ability to use, this is the one he is using
	private Ability currentAbility;
	
	//gameplay objects
	private Unit[] units;
	private int selectedUnitIndex = 0;
	private int spawnUnitIndex = 0; //index of next unit that needs to be spawned in (setup phase)
	private Map map;
	
	//hud icons
	private UnitInfoDrawableButton unitInfoButton;
	private Button moveButton;
	private Button abilityButton;
	private Button deselectButton;
	private Button endTurnButton;
	
	private Button spawnUnitButton;
	
	//damage indicators
	private Paint indicatorPaint;
	private List<HealthIndicator> indicators = new LinkedList<HealthIndicator>();
	
	//movement hud icons
	private Button movementButton;
	private Button leftRotateButton;
	private Button rightRotateButton;
	
	//determine presses vs scrolls
	private TouchEvent previousEvent;
	private GPoint previousTouchDownTile;
	
	public Player(String gamertag, boolean isPlayerOne, Map map, int numUnits) {
		this.gamertag = gamertag;
		this.isPlayerOne = isPlayerOne;
		playerId = (int)System.currentTimeMillis() + Util.getRand(); //TODO potentially get a better ID system going...
		
		units = new Unit[numUnits];
		this.map = map;
		
		//just adding stock units for test purposes
		units[0] = new Assault(playerId);
		units[1] = new Assault(playerId);
		units[2] = new Sniper(playerId);
		
		//indicator font
		indicatorPaint = new Paint();
		indicatorPaint.setTextSize(40);
		indicatorPaint.setTextAlign(Align.CENTER);
		indicatorPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		
		//coordinates of each hud button
		int width, height;
		if(Game.isScaled()) {
			width = Game.G_WIDTH; height = Game.G_HEIGHT;
		}
		else {
			width = Game.P_WIDTH; height = Game.P_HEIGHT;
		}
		
		int unitInfoButtonX = (width / 2) - (2 * GameplayAssets.unitInfoIcon.getWidth()) - GameplayAssets.unitInfoIcon.getWidth() / 2;
		int unitInfoButtonY = height - GameplayAssets.unitInfoIcon.getHeight();
		int moveButtonX = (width / 2) - GameplayAssets.moveIcon.getWidth() - GameplayAssets.moveIcon.getWidth() / 2;
		int moveButtonY = height - GameplayAssets.moveIcon.getHeight();
		int abilityButtonX = (width / 2) - (GameplayAssets.abilityIcon.getWidth() / 2);
		int abilityButtonY = height - GameplayAssets.abilityIcon.getHeight();
		int deselectButtonX = (width / 2) + (GameplayAssets.deselectIcon.getWidth() / 2);
		int deselectButtonY = height - GameplayAssets.deselectIcon.getHeight();
		int endTurnButtonX = (width / 2) + (GameplayAssets.endTurnIcon.getWidth() / 2) + GameplayAssets.endTurnIcon.getWidth();
		int endTurnButtonY = height - GameplayAssets.endTurnIcon.getHeight();
		
		int rightRotateButtonX = moveButtonX;
		int rightRotateButtonY = moveButtonY - GameplayAssets.rightRotateIcon.getHeight();
		int leftRotateButtonX = moveButtonX;
		int leftRotateButtonY = rightRotateButtonY - GameplayAssets.leftRotateIcon.getHeight();
		int movementButtonX = moveButtonX;
		int movementButtonY = leftRotateButtonY - GameplayAssets.movementIcon.getHeight();
		
		int spawnUnitButtonX = (width / 2) - (GameplayAssets.abilityIcon.getWidth() / 2);
		int spawnUnitButtonY = height - GameplayAssets.abilityIcon.getHeight();
		
		//create buttons
		unitInfoButton = new UnitInfoDrawableButton(new Paint(), GameplayAssets.unitInfoIcon, null, unitInfoButtonX, unitInfoButtonY);
		moveButton = new Button(GameplayAssets.moveIcon, null, moveButtonX, moveButtonY);
		abilityButton = new Button(GameplayAssets.abilityIcon, null, abilityButtonX, abilityButtonY);
		deselectButton = new Button(GameplayAssets.deselectIcon, null, deselectButtonX, deselectButtonY);
		endTurnButton = new Button(GameplayAssets.endTurnIcon, null, endTurnButtonX, endTurnButtonY);
		
		movementButton = new Button(GameplayAssets.movementIcon, null, movementButtonX, movementButtonY);
		leftRotateButton = new Button(GameplayAssets.leftRotateIcon, null, leftRotateButtonX, leftRotateButtonY);
		rightRotateButton = new Button(GameplayAssets.rightRotateIcon, null, rightRotateButtonX, rightRotateButtonY);
		
		spawnUnitButton = new Button(GameplayAssets.spawnUnitIcon, null, spawnUnitButtonX, spawnUnitButtonY);
		
		disableButtons();
		endTurnButton.disable();
	}
	
	public void update(List<TouchEvent> events) {
		
		//----------------------------------------
		//--SET UP PHASE--
		//----------------------------------------
		if(isSetupPhase) {
			updateSetupPhase(events);
		}
		//----------------------------------------
		//--TURN PHASE--
		//----------------------------------------
		else {
			updateTurnPhase(events);
		}
	}
	
	//only runs during the setup phase
	//setup phase consists of unit placement
	private void updateSetupPhase(List<TouchEvent> events) {
		
		//----------------------------------------
		//--UPDATE BUTTON STATES--
		//----------------------------------------
		events = moveButton.update(events);
		events = spawnUnitButton.update(events);
		events = deselectButton.update(events);
		if(spawnUnitIndex == units.length) {
			endTurnButton.enable();
			events = endTurnButton.update(events);
			if(endTurnButton.state == Button.ACTIVATED) {
				endTurnButton.disarm();
				isSetupPhase = false;
				selectedUnitIndex = 0;
				map.switchTurn();
			}
		}
		
		//what action is the player currently doing
		switch(currentAction) {
			case SELECTION:
				selection(events);
				break;
			case CHOOSE_MOVEMENT:
				chooseMovementSetup(events);
				break;
			case SPAWN_UNIT:
				spawnUnit(events);
				break;
			default:
				throw new IllegalArgumentException("no such action");
		}
		
		if(moveButton.state == Button.ACTIVATED) {
			moveButton.disarm();
			if(selectedUnitIndex != -1) {
				if(currentAction == CHOOSE_MOVEMENT)
					currentAction = SELECTION;
				else
					currentAction = CHOOSE_MOVEMENT;
			}
		}
		
		if(spawnUnitButton.state == Button.ACTIVATED) {
			spawnUnitButton.disarm();
			if(currentAction == SPAWN_UNIT)
				currentAction = SELECTION;
			else
				currentAction = SPAWN_UNIT;
		}
		
		//deselected the current unit
		if(deselectButton.state == Button.ACTIVATED) {
			deselectButton.disarm();
			selectedUnitIndex = -1; //deselect the unit
			currentAction = SELECTION;
			disableButtons(); //disable hud buttons
		}
	}
	
	private void updateTurnPhase(List<TouchEvent> events) {
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
		//--UPDATE INDICATORS--
		//----------------------------------------
		for(int i = 0; i < indicators.size(); i++) {
			indicators.get(i).update();
			if(indicators.get(i).isFinished()) {
				indicators.remove(i);
				i--;
			}
		}
		
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
			case CHOOSE_MOVEMENT:
				chooseMovementTurn(events);
				break;
			case USE_MOVEMENT:
				useMovement(events);
				break;
			case CHOOSE_ABILITY:
				chooseAbilities(events);
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
			map.moveToTile(units[selectedUnitIndex].getXYCoordinate());
			unitInfoButton.disarm();
		}
		if(moveButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "move button activated");
			//if the movement button has already been selected, then we deselect it
			if(currentAction == CHOOSE_MOVEMENT)
				currentAction = SELECTION;
			else
				currentAction = CHOOSE_MOVEMENT;
			moveButton.disarm();
		}
		//TODO if a unit is selected this needs to pull up the "drop-up" menu to show the last of abilities
		if(abilityButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "ability button activated");
			if(currentAction == CHOOSE_ABILITY)
				currentAction = SELECTION;
			else
				currentAction = CHOOSE_ABILITY;
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
	
	/**
	 * Loop through the events to check if a tile was touched by the player
	 * @param events List of TouchEvents to loop through
	 * @return Return a point specifying what tile was touched
	 */
	public GPoint getTileTouched(List<TouchEvent> events) {
		GPoint pointTouched = null;
		boolean isUsed = false;
		//sometimes getting null pointer exceptions for reasons unknown (haha, the killers)
		try {
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
				   map.getTileTouched(events.get(i)).equals(previousTouchDownTile))) {
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		return pointTouched;
	}
	
	////////////////////////////////////////////
	////PLAYER IS ATTEMPTING TO SPAWN A UNIT
	////////////////////////////////////////////
	private void spawnUnit(List<TouchEvent> events) {
		selectedUnitIndex = -1;
		disableButtons();
		GPoint tile = getTileTouched(events);
		//only let player one spawn in player one's base
		if(isPlayerOne) {
			if(tile != null && map.getTile(tile).getFeatureType() == MapFeature.PLAYER_ONE_BASE && !map.getTile(tile).hasUnit()) {
				units[spawnUnitIndex].setXYCoordinate(tile, map);
				selectedUnitIndex = spawnUnitIndex;
				spawnUnitIndex++;
				currentAction = SELECTION;
				enableButtons();
			}
		}
		//only let player two spawn in player two's base
		else {
			if(tile != null && map.getTile(tile).getFeatureType() == MapFeature.PLAYER_TWO_BASE && !map.getTile(tile).hasUnit()) {
				units[spawnUnitIndex].setXYCoordinate(tile, map);
				selectedUnitIndex = spawnUnitIndex;
				spawnUnitIndex++;
				currentAction = SELECTION;
				enableButtons();
			}
		}
		if(spawnUnitIndex == units.length)
			spawnUnitButton.disable();
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
				if(tile != null && tile.equals(tileTouched)) { //if we did touch it, set that unit as the currently selected unit
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
	private void useMovement(List<TouchEvent> events) {
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
					if(tileTouched.equals(movementPoints[row][col])) {
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
				if(tileTouched.equals(unitTile)) {
					selectedUnitIndex = i;
					return;
				}
			}
			currentAction = SELECTION;
		}
		
	}
	
	////////////////////////////////////////////
	////PLAYER IS ALLOWED TO ROTATE A SPAWNED UNIT DURING SETUP
	////////////////////////////////////////////
	private void chooseMovementSetup(List<TouchEvent> events) {
		events = leftRotateButton.update(events);
		events = rightRotateButton.update(events);
		
		if(leftRotateButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "left rotate button pressed!");
			units[selectedUnitIndex].rotateLeft();
			leftRotateButton.disarm();
		}
		if(rightRotateButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "right rotate button pressed!");
			units[selectedUnitIndex].rotateRight();
			rightRotateButton.disarm();
		}
		//if we touch something other than the movement buttons then exit out of the movement state
		for(int i = 0; i < events.size(); i++) {
			GPoint tileTouched = getTileTouched(events);
			if(tileTouched != null) {
				currentAction = SELECTION;
			}
		}
	}
	
	////////////////////////////////////////////
	////PLAYER IS SELECTING HOW TO MOVE A SELECTED UNIT DURING ACTUAL TURN
	////////////////////////////////////////////
	private void chooseMovementTurn(List<TouchEvent> events) {
		events = movementButton.update(events);
		events = leftRotateButton.update(events);
		events = rightRotateButton.update(events);
		
		if(movementButton.state == Button.ACTIVATED) {
			currentAction = USE_MOVEMENT;
			movementButton.disarm();
		}
		if(leftRotateButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "left rotate button pressed!");
			if(units[selectedUnitIndex].getPointsLeft() >= units[selectedUnitIndex].getRotationCost()) {
				units[selectedUnitIndex].rotateLeft();
				units[selectedUnitIndex].usePoints(units[selectedUnitIndex].getRotationCost());
			}
			else {
				currentAction = SELECTION;
			}
			leftRotateButton.disarm();
		}
		if(rightRotateButton.state == Button.ACTIVATED) {
			Log.i("combatgame", "right rotate button pressed!");
			if(units[selectedUnitIndex].getPointsLeft() >= units[selectedUnitIndex].getRotationCost()) {
				units[selectedUnitIndex].rotateRight();
				units[selectedUnitIndex].usePoints(units[selectedUnitIndex].getRotationCost());
			}
			else {
				currentAction = SELECTION;
			}
			rightRotateButton.disarm();
		}
		//if we touch something other than the movement buttons then exit out of the movement state
		for(int i = 0; i < events.size(); i++) {
			GPoint tileTouched = getTileTouched(events);
			if(tileTouched != null) {
				currentAction = SELECTION;
			}
		}
	}
	
	////////////////////////////////////////////
	////PLAYER IS SELECTING A UNIT'S ABILITY TO USE
	////////////////////////////////////////////
	private void chooseAbilities(List<TouchEvent> events) {
		Ability[] abilities = units[selectedUnitIndex].getAbilities();
		for(int i = 0; i < abilities.length; i++) {
			Button ability = abilities[i].getButton();
			events = ability.update(events);
			if(ability.state == Button.ACTIVATED) {
				ability.disarm();
				//if the unit can't afford this ability then go back to the selection state
				if(units[selectedUnitIndex].getPointsLeft() < abilities[i].getCost()) {
					currentAction = SELECTION;
				}
				else {
					currentAbility = abilities[i];
					currentAction = USE_ABILITY;
				}
			}
		}
		//if we touch something other than the movement buttons then exit out of the movement state
		for(int i = 0; i < events.size(); i++) {
			GPoint tileTouched = getTileTouched(events);
			if(tileTouched != null) {
				currentAction = SELECTION;
			}
		}
	}
	
	////////////////////////////////////////////
	////PLAYER IS USING A UNIT'S ABILITY
	////////////////////////////////////////////
	private void useAbility(List<TouchEvent> events) {
		//get the tiles that can be attacked
		attackableTiles = currentAbility.getTilesAttackable(units[selectedUnitIndex], map);
		
		GPoint tileTouched = getTileTouched(events);
		if(tileTouched != null) {
			//loop through events to see if we pressed one of the attackable tiles
			for(int i = 0; i < attackableTiles.size(); i++) {
				if(tileTouched.equals(attackableTiles.get(i))) {
					//decrement action points
					units[selectedUnitIndex].usePoints(currentAbility.getCost());
					//get the tiles that were affected by the attack
					List<AttackedTile> tilesAffected = currentAbility.getTilesAffected(tileTouched, map);
					for(int j = 0; j < tilesAffected.size(); j++) {
						MapTile tile = map.getTile(tilesAffected.get(j).tile);
						if(tile.hasUnit()){//there is a unit on the tile
							int unitId = tile.getUnit_id();
							Unit unit = map.getUnit(unitId);
							//reduce unit health by attack dmg
							if(unit != null) {
								int damageDone = unit.takeDamage(tilesAffected.get(j).damageTaken, map);
								indicators.add(new HealthIndicator(map, new GPoint(tileTouched.row, tileTouched.col), damageDone, indicatorPaint, Color.RED));
							}
						}
					}
				}
			}
			currentAction = SELECTION;
		}
	}
	
	public void render(Graphics2D g) {
		//---------------------------------------
		//--Render spawn overlays in our base, but only during setup phase
		//---------------------------------------
		if(isSetupPhase && spawnUnitIndex != units.length && currentAction == SPAWN_UNIT) {
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
		if(currentAction == USE_MOVEMENT && movementPoints != null) {
			for(int row = 1; row < movementPoints.length; row++) {
				for(int col = 0; col < movementPoints[row].length; col++) {
					g.drawBitmap(GameplayAssets.selectionOverlay, movementPoints[row][col].col * map.getTileWidthInPx() - map.getMapOffsetX(), movementPoints[row][col].row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
					g.drawBitmap(GameplayAssets.numberOverlays[row], movementPoints[row][col].col * map.getTileWidthInPx() - map.getMapOffsetX(), movementPoints[row][col].row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
				}
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
		//--Render ability overlays if the user is trying to use one
		//---------------------------------------
		if(currentAction == USE_ABILITY && attackableTiles != null) {
			for(int i = 0; i < attackableTiles.size(); i++) {
				GPoint tile = attackableTiles.get(i);
				g.drawBitmap(GameplayAssets.attackOverlay, tile.col * map.getTileWidthInPx() - map.getMapOffsetX(), tile.row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
			}
		}
		
		//---------------------------------------
		//--Render any indicators that may be active
		//---------------------------------------
		for(int i = 0; i < indicators.size(); i++) {
			indicators.get(i).render(g);
		}
		
		//---------------------------------------
		//--Render our turn HUD at the very end
		//---------------------------------------
		if(!isSetupPhase) {
			unitInfoButton.render(g);
			moveButton.render(g);
			abilityButton.render(g);
			deselectButton.render(g);
			endTurnButton.render(g);
		}
		
		//---------------------------------------
		//--Render spawn hud if we are in the setup phase
		//---------------------------------------
		if(isSetupPhase) {
			unitInfoButton.render(g);
			moveButton.render(g);
			spawnUnitButton.render(g);
			deselectButton.render(g);
			endTurnButton.render(g);
		}
		
		//---------------------------------------
		//--Render rotate buttons if the user has pressed the "move" button during setup phase
		//---------------------------------------
		if(currentAction == CHOOSE_MOVEMENT) {
			leftRotateButton.render(g);
			rightRotateButton.render(g);
		}
		
		//---------------------------------------
		//--Render movement buttons if the user has pressed the "move" button during an actual turn
		//---------------------------------------
		if(currentAction == CHOOSE_MOVEMENT && !isSetupPhase) {
			movementButton.render(g);
			leftRotateButton.render(g);
			rightRotateButton.render(g);
		}
		
		//---------------------------------------
		//--Render ability buttons if the user has pressed the "ability" button
		//---------------------------------------
		if(currentAction == CHOOSE_ABILITY) {
			//TODO: render ability buttons for the player to choose
			Ability[] abilities = units[selectedUnitIndex].getAbilities();
			for(int i = 0; i < abilities.length; i++) {
				abilities[i].renderButton(g, abilityButton.getX(), abilityButton.getY() - ((abilities.length-i) * GameplayAssets.basicAttackIcon.getHeight()));
			}
		}
		
		//reset overlays
		movementPoints = null;
		attackableTiles = null;
	}
	
	////////////////////////////////////////////
	////RENDER OUR UNITS IF THEY ARE VISIBLE BY THE OTHER PLAYER
	////////////////////////////////////////////
	public void renderVisibleUnits(Graphics2D g, boolean[][] lightmap) {
		for(int i = 0; i < units.length; i++) {
			GPoint coordinate = units[i].getXYCoordinate();
			if(!units[i].isDead() && coordinate != null && lightmap[coordinate.row][coordinate.col])
				g.drawBitmap(units[i].getSprite(), coordinate.col * map.getTileWidthInPx() - map.getMapOffsetX(), coordinate.row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
		}
	}
	
	public void dispose() {
		
	}
	
	public boolean[][] constructLightMap(boolean[][] lightmap) {
		//our base is always visible, so find out where our base currently is
		for(int row = 0; row < map.getNum_vertical_tiles(); row++) {
			for(int col = 0; col < map.getNum_horizontal_tiles(); col++) {
				if(isPlayerOne && map.getTile(row, col).getFeatureType() == MapFeature.PLAYER_ONE_BASE)
					lightmap[row][col] = true;
				else if(!isPlayerOne && map.getTile(row, col).getFeatureType() == MapFeature.PLAYER_TWO_BASE)
					lightmap[row][col] = true;	
			}
		}
		//get the lightmaps for all of our units
		for(int i = 0; i < units.length; i++) {
			if(!units[i].isDead() && units[i].getXYCoordinate() != null) {
				List<GPoint> lightPoints = Vision.getSprintVision(map, units[i]);
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
							map.moveToTile(new GPoint(row, col));
							return;
						}
					}
					else
						if(map.getTile(row, col).getFeatureType() == MapFeature.PLAYER_TWO_BASE) {
							Log.i("combatgame", "row: " + row + ", col: " + col);
							map.moveToTile(new GPoint(row, col));
							return;
						}
				}
			}
		//otherwise scroll to the selected unit (if there is no selected unit, pick a new unit to be selected and scroll to him), reset action points
		else {
			indicators.clear(); //remove any indicators if we had any
			for(int i = 0; i < units.length; i++)
				units[i].resetPoints();
			
			currentAction = SELECTION;
			if(selectedUnitIndex != -1 && !units[selectedUnitIndex].isDead()) {
				map.moveToTile(units[selectedUnitIndex].getXYCoordinate());
			}
			else {
				for(int i = 0; i < units.length; i++) {
					if(!units[i].isDead()) {
						enableButtons();
						selectedUnitIndex = i;
						map.moveToTile(units[selectedUnitIndex].getXYCoordinate());
					}
				}
			}
		}
	}
	
	public Unit[] getUnits() {
		return units;
	}
	
	public int getId() {
		return playerId;
	}
	public String getGamertag() {
		return gamertag;
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

	public Unit getUnit(int unitId) {
		for ( int i = 0; i < units.length; i++)
			if(units[i].getUnit_id() == unitId)
				return units[i];
		return null;
	}
}
