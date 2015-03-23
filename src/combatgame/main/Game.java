package combatgame.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import combatgame.input.TouchHandler;
import combatgame.state.*;
import android.os.Bundle;
import android.os.PowerManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.util.Log;

public class Game extends Activity implements StateManager {

	State currentState;
	RenderView renderView;
	PowerManager.WakeLock wakeLock;
	
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
	
	private boolean configurationChanged = false;
	private boolean isStartUp = true;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
        if(savedInstanceState != null) {
        	configurationChanged = true;
        	isStartUp = false;
        }
        else {
        	isStartUp = true;
        }
        
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
		
		if(renderView != null)
			renderView.destroy();
		renderView = new RenderView(this, frameBuffer);
		
		touchHandler = new TouchHandler(this, renderView, scaleX, scaleY);
		
		setContentView(renderView);
	}

	@Override
	public View getCurrentFocus() {
		return renderView;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		currentState.keyEvent(event);
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		return false;
	}
	
	public int getScreenOrientation() {
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(P_WIDTH < P_HEIGHT)
            orientation = Configuration.ORIENTATION_PORTRAIT;
        else
             orientation = Configuration.ORIENTATION_LANDSCAPE;
	    return orientation;
	}
	
	@Override
	public void setState(State state) {
		if(state == null) {
			throw new IllegalArgumentException("The freakin screen is null, idiot");
		}
		isBackPressed = false;
		this.currentState.pause(this, false);
		this.currentState.dispose();
		state.resume(this);
		state.update(0);
		this.currentState = state;
	}
	
	@Override
	public Activity getActivity() {
		return this;
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
		boolean temp = isBackPressed;
		isBackPressed = false;
		return temp;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i("combatgame", "on resume");
		isBackPressed = false;
		wakeLock.acquire();
		if(isStartUp) {
			Log.i("combatgame", "startup if");
			isStartUp = false;
			currentState = getInitialState();
			deleteCache();
		}
		else {
			try {
				Log.i("combatgame", "try loading cache");
				//read in which state we were last at so we know what to cast our stream as
				FileInputStream finput = openFileInput("state");
			    ObjectInputStream oinput = new ObjectInputStream(finput);
			    StateWrapper sw = (StateWrapper) oinput.readObject();
			    finput.close();
			    oinput.close();
			    
				FileInputStream fis = openFileInput("storage");
			    ObjectInputStream ois = new ObjectInputStream(fis);
			    
			    Object temp = ois.readObject();
			    fis.close();
			    ois.close();
			    
			    //cast our stream depending on which state we left off at
			    switch(sw.getState()) {
			    	case State.MAIN_MENU:
			    		currentState = (MainMenuState) temp;
			    		break;
			    	case State.ABOUT:
			    		currentState = (AboutState) temp;
			    		break;
			    	case State.CONNECTION:
			    		currentState = (ConnectionState) temp;
			    		break;
			    	case State.BLUETOOTH:
			    		currentState = (BluetoothGameState) temp;
			    		break;
			    	case State.HOST_JOIN:
			    		currentState = (HostJoinState) temp;
			    		break;
			    	case State.HOT_SEAT:
			    		currentState = (HotSeatState) temp;
			    		break;
			    	case State.MAP_SELECTION:
			    		currentState = (MapSelectionState) temp;
			    		break;
			    	case State.GAMEMODE_SELECTION:
			    		currentState = (GamemodeSelectionState) temp;
			    		break;
		    		default:
		    			throw new IllegalArgumentException("Invalid state");
			    }
			} catch(FileNotFoundException e) {
				Log.i("combatgame", "cache not found");
			} catch (Exception e) {
				Log.i("combatgame", "weird catch");
				e.printStackTrace();
				currentState = getInitialState();
			} finally {
				if(configurationChanged && getScreenOrientation() != Configuration.ORIENTATION_LANDSCAPE) {
					Log.i("combatgame", "config changed finally");
					configurationChanged = false;
				}
				else {
					deleteCache();
				}
			}
		}
		currentState.resume(this);
		renderView.resume();
	}
	
	public void deleteCache() {
		Log.i("combatgame", "deleting cache");
		deleteFile("storage");
		deleteFile("state");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.i("combatgame", "on pause");
		wakeLock.release();
		currentState.pause(this, true);
		renderView.pause();
		currentState.dispose();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("combatgame", "on destroy");
		//delete the cache if we are closing the app
		if(isFinishing()) {
			deleteCache();
		}
		//if the game isn't closing up serialize the current state so we can pick up where we left off
		else {
			Log.i("combatgame", "saving state");
			try {
				//save the state we are leaving off at
				FileOutputStream fout = openFileOutput("state", Context.MODE_PRIVATE);
				ObjectOutputStream oout = new ObjectOutputStream(fout);
				oout.writeObject(new StateWrapper(currentState.getStateID()));
				oout.close();
				fout.close();
				
				//save the actual state data
				FileOutputStream fos = openFileOutput("storage", Context.MODE_PRIVATE);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(currentState);
				oos.close();
				fos.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
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