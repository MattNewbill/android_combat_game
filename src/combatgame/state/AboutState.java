package combatgame.state;

import combatgame.graphics.Graphics2D;
import combatgame.main.*;
import combatgame.input.TouchEvent;
import combatgame.widgets.Button;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AboutState extends State implements Serializable {

	private static final long serialVersionUID = -2419789858256322322L;
	transient Button backButton;
	transient Bitmap contributors;
	transient Bitmap awesomeContributors;
	
	int contributorsX, contributorsY;
	
	public AboutState(StateManager stateManager) {
		super(stateManager);
		Game.shouldScale(true);
	}
	
	@Override
	public int getStateID() {
		return State.ABOUT;
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		events = backButton.update(events);
		
		if(backButton.state == Button.ACTIVATED || stateManager.isBackPressed())
			stateManager.setState(new MainMenuState(stateManager));
	}

	@Override
	public void render(Graphics2D g, float delta) {
		//draw back button
		backButton.render(g);
		
		//draw awesome contributors
		//g.drawBitmap(contributors, contributorsX, contributorsY, null);
		g.drawBitmap(awesomeContributors, 0, 0, null);
	}

	@Override
	public void pause(Context context, boolean saveData) {

	}

	@Override
	public void resume(StateManager stateManager) {
		this.stateManager = stateManager;
		
		//load assets
		AssetManager am = stateManager.getAssetManager();
		try {		
			Bitmap backUnarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			contributors = BitmapFactory.decodeStream(am.open("images/about_us/awesome_contributors.png"));
			awesomeContributors = BitmapFactory.decodeStream(am.open("images/about_us/even_more_awesome_contributors.png"));
			
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
	public void dispose() {
		super.dispose();
		if(backButton != null) {
			backButton.recycle();
			backButton = null;
		}
		if(contributors != null) {
			contributors.recycle();
			contributors = null;
		}
		if(awesomeContributors != null) {
			awesomeContributors.recycle();
			awesomeContributors = null;
		}
	}
	
}
