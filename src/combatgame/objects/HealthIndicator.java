package combatgame.objects;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import combatgame.graphics.GPoint;
import combatgame.graphics.Graphics2D;

public class HealthIndicator {

	private Map map;
	private GPoint tile;
	private int yOffset = 0;
	private final static int Y_STEP = 1;
	private int alpha = 255;
	private final static int ALPHA_STEP = 3;
	
	
	private String text;
	
	private Paint font;
	private int color;
	
	private boolean isFinished = false;
	
	public HealthIndicator(Map map, GPoint tile, int yOffset, int damageDone, Paint paint, boolean isArmor) {
		this.map = map;
		this.tile = tile;
		this.yOffset = yOffset;
		this.font = paint;
		
		if(damageDone < 0) {
			if(isArmor)
				this.color = Color.BLUE;
			else
				this.color = Color.RED;
			text = "" + damageDone;
		}
		else {
			this.color = Color.GREEN;
			text = "+" + damageDone;
		}
	}
	
	public void update() {
		alpha -= ALPHA_STEP;
		yOffset += Y_STEP;
		if(alpha <= 0) {
			alpha = 0;
			isFinished = true;
		}
	}
	
	public void render(Graphics2D g) {
		font.setColor(color);
		font.setAlpha(alpha);
		g.drawText(text, tile.col * map.getTileWidthInPx() - map.getMapOffsetX() + (map.getTileWidthInPx() / 2), tile.row * map.getTileHeightInPx() - map.getMapOffsetY() - yOffset + (map.getTileHeightInPx() / 2), font);
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	
	
}
