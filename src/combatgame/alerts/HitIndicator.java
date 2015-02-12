package combatgame.alerts;

import android.graphics.Bitmap;

import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.graphics.Graphics2D;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import combatgame.units.Ability;
import combatgame.units.recon.TrickShot;
import combatgame.util.Util;

public class HitIndicator {

	private GPoint tile;
	private Map map;
	private Bitmap indicator;

	public HitIndicator(Unit attackingUnit, Ability currentAbility,	Unit defendingUnit, GPoint tile, Map map) {
		this.tile = tile.clone();
		this.map = map;
		
		//trick shot attack from recon class
		if (TrickShot.ABILITY_TYPE.equals(currentAbility.getType())) {
			int random_direction = Math.abs(Util.getRand() % 4);
			switch (random_direction) {
			case 0:
				indicator = GameplayAssets.hitIndicatorUpIcon;
				break;
			case 1:
				indicator = GameplayAssets.hitIndicatorDownIcon;
				break;
			case 2:
				indicator = GameplayAssets.hitIndicatorLeftIcon;
				break;
			case 3:
				indicator = GameplayAssets.hitIndicatorRightIcon;
				break;
			default:
				throw new IllegalArgumentException();
			}

		} else {//regular attack
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
					indicator = GameplayAssets.hitIndicatorUpIcon;
				else
					indicator = GameplayAssets.hitIndicatorDownIcon;
			} else {
				if (colOffset < 0)// this checks if it is above or below you
									// (above is negative, below is pos)
					indicator = GameplayAssets.hitIndicatorLeftIcon;
				else
					indicator = GameplayAssets.hitIndicatorRightIcon;
			}
		}
	}

	public void render(Graphics2D g) {
		g.drawBitmap(indicator,
				tile.col * map.getTileWidthInPx() - map.getMapOffsetX(),
				tile.row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
	}

}
