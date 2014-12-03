package combatgame.alerts;

import android.graphics.Color;
import android.graphics.Paint;

import combatgame.graphics.GPoint;
import combatgame.objects.Map;

public class APNotification extends Notification {

	public APNotification(Map map, GPoint tile, int yOffset, Paint paint) {
		super(map, tile, yOffset, "Not enough AP", paint, Color.CYAN);
	}

}
