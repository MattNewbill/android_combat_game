package combatgame.main;

import combatgame.input.TouchHandler;
import combatgame.state.*;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.util.Log;

/**
 * **NOT HAPPY**
 * TODO: Find a way to propogate back button presses to each state in a clean way
 */

public class Game extends Activity implements StateManager {

	State currentState;
	RenderView renderView;
	WakeLock wakeLock;
	
	TouchHandler touchHandler;
	AssetManager assetManager;
	
	//should we scale and translate the rendered iamge to
	//fit the phone's screen
	static boolean shouldScale = false;
	
	//width and height that the game is optimized for and
	//that positioning is calculated for
	public static final int G_WIDTH = 1280;
	public static final int G_HEIGHT = 720;
	
	//actual width and height of phone
	public static int P_WIDTH;
	public static int P_HEIGHT;
	
	//scale of the current phone in relation to our target phone
	public static double scaleX;
	public static double scaleY;
	
	//was the back button pressed
	private static boolean isBackPressed = false;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        
        Display display = getWindowManager().getDefaultDisplay();
        if(android.os.Build.VERSION.SDK_INT >= 13) {
             Point size = new Point();
             display.getSize(size);
             P_WIDTH = size.x;
             P_HEIGHT = size.y;
        }
        else {
        	P_WIDTH = display.getWidth();
        	P_HEIGHT = display.getHeight();
        }
       
        Log.i("combatgame", ""+P_WIDTH);
        Log.i("combatgame", ""+P_HEIGHT);
        
        //create the frame buffer that we draw everything to
        //we create the sprites and do all updates/draws relative to these coordinates
        //upscaling/downscaling happens at the hardware level afterwards
        Bitmap frameBuffer = Bitmap.createBitmap(G_WIDTH, G_HEIGHT, Config.ARGB_8888);
        
        
        //add scaling factor here so we can translate the relative coordinates of our
        //framebuffer to the actual target phone's screen coordinates
        scaleX = G_WIDTH / (double)P_WIDTH;
        scaleY = G_HEIGHT / (double)P_HEIGHT;
        
        Log.i("combatgame", "scale x: " +scaleX);
        Log.i("combatgame", "scale y: " +scaleY);
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wake lock");
		
		assetManager = getAssets();
		
		currentState = getInitialState();
		renderView = new RenderView(this, frameBuffer);
		
		touchHandler = new TouchHandler(this, renderView, scaleX, scaleY);
		
		setContentView(renderView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void setState(State state) {
		if(state == null) {
			throw new IllegalArgumentException("The freakin screen is null, idiot");
		}
		isBackPressed = false;
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
		return new MainMenuState(this);
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		isBackPressed = true;
	}
	
	@Override
	public boolean isBackPressed() {
		return isBackPressed;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		isBackPressed = false;
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
		if(isFinishing()) {
			currentState.dispose();
		}
	}
	
	@Override
	public AssetManager getAssetManager() {
		return assetManager;
	}
	
	@Override
	public TouchHandler getTouchHandler() {
		return touchHandler;
	}
	
	public static void shouldScale(boolean scale) {
		shouldScale = scale;
	}
	
	public static boolean isScaled() {
		return shouldScale;
	}

}
