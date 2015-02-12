package combatgame.alerts;

import android.graphics.Paint;
import combatgame.graphics.GPoint;
import combatgame.graphics.Graphics2D;
import combatgame.objects.Map;

public class Notification{

	private Map map;
	private GPoint tile;
	private int yOffset = 0;
	private final static int Y_STEP = 1;
	private int alpha = 255;
	private final static int ALPHA_STEP = 3;
	
	
	private String notification;
	
	private Paint font;
	private int color;
	
	private boolean isFinished = false;
	
	public Notification(Map map, GPoint tile, int yOffset, String notification, Paint paint, int color) {
		this.map = map;
		this.tile = tile;
		this.yOffset = yOffset;
		this.notification = notification;
		this.font = paint;
		this.color = color;
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
		g.drawText(notification, tile.col * map.getTileWidthInPx() - map.getMapOffsetX() + (map.getTileWidthInPx() / 2), tile.row * map.getTileHeightInPx() - map.getMapOffsetY() - yOffset + (map.getTileHeightInPx() / 2), font);
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
}
