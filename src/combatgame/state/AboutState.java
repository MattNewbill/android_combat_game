package combatgame.state;

import combatgame.graphics.Graphics2D;
import combatgame.main.*;
import combatgame.input.TouchEvent;
import combatgame.util.*;
import java.util.List;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * **HAPPY**
 */

public class AboutState extends State {

	Bitmap backButton;
	Bitmap contributors;
	
	int backButtonX, backButtonY;
	int contributorsX, contributorsY;
	
	public AboutState(StateManager stateManager) {
		super(stateManager);
		this.stateManager.shouldScale(true);
		
		//load assets
		AssetManager am = stateManager.getAssetManager();
		try {
			backButton = BitmapFactory.decodeStream(am.open("images/about_us/back_button_original.png"));
			contributors = BitmapFactory.decodeStream(am.open("images/about_us/awesome_contributors.png"));
			
			backButtonX = 0;
			backButtonY = Game.G_HEIGHT - backButton.getHeight();
			contributorsX = Game.G_WIDTH / 2 - contributors.getWidth() / 2;
			contributorsY = Game.G_HEIGHT / 2 - contributors.getHeight() / 2;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		for(int i = 0; i < events.size(); i++) {
			if(events.get(i).type == TouchEvent.TOUCH_UP) {
				if(Util.isInBounds(events.get(i), backButtonX, backButtonY, backButton.getWidth(), backButton.getHeight())) {
					stateManager.setState(new MainMenuState(stateManager));
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		//draw back button
		g.drawBitmap(backButton, backButtonX, backButtonY, null);
		
		//draw awesome contributors
		g.drawBitmap(contributors, contributorsX, contributorsY, null);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		if(backButton != null)
			backButton.recycle();
		if(contributors != null)
			contributors.recycle();
	}
	
}
