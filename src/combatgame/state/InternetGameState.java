package combatgame.state;

import java.util.List;

import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.main.*;
import combatgame.network.Internet;
import combatgame.widgets.Button;
import combatgame.assets.*;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class InternetGameState extends GameState {

	private static final long serialVersionUID = 1L;

	private transient Button postButton, getButton;
	
	public InternetGameState(StateManager stateManager) {
		super(stateManager);
	}

	@Override
	public int getStateID() {
		return State.INTERNET;
	}
	
	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		events = postButton.update(events);
		events = getButton.update(events);
		
		if(postButton.state == Button.ACTIVATED) {
			//post
			postButton.disarm();
		}
		
		if(getButton.state == Button.ACTIVATED) {
			//get
			Log.i("combatgame", Internet.getJSON("http://www.newbillity.com/apitest.php"));
			getButton.disarm();
		}
		
		
	}

	@Override
	public void render(Graphics2D g, float delta) {
		postButton.render(g);
		getButton.render(g);
	}

	@Override
	public void pause(Context context, boolean saveData) {

	}

	@Override
	public void resume(StateManager stateManager) {
		this.stateManager = stateManager;
		AssetManager am = this.stateManager.getAssetManager();
		
		try {
			Bitmap internetBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/menu/online.png"));
			Bitmap internetBitmapArmed = BitmapFactory.decodeStream(am.open("images/menu/online_armed.png"));
			Bitmap hotSeatBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/menu/solo.png"));
			Bitmap hotSeatBitmapArmed = BitmapFactory.decodeStream(am.open("images/menu/solo_armed.png"));
			
			int internetButtonX = (Game.G_WIDTH / 2) - internetBitmapDisarmed.getWidth() / 2;
			int internetButtonY = (int) (Game.G_HEIGHT / 2 - (internetBitmapDisarmed.getHeight() - 50));
			int hotSeatButtonX = (Game.G_WIDTH / 2) - hotSeatBitmapDisarmed.getWidth() / 2;
			int hotSeatButtonY = (int) (Game.G_HEIGHT / 2 - (hotSeatBitmapDisarmed.getHeight() - 50) + internetBitmapDisarmed.getHeight() + internetBitmapDisarmed.getHeight());
			
			postButton = new Button(internetBitmapDisarmed, internetBitmapArmed, internetButtonX, internetButtonY);
			getButton = new Button(hotSeatBitmapDisarmed, hotSeatBitmapArmed, hotSeatButtonX, hotSeatButtonY);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void gameover() {
		
	}

	@Override
	public void dispose() {
		super.dispose();
		
		if(postButton != null) {
			postButton.recycle();
			postButton = null;
		}
		
		if(getButton != null) {
			getButton.recycle();
			getButton = null;
		}
	}
	
}
