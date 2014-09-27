package combatgame.main;

import combatgame.input.TouchHandler;
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
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.util.Log;

public class Game extends Activity implements StateManager {

	State currentState;
	RenderView renderView;
	WakeLock wakeLock;
	
	TouchHandler touchHandler;
	AssetManager assetManager;
	
	//should we scale and translate the rendered iamge to
	//fit the phone's screen
	boolean shouldScale = false;
	
	//width and height that the game is optimized for and
	//that positioning is calculated for
	public static final int G_WIDTH = 1280;
	public static final int G_HEIGHT = 720;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        
        //this is only available >= API 13
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        
        //the optimal width and height
        Log.i("combatgame", ""+size.x);
        Log.i("combatgame", ""+size.y);
        
        //create the frame buffer that we draw everything to
        //we create the sprites and do all updates/draws relative to these coordinates
        //upscaling/downscaling happens at the hardware level afterwards
        Bitmap frameBuffer = Bitmap.createBitmap(G_WIDTH, G_HEIGHT, Config.RGB_565);
        
        
        //add scaling factor here so we can translate the relative coordinates of our
        //framebuffer to the actual target phone's screen coordinates
        double scaleX = G_WIDTH / (double)size.x;
        double scaleY = G_HEIGHT / (double)size.y;
        
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
	
	@Override
	public void shouldScale(boolean shouldScale) {
		this.shouldScale = shouldScale;
	}
	
	@Override
	public boolean isScaled() {
		return shouldScale;
	}

}
