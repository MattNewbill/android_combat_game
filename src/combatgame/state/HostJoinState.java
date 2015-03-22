package combatgame.state;

import java.util.List;

import org.json.JSONObject;

import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.io.PreferencesHelper;
import combatgame.main.*;
import combatgame.network.Internet;
import combatgame.widgets.Button;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HostJoinState extends State {

	private static final long serialVersionUID = 1L;

	private transient Bitmap background;
	private transient Button hostButton, joinButton, backButton;
	
	public HostJoinState(StateManager stateManager) {
		super(stateManager);
	}

	@Override
	public int getStateID() {
		return State.HOST_JOIN;
	}
	
	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		events = hostButton.update(events);
		events = joinButton.update(events);
		events = backButton.update(events);
		
		if(hostButton.state == Button.ACTIVATED) {
			hostButton.disarm();
			//imm.showSoftInput(stateManager.getActivity().getCurrentFocus(), 0);
			checkAccountStatus(true);
		}
		else if(joinButton.state == Button.ACTIVATED) {
			joinButton.disarm();
			checkAccountStatus(false);
		}
		else if(backButton.state == Button.ACTIVATED || stateManager.isBackPressed()) {
			backButton.disarm();
			stateManager.setState(new ConnectionState(stateManager));
		}
		
	}

	@Override
	public void render(Graphics2D g, float delta) {
		//render background
		g.drawBitmap(background, 0, 0, null);
		
		//render buttons
		hostButton.render(g);
		joinButton.render(g);
		backButton.render(g);
	}

	@Override
	public void pause(Context context, boolean saveData) {

	}

	@Override
	public void resume(StateManager stateManager) {
		this.stateManager = stateManager;
		AssetManager am = this.stateManager.getAssetManager();
		
		try {
			Bitmap hostBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/menu/online.png"));
			Bitmap hostBitmapArmed = BitmapFactory.decodeStream(am.open("images/menu/online_armed.png"));
			Bitmap joinBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/menu/solo.png"));
			Bitmap joinBitmapArmed = BitmapFactory.decodeStream(am.open("images/menu/solo_armed.png"));
			Bitmap backButtonDisarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backButtonArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			
			int internetButtonX = (Game.G_WIDTH / 2) - hostBitmapDisarmed.getWidth() / 2;
			int internetButtonY = (int) (Game.G_HEIGHT / 2 - (hostBitmapDisarmed.getHeight() - 50));
			int hotSeatButtonX = (Game.G_WIDTH / 2) - joinBitmapDisarmed.getWidth() / 2;
			int hotSeatButtonY = (int) (Game.G_HEIGHT / 2 - (joinBitmapDisarmed.getHeight() - 50) + hostBitmapDisarmed.getHeight() + 10);
			int backButtonX = 40;
			int backButtonY = 555;
			
			hostButton = new Button(hostBitmapDisarmed, hostBitmapArmed, internetButtonX, internetButtonY);
			joinButton = new Button(joinBitmapDisarmed, joinBitmapArmed, hotSeatButtonX, hotSeatButtonY);
			backButton = new Button(backButtonDisarmed, backButtonArmed, backButtonX, backButtonY);
			
			background = BitmapFactory.decodeStream(am.open("images/menu/background.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		
		if(hostButton != null) {
			hostButton.recycle();
			hostButton = null;
		}
		if(joinButton != null) {
			joinButton.recycle();
			joinButton = null;
		}
		if(backButton != null) {
			backButton.recycle();
			backButton = null;
		}
		if(background != null) {
			background.recycle();
			background = null;
		}
	}
	
	private void checkAccountStatus(boolean isHosting) {
		PreferencesHelper prefs = new PreferencesHelper(stateManager.getActivity());
		long id = prefs.getID();
		//id = 1L;
		//no id on record
		if(id == -1) {
			Log.i("combatgame", "no id");
			stateManager.setState(new CreateAccountState(stateManager));
		}
		else {
			//query database with id to see if it's valid
			Log.i("combatgame", "found id");
			String query = "http://www.newbillity.com/android_combat_game_web/public/users/get_is_valid_user_by_id/"+id;
			String response = Internet.getJSON(query);
			try {
				JSONObject parsedResponse = new JSONObject(response);
				boolean isValid = parsedResponse.getBoolean("is_valid");
				if(isValid) {
					Log.i("combatgame", "valid");
					if(isHosting)
						//take us to game creation
						;
					else
						//take us to server browser
						;
				}
				else {
					Log.i("combatgame", "not valid");
					stateManager.setState(new CreateAccountState(stateManager));
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
