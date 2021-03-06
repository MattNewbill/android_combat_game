package combatgame.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class GraphicsCPU implements Graphics2D {

	Canvas drawingCanvas;
	
	public GraphicsCPU(Bitmap buffer) {
		drawingCanvas = new Canvas(buffer);
	}
	
	//public GraphicsCPU() {
	//}
	
	public void setCanvas(Canvas canvas) {
		drawingCanvas = canvas;
	}
	
	@Override
	public void drawRGB(int r, int g, int b) {
		drawingCanvas.drawRGB(r, g, b);
	}
	
	@Override
	public void drawRect(float left, float top, float right, float bottom, Paint paint) {
		drawingCanvas.drawRect(left, top, right, bottom, paint);
	}
	
	@Override
	public void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, Paint paint) {
		//drawingCanvas.drawRoundRect(left, top, right, bottom, rx, ry, paint);
		drawingCanvas.drawRoundRect(new RectF(left, top, right, bottom), rx, ry, paint);
	}

	@Override
	public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
		drawingCanvas.drawBitmap(bitmap, left, top, paint);
	}

	@Override
	public void drawText(String text, float x, float y, Paint paint) {
		drawingCanvas.drawText(text, x, y, paint);
		
	}

	@Override
	public int getHeight() {
		return drawingCanvas.getHeight();
	}

	@Override
	public int getWidth() {
		return drawingCanvas.getWidth();
	}
}
