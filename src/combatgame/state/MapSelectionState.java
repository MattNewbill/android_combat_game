package combatgame.state;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.main.StateManager;
import combatgame.widgets.Button;
import combatgame.widgets.ListView;
import combatgame.widgets.MapListView;

public class MapSelectionState extends State {

	Button okButton;
	Button backButton;
	
	ListView listView;
	public static final String mapPath = "maps/geometry";
	String selectedMap;
	
	boolean isCurrentMapThumbnailLoaded = false;
	Bitmap selectedMapThumbnail;
	
	Paint selectedMapNamePaint;
	Paint selectedMapSizePaint;
	Paint selectedMapDescriptionPaint;
	
	public MapSelectionState(StateManager stateManager) {
		super(stateManager);
		Game.shouldScale(true);
		
		AssetManager am = stateManager.getAssetManager();
		try {
			Bitmap ok = BitmapFactory.decodeStream(am.open("images/interface_buttons/ok_button.png"));
			Bitmap okArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/ok_button_armed.png"));
			Bitmap back = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			
			int okButtonX = Game.G_WIDTH - ok.getWidth() - 25;
			int okButtonY = Game.G_HEIGHT - ok.getHeight() - 15;
			int backButtonX = okButtonX - back.getWidth() - 10;
			int backButtonY = okButtonY;
			
			okButton = new Button(ok, okArmed, okButtonX, okButtonY);
			backButton = new Button(back, backArmed, backButtonX, backButtonY);
			
			String[] mapList = am.list(mapPath);
			Assert.assertTrue("no maps", mapList.length > 0); //assume we have at least one map
			listView = new MapListView(am, mapList);
			
			selectedMapNamePaint = new Paint();
			selectedMapNamePaint.setTextSize(36);
			selectedMapNamePaint.setColor(Color.WHITE);
			
			selectedMapSizePaint = new Paint();
			selectedMapSizePaint.setTextSize(32);
			selectedMapSizePaint.setColor(Color.WHITE);
			
			selectedMapDescriptionPaint = new Paint();
			selectedMapDescriptionPaint.setTextSize(28);
			selectedMapDescriptionPaint.setColor(Color.WHITE);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		events = okButton.update(events);
		events = backButton.update(events);
		listView.update(events);
		
		String temp = listView.getSelectedItem();
		if(!temp.equals(selectedMap)) {
			selectedMap = listView.getSelectedItem();
			isCurrentMapThumbnailLoaded = false;
		}
		
		if(okButton.state == Button.ACTIVATED) {
			okButton.disarm();
			//stateManager.setState(new GamemodeSelectionState(stateManager, mapPath+"/"+selectedMap));
			stateManager.setState(new HotSeatState(stateManager, mapPath+"/"+selectedMap, "woodland", null));
		}
		else if(backButton.state == Button.ACTIVATED) {
			backButton.disarm();
			stateManager.setState(new ConnectionState(stateManager));
		}
		else if(stateManager.isBackPressed()) {
			stateManager.setState(new ConnectionState(stateManager));
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		okButton.render(g);
		backButton.render(g);
		listView.render(g);
		
		//TODO: render preview of currently selected map
		if(!isCurrentMapThumbnailLoaded) {
			AssetManager am = stateManager.getAssetManager();
			try {
				selectedMapThumbnail = BitmapFactory.decodeStream(am.open("images/no_image_available_upscaled.png"));
				isCurrentMapThumbnailLoaded = true;
			} catch(Exception e) {
					e.printStackTrace();
			}
		}
		
		g.drawBitmap(selectedMapThumbnail, 810, 25, null);
		
		//TODO: draw description of map
		g.drawText(selectedMap.substring(0, selectedMap.length()-4), 810, 25 + selectedMapThumbnail.getHeight() + 30, selectedMapNamePaint);
		g.drawText("Size: medium", 810, 25 + selectedMapThumbnail.getHeight() + 60, selectedMapSizePaint);
		g.drawText("map description", 810, 25 + selectedMapThumbnail.getHeight() + 85, selectedMapDescriptionPaint);
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		okButton.recycle();
		backButton.recycle();
	}

}
