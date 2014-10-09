package combatgame.widgets;

import combatgame.util.Util;
import combatgame.graphics.*;
import combatgame.input.*;
import java.util.List;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

/**
 * **HAPPY**
 */

public class Button {

	public static final int DISARMED = 0;
	public static final int ARMED = 1;
	public static final int ACTIVATED = 2;
	public static final int DISABLED = 3;
	public int state = 0;
	
	private Bitmap armedImage;
	private Bitmap disarmedImage;
	private Bitmap disabledImage;
	private int x, y;
	
	public Button(Bitmap disarmedImage, Bitmap armedImage, int x, int y) {
		//disarmed image cannot be null
		if(disarmedImage == null)
			throw new IllegalArgumentException("disarmed image cannot be null");
		//if the armed image is null then we use the disarmed image instead
		if(armedImage == null)
			this.armedImage = disarmedImage;
		else
			this.armedImage = armedImage;
		this.disarmedImage = disarmedImage;
		this.disabledImage = Util.toGrayscale(disarmedImage);
		this.x = x;
		this.y = y;
	}
	
	public Point getCoordinates() {
		return new Point(x, y);
	}
	
	public List<TouchEvent> update(List<TouchEvent> events) {
		//image is disabled so we shouldn't handle touch events
		if(state == DISABLED)
			return events;
		List<TouchEvent> unusedEvents = new ArrayList<TouchEvent>();
		for(int i = 0; i < events.size(); i++) {
			if(events.get(i).type == TouchEvent.TOUCH_DOWN) {
				if(Util.isInBounds(events.get(i), x, y, disarmedImage.getWidth(), disarmedImage.getHeight())) {
					state = ARMED;
				}
				else {
					unusedEvents.add(events.get(i));
				}
			}
			else if(events.get(i).type == TouchEvent.TOUCH_UP) {
				if(Util.isInBounds(events.get(i), x, y, disarmedImage.getWidth(), disarmedImage.getHeight())) {
					state = ACTIVATED;
				}
				else {
					state = DISARMED;
					unusedEvents.add(events.get(i));
				}
			}
			else {
				unusedEvents.add(events.get(i));
			}
		}
		return unusedEvents;
	}
	
	public void render(Graphics2D g) {
		if(state == ARMED)
			g.drawBitmap(armedImage, x, y, null);
		else if(state == DISABLED)
			g.drawBitmap(disabledImage, x, y, null);
		else
			g.drawBitmap(disarmedImage, x, y, null);
	}
	
	public void disarm() {
		state = DISARMED;
	}
	
	public void arm() {
		state = ARMED;
	}
	
	public void disable() {
		state = DISABLED;
	}
	
	public void enable() {
		state = DISARMED;
	}
	
	public void recycle() {
		disarmedImage.recycle();
		if(armedImage != disarmedImage && armedImage != null) {
			armedImage.recycle();
		}
	}
}
