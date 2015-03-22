package combatgame.state;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.io.PreferencesHelper;
import combatgame.main.Game;
import combatgame.main.StateManager;
import combatgame.network.Internet;
import combatgame.widgets.Button;
import combatgame.widgets.EditText;

public class CreateAccountState extends State {

	private static final long serialVersionUID = 1L;

	private transient InputMethodManager imm;
	
	private transient Button createAccountButton, backButton;
	private transient EditText textField;
	
	public CreateAccountState(StateManager stateManager) {
		super(stateManager);
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		
		textField.setFocus(imm.isActive());
		
		createAccountButton.update(events);
		backButton.update(events);
		
		if(createAccountButton.state == Button.ACTIVATED) {
			createAccountButton.disarm();
			if(!textField.getText().equals("")) {
				//create new account
				createUser();
			}
		}
		else if(backButton.state == Button.ACTIVATED || stateManager.isBackPressed()) {
			backButton.disarm();
			stateManager.setState(new HostJoinState(stateManager));
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		createAccountButton.render(g);
		backButton.render(g);
		textField.render(g);
	}

	@Override
	public void pause(Context context, boolean saveData) {
		
	}

	@Override
	public void keyEvent(KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			if(event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
				textField.removeCharacter();
			}
			else {
				char c = (char)event.getUnicodeChar();
				if(Character.isLetter(c))
					textField.addCharacter(c);
			}
		}
	}
	
	@Override
	public void resume(StateManager stateManager) {
		this.stateManager = stateManager;
		AssetManager am = stateManager.getAssetManager();
		
		try {
			Bitmap createAccountButtonDisarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap createAccountButtonArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			Bitmap backButtonDisarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backButtonArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			
			int createAccountButtonX = Game.G_WIDTH / 2 - createAccountButtonDisarmed.getWidth() / 2;
			int createAccountButtonY = Game.G_HEIGHT / 2;
			int backButtonX = 40;
			int backButtonY = 555;
			
			createAccountButton = new Button(createAccountButtonDisarmed, createAccountButtonArmed, createAccountButtonX, createAccountButtonY);
			backButton = new Button(backButtonDisarmed, backButtonArmed, backButtonX, backButtonY);
			
			textField = new EditText("Enter game name", Game.G_WIDTH / 2, Game.G_HEIGHT / 2-100, true, 15, Color.WHITE, Color.BLACK, Color.GRAY);
			
			imm = (InputMethodManager) stateManager.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
			imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		} catch(Exception e) {
			
		}
	}

	@Override
	public void dispose() {
		if(createAccountButton != null) {
			createAccountButton.recycle();
			createAccountButton = null;
		}
		if(backButton != null) {
			backButton.recycle();
			backButton = null;
		}
		textField = null;
	}
	
	@Override
	public int getStateID() {
		return State.CREATE_ACCOUNT;
	}
	
	private void createUser() {
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
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
