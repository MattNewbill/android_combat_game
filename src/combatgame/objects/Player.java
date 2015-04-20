package combatgame.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import combatgame.alerts.APNotification;
import combatgame.alerts.HealthIndicator;
import combatgame.alerts.HitIndicator;
import combatgame.alerts.MoveNotification;
import combatgame.alerts.Notification;
import combatgame.assets.GameplayAssets;
import combatgame.graphics.*;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.util.*;
import combatgame.widgets.Button;
import combatgame.widgets.UnitInfoDrawableButton;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	//used for determining what base to spawn in/attack
	boolean isPlayerOne;
	
	protected String gamertag;
	protected int playerId;
	
	//action
	public static final int SELECTION = 0;
	public static final int CHOOSE_MOVEMENT = 1;
	public static final int USE_MOVEMENT = 2;
	public static final int CHOOSE_ABILITY = 3;
	public static final int USE_ABILITY = 4;
	public static final int SPAWN_UNIT = 5;
	public static final int RESPAWN_UNIT = 6;
	protected int currentAction = SELECTION;
	
	//movement state
	protected GPoint[][] movementPoints;
	protected List<GPoint> attackableTiles;
	
	//gameplay state
	protected boolean isSetupPhase = true;
	
	//if the player has selected an ability to use, this is the one he is using
	protected Ability currentAbility;
	
	//gameplay objects
	protected Unit[] units;
	protected int selectedUnitIndex = -1;
	protected int spawnUnitIndex = 0; //index of next unit that needs to be spawned in (setup phase)
	protected Unit enemyUnitSelected = null; // enemy unit we currently have selected
	
	protected Map map;
	
	//hud icons
	protected transient UnitInfoDrawableButton unitInfoButton;
	protected transient Button moveButton;
	protected transient Button abilityButton;
	protected transient Button deselectButton;
	protected transient Button endTurnButton;
	
	protected transient Button spawnUnitButton;
	protected transient Button respawnUnitButton;
	
	//damage indicators/notifications
	protected transient Paint indicatorPaint;
	protected List<HealthIndicator> healthIndicators = new LinkedList<HealthIndicator>();
	protected List<HitIndicator> hitIndicators = new ArrayList<HitIndicator>();
	protected List<Notification> notifications = new ArrayList<Notification>();
	
	//movement hud icons
	protected transient Button movementButton;
	protected transient Button leftRotateButton;
	protected transient Button rightRotateButton;
	protected transient Button leftRotateSetupButton;
	protected transient Button rightRotateSetupButton;
	
	//confirm end turn data
	protected transient Button yesButton;
	protected transient Button noButton;
	protected boolean isEndingTurn = false;
	
	//determine presses vs scrolls
	protected TouchEvent previousEvent;
	protected TouchEvent previousTouchDownEvent;
	protected GPoint previousTouchDownTile;
	public static final int INELIGIBLE_TOUCH_DISTANCE = 10;
	
	//attack and heal overlays on the attackable tiles
	protected transient Paint attackOverlayPaint;
	protected transient Paint healOverlayPaint;
	
	public Player(String gamertag, boolean isPlayerOne, Map map, Unit[] units) {
		this.gamertag = gamertag;
		this.isPlayerOne = isPlayerOne;
		playerId = (int)System.currentTimeMillis() + Util.getRand(); //TODO potentially get a better ID system going...
		
		this.map = map;
		this.units = units;
		for(int i = 0; i < units.length; i++) {
			units[i].setPlayer_id(playerId);
		}
		Tooltip.reset();
	}
	
	public List<TouchEvent> update(List<TouchEvent> events) {
		
		//----------------------------------------
		//--TOOLTIP ACTIVE--
		//----------------------------------------
		events = Tooltip.updateEvents(events);
		
		//----------------------------------------
		//--SET UP PHASE--
		//----------------------------------------
		if(isSetupPhase) {
			events = updateSetupPhase(events);
		}
		//----------------------------------------
		//--TURN PHASE--
		//----------------------------------------
		else {
			events = updateTurnPhase(events);
		}
		
		return events;
	}
	
	//only runs during the setup phase
	//setup phase consists of unit placement
	protected List<TouchEvent> updateSetupPhase(List<TouchEvent> events) {
		
		Tooltip.showTooltip( isPlayerOne, Tooltip.PLACE_UNIT);
		
		//----------------------------------------
		//--UPDATE BUTTON STATES--
		//----------------------------------------
		if((spawnUnitIndex == -1 || selectedUnitIndex == -1) || currentAction == SPAWN_UNIT)
			respawnUnitButton.disable();
		else
			if(respawnUnitButton.state == Button.DISABLED){
				respawnUnitButton.enable();
				Tooltip.showTooltip( isPlayerOne, Tooltip.EDIT_UNIT);
			}
		
		events = unitInfoButton.update(events);
		events = moveButton.update(events);
		events = spawnUnitButton.update(events);
		events = respawnUnitButton.update(events);
		if(spawnUnitIndex == units.length) {
			if(endTurnButton.state == Button.DISABLED){
				endTurnButton.enable();
				Tooltip.showTooltip( isPlayerOne, Tooltip.END_TURN);
			}
			events = endTurnButton.update(events);
			if(endTurnButton.state == Button.ACTIVATED) {
				endTurnButton.disarm();
				isSetupPhase = false;
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
			case RESPAWN_UNIT:
				respawnUnit(events);
				break;
			default:
				throw new IllegalArgumentException("no such action");
		}
		
		if(unitInfoButton.state == Button.ACTIVATED) {
			map.moveToTile(units[selectedUnitIndex].getXYCoordinate());
			unitInfoButton.disarm();
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
			if(currentAction == SPAWN_UNIT) {
				currentAction = SELECTION;
				selectedUnitIndex = -1;
			}
			else {
				currentAction = SPAWN_UNIT;
			}
		}
		
		if(respawnUnitButton.state == Button.ACTIVATED) {
			respawnUnitButton.disarm();
			if(currentAction == RESPAWN_UNIT)
				currentAction = SELECTION;
			else
				currentAction = RESPAWN_UNIT;
		}
		
		if(selectedUnitIndex != -1)
			unitInfoButton.updateTextSetupInfo(units[selectedUnitIndex]);
		return events;
	}
	
	protected List<TouchEvent> updateTurnPhase(List<TouchEvent> events) {
		if(isEndingTurn) {
			events = yesButton.update(events);
			events = noButton.update(events);
			
			if(yesButton.state == Button.ACTIVATED) {
				yesButton.disarm();
				hitIndicators.clear(); //remove any hit indicators
				map.switchTurn();
				isEndingTurn = false;
			}
			if(noButton.state == Button.ACTIVATED) {
				noButton.disarm();
				isEndingTurn = false;
			}
			return events;
		}
		
		Tooltip.showTooltip( isPlayerOne, Tooltip.FIRST_TURN);
		
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
		for(int i = 0; i < healthIndicators.size(); i++) {
			healthIndicators.get(i).update();
			if(healthIndicators.get(i).isFinished()) {
				healthIndicators.remove(i);
				i--;
			}
		}
		for(int i = 0; i < notifications.size(); i++) {
			notifications.get(i).update();
			if(notifications.get(i).isFinished()) {
				notifications.remove(i);
				i--;
			}
		}
		
		//----------------------------------------
		//--TURN BASED GAME LOGIC--
		//----------------------------------------
		
		//update the unit info button's text
		if(selectedUnitIndex != -1)
			unitInfoButton.updateTextInfo(units[selectedUnitIndex]);
		if(enemyUnitSelected != null)
			unitInfoButton.updateEnemyTextInfo(enemyUnitSelected);
		
		//what action is the player currently doing
		switch(currentAction) {
			case SELECTION:
				selection(events);
				break;
			case CHOOSE_MOVEMENT:
				chooseMovementTurn(events);
				Tooltip.showTooltip( isPlayerOne, Tooltip.MOVE_ALL);
				break;
			case USE_MOVEMENT:
				useMovement(events);
				Tooltip.showTooltip( isPlayerOne, Tooltip.MOVE_UNIT);
				break;
			case CHOOSE_ABILITY:
				chooseAbilities(events);
				if(units[selectedUnitIndex].getUnitType() == Unit.UnitType.ASSAULT)
					Tooltip.showTooltip( isPlayerOne, Tooltip.ASSAULT_ATTACKS);
				if(units[selectedUnitIndex].getUnitType() == Unit.UnitType.SNIPER)
					Tooltip.showTooltip( isPlayerOne, Tooltip.SNIPER_ATTACKS);
				if(units[selectedUnitIndex].getUnitType() == Unit.UnitType.RECON)
					Tooltip.showTooltip( isPlayerOne, Tooltip.RECON_ATTACKS);
				if(units[selectedUnitIndex].getUnitType() == Unit.UnitType.CQC)
					Tooltip.showTooltip( isPlayerOne, Tooltip.JUGGERNAUT_ATTACKS);
				if(units[selectedUnitIndex].getUnitType() == Unit.UnitType.MEDIC)
					Tooltip.showTooltip( isPlayerOne, Tooltip.MEDIC_ATTACKS);
				break;
			case USE_ABILITY:
				useAbility(events);
				Tooltip.showTooltip( isPlayerOne, Tooltip.ATTACK_UNIT);
				break;
			default:
				throw new IllegalArgumentException("no such action");
		}
		
		//----------------------------------------
		//--CHECK BUTTON STATES--
		//----------------------------------------
		
		//check if hud icons were pressed
		if(unitInfoButton.state == Button.ACTIVATED) {
			map.moveToTile(units[selectedUnitIndex].getXYCoordinate());
			unitInfoButton.disarm();
		}
		if(moveButton.state == Button.ACTIVATED) {
			//if the movement button has already been selected, then we deselect it
			if(currentAction == CHOOSE_MOVEMENT)
				currentAction = SELECTION;
			else
				currentAction = CHOOSE_MOVEMENT;
			moveButton.disarm();
		}
		if(abilityButton.state == Button.ACTIVATED) {
			if(currentAction == CHOOSE_ABILITY)
				currentAction = SELECTION;
			else
				currentAction = CHOOSE_ABILITY;
			abilityButton.disarm();
		}
		//deselected the current unit
		if(deselectButton.state == Button.ACTIVATED) {
			deselectButton.disarm();
			selectedUnitIndex = -1; //deselect the unit
			enemyUnitSelected = null;
			currentAction = SELECTION;
			disableButtons(); //disable hud buttons
		}
		//ends the turn
		if(endTurnButton.state == Button.ACTIVATED) {
			//TODO: add a "are you sure you want to finish your turn" dialog thingy
			isEndingTurn = true;
			endTurnButton.disarm();
		}	
		return events;
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
				if(events.get(i).type == TouchEvent.TOUCH_DOWN) {
					previousTouchDownEvent = events.get(i);
					previousTouchDownTile = map.getTileTouched(events.get(i));
				}
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
				}
				//we did not use this particular touch event
				else {
					isUsed = false;
				}
				//if we scrolled more than 50 pixels in either direction then we can no longer press down on a tile
				if(events.get(i).type == TouchEvent.TOUCH_DRAGGED) {
					if(previousTouchDownEvent != null) {
						TouchEvent ev = events.get(i);
						if(ev.x - previousTouchDownEvent.x > INELIGIBLE_TOUCH_DISTANCE ||
						   previousTouchDownEvent.x - ev.x > INELIGIBLE_TOUCH_DISTANCE ||
						   ev.y - previousTouchDownEvent.y > INELIGIBLE_TOUCH_DISTANCE ||
						   previousTouchDownEvent.y - ev.y > INELIGIBLE_TOUCH_DISTANCE) {
							previousTouchDownTile = null; previousTouchDownEvent = null;
						}
					}
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
	protected void spawnUnit(List<TouchEvent> events) {
		disableButtons();
		selectedUnitIndex = spawnUnitIndex;
		
		GPoint tile = getTileTouched(events);
		if(tile != null) {
			//only let player one spawn in player one's base
			if(isPlayerOne) {
				if(map.getTile(tile).getFeatureType() == MapFeature.PLAYER_ONE_BASE && !map.getTile(tile).hasUnit()) {
					units[spawnUnitIndex].setXYCoordinate(tile, map);
					int defaultDirection = getDefaultDirection(units[spawnUnitIndex].getXYCoordinate());
					units[spawnUnitIndex].setDirectionFacing(defaultDirection);
					selectedUnitIndex = spawnUnitIndex;
					spawnUnitIndex++;
					currentAction = SELECTION;
					enableButtons();
					if(spawnUnitIndex == units.length)
						spawnUnitButton.disable();
					return;
				}
			}
			//only let player two spawn in player two's base
			else {
				if(map.getTile(tile).getFeatureType() == MapFeature.PLAYER_TWO_BASE && !map.getTile(tile).hasUnit()) {
					units[spawnUnitIndex].setXYCoordinate(tile, map);
					int defaultDirection = getDefaultDirection(units[spawnUnitIndex].getXYCoordinate());
					units[spawnUnitIndex].setDirectionFacing(defaultDirection);
					selectedUnitIndex = spawnUnitIndex;
					spawnUnitIndex++;
					currentAction = SELECTION;
					enableButtons();
					if(spawnUnitIndex == units.length)
						spawnUnitButton.disable();
					return;
				}
			}
			currentAction = SELECTION;
			selectedUnitIndex = -1;
		}
	}
	
	protected int getDefaultDirection(GPoint currentUnitPosition) {
		int spacesRightOfUnit = map.num_horizontal_tiles - currentUnitPosition.col - 1;
		int spacesLeftOfUnit = map.num_horizontal_tiles - spacesRightOfUnit - 1;
		
		int spacesBelowUnit = map.num_vertical_tiles - currentUnitPosition.row - 1;
		int spacesAboveUnit = map.num_vertical_tiles - spacesBelowUnit - 1;
		
		int maxDistance = Math.max(spacesAboveUnit, Math.max(spacesBelowUnit, Math.max(spacesRightOfUnit,spacesLeftOfUnit)));
		int direction = Unit.FACING_DOWN;//shouldn't ever default to this
		
		if(maxDistance == spacesRightOfUnit)
			direction = Unit.FACING_RIGHT;
		else if(maxDistance == spacesLeftOfUnit)
			direction = Unit.FACING_LEFT;
		else if(maxDistance == spacesAboveUnit)
			direction = Unit.FACING_UP;
		else if(maxDistance == spacesBelowUnit)
			direction = Unit.FACING_DOWN;
		
		return direction;
	}

	////////////////////////////////////////////
	////PLAYER IS ATTEMPTING TO RE-SPAWN A UNIT
	////////////////////////////////////////////
	protected void respawnUnit(List<TouchEvent> events) {
		GPoint tile = getTileTouched(events);
		//only let player one spawn in player one's base
		if(isPlayerOne) {
			if(tile != null && map.getTile(tile).getFeatureType() == MapFeature.PLAYER_ONE_BASE) {
				if(!map.getTile(tile).hasUnit() || map.getTile(tile).getUnit_id() == units[selectedUnitIndex].getUnit_id()) {
					units[selectedUnitIndex].setXYCoordinate(tile, map);
					currentAction = SELECTION;
					enableButtons();
				}
			}
		}
		//only let player two spawn in player two's base
		else {
			if(tile != null && map.getTile(tile).getFeatureType() == MapFeature.PLAYER_TWO_BASE) {
				if(!map.getTile(tile).hasUnit() || map.getTile(tile).getUnit_id() == units[selectedUnitIndex].getUnit_id()) {
					units[selectedUnitIndex].setXYCoordinate(tile, map);
					currentAction = SELECTION;
					enableButtons();
				}
			}
		}
		if(spawnUnitIndex == units.length)
			spawnUnitButton.disable();
	}
	
	////////////////////////////////////////////
	////PLAYER IS SELECTING UNIT
	////////////////////////////////////////////
	protected void selection(List<TouchEvent> events) {
		//check what tile is pressed by the user
		GPoint tileTouched = getTileTouched(events); //get the tile touched by the user
		if(tileTouched != null) {
			GPoint tile = null;
			boolean isUnitSelected = false;
			for(int i = 0; i < units.length; i++) { //loop through our units to see if we touched one
				tile = units[i].getXYCoordinate();
				if(tile != null && tile.equals(tileTouched) && !units[i].isDead()) { //if we did touch it, set that unit as the currently selected unit
					movementPoints = null;
					selectedUnitIndex = i;
					isUnitSelected = true;
					enemyUnitSelected = null;
					enableButtons();
					break;
				}
			}
			//check to see if we selected an enemy unit that was visible
			boolean[][] lightmap = map.getLightmap();
			for(int row = 0; row < lightmap.length; row++) {
				for(int col = 0; col < lightmap[0].length; col++) {
					if(lightmap[row][col] && tileTouched.row == row && tileTouched.col == col) {
						if(map.getTile(row, col).hasUnit() && map.getTile(row, col).getPlayer_id() != playerId) {
							enemyUnitSelected = map.getUnit(map.getTile(row, col).getUnit_id());
							unitInfoButton.disable();
							moveButton.disable();
							abilityButton.disable();
							deselectButton.enable();
							isUnitSelected = true;
							selectedUnitIndex = -1;
							break;
						}
					}
				}
			}
			//if none of our units were selected then we deselect the currently selected unit
			if(!isUnitSelected) {
				selectedUnitIndex = -1;
				enemyUnitSelected = null;
				disableButtons();
			}
		}
	}
	
	////////////////////////////////////////////
	////PLAYER IS MOVING A SELECTED UNIT
	////////////////////////////////////////////
	protected void useMovement(List<TouchEvent> events) {
		if(units[selectedUnitIndex].getPointsLeft()/units[selectedUnitIndex].getMovementCost() == 0) {
			currentAction = SELECTION;
			return;
		}
		movementPoints = Movement.getMovement(map, units[selectedUnitIndex]);
		
		//check to see if path is blocked
		if(movementPoints[1].length==0)
		{
			currentAction = SELECTION;
			notifications.add(new MoveNotification(map, units[selectedUnitIndex].getXYCoordinate(), 0, indicatorPaint));
		}
		
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
				if(tileTouched.equals(unitTile) && !units[i].isDead()) {
					selectedUnitIndex = i;
					movementPoints = null;
					return;
				}
			}
			currentAction = SELECTION;
		}
		
	}
	
	////////////////////////////////////////////
	////PLAYER IS ALLOWED TO ROTATE A SPAWNED UNIT DURING SETUP
	////////////////////////////////////////////
	protected void chooseMovementSetup(List<TouchEvent> events) {
		events = leftRotateSetupButton.update(events);
		events = rightRotateSetupButton.update(events);
		if(leftRotateSetupButton.state == Button.ACTIVATED) {
			units[selectedUnitIndex].rotateLeft();
			leftRotateSetupButton.disarm();
		}
		if(rightRotateSetupButton.state == Button.ACTIVATED) {
			units[selectedUnitIndex].rotateRight();
			rightRotateSetupButton.disarm();
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
	protected void chooseMovementTurn(List<TouchEvent> events) {
		events = movementButton.update(events);
		events = leftRotateButton.update(events);
		events = rightRotateButton.update(events);
		
		if(movementButton.state == Button.ACTIVATED) {
			if(units[selectedUnitIndex].getPointsLeft() >= units[selectedUnitIndex].getMovementCost()) {
				currentAction = USE_MOVEMENT;
			}
			else {
				notifications.add(new APNotification(map, units[selectedUnitIndex].getXYCoordinate(), 0, indicatorPaint));
				currentAction = SELECTION;
			}
			movementButton.disarm();
		}
		if(leftRotateButton.state == Button.ACTIVATED) {
			if(units[selectedUnitIndex].getPointsLeft() >= units[selectedUnitIndex].getRotationCost()) {
				units[selectedUnitIndex].rotateLeft();
				units[selectedUnitIndex].usePoints(units[selectedUnitIndex].getRotationCost());
			}
			else {
				notifications.add(new APNotification(map, units[selectedUnitIndex].getXYCoordinate(), 0, indicatorPaint));
				currentAction = SELECTION;
			}
			leftRotateButton.disarm();
		}
		if(rightRotateButton.state == Button.ACTIVATED) {
			if(units[selectedUnitIndex].getPointsLeft() >= units[selectedUnitIndex].getRotationCost()) {
				units[selectedUnitIndex].rotateRight();
				units[selectedUnitIndex].usePoints(units[selectedUnitIndex].getRotationCost());
			}
			else {
				notifications.add(new APNotification(map, units[selectedUnitIndex].getXYCoordinate(), 0, indicatorPaint));
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
	protected void chooseAbilities(List<TouchEvent> events) {
		Ability[] abilities = units[selectedUnitIndex].getAbilities();
		for(int i = 0; i < abilities.length; i++) {
			Button ability = abilities[i].getButton();
			events = ability.update(events);
			if(ability.state == Button.ACTIVATED) {
				ability.disarm();
				//if the unit can't afford this ability then go back to the selection state
				if(units[selectedUnitIndex].getPointsLeft() < abilities[i].getCost()) {
					notifications.add(new APNotification(map, units[selectedUnitIndex].getXYCoordinate(), 0, indicatorPaint));
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
	protected void useAbility(List<TouchEvent> events) {
		//get the tiles that can be attacked
		attackableTiles = currentAbility.getTilesAttackable(units[selectedUnitIndex], map);
		
		GPoint tileTouched = getTileTouched(events);
		if(tileTouched != null) {
			//loop through events to see if we pressed one of the attackable tiles
			for(int i = 0; i < attackableTiles.size(); i++) {
				if(tileTouched.equals(attackableTiles.get(i))) {
					if(currentAbility.isValidTileToAttack(units[selectedUnitIndex], tileTouched, map)) {
						//decrement action points
						units[selectedUnitIndex].usePoints(currentAbility.getCost());
						//get the tiles that were affected by the attack
						List<AttackedTile> tilesAffected = currentAbility.getTilesAffected(units[selectedUnitIndex],tileTouched, map);
						for(int j = 0; j < tilesAffected.size(); j++) {
							MapTile tile = map.getTile(tilesAffected.get(j).tile);
							if(tile.hasUnit()){//there is a unit on the tile
								int unitId = tile.getUnit_id();
								Unit unit = map.getUnit(unitId);
								//reduce unit health by attack dmg
								if(unit != null) {
									DamageDealt damageDone = unit.takeDamage(tilesAffected.get(j).damageTaken, map);
									healthIndicators.add(new HealthIndicator(map, new GPoint(tilesAffected.get(j).tile.row, tilesAffected.get(j).tile.col), 30, damageDone.healthDamage, indicatorPaint, false));
									if(damageDone.isAttack && damageDone.armorDamage < 0) {
										healthIndicators.add(new HealthIndicator(map, new GPoint(tilesAffected.get(j).tile.row, tilesAffected.get(j).tile.col), 0, damageDone.armorDamage, indicatorPaint, true));
									}
									if(units[selectedUnitIndex] != unit && damageDone.isAttack) //don't send indicator for heals
										map.sendHitIndicator(new HitIndicator(units[selectedUnitIndex], currentAbility, unit, tilesAffected.get(j).tile, map), unit);
								}
							}
						}
						break;
					}
				}
			} //end for loop
			currentAction = SELECTION;
		}
	}
	
	public void render(Graphics2D g) {
		//---------------------------------------
		//--Render spawn overlays in our base, but only during setup phase
		//---------------------------------------
		if(isSetupPhase && ((spawnUnitIndex != units.length && currentAction == SPAWN_UNIT) || currentAction == RESPAWN_UNIT)) {
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
					g.drawBitmap(GameplayAssets.numberOverlays[row * units[selectedUnitIndex].getMovementCost()], movementPoints[row][col].col * map.getTileWidthInPx() - map.getMapOffsetX(), movementPoints[row][col].row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
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
		
		if(enemyUnitSelected != null)
			g.drawBitmap(GameplayAssets.selectionOverlay, enemyUnitSelected.getXYCoordinate().col * map.getTileWidthInPx() - map.getMapOffsetX(), enemyUnitSelected.getXYCoordinate().row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
		
		//---------------------------------------
		//--Render ability overlays if the user is trying to use one
		//---------------------------------------
		if(currentAction == USE_ABILITY && attackableTiles != null) {
			for(int i = 0; i < attackableTiles.size(); i++) {
				GPoint tile = attackableTiles.get(i);
				if(currentAbility.getDamage() < 0) { //we are looking to heal someone so overlay the tiles with a green border
					g.drawRect(tile.col * map.getTileWidthInPx() - map.getMapOffsetX(), tile.row * map.getTileHeightInPx() - map.getMapOffsetY(), (tile.col+1) * map.getTileWidthInPx() - map.getMapOffsetX(), (tile.row+1) * map.getTileHeightInPx() - map.getMapOffsetY(), healOverlayPaint);
					if(currentAbility.isValidTileToAttack(units[selectedUnitIndex], attackableTiles.get(i), map))
						g.drawBitmap(GameplayAssets.healOverlay, tile.col * map.getTileWidthInPx() - map.getMapOffsetX(), tile.row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
				}
				else { //we are looking to attack someone so overlay the tiles with a red border
					g.drawRect(tile.col * map.getTileWidthInPx() - map.getMapOffsetX(), tile.row * map.getTileHeightInPx() - map.getMapOffsetY(), (tile.col+1) * map.getTileWidthInPx() - map.getMapOffsetX(), (tile.row+1) * map.getTileHeightInPx() - map.getMapOffsetY(), attackOverlayPaint);
					if(currentAbility.isValidTileToAttack(units[selectedUnitIndex], attackableTiles.get(i), map))
						g.drawBitmap(GameplayAssets.attackOverlay, tile.col * map.getTileWidthInPx() - map.getMapOffsetX(), tile.row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
				}
			}
		}
		
		//---------------------------------------
		//--Render any indicators that may be active
		//---------------------------------------
		for(int i = 0; i < hitIndicators.size(); i++) {
			hitIndicators.get(i).render(g);
			Tooltip.showTooltip( isPlayerOne, Tooltip.DAMAGED_UNIT);
		}
		for(int i = 0; i < healthIndicators.size(); i++) {
			healthIndicators.get(i).render(g);
		}
		for(int i = 0; i < notifications.size(); i++) {
			notifications.get(i).render(g);
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
			respawnUnitButton.render(g);
			endTurnButton.render(g);
		}
		
		//---------------------------------------
		//--Render rotate buttons if the user has pressed the "move" button during setup phase
		//---------------------------------------
		if(currentAction == CHOOSE_MOVEMENT && isSetupPhase) {
			leftRotateSetupButton.render(g);
			rightRotateSetupButton.render(g);
		}
		
		//---------------------------------------
		//--Render movement buttons if the user has pressed the "move" button during an actual turn
		//---------------------------------------
		if(currentAction == CHOOSE_MOVEMENT && !isSetupPhase) {
			movementButton.render(g, GameplayAssets.movementIcons[units[selectedUnitIndex].getMovementCost()]);
			leftRotateButton.render(g);
			rightRotateButton.render(g);
		}
		
		//---------------------------------------
		//--Render ability buttons if the user has pressed the "ability" button
		//---------------------------------------
		if(currentAction == CHOOSE_ABILITY) {
			Ability[] abilities = units[selectedUnitIndex].getAbilities();
			for(int i = 0; i < abilities.length; i++) {
				abilities[i].renderButton(g, abilityButton.getX(), abilityButton.getY() - ((abilities.length-i) * GameplayAssets.throwGrenadeIcon.getHeight()));
			}
		}
		
		//if the user hit the end turn button, put up a dialog making sure they wanted to do that
		if(isEndingTurn) {
			g.drawBitmap(GameplayAssets.endTurnDialogIcon, Game.G_WIDTH / 2 - GameplayAssets.exitDialogIcon.getWidth() / 2, Game.G_HEIGHT / 2 - GameplayAssets.exitDialogIcon.getHeight() / 2, null); //TODO: scale for larger devices
			yesButton.render(g);
			noButton.render(g);
		}
		
		//reset overlays
		movementPoints = null;
		attackableTiles = null;
		
		Tooltip.render(g);
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
		indicatorPaint = null;
		attackOverlayPaint = null;
		healOverlayPaint = null;
		
		healthIndicators.clear();
		notifications.clear();
	}
	
	public void resume() {
		
		//load unit sprites
		for(int i = 0; i < units.length; i++)
			units[i].loadSprites();
		
		//indicator font
		indicatorPaint = new Paint();
		indicatorPaint.setTextSize(40);
		indicatorPaint.setTextAlign(Align.CENTER);
		indicatorPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		
		//overlays
		attackOverlayPaint = new Paint();
		attackOverlayPaint.setColor(Color.parseColor("#44FF0000"));
		
		healOverlayPaint = new Paint();
		healOverlayPaint.setColor(Color.parseColor("#4400FF00"));
		
		//coordinates of each hud button
		int width, height;
		if(Game.isScaled()) {
			width = Game.G_WIDTH; height = Game.G_HEIGHT;
		}
		else {
			width = Game.P_WIDTH; height = Game.P_HEIGHT;
		}
		
		int unitInfoButtonX = (width / 2) - (GameplayAssets.unitInfoIcon.getWidth()) - GameplayAssets.unitInfoIcon.getWidth() / 2;
		int unitInfoButtonY = height - GameplayAssets.unitInfoIcon.getHeight();
		int moveButtonX = (width / 2) - GameplayAssets.moveIcon.getWidth();
		int moveButtonY = height - GameplayAssets.moveIcon.getHeight();
		int abilityButtonX = width / 2;
		int abilityButtonY = height - GameplayAssets.abilityIcon.getHeight();
		int deselectButtonX = (width / 2) + GameplayAssets.deselectIcon.getWidth();
		int deselectButtonY = height - GameplayAssets.deselectIcon.getHeight();
		int endTurnButtonX = (width / 2) + GameplayAssets.endTurnIcon.getWidth() + GameplayAssets.endTurnIcon.getWidth();
		int endTurnButtonY = height - GameplayAssets.endTurnIcon.getHeight();
		
		int rightRotateButtonX = moveButtonX;
		int rightRotateButtonY = moveButtonY - GameplayAssets.rightRotateIcon.getHeight();
		int leftRotateButtonX = moveButtonX;
		int leftRotateButtonY = rightRotateButtonY - GameplayAssets.leftRotateIcon.getHeight();
		int movementButtonX = moveButtonX;
		int movementButtonY = leftRotateButtonY - GameplayAssets.movementIcons[1].getHeight();
		
		int spawnUnitButtonX = width / 2;
		int spawnUnitButtonY = height - GameplayAssets.abilityIcon.getHeight();
		int respawnUnitButtonX = (width / 2) + GameplayAssets.respawnUnitIcon.getWidth();
		int respawnUnitButtonY = height - GameplayAssets.respawnUnitIcon.getHeight();
		
		Paint unitInfoPaint = new Paint();
		unitInfoPaint.setColor(Color.BLACK);
		unitInfoPaint.setTextSize(28); //TODO: scale according to device size
		
		//create buttons
		unitInfoButton = new UnitInfoDrawableButton(unitInfoPaint, GameplayAssets.unitInfoIcon, null, unitInfoButtonX, unitInfoButtonY);
		moveButton = new Button(GameplayAssets.moveIcon, null, moveButtonX, moveButtonY);
		abilityButton = new Button(GameplayAssets.abilityIcon, null, abilityButtonX, abilityButtonY);
		deselectButton = new Button(GameplayAssets.deselectIcon, null, deselectButtonX, deselectButtonY);
		endTurnButton = new Button(GameplayAssets.endTurnIcon, null, endTurnButtonX, endTurnButtonY);
		
		movementButton = new Button(GameplayAssets.movementIcons[1], null, movementButtonX, movementButtonY);
		leftRotateButton = new Button(GameplayAssets.leftRotateIcon, null, leftRotateButtonX, leftRotateButtonY);
		rightRotateButton = new Button(GameplayAssets.rightRotateIcon, null, rightRotateButtonX, rightRotateButtonY);
		leftRotateSetupButton = new Button(GameplayAssets.leftRotateSetupIcon, null, leftRotateButtonX, leftRotateButtonY);
		rightRotateSetupButton = new Button(GameplayAssets.rightRotateSetupIcon, null, rightRotateButtonX, rightRotateButtonY);
		
		spawnUnitButton = new Button(GameplayAssets.spawnUnitIcon, null, spawnUnitButtonX, spawnUnitButtonY);
		respawnUnitButton = new Button(GameplayAssets.respawnUnitIcon, null, respawnUnitButtonX, respawnUnitButtonY);
		
		yesButton = new Button(GameplayAssets.yesIcon, GameplayAssets.yesArmedIcon, Game.G_WIDTH / 2 - GameplayAssets.yesIcon.getWidth() - 10, Game.G_HEIGHT / 2 + GameplayAssets.yesIcon.getHeight() / 2 + 30);
		noButton = new Button(GameplayAssets.noIcon, GameplayAssets.noArmedIcon, Game.G_WIDTH / 2 + 10, Game.G_HEIGHT / 2 + GameplayAssets.noIcon.getHeight() / 2 + 30);
		
		Player thisPlayersTurn = map.getCurrentPlayersTurn();
		if(thisPlayersTurn == this && isSetupPhase) {
			disableButtons();
			endTurnButton.disable();
		}
		if(thisPlayersTurn == this && !isSetupPhase) {
			disableButtons();
		}
		else {
			disableButtons();
			endTurnButton.disable();
		}
		selectedUnitIndex = -1;
		currentAction = SELECTION;
		
		for(int i = 0; i < hitIndicators.size(); i++)
			hitIndicators.get(i).loadIndicator();
		
		Tooltip.onResume();
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
							map.moveToTile(new GPoint(row, col));
							return;
						}
				}
			}
		//otherwise scroll to the selected unit (if there is no selected unit, pick a new unit to be selected and scroll to him), reset action points
		else {
			healthIndicators.clear(); //remove any indicators if we had any
			notifications.clear();
			enemyUnitSelected = null;
			endTurnButton.enable();
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
						break;
					}
				}
			}
		}
	}
	
	public void addHitIndicator(HitIndicator indicator) {
		hitIndicators.add(indicator);
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
	protected void disableButtons() {
		unitInfoButton.disable();
		moveButton.disable();
		abilityButton.disable();
		deselectButton.disable();
	}
	
	//enable the hud buttons (generally when a player has just selected one of their units)
	protected void enableButtons() {
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