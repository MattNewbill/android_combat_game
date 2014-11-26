package combatgame.state;

import combatgame.graphics.Graphics2D;
import combatgame.main.*;
import combatgame.widgets.*;
import combatgame.input.*;
import combatgame.state.*;
import java.util.List;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ConnectionState extends State {

	public static final int V_BUTTON_MARGIN = 10;
	
	Button internetButton;
	Button bluetoothButton;
	Button hotSeatButton;
	Button backButton;
	
	Bitmap background;
	
	public ConnectionState(StateManager sm) {
		super(sm);
		Game.shouldScale(true);
		
		AssetManager am = this.stateManager.getAssetManager();
		
		try {
			Bitmap internetBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/menu/online.png"));
			Bitmap internetBitmapArmed = BitmapFactory.decodeStream(am.open("images/menu/online_armed.png"));
			Bitmap bluetoothBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/menu/bluetooth.png"));
			Bitmap bluetoothBitmapArmed = BitmapFactory.decodeStream(am.open("images/menu/bluetooth_armed.png"));
			Bitmap hotSeatBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/menu/solo.png"));
			Bitmap hotSeatBitmapArmed = BitmapFactory.decodeStream(am.open("images/menu/solo_armed.png"));
			Bitmap backUnarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			
			int internetButtonX = (Game.G_WIDTH / 2) - internetBitmapDisarmed.getWidth() / 2;
			int internetButtonY = (int) (Game.G_HEIGHT / 2 - (internetBitmapDisarmed.getHeight() - 50));
			int bluetoothButtonX = (Game.G_WIDTH / 2) - bluetoothBitmapDisarmed.getWidth() / 2;
			int bluetoothButtonY = (int) (Game.G_HEIGHT / 2 - (bluetoothBitmapDisarmed.getHeight() - 50) + V_BUTTON_MARGIN + internetBitmapDisarmed.getHeight());
			int hotSeatButtonX = (Game.G_WIDTH / 2) - hotSeatBitmapDisarmed.getWidth() / 2;
			int hotSeatButtonY = (int) (Game.G_HEIGHT / 2 - (hotSeatBitmapDisarmed.getHeight() - 50) + (V_BUTTON_MARGIN * 2) + internetBitmapDisarmed.getHeight() + bluetoothBitmapDisarmed.getHeight());
			int backButtonX = Game.G_HEIGHT - hotSeatButtonY - hotSeatBitmapDisarmed.getHeight();
			int backButtonY = hotSeatButtonY;
			
			internetButton = new Button(internetBitmapDisarmed, internetBitmapArmed, internetButtonX, internetButtonY);
			bluetoothButton = new Button(bluetoothBitmapDisarmed, bluetoothBitmapArmed, bluetoothButtonX, bluetoothButtonY);
			hotSeatButton = new Button(hotSeatBitmapDisarmed, hotSeatBitmapArmed, hotSeatButtonX, hotSeatButtonY);
			backButton = new Button(backUnarmed, backArmed, backButtonX, backButtonY);
			
			background = BitmapFactory.decodeStream(am.open("images/menu/background.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		events = internetButton.update(events);
		events = bluetoothButton.update(events);
		events = hotSeatButton.update(events);
		events = backButton.update(events);
		
		if(internetButton.state == Button.ACTIVATED) {
			//new game over the internet
			internetButton.disarm();
			//stateManager.setState(new InternetGameState(stateManager));
		}
		else if(bluetoothButton.state == Button.ACTIVATED){
			//new game over bluetooth
			bluetoothButton.disarm();
			//stateManager.setState(new BluetoothGameState(stateManager));
		}
		else if(hotSeatButton.state == Button.ACTIVATED) {
			//new game with two players on one phone
			//stateManager.setState(new HotSeatState(stateManager, "maps/geometry/test_map.txt", "woodland", null));
			stateManager.setState(new MapSelectionState(stateManager));
		}
		else if(backButton.state == Button.ACTIVATED || stateManager.isBackPressed()) {
			//take us back to the main menu
			stateManager.setState(new MainMenuState(stateManager));
		}
		
	}

	@Override
	public void render(Graphics2D g, float delta) {
		//render background
		g.drawBitmap(background, 0, 0, null);
		
		//draw internet connection button
		internetButton.render(g);
		
		//draw bluetooth button
		bluetoothButton.render(g);
		
		//draw one phone, two player button
		hotSeatButton.render(g);
		
		//draw back button
		backButton.render(g);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		if(internetButton != null)
			internetButton.recycle();
		if(bluetoothButton != null)
			bluetoothButton.recycle();
		if(hotSeatButton != null)
			hotSeatButton.recycle();
		if(backButton != null)
			backButton.recycle();
	}
	
}
