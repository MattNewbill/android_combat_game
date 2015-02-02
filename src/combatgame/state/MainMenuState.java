package combatgame.state;

import combatgame.widgets.*;
import combatgame.input.*;
import combatgame.main.*;
import combatgame.graphics.*;
import combatgame.main.StateManager;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MainMenuState extends State {

	private static final long serialVersionUID = 1L;

	static final int V_BUTTON_MARGIN = 10;
	
	transient Button startButton;
	transient Button aboutButton;
	transient Button exitButton;
	
	transient Bitmap background;
	
	public MainMenuState(StateManager stateManager) {
		super(stateManager);
		Game.shouldScale(true);
	}

	@Override
	public int getStateID() {
		return State.MAIN_MENU;
	}
	
	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		events = startButton.update(events);
		events = aboutButton.update(events);
		events = exitButton.update(events);
		
		if(startButton.state == Button.ACTIVATED) {
			startButton.disarm();
			Log.i("combatgame", "start button pressed");
			//bring up the next screen, whether that's the connection screen or straight to the game screen
			stateManager.setState(new ConnectionState(stateManager));
		}
		else if(aboutButton.state == Button.ACTIVATED) {
			aboutButton.disarm();
			Log.i("combatgame", "about button pressed");
			//spash screen shamelessly promoting the hard work we've all put in
			stateManager.setState(new AboutState(stateManager));
		}
		else if(exitButton.state == Button.ACTIVATED || stateManager.isBackPressed()) {
			exitButton.disarm();
			Log.i("combatgame", "exiting game");
			//exit the app
			//not sure if this is considered bad form or not....guess we'll find out
			stateManager.getActivity().finish();
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		//render background
		g.drawBitmap(background, 0, 0, null);
		
		//draw start button
		startButton.render(g);
		
		//draw about button
		aboutButton.render(g);
		
		//draw exit button
		exitButton.render(g);
	}

	@Override
	public void pause(Context context, boolean saveData) {
		
	}

	@SuppressLint("NewApi")
	@Override
	public void resume(StateManager stateManager) {
		this.stateManager = stateManager;
		AssetManager am = stateManager.getAssetManager();
		
		try {
			//load images
			Bitmap startButtonDisarmed = BitmapFactory.decodeStream(am.open("images/menu/start_button.png"));
			Bitmap aboutButtonDisarmed = BitmapFactory.decodeStream(am.open("images/menu/about_button.png"));
			Bitmap exitButtonDisarmed = BitmapFactory.decodeStream(am.open("images/menu/exit_button.png"));
			
			Bitmap startButtonArmed = BitmapFactory.decodeStream(am.open("images/menu/start_button_armed.png"));
			Bitmap aboutButtonArmed = BitmapFactory.decodeStream(am.open("images/menu/about_button_armed.png"));
			Bitmap exitButtonArmed = BitmapFactory.decodeStream(am.open("images/menu/exit_button_armed.png"));
			
			Log.i("combatgame", ""+startButtonDisarmed.getByteCount());
			Log.i("combatgame", ""+startButtonArmed.getByteCount());
			Log.i("combatgame", ""+aboutButtonDisarmed.getByteCount());
			Log.i("combatgame", ""+aboutButtonArmed.getByteCount());
			Log.i("combatgame", ""+exitButtonDisarmed.getByteCount());
			Log.i("combatgame", ""+exitButtonArmed.getByteCount());
			
			//determine button coordinates
			int startButtonX = (Game.G_WIDTH / 2) - startButtonDisarmed.getWidth() / 2;
			int startButtonY = (int) (Game.G_HEIGHT / 2 - (startButtonDisarmed.getHeight() - 50));
			int aboutButtonX = (Game.G_WIDTH / 2) - aboutButtonDisarmed.getWidth() / 2;
			int aboutButtonY = (int) (Game.G_HEIGHT / 2 - (aboutButtonDisarmed.getHeight() - 50) + V_BUTTON_MARGIN + startButtonDisarmed.getHeight());
			int exitButtonX = (Game.G_WIDTH / 2) - exitButtonDisarmed.getWidth() / 2;
			int exitButtonY = (int) (Game.G_HEIGHT / 2 - (exitButtonDisarmed.getHeight() - 50) + (V_BUTTON_MARGIN * 2) + startButtonDisarmed.getHeight() + aboutButtonDisarmed.getHeight());
			
			//create actual buttons
			startButton = new Button(startButtonDisarmed, startButtonArmed, startButtonX, startButtonY);
			aboutButton = new Button(aboutButtonDisarmed, aboutButtonArmed, aboutButtonX, aboutButtonY);
			exitButton = new Button(exitButtonDisarmed, exitButtonArmed, exitButtonX, exitButtonY);
			
			background = BitmapFactory.decodeStream(am.open("images/menu/background.png"));
			Log.i("combatgame", ""+background.getByteCount());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if(startButton != null) {
			startButton.recycle();
			startButton = null;
		}
		if(aboutButton != null) {
			aboutButton.recycle();
			aboutButton = null;
		}
		if(exitButton != null) {
			exitButton.recycle();
			exitButton = null;
		}
		if(background != null) {
			background.recycle();
			background = null;
		}
	}

}
