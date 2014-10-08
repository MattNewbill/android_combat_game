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

/**
 * **HAPPY**
 */

public class ConnectionState extends State {

	public static final int V_BUTTON_MARGIN = 10;
	
	Button internetButton;
	Button bluetoothButton;
	Button hotSeatButton;
	
	public ConnectionState(StateManager sm) {
		super(sm);
		this.stateManager.shouldScale(true);
		
		AssetManager am = this.stateManager.getAssetManager();
		
		try {
			Bitmap internetBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/connection_menu/online_original.png"));
			Bitmap internetBitmapArmed = BitmapFactory.decodeStream(am.open("images/connection_menu/online_original_armed.png"));
			Bitmap bluetoothBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/connection_menu/bluetooth_original.png"));
			Bitmap bluetoothBitmapArmed = BitmapFactory.decodeStream(am.open("images/connection_menu/bluetooth_original_armed.png"));
			Bitmap hotSeatBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/connection_menu/solo_original.png"));
			Bitmap hotSeatBitmapArmed = BitmapFactory.decodeStream(am.open("images/connection_menu/solo_original_armed.png"));
			
			int internetButtonX = (Game.G_WIDTH / 2) - internetBitmapDisarmed.getWidth() / 2;
			int internetButtonY = (int) (Game.G_HEIGHT / 2 - (internetBitmapDisarmed.getHeight() * 1.5));
			int bluetoothButtonX = (Game.G_WIDTH / 2) - bluetoothBitmapDisarmed.getWidth() / 2;
			int bluetoothButtonY = (int) (Game.G_HEIGHT / 2 - (bluetoothBitmapDisarmed.getHeight() * 1.5) + V_BUTTON_MARGIN + internetBitmapDisarmed.getHeight());
			int hotSeatButtonX = (Game.G_WIDTH / 2) - hotSeatBitmapDisarmed.getWidth() / 2;
			int hotSeatButtonY = (int) (Game.G_HEIGHT / 2 - (hotSeatBitmapDisarmed.getHeight() * 1.5) + (V_BUTTON_MARGIN * 2) + internetBitmapDisarmed.getHeight() + bluetoothBitmapDisarmed.getHeight());
			
			internetButton = new Button(internetBitmapDisarmed, internetBitmapArmed, internetButtonX, internetButtonY);
			bluetoothButton = new Button(bluetoothBitmapDisarmed, bluetoothBitmapArmed, bluetoothButtonX, bluetoothButtonY);
			hotSeatButton = new Button(hotSeatBitmapDisarmed, hotSeatBitmapArmed, hotSeatButtonX, hotSeatButtonY);
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
		
		if(internetButton.state == Button.ACTIVATED) {
			//new game over the internet
			stateManager.setState(new InternetGameState(stateManager));
		}
		else if(bluetoothButton.state == Button.ACTIVATED){
			//new game over bluetooth
			stateManager.setState(new BluetoothGameState(stateManager));
		}
		else if(hotSeatButton.state == Button.ACTIVATED) {
			//new game with two players on one phone
			stateManager.setState(new HotSeatState(stateManager));
		}
		
	}

	@Override
	public void render(Graphics2D g, float delta) {
		//draw internet connection button
		internetButton.render(g);
		
		//draw bluetooth button
		bluetoothButton.render(g);
		
		//draw one phone, two player button
		hotSeatButton.render(g);
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
	}
	
}
