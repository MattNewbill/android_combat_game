package combatgame.state;

import android.content.res.AssetManager;
import combatgame.main.*;
import combatgame.assets.GameplayAssets;
import combatgame.graphics.Graphics2D;

public class BluetoothGameState extends GameState {

	public BluetoothGameState(StateManager stateManager) {
		super(stateManager);
		
		AssetManager am = this.stateManager.getAssetManager();
		
		//GameplayAssets.loadGameplayAssets(am);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void render(Graphics2D g, float delta) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}
	
	@Override
	public void gameover() {
		
	}

	@Override
	public void dispose() {
		GameplayAssets.dispose();
	}
	
}
