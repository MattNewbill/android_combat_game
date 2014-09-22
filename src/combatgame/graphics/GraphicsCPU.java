package combatgame.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GraphicsCPU implements Graphics2D {

	Canvas drawingCanvas;
	
	public GraphicsCPU(Bitmap buffer) {
		drawingCanvas = new Canvas(buffer);
	}
	
	@Override
	public void drawRGB(int r, int g, int b) {
		drawingCanvas.drawRGB(r, g, b);
	}
	
	@Override
	public void drawRect(float left, float top, float right, float bottom, Paint paint) {
		drawingCanvas.drawRect(left, top, right, bottom, paint);
	}
}
