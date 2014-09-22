package combatgame.main;

import combatgame.state.*;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.view.Menu;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.util.Log;

public class Game extends Activity implements StateManager {

	State currentState;
	RenderView renderView;
	WakeLock wakeLock;
	
	int width;
	int height;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //the optimal width and height
        Log.i("combatgame", ""+size.x);
        Log.i("combatgame", ""+size.y);
        width = size.x;
        height = size.y;
        //create the frame buffer that we draw everything to
        //we create the sprites and do all updates/draws relative to these coordinates
        //upscaling/downscaling happens at the hardware level afterwards
        Bitmap frameBuffer = Bitmap.createBitmap(width, height, Config.RGB_565);
        
        
        //add scaling factor here so we can translate the relative coordinates of our
        //framebuffer to the actual target phone's screen coordinates
        int scaleX;
        int scaleY;
        
        renderView = new RenderView(this, frameBuffer);
        currentState = getInitialState();
		setContentView(renderView);
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wake lock");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void setState(State state) {
		if(state == null) {
			throw new IllegalArgumentException("The freakin screen is null, idiot");
		}
		this.currentState.pause();
		this.currentState.dispose();
		state.resume();
		state.update(0);
		this.currentState = state;
	}
	
	@Override
	public State getCurrentState() {
		return currentState;
	}
	
	@Override
	public State getInitialState() {
		return new TestState(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		currentState.resume();
		renderView.resume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		currentState.pause();
		renderView.pause();
	}

}
