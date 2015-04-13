package combatgame.state;

import combatgame.graphics.Graphics2D;
import combatgame.main.*;
import combatgame.widgets.*;
import combatgame.input.*;

import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

public class ConnectionState extends State {

	private static final long serialVersionUID = 1L;

	public static final int V_BUTTON_MARGIN = 10;
	
	transient Button singleButton;
	transient Button hotSeatButton;
	transient Button internetButton;
	transient Button backButton;
	
	boolean isSingleFeatureNotAvailableDialogShowing = false;
	
	transient Bitmap background;

	private transient Button singleOkButton;
	private transient Paint exitDialogPaint;
	private transient Bitmap onlineSoon;
	private transient Bitmap singleSoon;
	
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
		events = singleButton.update(events);
		events = hotSeatButton.update(events);
		events = backButton.update(events);
		
		if(internetButton.state == Button.ACTIVATED) {
			//new game over the internet
			internetButton.disarm();
			stateManager.setState(new HostJoinState(stateManager));
		}
		else if(singleButton.state == Button.ACTIVATED){
			//new game single player
			isSingleFeatureNotAvailableDialogShowing = true;
			singleButton.disarm();
			//stateManager.setState(new SingleGameState(stateManager));
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
		
		if(isSingleFeatureNotAvailableDialogShowing) {
			events = singleOkButton.update(events);
			if(singleOkButton.state == Button.ACTIVATED) {
				singleOkButton.disarm();
				isSingleFeatureNotAvailableDialogShowing = false;
				internetButton.enable();
				singleButton.enable();
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
		
		//draw single player button
		singleButton.render(g);
		
		//draw one phone, two player button
		hotSeatButton.render(g);
		
		//draw back button
		backButton.render(g);
		
		if(isSingleFeatureNotAvailableDialogShowing){
			g.drawRect(0, 0, Game.G_WIDTH, Game.G_HEIGHT, exitDialogPaint);
			g.drawBitmap(singleSoon, Game.G_WIDTH / 2 - singleSoon.getWidth() / 2, Game.G_HEIGHT / 2 - singleSoon.getHeight() / 2, null); //TODO: scale for larger devices
			singleOkButton.render(g);
			internetButton.disable();
			singleButton.disable();
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
			Bitmap singleBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/menu/solo.png"));
			Bitmap singleBitmapArmed = BitmapFactory.decodeStream(am.open("images/menu/solo_armed.png"));
			Bitmap hotSeatBitmapDisarmed = BitmapFactory.decodeStream(am.open("images/menu/local.png"));
			Bitmap hotSeatBitmapArmed = BitmapFactory.decodeStream(am.open("images/menu/local_armed.png"));
			Bitmap backUnarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			
			Bitmap singleOkArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/ok_button_armed.png"));;
			Bitmap singleOkDisarmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/ok_button.png"));
			onlineSoon = BitmapFactory.decodeStream(am.open("images/menu/online_soon.png"));
			singleSoon = BitmapFactory.decodeStream(am.open("images/menu/single_soon.png"));
			
			
			
			int singleButtonX = (Game.G_WIDTH / 2) - singleBitmapDisarmed.getWidth() / 2;
			int singleButtonY = (int) (Game.G_HEIGHT / 2 - (singleBitmapDisarmed.getHeight() - 50));
			int hotSeatButtonX = (Game.G_WIDTH / 2) - hotSeatBitmapDisarmed.getWidth() / 2;
			int hotSeatButtonY = (int) (Game.G_HEIGHT / 2 - (hotSeatBitmapDisarmed.getHeight() - 50) + V_BUTTON_MARGIN + singleBitmapDisarmed.getHeight());
			int internetButtonX = (Game.G_WIDTH / 2) - internetBitmapDisarmed.getWidth() / 2;
			int internetButtonY = (int) (Game.G_HEIGHT / 2 - (internetBitmapDisarmed.getHeight() - 50) + (V_BUTTON_MARGIN * 2) + singleBitmapDisarmed.getHeight() + hotSeatBitmapDisarmed.getHeight());
			int backButtonX = Game.G_HEIGHT - internetButtonY - internetBitmapDisarmed.getHeight();
			int backButtonY = internetButtonY;
			int singleOkButtonX = Game.G_WIDTH/2 - singleOkArmed.getWidth()/2;
			int singleOkButtonY = Game.G_HEIGHT - singleOkArmed.getHeight() - 150;
			
			internetButton = new Button(internetBitmapDisarmed, internetBitmapArmed, internetButtonX, internetButtonY);
			singleButton = new Button(singleBitmapDisarmed, singleBitmapArmed, singleButtonX, singleButtonY);
			hotSeatButton = new Button(hotSeatBitmapDisarmed, hotSeatBitmapArmed, hotSeatButtonX, hotSeatButtonY);
			backButton = new Button(backUnarmed, backArmed, backButtonX, backButtonY);
			singleOkButton = new Button(singleOkDisarmed,singleOkArmed , singleOkButtonX, singleOkButtonY);

			
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
		if(singleButton != null) {
			singleButton.recycle();
			singleButton = null;
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
		if(singleSoon != null) {
			singleSoon.recycle();
			singleSoon = null;
		}
		if(singleOkButton != null) {
			singleOkButton.recycle();
			singleOkButton = null;
		}
		if(background != null) {
			background.recycle();
			background = null;
		}
		exitDialogPaint = null;
	}
	
}
