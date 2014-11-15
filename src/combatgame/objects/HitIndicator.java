package combatgame.objects;

import android.graphics.Bitmap;
import combatgame.assets.GameplayAssets;
import combatgame.graphics.GPoint;
import combatgame.graphics.Graphics2D;

public class HitIndicator {

	private GPoint tile;
	private Map map;
	private Bitmap indicator;
	
	public HitIndicator(Unit attackingUnit, Unit defendingUnit, GPoint tile, Map map) {
		this.tile = tile.clone();
		this.map = map;
		
		//determine which direction should the indicator be pointing
		GPoint attackingTile = attackingUnit.getXYCoordinate();
		GPoint defendingTile = defendingUnit.getXYCoordinate();
		int rowOffset = attackingTile.row - defendingTile.row;
		int colOffset = attackingTile.col - defendingTile.col;
		if(Math.abs(rowOffset) > Math.abs(colOffset)) {
			if(rowOffset < 0)
				indicator = GameplayAssets.hitIndicatorUpIcon;
			else
				indicator = GameplayAssets.hitIndicatorDownIcon;
		}
		else {
			if(colOffset < 0)
				indicator = GameplayAssets.hitIndicatorLeftIcon;
			else
				indicator = GameplayAssets.hitIndicatorRightIcon;
		}
	}
	
	public void render(Graphics2D g) {
		g.drawBitmap(indicator, tile.col * map.getTileWidthInPx() - map.getMapOffsetX(), tile.row * map.getTileHeightInPx() - map.getMapOffsetY(), null);
	}
	
}
