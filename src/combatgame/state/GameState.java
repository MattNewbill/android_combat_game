package combatgame.state;

import combatgame.main.*;
import combatgame.assets.*;
import combatgame.objects.*;
import combatgame.graphics.*;
import combatgame.input.*;
import java.util.List;
import android.graphics.Paint;
import android.util.Log;
import android.content.res.AssetManager;

/**
 * **NOT HAPPY**
 * TODO: Potentially change the way "switching turns" is handled...Get feedback on how that should be implemented.  
 * Playing on the same phone vs playing on two different phones will also play a part in how this should be structured...
 * Change how players are created, we might need to change the constructors depending on how much info we have when the game first starts
 */

public class GameState extends State {
	
	Map map;
	Player player1, player2; //TODO
	Player thisPlayersTurn; //TODO
	
	Paint paint;
	
	public GameState(StateManager stateManager) {
		super(stateManager);
		this.stateManager.shouldScale(false);
		
		paint = new Paint();
		
		//create map
		AssetManager am = this.stateManager.getAssetManager();
		map = new Map (am, "maps/test_map.txt");
		
		//load all gameplay assets
		GameplayAssets.loadGameplayAssets(am);
		
		//create players
		player1 = new Player(this, 3, map); //TODO
		player2 = new Player(this, 3, map); //TODO
		thisPlayersTurn = player1; //TODO
		
		//initializeGame();
	}
	
	//TODO
	public void switchTurn() {
		if(thisPlayersTurn == player1)
			thisPlayersTurn = player2;
		else
			thisPlayersTurn = player1;
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		map.update(events);
		thisPlayersTurn.update(events);
	}

	@Override
	public void render(Graphics2D g, float delta) {
		map.render(g, paint);
		player1.render(g);
		player2.render(g);
		g.drawText(""+thisPlayersTurn.getId(), Game.P_WIDTH / 2, 20, new Paint());
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}
