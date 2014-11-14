package combatgame.state;

import combatgame.main.*;
import combatgame.assets.*;
import combatgame.objects.*;
import combatgame.widgets.Button;
import combatgame.graphics.*;
import combatgame.input.*;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import android.content.res.AssetManager;

/**
 * **HAPPY**
 */

public class HotSeatState extends GameState {
	
	Map map;
	
	Paint paint;
	
	boolean isGameOver = false;
	
	boolean switchTurns = false;
	Paint switchTurnsFill;
	Paint switchTurnsFont;
	Button switchTurnsOKButton;
	
	boolean isExitDialogShowing = false;
	Paint exitDialogPaint;
	Button yesButton;
	Button noButton;
	
	public HotSeatState(StateManager stateManager) {
		super(stateManager);
		
		//if the device's resolution is our target size or lower, then we scale the game to our target size
		if(Game.P_WIDTH < Game.G_WIDTH && Game.P_HEIGHT < Game.G_HEIGHT)
			Game.shouldScale(true);
		//anything bigger and we don't scale
		else
			Game.shouldScale(false);
		
		paint = new Paint();
		switchTurnsFill = new Paint(); switchTurnsFill.setColor(Color.BLACK);
		switchTurnsFont = new Paint(); switchTurnsFont.setColor(Color.WHITE); switchTurnsFont.setTextAlign(Align.CENTER); switchTurnsFont.setTextSize(60); //TODO: scale this font size
		
		AssetManager am = this.stateManager.getAssetManager();
		
		//load all gameplay assets
		GameplayAssets.loadGameplayAssets(am);
		
		//create map
		map = new Map (this, am, "maps/test_map.txt");
		
		switchTurnsOKButton = new Button(GameplayAssets.okIcon, GameplayAssets.okArmedIcon, Game.G_WIDTH / 2 - (GameplayAssets.okIcon.getWidth() / 2), Game.G_HEIGHT / 2 + 30); //TODO: scale placement of button
		
		yesButton = new Button(GameplayAssets.yesIcon, GameplayAssets.yesArmedIcon, Game.G_WIDTH / 2 - GameplayAssets.yesIcon.getWidth() - 10, Game.G_HEIGHT / 2 + GameplayAssets.yesIcon.getHeight() / 2 + 30);
		noButton = new Button(GameplayAssets.noIcon, GameplayAssets.noArmedIcon, Game.G_WIDTH / 2 + 10, Game.G_HEIGHT / 2 + GameplayAssets.noIcon.getHeight() / 2 + 30);
		exitDialogPaint = new Paint();
		exitDialogPaint.setColor(Color.BLACK);
		exitDialogPaint.setAlpha(125);
	}

	@Override
	public void update(float delta) {
		if(!isGameOver) {
			List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
			if(!isExitDialogShowing)
				isExitDialogShowing = stateManager.isBackPressed();
			if(isExitDialogShowing) {
				events = yesButton.update(events);
				events = noButton.update(events);
				if(yesButton.state == Button.ACTIVATED) {
					yesButton.disarm();
					stateManager.setState(new MainMenuState(stateManager));
				}
				if(noButton.state == Button.ACTIVATED) {
					noButton.disarm();
					isExitDialogShowing = false;
				}
			}
			else {
				if(switchTurns) {
					events = switchTurnsOKButton.update(events);
					events.clear();
					if(switchTurnsOKButton.state == Button.ACTIVATED) {
						switchTurnsOKButton.disarm();
						switchTurns = false;
						map.doneSwitchingTurns();
					}
				}
				map.update(events);
			}
		}
		else {
			//TODO: maybe take us to a game breakdown state which records stats for the match
			//right now, just take us back to the main menu
			stateManager.setState(new MainMenuState(stateManager));
		}
	}

	@Override
	public void render(Graphics2D g, float delta) {
		map.render(g, paint);
		switchTurns = map.isSwitchingTurns();
		if(switchTurns) {
			if(Game.isScaled()) {
				g.drawRect(0, 0, Game.G_WIDTH, Game.G_HEIGHT, switchTurnsFill);
				g.drawText("Are you ready " + map.getCurrentPlayersTurn().getGamertag() + "?", Game.G_WIDTH / 2, Game.G_HEIGHT / 2, switchTurnsFont);
			}
			else {
				g.drawRect(0, 0, Game.P_WIDTH, Game.P_HEIGHT, switchTurnsFill);
				g.drawText("Are you ready " + map.getCurrentPlayersTurn().getGamertag() + "?", Game.P_WIDTH / 2, Game.P_HEIGHT / 2, switchTurnsFont);
			}
			switchTurnsOKButton.render(g);
		}
		
		if(isExitDialogShowing) {
			g.drawRect(0, 0, Game.G_WIDTH, Game.G_HEIGHT, exitDialogPaint);
			g.drawBitmap(GameplayAssets.exitDialogIcon, Game.G_WIDTH / 2 - GameplayAssets.exitDialogIcon.getWidth() / 2, Game.G_HEIGHT / 2 - GameplayAssets.exitDialogIcon.getHeight() / 2, null); //TODO: scale for larger devices
			yesButton.render(g);
			noButton.render(g);
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		GameplayAssets.dispose();
		if(map != null)
			map.dispose();
	}

	@Override
	public void gameover() {
		isGameOver = true;
	}

}
