package combatgame.graphics;

import android.graphics.Paint;
import android.graphics.Bitmap;

public interface Graphics2D {

	
	public void drawRGB(int r, int g, int b);
	public void drawRect(float left, float top, float right, float bottom, Paint paint);
	public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint);
	public void drawText(String text, float x, float y, Paint paint);
	public int getHeight();
	public int getWidth();
}
