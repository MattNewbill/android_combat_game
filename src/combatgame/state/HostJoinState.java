package combatgame.state;

import java.util.List;

import org.json.JSONObject;

import combatgame.graphics.Graphics2D;
import combatgame.input.KeyboardManager;
import combatgame.input.TouchEvent;
import combatgame.io.PreferencesHelper;
import combatgame.main.*;
import combatgame.network.Internet;
import combatgame.widgets.Button;
import combatgame.widgets.EditText;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;

public class HostJoinState extends State {

	private static final long serialVersionUID = 1L;

	private transient KeyboardManager km;
	
	private transient Bitmap background;
	private transient Button hostButton, joinButton, createAccountButton, backButton;
	private transient EditText textField;
	
	private boolean isCreatingAccount = false;
	private boolean isHosting = false;
	
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
		
		if(isCreatingAccount) {
			textField.setFocus(km.hasFocus());
			if(textField.update(events))
				km.showKeyboard();
			events = createAccountButton.update(events);
		}
		else {
			events = hostButton.update(events);
			events = joinButton.update(events);
		}
		events = backButton.update(events);
		
		if(hostButton.state == Button.ACTIVATED) {
			hostButton.disarm();
			isHosting = true;
			checkAccountStatus();
		}
		else if(joinButton.state == Button.ACTIVATED) {
			joinButton.disarm();
			isHosting = false;
			checkAccountStatus();
		}
		else if(createAccountButton.state == Button.ACTIVATED) {
			createAccountButton.disarm();
			//hide keyboard
			km.hideKeyboard();
			if(!textField.getText().equals("")) {
				//create new account
				boolean status = createUser();
				if(status) {
					if(isHosting)
						host();
					else
						join();
				}
				else {
					//display account creation failed etc
				}
			}
		}
		else if(backButton.state == Button.ACTIVATED || stateManager.isBackPressed()) {
			backButton.disarm();
			if(isCreatingAccount) {
				km.hideKeyboard();
				isCreatingAccount = false;
				textField.clear();
			}
			else {
				stateManager.setState(new ConnectionState(stateManager));
			}
		}
		
	}

	@Override
	public void render(Graphics2D g, float delta) {
		//render background
		g.drawBitmap(background, 0, 0, null);
		
		//render buttons
		if(isCreatingAccount) {
			textField.render(g);
			createAccountButton.render(g);
		}
		else {
			hostButton.render(g);
			joinButton.render(g);
		}
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
			Bitmap createAccountButtonDisarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap createAccountButtonArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			Bitmap backButtonDisarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backButtonArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			
			int internetButtonX = (Game.G_WIDTH / 2) - hostBitmapDisarmed.getWidth() / 2;
			int internetButtonY = (int) (Game.G_HEIGHT / 2 - (hostBitmapDisarmed.getHeight() - 50));
			int hotSeatButtonX = (Game.G_WIDTH / 2) - joinBitmapDisarmed.getWidth() / 2;
			int hotSeatButtonY = (int) (Game.G_HEIGHT / 2 - (joinBitmapDisarmed.getHeight() - 50) + hostBitmapDisarmed.getHeight() + 10);
			int createAccountButtonX = Game.G_WIDTH / 2 - createAccountButtonDisarmed.getWidth() / 2;
			int createAccountButtonY = Game.G_HEIGHT / 2;
			int backButtonX = 40;
			int backButtonY = 555;
			
			hostButton = new Button(hostBitmapDisarmed, hostBitmapArmed, internetButtonX, internetButtonY);
			joinButton = new Button(joinBitmapDisarmed, joinBitmapArmed, hotSeatButtonX, hotSeatButtonY);
			createAccountButton = new Button(createAccountButtonDisarmed, createAccountButtonArmed, createAccountButtonX, createAccountButtonY);
			backButton = new Button(backButtonDisarmed, backButtonArmed, backButtonX, backButtonY);
			
			textField = new EditText("Enter gamer tag", Game.G_WIDTH / 2, Game.G_HEIGHT / 2-100, true, 15, Color.WHITE, Color.BLACK, Color.GRAY);
			
			km = new KeyboardManager(stateManager.getActivity());
			if(isCreatingAccount)
				km.showKeyboard();
			
			background = BitmapFactory.decodeStream(am.open("images/menu/background.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void keyEvent(KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			if(event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
				textField.removeCharacter();
			}
			else {
				char c = (char)event.getUnicodeChar();
				if(Character.isLetter(c) || Character.isDigit(c))
					textField.addCharacter(c);
			}
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
		if(createAccountButton != null) {
			createAccountButton.recycle();
			createAccountButton = null;
		}
		if(km != null) {
			km.destroy();
			km = null;
		}
		textField = null;
	}
	
	private void checkAccountStatus() {
		PreferencesHelper prefs = new PreferencesHelper(stateManager.getActivity());
		long id = prefs.getID();
		//id = 1L;
		//no id on record
		if(id == -1) {
			Log.i("combatgame", "no id");
			isCreatingAccount = true;
			km.showKeyboard();
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
						host();
					else
						join();
				}
				else {
					Log.i("combatgame", "not valid");
					isCreatingAccount = true;
					km.showKeyboard();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void host() {
		Log.i("combatgame", "host");
	}
	
	private void join() {
		Log.i("combatgame", "join");
	}
	
	private boolean createUser() {
		boolean success = false;
		try {
			JSONObject user = new JSONObject();
			user.put("name", textField.getText());
			String URL = "http://www.newbillity.com/android_combat_game_web/public/users/create_user";
			String resultString = Internet.postJSON(URL, user);
			
			JSONObject parsedResult = new JSONObject(resultString);
			long id = parsedResult.getLong("user_id");
			
			PreferencesHelper prefs = new PreferencesHelper(stateManager.getActivity());
			prefs.putNameAndID(textField.getText(), id);
			
			stateManager.setState(new HostJoinState(stateManager));
			
			success = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
}
