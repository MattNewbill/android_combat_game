package combatgame.util;

import combatgame.input.*;

public class Util {

	private Util() {}
	
	/**
	 * Check if a touch event is within the bounding box.  
	 * No check is done to see if width or height is > 0
	 * @param event TouchEvent to be tested
	 * @param x Top left x coordinate
	 * @param y Top left y coordinate
	 * @param width Width of bounding box
	 * @param height Height of bounding box
	 * @return True if within bounding box, false if not
	 */
	public static boolean isInBounds(TouchEvent event, int x, int y, int width, int height) {
		if(event.x > x && event.x < x + width - 1 &&
		   event.y > y && event.y < y + height - 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
