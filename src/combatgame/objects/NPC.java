package combatgame.objects;

import java.util.ArrayList;
import java.util.List;

import combatgame.alerts.HealthIndicator;
import combatgame.alerts.HitIndicator;
import combatgame.graphics.*;
import combatgame.input.TouchEvent;
import combatgame.units.Ability;
import combatgame.units.AttackedTile;
import combatgame.util.*;

public class NPC  extends Player 
{
	private static final long serialVersionUID = 1L;
	
	protected int rando;
	protected boolean newTurn, endturnplz, didsomething;
	protected List<GPoint> vis;
	protected GPoint target;
	protected boolean[] UnitNoCombat;
	protected boolean[] respondedToHits;

	public NPC(String gamertag, boolean isPlayerOne, Map map, Unit[] units) 
	{
		super(gamertag, isPlayerOne, map, units);
		Tooltip.turnOff(isPlayerOne);
		vis= new ArrayList<GPoint>();
		newTurn = true;
		UnitNoCombat = new boolean[units.length];
		respondedToHits = new boolean[units.length];
	}
	
	@Override
	public List<TouchEvent> update(List<TouchEvent> events) 
	{
		
		if(isSetupPhase)
			updateSetupPhase();
		else
			updateTurnPhase();
		
		return events;
	}
	
	protected void updateSetupPhase() 
	{
		// spawns one unit per frame
		
		List<GPoint> base = new ArrayList<GPoint>();
		
		// gets possible spawn points
		for(int row = 0; row < map.getNum_vertical_tiles(); row++) 
		{
			for(int col = 0; col < map.getNum_horizontal_tiles(); col++) 
			{
				if(!isPlayerOne && map.getTile(row, col).getFeatureType() == MapFeature.PLAYER_TWO_BASE)
					if(!map.getTile(row, col).hasUnit())
						base.add(new GPoint(row,col));
			}
		}
		
		// if more units to spawn, pick a random valid spawn point and spawn
		if(spawnUnitIndex < units.length)
		{
			rando = (int)(Math.random()*base.size()); 
			spawnUnit(base.get(rando));
			base.clear();
		}
		else
		{
			// if everything spawned, switch turn
			isSetupPhase = false;
			map.switchTurn();
		}
	}
	
	protected void updateTurnPhase() 
	{
		
		if(newTurn)
		{
			newTurn = false;
			
			for(int i = 0; i < UnitNoCombat.length; i++)
				UnitNoCombat[i] = true;
			
			// rotate based on hit indicators
			respondToHits();
		}
		
		endturnplz = true;
		
		for(int i = 0; i < units.length; i++)
		{
			if(!units[i].isDead)
			{
				// cheat peek
				strategize(units[i]);
				
				// if points left for an attack
				if( units[i].getPointsLeft()>=units[i].abilities[0].getCost() )
				{	
					endturnplz = false;
					
					if(units[i].getUnitType()==Unit.UnitType.ASSAULT)
						assaultAction(units[i],i);
					else if(units[i].getUnitType()==Unit.UnitType.CQC)
						cqcAction(units[i],i);
					else if(units[i].getUnitType()==Unit.UnitType.MEDIC)
						medicAction(units[i],i);
					else if(units[i].getUnitType()==Unit.UnitType.RECON)
						reconAction(units[i],i);
					else if(units[i].getUnitType()==Unit.UnitType.SNIPER)
						sniperAction(units[i],i);
					else if(units[i].getUnitType()==Unit.UnitType.PRESIDENT)
						presidentAction(units[i],i);
				}
				// else if points left for move and unit has not been in combat. exploring
				else if( (units[i].getPointsLeft()>=units[i].getMovementCost()) && UnitNoCombat[i] )
				{
					useMovement(units[i]);
					
					// 1/5 chance of random rotate while exploring
					if( units[i].getPointsLeft() > 0 )
					{
						rando = (int)(Math.random()*5);
						if(rando == 0)
							randomRotate(units[i]);
					}
				}
			}
		}
		if(endturnplz)
		{
			newTurn = true;
			hitIndicators.clear(); // remove any hit indicators
			map.switchTurn();
		}
	}
	
	protected void spawnUnit(GPoint T) 
	{	
		if(T != null) 
		{
			// spawns unit with random direction
			units[spawnUnitIndex].setXYCoordinate(T, map);
			units[spawnUnitIndex].setDirectionFacing(((int)(Math.random()*4)));
			spawnUnitIndex++;
		}
	}

	protected void assaultAction(Unit U, int I)
	{
		// assault tries grenade, or rifle fire, or basic movement movement
		
		didsomething = false;
		
		if(U.pointsLeft >= U.abilities[1].getCost())
			grenadeAction(U, U.abilities[1], I);
		
		if(!didsomething)
		{
			if(U.pointsLeft >= U.abilities[0].getCost())
				defaultAction(U, U.abilities[0], I);
		}
		
		if(!didsomething)
			useMovement(U);
	}

	protected void reconAction(Unit U, int I)
	{
		// recon tries trick shot and then runs away, or does normal attack, or basic movement
		
		didsomething = false;
		
		if(U.pointsLeft >= U.abilities[1].getCost())
		{
			defaultAction(U, U.abilities[1], I);
			
			if(U.pointsLeft > 0)
			{
				rando = (int)(Math.random()*2); 
				if(rando == 0)
					U.rotateLeft();
				else
					U.rotateRight();
			}
				
		}
		
		if(!didsomething)
		{
			if(U.pointsLeft >= U.abilities[0].getCost())
				defaultAction(U, U.abilities[0], I);
		}
			
		if(!didsomething)
			useMovement(U);
	}

	protected void medicAction(Unit U, int I)
	{
		// medic tries single heal, or area heal, or pistol shot, or basic movement
		
		didsomething = false;
		
		if(U.pointsLeft >= U.abilities[1].getCost())
			healAction(U, U.abilities[1], I, 60);
		
		if(!didsomething)
		{
			if(U.pointsLeft >= U.abilities[2].getCost())
				healAction(U, U.abilities[2], I, 85);
		}
		
		if(!didsomething)
		{
			if(U.pointsLeft >= U.abilities[0].getCost())
				defaultAction(U, U.abilities[0], I);
		}
		
		if(!didsomething)
			useMovement(U);
		
	}

	protected void cqcAction(Unit U, int I)
	{
		// cqc tries concentrated shot, or wide shot, or basic movement
		
		didsomething = false;
		
		if(U.pointsLeft >= U.abilities[1].getCost())
			defaultAction(U, U.abilities[1], I);

		if(!didsomething)
		{
			if(U.pointsLeft >= U.abilities[0].getCost())
				defaultAction(U, U.abilities[0], I);
		}
		
		if(!didsomething)
			useMovement(U);
	}
	
	protected void sniperAction(Unit U, int I)
	{
		// sniper tries charged shot, or quick shot, or basic movement
		
		didsomething = false;
		
		if(U.pointsLeft >= U.abilities[1].getCost())
			defaultAction(U, U.abilities[1], I);

		if(!didsomething)
		{
			if(U.pointsLeft >= U.abilities[0].getCost())
				defaultAction(U, U.abilities[0], I);
		}
		
		if(!didsomething)
			useMovement(U);
	}
	
	protected void presidentAction(Unit U, int I)
	{
		// president not implemented so just basic movement
		
		useMovement(U);
	}

	protected GPoint findTarget(Ability A, Unit U)
	{
		// searches attackable tiles and picks the target with the least hp
		
		GPoint itarget = null;
		vis = A.getTilesAttackable(U, map);
		
		for(int i = 0; i < vis.size(); i++)
		{
			if(map.getTile(vis.get(i)).hasUnit())
				if(map.getTile(vis.get(i)).getPlayer_id() != playerId)
				{
					if(itarget == null)
						itarget = new GPoint(vis.get(i));
					
					if(map.getUnit(map.getTile(itarget).getUnit_id()).health > map.getUnit(map.getTile(vis.get(i)).getUnit_id()).health)
						itarget = new GPoint(vis.get(i));
				}
		}
		vis.clear();
		return itarget;
	}
	
	protected GPoint findGrenadeTarget(Ability A, Unit U)
	{
		// searches for an attackable spot that would hit the most enemies for the most damage
		// must hit at least 2 enemies
		// 1/24 chance to see aoe
		
		int Cur_units = 0;
		int Cur_damage = 0;
		int New_units = 0;
		int New_damage = 0;
		
		GPoint itarget = null;
		
		rando = (int)(Math.random()*24);
		if(rando == 0)
			vis = A.getTilesAttackable(U, map);
		else
			vis = Vision.getSprintVision(map, U, 3);
		
		for(int i = 0; i < vis.size(); i++)
		{
			List<AttackedTile> tilesAffected = A.getTilesAffected(U, vis.get(i), map);
			for(int j = 0; j < tilesAffected.size(); j++) 
			{
				if(map.getTile(tilesAffected.get(j).tile).hasUnit())
				{
					if(map.getTile(tilesAffected.get(j).tile).getPlayer_id() != playerId)
					{
						New_units++;
						New_damage += tilesAffected.get(j).damageTaken;
					}
				}
			}
			
			if( (itarget == null) || (New_units > Cur_units) || ((New_units == Cur_units) && (New_damage > Cur_damage)) )
			{
				itarget = new GPoint(vis.get(i));
				Cur_units = New_units;
				Cur_damage = New_damage;
			}
			
			New_units = 0;
			New_damage = 0;

		}
		vis.clear();
		
		if(Cur_units < 2)
			itarget = null;
		
		return itarget;
	}

	protected GPoint findHealTarget(Ability A, Unit U, int HP)
	{
		// searches for a target with less than the specified HP
		
		GPoint itarget = null;
		vis = A.getTilesAttackable(U, map);
		
		for(int i = 0; i < vis.size(); i++)
		{
			if(map.getTile(vis.get(i)).hasUnit())
				if(map.getTile(vis.get(i)).getPlayer_id() == playerId)
				{
					if(itarget == null)
						itarget = new GPoint(vis.get(i));
					
					if(map.getUnit(map.getTile(itarget).getUnit_id()).health > map.getUnit(map.getTile(vis.get(i)).getUnit_id()).health)
						itarget = new GPoint(vis.get(i));
				}
		}
		vis.clear();
		
		if( map.getUnit(map.getTile(itarget).getUnit_id()).health > HP )
			itarget = null;
		
		return itarget;
	}
	
	protected void defaultAction(Unit U, Ability A, int I)
	{
		// basic action. searches for a target and attacks with selected ability
		
		target = findTarget(A, U);
		if(target != null)
		{
			useAbility(U, A, target);
			didsomething = true;
			UnitNoCombat[I] = false;
		}
	}
	
	protected void grenadeAction(Unit U, Ability A, int I)
	{
		// assault searches for grenade target throw
		
		target = findGrenadeTarget(A, U);
		if(target != null)
		{
			useAbility(U, A, target);
			didsomething = true;
			UnitNoCombat[I] = false;
		}
	}
	
	protected void healAction(Unit U, Ability A, int I, int HP)
	{
		// medic searches for target to heal
		
		target = findHealTarget(A, U, HP);
		if(target != null)
		{
			useAbility(U, A, target);
			didsomething = true;
		}
	}
	
	protected void useAbility(Unit U, Ability A, GPoint T)
	{
		// uses the selected ability on the selected target
		// code copied from player class
		
		// decrement action points
		U.usePoints(A.getCost());
		
		// get the tiles that were affected by the attack
		List<AttackedTile> tilesAffected = A.getTilesAffected(U,T, map);
		
		for(int j = 0; j < tilesAffected.size(); j++) {
			MapTile tile = map.getTile(tilesAffected.get(j).tile);
			
			if(tile.hasUnit()){// there is a unit on the tile
				int unitId = tile.getUnit_id();
				Unit unit = map.getUnit(unitId);
				
				// reduce unit health by attack dmg
				if(unit != null) {
					DamageDealt damageDone = unit.takeDamage(tilesAffected.get(j).damageTaken, map);
					healthIndicators.add(new HealthIndicator(map, new GPoint(tilesAffected.get(j).tile.row, tilesAffected.get(j).tile.col), 30, damageDone.healthDamage, indicatorPaint, false));
					if(damageDone.isAttack && damageDone.armorDamage < 0) {
						healthIndicators.add(new HealthIndicator(map, new GPoint(tilesAffected.get(j).tile.row, tilesAffected.get(j).tile.col), 0, damageDone.armorDamage, indicatorPaint, true));
					}
					if(U != unit && damageDone.isAttack) //don't send indicator for heals
						map.sendHitIndicator(new HitIndicator(U, A, unit, tilesAffected.get(j).tile, map), unit);
				}
			}
		}
	}
	
	protected void useMovement(Unit U)
	{
		//if movement forward is possible, move forward 1 square
		//otherwise, rotate left or right
		
		movementPoints = Movement.getMovement(map, U);
		
		if(movementPoints[1].length==0)
		{
			randomRotate(U);
		}
		else
		{
			U.setXYCoordinate(movementPoints[1][0], map);
			U.usePoints(U.getMovementCost());
		}
		
	}
	
	protected void respondToHits()
	{
		// if a unit is on the same square as a hit indicator, have the unit face where it came from
		// each unit only responds to at most 1 indicator so it doesnt waste rotations
		
		for(int u = 0; u < units.length; u++)
			respondedToHits[u] = false;
		
		for(int u = 0; u < units.length; u++)
			for(int i = 0; i < hitIndicators.size(); i++ )
				if(units[u].getXYCoordinate().equals(hitIndicators.get(i).getXY()))
					if(!respondedToHits[u])
					{
						rotateToHits(units[u], hitIndicators.get(i));
						respondedToHits[u] = true;
					}
	}
	
	protected void rotateToHits(Unit U, HitIndicator I)
	{
		// rotate unit to face indicator direction
		
		// 0 up, 1 right, 2 down, 3 left
		
		while( !(U.getDirectionFacing() == I.getDirection()) )
		{
			if(U.getDirectionFacing()==0)
			{
				if(I.getDirection()==3)
					U.rotateLeft();
				else
					U.rotateRight();
			}
			else if(U.getDirectionFacing()==1)
			{
				if(I.getDirection()==0)
					U.rotateLeft();
				else
					U.rotateRight();
			}
			else if(U.getDirectionFacing()==2)
			{
				if(I.getDirection()==1)
					U.rotateLeft();
				else
					U.rotateRight();
			}
			else if(U.getDirectionFacing()==3)
			{
				if(I.getDirection()==2)
					U.rotateLeft();
				else
					U.rotateRight();
			}
			else
				return;//error
			
			U.usePoints(U.getRotationCost());
		}
	}
	
	protected void randomRotate(Unit U)
	{
		// random rotate while exploring
		
		didsomething = true;
		rando = (int)(Math.random()*2); 
		if(rando == 0)
			U.rotateLeft();
		else
			U.rotateRight();
		
		U.usePoints(U.getRotationCost());
	}
	
	protected void strategize(Unit U)
	{
		// cheat peek. 1/30 chance to look left and right to see if there are any enemies there
		// without spending points. Then rotate using points if enemies found
		// helps prevent the npc from just walking straight across the map
		// seaching simulator
		
		rando = (int)(Math.random()*30);
		if(rando == 0)
		{
			U.rotateLeft();
			target = findTarget(U.abilities[0], U);
			if(target != null)
				U.usePoints(U.getRotationCost());
			else
			{
				U.rotateRight();
				U.rotateRight();
				target = findTarget(U.abilities[0], U);
				if(target != null)
					U.usePoints(U.getRotationCost());
				else
					U.rotateLeft();
			}
		}
	}
	
	@Override
	public void render(Graphics2D g) {} // dont render during npc's turn
	
	@Override
	public boolean[][] constructLightMap(boolean[][] lightmap)
	{
		//dont light anything during npc's turn
		
			for(int row = 0; row < map.getNum_vertical_tiles(); row++)
				for(int col = 0; col < map.getNum_horizontal_tiles(); col++)
						lightmap[row][col] = false;
			return lightmap;
		}

}