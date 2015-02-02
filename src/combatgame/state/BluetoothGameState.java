package combatgame.state;

import android.content.Context;
import android.content.res.AssetManager;
import combatgame.main.*;
import combatgame.assets.GameplayAssets;
import combatgame.graphics.Graphics2D;

public class BluetoothGameState extends GameState {

	private static final long serialVersionUID = 1L;

	public BluetoothGameState(StateManager stateManager) {
		super(stateManager);
		
		AssetManager am = this.stateManager.getAssetManager();
		
		//GameplayAssets.loadGameplayAssets(am);
	}

	@Override
	public int getStateID() {
		return State.BLUETOOTH;
	}
	
	@Override
	public void update(float delta) {

	}

	@Override
	public void render(Graphics2D g, float delta) {

	}

	@Override
	public void pause(Context context, boolean saveData) {

	}

	@Override
	public void resume(StateManager stateManager) {
		
	}
	
	@Override
	public void gameover() {
		
	}

	@Override
	public void dispose() {
		super.dispose();
		GameplayAssets.dispose();
	}
	
}
