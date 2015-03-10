package combatgame.alerts;

import android.graphics.Color;
import android.graphics.Paint;

import combatgame.graphics.GPoint;
import combatgame.objects.Map;

public class MoveNotification extends Notification {

	public MoveNotification(Map map, GPoint tile, int yOffset, Paint paint) {
		super(map, tile, yOffset, "Path Blocked", paint, Color.CYAN);
	}

}
