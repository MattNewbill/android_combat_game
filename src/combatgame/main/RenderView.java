package combatgame.main;

import combatgame.graphics.*;
import combatgame.util.Util;
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
	
	final int TARGET_FPS = 90;
	int targetTime = 1000 / TARGET_FPS;
	
	Paint fpsPaint;
	
	public RenderView(Game game, Bitmap frameBuffer) {
		super(game);
		this.game = game;
		this.frameBuffer = frameBuffer;
		holder = getHolder();
		drawingCanvas = new GraphicsCPU(frameBuffer);
		
		fpsPaint = new Paint();
		fpsPaint.setColor(Color.WHITE);
		fpsPaint.setTextSize(24);
	}

	public void run() {
		
		Rect destinationRect = new Rect();
		long startTimeFrame = System.nanoTime();
		long startTimeSleep;
		long fps = 0;
		int frames = 0;
		
		//debug
		long startTimeUpdate;
		long startTimeRender;
		long drawTime = 0;
		
		while(isRunning) {
			//make sure we have a surface to draw on
			if(!holder.getSurface().isValid()) {
				continue;
			}
			
			drawingCanvas.drawRGB(0, 0, 0); //clear the screen
			
			startTimeSleep = System.nanoTime();
			long delta = (System.nanoTime() - startTimeSleep) / 1000000;
			
			startTimeUpdate = System.currentTimeMillis();
			game.getCurrentState().update(delta); //update current state (screen)
			long endTimeUpdate = System.currentTimeMillis() - startTimeUpdate;
			
			startTimeRender = System.currentTimeMillis();
			game.getCurrentState().render(drawingCanvas, delta); //render current state (screen)
			drawingCanvas.drawText(Long.toString(fps), 30, 30, fpsPaint); //draw fps
			drawingCanvas.drawText("U: " + endTimeUpdate, 30, 50, fpsPaint);
			drawingCanvas.drawText("R: " + (System.currentTimeMillis() - startTimeRender), 30, 70, fpsPaint);
			drawingCanvas.drawText("D: " + drawTime, 30, 90, fpsPaint);
			
			long startTimeCleanup = System.currentTimeMillis();
			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(destinationRect);
			if(Game.isScaled())
				canvas.drawBitmap(frameBuffer, null, destinationRect, null); //scales and translate automatically to fit screen
			else
				//canvas.drawBitmap(frameBuffer, null, new Rect(0, 0, Game.G_WIDTH, Game.G_HEIGHT), null); //TODO: perhaps change this to P_WIDTH, P_HEIGHT....test on larger devices to see for sure
				canvas.drawBitmap(frameBuffer, 0, 0, null);
			holder.unlockCanvasAndPost(canvas);
			drawTime = System.currentTimeMillis() - startTimeCleanup;
			
			//other fps stuff
			long elapsedTime = (System.nanoTime() - startTimeFrame) / 1000000;
			frames++;
			if(elapsedTime > 1000) {
				fps = frames;
				frames = 0;
				startTimeFrame = System.nanoTime();
			}
			
			//sleep if we've rendered faster than our target tick time
			elapsedTime = (System.nanoTime() - startTimeSleep) / 1000000;
			if(elapsedTime < targetTime) {
				try {
					Thread.sleep(targetTime - elapsedTime);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
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
