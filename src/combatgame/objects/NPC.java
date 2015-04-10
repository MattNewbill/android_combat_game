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

public class NPC  extends Player {
	protected static final long serialVersionUID = 1L;
	
	protected int rando;
	protected boolean endturnplz, didsomething;
	protected List<GPoint> vis;
	protected GPoint target;

	public NPC(String gamertag, boolean isPlayerOne, Map map, Unit[] units) {
		super(gamertag, isPlayerOne, map, units);
		Tooltip.turnOff(isPlayerOne);
		vis= new ArrayList<GPoint>();
	}
	
	@Override
	public List<TouchEvent> update(List<TouchEvent> events) {
		
		if(isSetupPhase)
			updateSetupPhase();
		else
			updateTurnPhase();
		
		return events;
	}
	
	protected void updateSetupPhase() {
		List<GPoint> base= new ArrayList<GPoint>();
		
		for(int row = 0; row < map.getNum_vertical_tiles(); row++) {
			for(int col = 0; col < map.getNum_horizontal_tiles(); col++) {
				if(!isPlayerOne && map.getTile(row, col).getFeatureType() == MapFeature.PLAYER_TWO_BASE)
					base.add(new GPoint(row,col));
			}
		}
		
		while(spawnUnitIndex < units.length)
		{
			rando = (int)(Math.random()*base.size()); 
			spawnUnit(base.get(rando));
			base.remove(rando);
		}
		
		base.clear();
		isSetupPhase = false;
		map.switchTurn();
	}
	
	protected void updateTurnPhase() {
		
		endturnplz = true;
		
		for(int i = 0; i < units.length; i++)
		{
			if( (units[i].getPointsLeft()>=units[i].movementCost) && (!units[i].isDead) )
			{	
				endturnplz = false;
				
				if(units[i].getUnitType()==Unit.UnitType.ASSAULT)
					assaultAction(units[i]);
				else if(units[i].getUnitType()==Unit.UnitType.CQC)
					cqcAction(units[i]);
				else if(units[i].getUnitType()==Unit.UnitType.MEDIC)
					medicAction(units[i]);
				else if(units[i].getUnitType()==Unit.UnitType.RECON)
					reconAction(units[i]);
				else if(units[i].getUnitType()==Unit.UnitType.SNIPER)
					sniperAction(units[i]);
				else if(units[i].getUnitType()==Unit.UnitType.PRESIDENT)
					presidentAction(units[i]);

			}
		}
		
		if(endturnplz)
		{
			hitIndicators.clear(); //remove any hit indicators
			map.switchTurn();
		}
	}
	
	protected void spawnUnit(GPoint T) {	
		if(T != null) {
			units[spawnUnitIndex].setXYCoordinate(T, map);
			units[spawnUnitIndex].setDirectionFacing(((int)(Math.random()*4)));
			spawnUnitIndex++;
		}
	}



		protected void assaultAction(Unit U)
		{
			defaultAction(U);
		}

		protected void cqcAction(Unit U)
		{
			defaultAction(U);
		}

		protected void reconAction(Unit U)
		{
			defaultAction(U);
		}

		protected void sniperAction(Unit U)
		{
			defaultAction(U);
		}

		protected void medicAction(Unit U)
		{
			defaultAction(U);
		}

		protected void presidentAction(Unit U)
		{
			defaultAction(U);
		}
	
		protected GPoint findTarget(Ability A, Unit U)
		{
			GPoint itarget = null;
			vis = A.getTilesAttackable(U, map);
			
			for(int i = 0; i < vis.size(); i++)
			{
				if(map.getTile(vis.get(i)).hasUnit())
					if(map.getTile(vis.get(i)).getPlayer_id() != playerId)
					{
						itarget = new GPoint(vis.get(i));
						i = vis.size();
					}
			}
			vis.clear();
			return itarget;
		}
		
		protected void defaultAction(Unit U)
		{
			didsomething = false;
			
			if(U.pointsLeft >= U.abilities[1].getCost())
			{
				target = findTarget(U.abilities[1], U);
				if(target != null)
				{
					useAbility(U, U.abilities[1], target);
					didsomething = true;
				}
			}
			
			if(!didsomething)
			{
				if(U.pointsLeft >= U.abilities[0].getCost())
				{
					target = findTarget(U.abilities[0], U);
					if(target != null)
					{
						useAbility(U, U.abilities[0], target);
						didsomething = true;
					}
				}
			}
					
			if(!didsomething)
			{
				useMovement(U);
			}
		}
	
		protected void useAbility(Unit U, Ability A, GPoint T)
		{
			//decrement action points
			U.usePoints(A.getCost());
			
			//get the tiles that were affected by the attack
			List<AttackedTile> tilesAffected = A.getTilesAffected(U,T, map);
			
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
						if(U != unit && damageDone.isAttack) //don't send indicator for heals
							map.sendHitIndicator(new HitIndicator(U, A, unit, tilesAffected.get(j).tile, map), unit);
					}
				}
			}
		}
		
		protected void useMovement(Unit U)
		{
			movementPoints = Movement.getMovement(map, U);
			
			if(movementPoints[1].length==0)
			{
				rando = (int)(Math.random()*2); 
				if(rando == 0)
					U.rotateLeft();
				else
					U.rotateRight();
				
				U.usePoints(U.getRotationCost());
			}
			else
			{
				U.setXYCoordinate(movementPoints[1][0], map);
				U.usePoints(U.getMovementCost());
			}
			
		}
}