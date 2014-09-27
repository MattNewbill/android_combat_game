package combatgame.input;

public class TouchEvent {
	
	public static final int TOUCH_DOWN = 0; //they're usually worth six points.........
	public static final int TOUCH_DRAGGED = 1;
	public static final int TOUCH_UP = 2;
	
	public int type;
	public int x, y;
	
}
