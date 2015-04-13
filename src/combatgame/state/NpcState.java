package combatgame.state;

import combatgame.main.*;
import combatgame.assets.*;
import combatgame.objects.*;
import combatgame.widgets.Button;
import combatgame.gamemode.GameMode;
import combatgame.graphics.*;
import combatgame.input.*;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.content.Context;
import android.content.res.AssetManager;

public class NpcState extends GameState {
	
	private static final long serialVersionUID = 1L;

	Map map;
	String mapPath;
	String tileSet;
	GameMode gm;
	
	transient Paint paint;
	
	boolean isGameOver = false;
	
	boolean switchTurns = false;
	transient Paint switchTurnsFill;
	transient Paint switchTurnsFont;
	transient Button switchTurnsOKButton;
	
	boolean isExitDialogShowing = false;
	transient Paint exitDialogPaint;
	transient Button yesButton;
	transient Button noButton;
	
	public NpcState(StateManager stateManager, String mapPath, String tileSet, GameMode gm) {
		super(stateManager);
		
		this.mapPath = mapPath;
		this.tileSet = tileSet;
		this.gm = gm;
	}

	@Override
	public int getStateID() {
		return State.HOT_SEAT;
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
		map.render(g);
		switchTurns = map.isSwitchingTurns();
		if(switchTurns) {
			if(Game.isScaled()) {
				g.drawRect(0, 0, Game.G_WIDTH, Game.G_HEIGHT, switchTurnsFill);
				g.drawText(map.getCurrentPlayersTurn().getGamertag() + "'s turn", Game.G_WIDTH / 2, Game.G_HEIGHT / 2, switchTurnsFont);
			}
			else {
				g.drawRect(0, 0, Game.P_WIDTH, Game.P_HEIGHT, switchTurnsFill);
				g.drawText(map.getCurrentPlayersTurn().getGamertag() + "'s turn", Game.P_WIDTH / 2, Game.P_HEIGHT / 2, switchTurnsFont);
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
	public void pause(Context context, boolean saveData) {
		
	}

	@Override
	public void resume(StateManager stateManager) {
		//if the device's resolution is our target size or lower, then we scale the game to our target size
		//if(Game.P_WIDTH < Game.G_WIDTH && Game.P_HEIGHT < Game.G_HEIGHT)
		//	Game.shouldScale(true);
		//anything bigger and we don't scale
		//else
		//	Game.shouldScale(false);
		Game.shouldScale(true); //TODO: change this
		this.stateManager = stateManager;
		
		AssetManager am = this.stateManager.getAssetManager();
		
		//load gameplay assets and only the specific tiles for the map that the user chose
		GameplayAssets.loadGameplayAssets(am, tileSet);
		
		//create map
		if(map == null)
			map = new Map(this, am, mapPath, gm);
		else
			gm.resume();
		
		map.resume(this, am, gm);
		
		paint = new Paint();
		switchTurnsFill = new Paint(); switchTurnsFill.setColor(Color.BLACK);
		switchTurnsFont = new Paint(); switchTurnsFont.setColor(Color.WHITE); switchTurnsFont.setTextAlign(Align.CENTER); switchTurnsFont.setTextSize(60); //TODO: scale this font size
		
		switchTurnsOKButton = new Button(GameplayAssets.okIcon, GameplayAssets.okArmedIcon, Game.G_WIDTH / 2 - (GameplayAssets.okIcon.getWidth() / 2), Game.G_HEIGHT / 2 + 30); //TODO: scale placement of button
		
		yesButton = new Button(GameplayAssets.yesIcon, GameplayAssets.yesArmedIcon, Game.G_WIDTH / 2 - GameplayAssets.yesIcon.getWidth() - 10, Game.G_HEIGHT / 2 + GameplayAssets.yesIcon.getHeight() / 2 + 30);
		noButton = new Button(GameplayAssets.noIcon, GameplayAssets.noArmedIcon, Game.G_WIDTH / 2 + 10, Game.G_HEIGHT / 2 + GameplayAssets.noIcon.getHeight() / 2 + 30);
		exitDialogPaint = new Paint();
		exitDialogPaint.setColor(Color.BLACK);
		exitDialogPaint.setAlpha(125);
	}

	@Override
	public void dispose() {
		super.dispose();
		GameplayAssets.dispose();
		if(map != null)
			map.dispose();
	}

	@Override
	public void gameover() {
		isGameOver = true;
	}

}
