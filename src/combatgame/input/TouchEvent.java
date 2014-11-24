package combatgame.input;

/**
 * **HAPPY**
 */

public class TouchEvent {
	
	public static final int TOUCH_DOWN = 0; //six points for the Eagles!!
	public static final int TOUCH_DRAGGED = 1;
	public static final int TOUCH_UP = 2;
	
	public int type;
	public int x, y;
	
	public void copy(TouchEvent e) {
		this.type = e.type;
		this.x = e.x;
		this.y = e.y;
	}
	
}
