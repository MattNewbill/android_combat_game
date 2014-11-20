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
	public static final int DISABLED = 2;
	public static final int ACTIVATED = 3;
	public int state = 0;
	
	protected Bitmap[] images = new Bitmap[4];
	protected int x;
	protected int y;
	
	public Button(Bitmap disarmedImage, Bitmap armedImage, int x, int y) {
		//disarmed image cannot be null
		if(disarmedImage == null)
			throw new IllegalArgumentException("disarmed image cannot be null");
		//if the armed image is null then we use the disarmed image instead
		if(armedImage == null)
			images[ARMED] = disarmedImage;
		else
			images[ARMED] = armedImage;
		images[DISARMED] = disarmedImage;
		images[DISABLED] = Util.toGrayscale(disarmedImage);
		images[ACTIVATED] = images[ARMED];
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
				if(Util.isInBounds(events.get(i), x, y, images[DISARMED].getWidth(), images[DISARMED].getHeight())) {
					state = ARMED;
				}
				else {
					unusedEvents.add(events.get(i));
				}
			}
			else if(events.get(i).type == TouchEvent.TOUCH_UP) {
				if(Util.isInBounds(events.get(i), x, y, images[DISARMED].getWidth(), images[DISARMED].getHeight())) {
					state = ACTIVATED;
				}
				else {
					state = DISARMED;
					unusedEvents.add(events.get(i));
				}
			}
			else if(events.get(i).type == TouchEvent.TOUCH_DRAGGED) {
				if(!Util.isInBounds(events.get(i), x, y, images[DISARMED].getWidth(), images[DISARMED].getHeight())) {
					unusedEvents.add(events.get(i));
				}
			}
		}
		return unusedEvents;
	}
	
	public void render(Graphics2D g) {
		g.drawBitmap(images[state], x, y, null);
	}
	
	public void render(Graphics2D g, Bitmap image) {
		g.drawBitmap(image, x, y, null);
	}
	
	public void render(Graphics2D g, int x, int y) {
		this.x = x; this.y = y;
		g.drawBitmap(images[state], x, y, null);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return images[state].getWidth();
	}
	
	public int getHeight() {
		return images[state].getHeight();
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
		for(int i = 0; i < images.length; i++) {
			if(images[i] != null)
				images[i].recycle();
		}
	}
}
