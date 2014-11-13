package combatgame.state;

import combatgame.graphics.Graphics2D;
import combatgame.main.*;
import combatgame.input.TouchEvent;
import combatgame.util.*;
import combatgame.widgets.Button;

import java.util.List;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * **HAPPY**
 */

public class AboutState extends State {

	Button backButton;
	Bitmap contributors;
	
	int contributorsX, contributorsY;
	
	public AboutState(StateManager stateManager) {
		super(stateManager);
		Game.shouldScale(true);
		
		//load assets
		AssetManager am = stateManager.getAssetManager();
		try {		
			Bitmap backUnarmed = BitmapFactory.decodeStream(am.open("images/about_us/back_button.png"));
			Bitmap backArmed = BitmapFactory.decodeStream(am.open("images/about_us/back_button_armed.png"));
			contributors = BitmapFactory.decodeStream(am.open("images/about_us/awesome_contributors.png"));
			
			int backButtonX = 0;
			int backButtonY = Game.G_HEIGHT - backUnarmed.getHeight();
			contributorsX = Game.G_WIDTH / 2 - contributors.getWidth() / 2;
			contributorsY = Game.G_HEIGHT / 2 - contributors.getHeight() / 2;
			
			backButton = new Button(backUnarmed, backArmed, backButtonX, backButtonY);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		events = backButton.update(events);
		
		if(backButton.state == Button.ACTIVATED)
			stateManager.setState(new MainMenuState(stateManager));
	}

	@Override
	public void render(Graphics2D g, float delta) {
		//draw back button
		backButton.render(g);
		
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
