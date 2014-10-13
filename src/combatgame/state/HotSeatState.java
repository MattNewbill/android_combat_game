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
 * **HAPPY**
 */

public class HotSeatState extends State {
	
	Map map;
	
	Paint paint;
	
	public HotSeatState(StateManager stateManager) {
		super(stateManager);
		this.stateManager.shouldScale(false);
		
		paint = new Paint();
		
		AssetManager am = this.stateManager.getAssetManager();
		
		//load all gameplay assets
		GameplayAssets.loadGameplayAssets(am);
		
		//create map
		map = new Map (am, "maps/test_map.txt");
	}

	@Override
	public void update(float delta) {
		List<TouchEvent> events = stateManager.getTouchHandler().getTouchEvents();
		map.update(events);
	}

	@Override
	public void render(Graphics2D g, float delta) {
		map.render(g, paint);
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

}