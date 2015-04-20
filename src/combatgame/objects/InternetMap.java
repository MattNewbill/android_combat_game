package combatgame.objects;

import java.util.List;

import android.content.res.AssetManager;
import combatgame.gamemode.GameMode;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.state.GameState;

public class InternetMap extends Map {

	private static final long serialVersionUID = 1L;

	private boolean isPlayerOne;
	
	public InternetMap(GameState gamestate, AssetManager am, String filePath, GameMode gm, boolean isPlayerOne) {
		super(gamestate, am, filePath, gm);
		
		this.isPlayerOne = isPlayerOne;
	}
	
	@Override
	public void update(List<TouchEvent> events) {
		if(isGameOver) {
			if(startTime == 0)
				startTime = System.currentTimeMillis();
			if(isWaitingToFade) {
				if(System.currentTimeMillis() - startTime > TIME_TILL_FADE) {
					isWaitingToFade = false;
					isFading = true;
					startTime = System.currentTimeMillis();
				}
			}
			else if(isFading) {
				if(fadePaintAlpha >= 255) {
					gamestate.gameover();
				}
				else {
					fadePaintAlpha += ALPHA_STEP;
					if(fadePaintAlpha > 255)
						fadePaintAlpha = 255;
					fadePaint.setAlpha(fadePaintAlpha);
				}
			}
		}
		else {
			//update current player's turn
			if((isPlayerOne && thisPlayersTurn == player1) || (!isPlayerOne && thisPlayersTurn == player2))
				events = thisPlayersTurn.update(events);
			
			//check to see if someone has won yet
			checkWinConditions(); //TODO: check gamemode for win conditions
			
			//update map scroll
			updateMap(events);
			
			//get new lightmap
			clearLightMap();
			if(isPlayerOne)
				lightmap = player1.constructLightMap(lightmap);
			else
				lightmap = player2.constructLightMap(lightmap);
		}
	}
	
	@Override
	protected void renderPlayers(Graphics2D g) {
		//render "enemy" units if they are visible
		if(isPlayerOne) {
			player2.renderVisibleUnits(g, lightmap);
			player1.render(g);
		}
		else {
			player1.renderVisibleUnits(g, lightmap);
			player2.render(g);
		}
	}
	
}
