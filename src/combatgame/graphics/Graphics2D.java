package combatgame.graphics;

import android.graphics.Paint;

public interface Graphics2D {

	
	public void drawRGB(int r, int g, int b);
	public void drawRect(float left, float top, float right, float bottom, Paint paint);
}
