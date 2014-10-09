package combatgame.util;

import combatgame.input.*;
import java.util.Random;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

/**
 * **HAPPY**
 */

public class Util {

	private static Random rand = new Random(System.currentTimeMillis());
	
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
	
	/**
	 * Converts a bitmap to a grayscaled version
	 * @param original Bitmap to be grayscaled
	 * @return New grayscaled Bitmap
	 */
	public static Bitmap toGrayscale(Bitmap original) {
		int width = original.getWidth();
		int height = original.getHeight();
		
		Bitmap grayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(grayscale);
		Paint paint = new Paint();
		ColorMatrix matrix = new ColorMatrix();
		matrix.setSaturation(0);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
		paint.setColorFilter(filter);
		c.drawBitmap(original, 0, 0, paint);
		return grayscale;
	}
	
	public static int getRand() {
		return rand.nextInt();
	}
}
