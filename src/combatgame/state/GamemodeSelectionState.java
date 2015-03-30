package combatgame.state;

import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import combatgame.gamemode.GameMode;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.main.Game;
import combatgame.main.StateManager;
import combatgame.objects.PartialMap;
import combatgame.widgets.Button;
import combatgame.widgets.GamemodeListView;

public class GamemodeSelectionState extends State {

	private static final long serialVersionUID = 1L;
	protected PartialMap map;
	protected transient GameMode selectedGamemode;
	
	protected transient GamemodeListView listview;
	
	protected transient Button startButton;
	protected transient Button backButton;
	
	protected transient Paint selectedGamemodeNamePaint;
	protected transient Paint selectedGamemodeLengthPaint;
	protected transient Paint selectedGamemodeDescriptionPaint;
	
	public GamemodeSelectionState(StateManager stateManager, PartialMap map) {
		super(stateManager);
		
		this.map = map;
	}

	@Override
	public int getStateID() {
		return State.GAMEMODE_SELECTION;
	}
	
	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		
		events = startButton.update(events);
		events = backButton.update(events);
		listview.update(events);
		selectedGamemode = listview.getSelectedGamemode();
		
		if(startButton.state == Button.ACTIVATED) {
			startButton.disarm();
			stateManager.setState(new HotSeatState(stateManager, map.getPath(), map.getTileset(), selectedGamemode));
		}
		else if(backButton.state == Button.ACTIVATED) {
			backButton.disarm();
			stateManager.setState(new MapSelectionState(stateManager));
		}
		else if(stateManager.isBackPressed()) {
			stateManager.setState(new MapSelectionState(stateManager));
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		startButton.render(g);
		backButton.render(g);
		listview.render(g);
		
		//draw preview
		Bitmap preview = selectedGamemode.getPreview();
		g.drawBitmap(preview, 810, 25, null);
		
		//draw name and size
		g.drawText(selectedGamemode.getName(), 810, 25 + preview.getHeight() + 30, selectedGamemodeNamePaint);
		g.drawText("Length: " + selectedGamemode.getLength(), 810, 25 + preview.getHeight() + 60, selectedGamemodeLengthPaint);
		
		//draw description
		String[] description = selectedGamemode.getDescription().split(" ");
		StringBuilder line = new StringBuilder("Description: ");
		StringBuilder temp = new StringBuilder(line.toString());
		int linesDrawn = 0;
		for(int i = 0; i < description.length; i++) {
			temp.append(description[i]).append(" ");
			if(selectedGamemodeDescriptionPaint.measureText(temp.toString()) + 810 > 1280) {
				g.drawText(line.toString(), 810, 25 + preview.getHeight() + 85 + 25 * linesDrawn, selectedGamemodeDescriptionPaint);
				linesDrawn++;
				line.delete(0, line.length()); temp.delete(0, temp.length());
				line.append(description[i]).append(" "); temp.append(description[i]).append(" ");
			}
			else {
				line.append(description[i]).append(" ");
			}	
		}
		g.drawText(line.toString(), 810, 25 + preview.getHeight() + 85 + 25 * linesDrawn, selectedGamemodeDescriptionPaint);
	}

	@Override
	public void pause(Context context, boolean saveData) {
		
	}

	@Override
	public void resume(StateManager stateManager) {
		Game.shouldScale(true);
		this.stateManager = stateManager;
		
		AssetManager am = stateManager.getAssetManager();
		this.listview = new GamemodeListView(am);
		try {
			Bitmap start = BitmapFactory.decodeStream(am.open("images/interface_buttons/start_button.png"));
			Bitmap startArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/start_button_armed.png"));
			Bitmap back = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button.png"));
			Bitmap backArmed = BitmapFactory.decodeStream(am.open("images/interface_buttons/back_button_armed.png"));
			
			int okButtonX = Game.G_WIDTH - start.getWidth() - 25;
			int okButtonY = Game.G_HEIGHT - start.getHeight() - 15;
			int backButtonX = okButtonX - back.getWidth() - 10;
			int backButtonY = okButtonY;
			
			startButton = new Button(start, startArmed, okButtonX, okButtonY);
			backButton = new Button(back, backArmed, backButtonX, backButtonY);
			
			selectedGamemodeNamePaint = new Paint();
			selectedGamemodeNamePaint.setTextSize(36);
			selectedGamemodeNamePaint.setColor(Color.WHITE);
			
			selectedGamemodeLengthPaint = new Paint();
			selectedGamemodeLengthPaint.setTextSize(32);
			selectedGamemodeLengthPaint.setColor(Color.WHITE);
			
			selectedGamemodeDescriptionPaint = new Paint();
			selectedGamemodeDescriptionPaint.setTextSize(28);
			selectedGamemodeDescriptionPaint.setColor(Color.WHITE);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if(startButton != null) {
			startButton.recycle();
			startButton = null;
		}
		if(backButton != null) {
			backButton.recycle();
			backButton = null;
		}
		if(listview != null) {
			listview.recycle();
			listview = null;
		}
		
		selectedGamemodeNamePaint = null;
		selectedGamemodeLengthPaint = null;
		selectedGamemodeDescriptionPaint = null;
	}
}
