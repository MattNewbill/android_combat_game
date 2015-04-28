package combatgame.alerts;

import java.io.Serializable;

import android.graphics.Bitmap;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.graphics.Graphics2D;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.recon.TrickShot;
import combatgame.util.Util;

public class HitIndicator implements Serializable {

	private static final long serialVersionUID = 1L;
	public GPoint tile;
	private Map map;
	private transient Bitmap indicator;
	public int direction;

	public HitIndicator(GPoint tile, int direction, Map map) {
		this.tile = tile;
		this.direction = direction;
		this.map = map;
		loadIndicator();
	}
	
	public HitIndicator(Unit attackingUnit, Ability currentAbility,	Unit defendingUnit, GPoint tile, Map map) {
		this.tile = tile.clone();
		this.map = map;
		
		//trick shot attack from recon class
		if (TrickShot.ABILITY_TYPE.equals(currentAbility.getType())) {
			direction = Math.abs(Util.getRand() % 4);
			loadIndicator();
		}
		else {//regular attack
			// determine which direction should the indicator be pointing
			GPoint attackingTile = attackingUnit.getXYCoordinate();
			GPoint defendingTile = defendingUnit.getXYCoordinate();
			int rowOffset = attackingTile.row - defendingTile.row;
			int colOffset = attackingTile.col - defendingTile.col;
			if (Math.abs(rowOffset) > Math.abs(colOffset)) {// checks which
															// distance is
															// greatest (col or
															// row)
				if (rowOffset < 0)// this checks if it is above or below you
									// (above is negative, below is pos)
					direction = 0;
				else
					direction = 2;
			} else {
				if (colOffset < 0)// this checks if it is above or below you
									// (above is negative, below is pos)
					direction = 3;
				else
					direction = 1;
			}
			loadIndicator();
		}
	}
	
	public void loadIndicator() {
		switch(direction) {
			case 0:
				indicator = GameplayAssets.hitIndicatorUpIcon;
				break;
			case 2:
				indicator = GameplayAssets.hitIndicatorDownIcon;
				break;
			case 3:
				indicator = GameplayAssets.hitIndicatorLeftIcon;
				break;
			case 1:
				indicator = GameplayAssets.hitIndicatorRightIcon;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	public void render(Graphics2D g) {
		g.drawBitmap(indicator,
				tile.col * map.getTileWidthInPx() - map.getMapOffsetX(),
				tile.row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
	}

	public GPoint getXY(){
		return tile;
	}
	
	public int getDirection(){
		return direction;
	}
}
