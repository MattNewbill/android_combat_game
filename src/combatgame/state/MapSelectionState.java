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
import combatgame.objects.PartialMap;
import combatgame.widgets.Button;
import combatgame.widgets.ListView;
import combatgame.widgets.MapListView;

public class MapSelectionState extends State {

	Button nextButton;
	Button backButton;
	
	MapListView listView;
	public static final String mapPath = "maps/geometry";
	String selectedMap;
	PartialMap selectedPartialMap;
	
	Paint selectedMapNamePaint;
	Paint selectedMapSizePaint;
	Paint selectedMapDescriptionPaint;
	
	public MapSelectionState(StateManager stateManager) {
		super(stateManager);
		Game.shouldScale(true);
		
		AssetManager am = stateManager.getAssetManager();
		try {
			Bitmap ok = BitmapFactory.decodeStream(am.open("images/interface_buttons/next_button.png"));
			Bitmap okArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/next_button_armed.png"));
			Bitmap back = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			
			int okButtonX = Game.G_WIDTH - ok.getWidth() - 25;
			int okButtonY = Game.G_HEIGHT - ok.getHeight() - 15;
			int backButtonX = okButtonX - back.getWidth() - 10;
			int backButtonY = okButtonY;
			
			nextButton = new Button(ok, okArmed, okButtonX, okButtonY);
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
		events = nextButton.update(events);
		events = backButton.update(events);
		listView.update(events);
		
		String temp = listView.getSelectedItem();
		if(!temp.equals(selectedMap)) {
			selectedMap = temp;
			selectedPartialMap = listView.getSelectedMap();
		}
		
		if(nextButton.state == Button.ACTIVATED) {
			nextButton.disarm();
			//stateManager.setState(new GamemodeSelectionState(stateManager, mapPath+"/"+selectedMap));
			Log.i("combatgame", ""+mapPath+"/"+selectedMap);
			Log.i("combatgame", ""+selectedPartialMap.getTileset());
			stateManager.setState(new HotSeatState(stateManager, mapPath+"/"+selectedMap, selectedPartialMap.getTileset(), null));
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
		nextButton.render(g);
		backButton.render(g);
		listView.render(g);
		
		//draw preview
		Bitmap preview = selectedPartialMap.getPreview();
		g.drawBitmap(preview, 810, 25, null);
		
		//draw name and size
		g.drawText(selectedPartialMap.getName(), 810, 25 + preview.getHeight() + 30, selectedMapNamePaint);
		g.drawText("Size: " + selectedPartialMap.getSize(), 810, 25 + preview.getHeight() + 60, selectedMapSizePaint);
		
		//draw description
		String[] description = selectedPartialMap.getDescription();
		StringBuilder line = new StringBuilder("Description: ");
		StringBuilder temp = new StringBuilder(line.toString());
		int linesDrawn = 0;
		for(int i = 0; i < description.length; i++) {
			temp.append(description[i]).append(" ");
			if(selectedMapDescriptionPaint.measureText(temp.toString()) + 810 > 1280) {
				g.drawText(line.toString(), 810, 25 + preview.getHeight() + 85 + 25 * linesDrawn, selectedMapDescriptionPaint);
				linesDrawn++;
				line.delete(0, line.length()); temp.delete(0, temp.length());
				line.append(description[i]).append(" "); temp.append(description[i]).append(" ");
			}
			else {
				line.append(description[i]).append(" ");
			}
				
		}
		g.drawText(line.toString(), 810, 25 + preview.getHeight() + 85 + 25 * linesDrawn, selectedMapDescriptionPaint);
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		if(nextButton != null)
			nextButton.recycle();
		if(backButton != null)
			backButton.recycle();
		if(listView != null)
			listView.recycle();
	}

}
