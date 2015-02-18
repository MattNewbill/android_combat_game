package combatgame.state;

import combatgame.graphics.Graphics2D;
import combatgame.main.*;
import combatgame.network.Internet;
import combatgame.widgets.*;
import combatgame.input.*;

import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class ConnectionState extends State {

	private static final long serialVersionUID = 1L;

	public static final int V_BUTTON_MARGIN = 10;
	
	transient Button internetButton;
	transient Button bluetoothButton;
	transient Button hotSeatButton;
	transient Button backButton;
	
	boolean isBluetoothFeatureNotAvailableDialogShowing = false;
	boolean isOnlineBattleFeatureNotAvailableDialogShowing = false;
	
	transient Bitmap background;

	private transient Button onlineBattleOkButton;
	private transient Button bluetoothOkButton;
	private transient Paint exitDialogPaint;
	private transient Bitmap onlineSoon;
	private transient Bitmap bluetoothSoon;
	
	public ConnectionState(StateManager sm) {
		super(sm);
	}

	@Override
	public int getStateID() {
		return State.CONNECTION;
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
			Log.i("combatgame", Internet.getJSON("http://www.newbillity.com/apitest.php"));
			isOnlineBattleFeatureNotAvailableDialogShowing = true;
			internetButton.disarm();
			//stateManager.setState(new InternetGameState(stateManager));
		}
		else if(bluetoothButton.state == Button.ACTIVATED){
			//new game over bluetooth
			isBluetoothFeatureNotAvailableDialogShowing = true;
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
		
		if(isBluetoothFeatureNotAvailableDialogShowing) {
			events = bluetoothOkButton.update(events);
			if(bluetoothOkButton.state == Button.ACTIVATED) {
				bluetoothOkButton.disarm();
				isBluetoothFeatureNotAvailableDialogShowing = false;
				internetButton.enable();
				bluetoothButton.enable();
				hotSeatButton.enable();
				backButton.enable();
			}
		}
		
		if(isOnlineBattleFeatureNotAvailableDialogShowing) {
			events = onlineBattleOkButton.update(events);
			if(onlineBattleOkButton.state == Button.ACTIVATED) {
				onlineBattleOkButton.disarm();
				isOnlineBattleFeatureNotAvailableDialogShowing = false;
				internetButton.enable();
				bluetoothButton.enable();
				hotSeatButton.enable();
				backButton.enable();
			}
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
		
		if(isBluetoothFeatureNotAvailableDialogShowing){
			g.drawRect(0, 0, Game.G_WIDTH, Game.G_HEIGHT, exitDialogPaint);
			g.drawBitmap(bluetoothSoon, Game.G_WIDTH / 2 - bluetoothSoon.getWidth() / 2, Game.G_HEIGHT / 2 - bluetoothSoon.getHeight() / 2, null); //TODO: scale for larger devices
			bluetoothOkButton.render(g);
			internetButton.disable();
			bluetoothButton.disable();
			hotSeatButton.disable();
			backButton.disable();
			
		}
		if(isOnlineBattleFeatureNotAvailableDialogShowing){
			g.drawRect(0, 0, Game.G_WIDTH, Game.G_HEIGHT, exitDialogPaint);
			g.drawBitmap(onlineSoon, Game.G_WIDTH / 2 - onlineSoon.getWidth() / 2, Game.G_HEIGHT / 2 - onlineSoon.getHeight() / 2, null); //TODO: scale for larger devices
			onlineBattleOkButton.render(g);
			internetButton.disable();
			bluetoothButton.disable();
			hotSeatButton.disable();
			backButton.disable();
		}
	}

	@Override
	public void pause(Context context, boolean saveData) {

	}

	@Override
	public void resume(StateManager stateManager) {
		Game.shouldScale(true);
		this.stateManager = stateManager;
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
			
			Bitmap onlineBattleOkArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/ok_button_armed.png"));
			Bitmap onlineBattleOkDisarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/ok_button.png"));
			Bitmap bluetoothOkArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/ok_button_armed.png"));;
			Bitmap bluetoothOkDisarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/ok_button.png"));
			onlineSoon = BitmapFactory.decodeStream(am.open("images/menu/online_soon.png"));
			bluetoothSoon = BitmapFactory.decodeStream(am.open("images/menu/bluetooth_soon.png"));
			
			int internetButtonX = (Game.G_WIDTH / 2) - internetBitmapDisarmed.getWidth() / 2;
			int internetButtonY = (int) (Game.G_HEIGHT / 2 - (internetBitmapDisarmed.getHeight() - 50));
			int bluetoothButtonX = (Game.G_WIDTH / 2) - bluetoothBitmapDisarmed.getWidth() / 2;
			int bluetoothButtonY = (int) (Game.G_HEIGHT / 2 - (bluetoothBitmapDisarmed.getHeight() - 50) + V_BUTTON_MARGIN + internetBitmapDisarmed.getHeight());
			int hotSeatButtonX = (Game.G_WIDTH / 2) - hotSeatBitmapDisarmed.getWidth() / 2;
			int hotSeatButtonY = (int) (Game.G_HEIGHT / 2 - (hotSeatBitmapDisarmed.getHeight() - 50) + (V_BUTTON_MARGIN * 2) + internetBitmapDisarmed.getHeight() + bluetoothBitmapDisarmed.getHeight());
			int backButtonX = Game.G_HEIGHT - hotSeatButtonY - hotSeatBitmapDisarmed.getHeight();
			int backButtonY = hotSeatButtonY;
			int onlineBattleOkButtonX = Game.G_WIDTH/2 - onlineBattleOkArmed.getWidth()/2 ;
			int onlineBattleOkButtonY = Game.G_HEIGHT - onlineBattleOkArmed.getHeight()- 150;
			int bluetoothOkButtonX = Game.G_WIDTH/2 - onlineBattleOkArmed.getWidth()/2;
			int bluetoothOkButtonY = Game.G_HEIGHT - onlineBattleOkArmed.getHeight() - 150;
			
			internetButton = new Button(internetBitmapDisarmed, internetBitmapArmed, internetButtonX, internetButtonY);
			bluetoothButton = new Button(bluetoothBitmapDisarmed, bluetoothBitmapArmed, bluetoothButtonX, bluetoothButtonY);
			hotSeatButton = new Button(hotSeatBitmapDisarmed, hotSeatBitmapArmed, hotSeatButtonX, hotSeatButtonY);
			backButton = new Button(backUnarmed, backArmed, backButtonX, backButtonY);
			onlineBattleOkButton = new Button(onlineBattleOkDisarmed,onlineBattleOkArmed , onlineBattleOkButtonX, onlineBattleOkButtonY);
			bluetoothOkButton = new Button(bluetoothOkDisarmed,bluetoothOkArmed , bluetoothOkButtonX, bluetoothOkButtonY);

			
			exitDialogPaint = new Paint();
			exitDialogPaint.setColor(Color.BLACK);
			exitDialogPaint.setAlpha(125);
			
			background = BitmapFactory.decodeStream(am.open("images/menu/background.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if(internetButton != null) {
			internetButton.recycle();
			internetButton = null;
		}
		if(bluetoothButton != null) {
			bluetoothButton.recycle();
			bluetoothButton = null;
		}
		if(hotSeatButton != null) {
			hotSeatButton.recycle();
			hotSeatButton = null;
		}
		if(backButton != null) {
			backButton.recycle();
			backButton = null;
		}
		if(onlineSoon != null) {
			onlineSoon.recycle();
			onlineSoon = null;
		}
		if(bluetoothSoon != null) {
			bluetoothSoon.recycle();
			bluetoothSoon = null;
		}
		if(bluetoothOkButton != null) {
			bluetoothOkButton.recycle();
			bluetoothOkButton = null;
		}
		if(onlineBattleOkButton != null) {
			onlineBattleOkButton.recycle();
			onlineBattleOkButton = null;
		}
		if(background != null) {
			background.recycle();
			background = null;
		}
		exitDialogPaint = null;
	}
	
}
