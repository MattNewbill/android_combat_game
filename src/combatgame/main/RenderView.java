package combatgame.main;

import combatgame.graphics.*;
import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.Log;

/**
 * **HAPPY**
 */

public class RenderView extends SurfaceView implements Runnable {

	Game game;
	Bitmap frameBuffer;
	Thread renderThread;
	SurfaceHolder holder;
	Graphics2D drawingCanvas;
	volatile boolean isRunning = false;
	
	public RenderView(Game game, Bitmap frameBuffer) {
		super(game);
		this.game = game;
		this.frameBuffer = frameBuffer;
		holder = getHolder();
		drawingCanvas = new GraphicsCPU(frameBuffer);
	}

	public void run() {
		
		Rect destinationRect = new Rect();
		long startTime = System.nanoTime();
		long fps = 0;
		
		while(isRunning) {
			//make sure we have a surface to draw on
			if(!holder.getSurface().isValid()) {
				continue;
			}
			
			drawingCanvas.drawRGB(0, 0, 0); //clear the screen
			
			long delta = (System.nanoTime() - startTime) / 1000000;
			startTime = System.nanoTime();
			
			game.getCurrentState().update(delta); //update current state (screen)
			game.getCurrentState().render(drawingCanvas, delta); //render current state (screen)
			drawingCanvas.drawText(Long.toString(fps), 30, 30, new Paint()); //draw fps
			
			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(destinationRect);
			if(game.shouldScale)
				canvas.drawBitmap(frameBuffer, null, destinationRect, null); //scales and translate automatically to fit screen
			else
				canvas.drawBitmap(frameBuffer, null, new Rect(0, 0, Game.G_WIDTH, Game.G_HEIGHT), null);
			holder.unlockCanvasAndPost(canvas);

			//fps stuff
			long elapsedTime = (System.nanoTime() - startTime) / 1000000;
			fps = 1000/elapsedTime;
			
		}
	}
	
	public void resume() {
		isRunning = true;
		renderThread = new Thread(this);
		renderThread.start();
	}
	
	public void pause() {
		isRunning = false;
		while(true) {
			try {
				renderThread.join();
				return;
			} catch(InterruptedException e) {
				//do nothing
			}
		}
	}
}
