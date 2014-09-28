package combatgame.state;

import combatgame.util.Util;
import combatgame.input.*;
import combatgame.main.*;
import combatgame.graphics.*;
import combatgame.main.StateManager;
import java.util.List;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MainMenuState extends State {

	static final int V_BUTTON_MARGIN = 10;
	
	Bitmap startButton;
	Bitmap aboutButton;
	Bitmap exitButton;
	
	int startButtonX, startButtonY;
	int aboutButtonX, aboutButtonY;
	int exitButtonX, exitButtonY;
	
	public MainMenuState(StateManager stateManager) {
		super(stateManager);
		this.stateManager.shouldScale(true);
		AssetManager am = stateManager.getAssetManager();
		//load main menu assets
		try {
			startButton = BitmapFactory.decodeStream(am.open("images/main_menu/start_button_original.png"));
			aboutButton = BitmapFactory.decodeStream(am.open("images/main_menu/about_button_original.png"));
			exitButton = BitmapFactory.decodeStream(am.open("images/main_menu/exit_button_original.png"));
			
			startButtonX = (Game.G_WIDTH / 2) - startButton.getWidth() / 2;
			startButtonY = (int) (Game.G_HEIGHT / 2 - (startButton.getHeight() * 1.5));
			aboutButtonX = (Game.G_WIDTH / 2) - aboutButton.getWidth() / 2;
			aboutButtonY = (int) (Game.G_HEIGHT / 2 - (aboutButton.getHeight() * 1.5) + V_BUTTON_MARGIN + startButton.getHeight());
			exitButtonX = (Game.G_WIDTH / 2) - exitButton.getWidth() / 2;
			exitButtonY = (int) (Game.G_HEIGHT / 2 - (exitButton.getHeight() * 1.5) + (V_BUTTON_MARGIN * 2) + startButton.getHeight() + aboutButton.getHeight());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		for(int i = 0; i < events.size(); i++) {
			if(events.get(i).type == TouchEvent.TOUCH_UP) {
				if(Util.isInBounds(events.get(i), startButtonX, startButtonY, startButton.getWidth(), startButton.getHeight())) {
					Log.i("combatgame", "start button pressed");
					//bring up the next screen, whether that's the connection screen or straight to the game screen
					stateManager.setState(new TestState(stateManager));
				}
				else if(Util.isInBounds(events.get(i), aboutButtonX, aboutButtonY, aboutButton.getWidth(), aboutButton.getHeight())) {
					Log.i("combatgame", "about button pressed");
					//spash screen shamelessly promoting the hard work we've all put in
					stateManager.setState(new AboutState(stateManager));
				}
				else if(Util.isInBounds(events.get(i), exitButtonX, exitButtonY, exitButton.getWidth(), exitButton.getHeight())) {
					Log.i("combatgame", "exit button pressed");
					//exit the app
					//not sure if this is considered bad form or not....guess we'll find out
					System.exit(0);
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		//draw start button
		g.drawBitmap(startButton, startButtonX, startButtonY, null);
		
		//draw about button
		g.drawBitmap(aboutButton, aboutButtonX, aboutButtonY, null);
		
		//draw exit button
		g.drawBitmap(exitButton, exitButtonX, exitButtonY, null);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		if(startButton != null)
			startButton.recycle();
		if(aboutButton != null)
			aboutButton.recycle();
		if(exitButton != null)
			exitButton.recycle();
	}

}
