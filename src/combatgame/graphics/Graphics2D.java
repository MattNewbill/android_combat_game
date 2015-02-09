package combatgame.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;

public interface Graphics2D {

	public void setCanvas(Canvas canvas);
	public void drawRGB(int r, int g, int b);
	public void drawRect(float left, float top, float right, float bottom, Paint paint);
	public void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, Paint paint);
	public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint);
	public void drawText(String text, float x, float y, Paint paint);
	public int getHeight();
	public int getWidth();
}
