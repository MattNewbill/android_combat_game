package combatgame.gamemode;

import java.io.Serializable;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import combatgame.objects.Map;
import combatgame.objects.Unit;

public abstract class GameMode implements Serializable {

	private static final long serialVersionUID = 1L;
	protected Unit[] player1Units;
	protected Unit[] player2Units;
	
	protected String gamemodeName;
	protected String gamemodeLength;
	protected String gamemodeDescription;
	
	protected transient Bitmap preview;
	
	public static final int IN_PROGRESS = 0;
	public static final int STALEMATE = 1;
	public static final int PLAYER_1_WINS = 2;
	public static final int PLAYER_2_WINS = 3;
	
	public GameMode(AssetManager am, String path) {
		try {
			preview = BitmapFactory.decodeStream(am.open("gamemodes/thumbnails/"+path));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public abstract Unit[] getPlayer1Units();
	public abstract Unit[] getPlayer2Units();
	public abstract int checkWinConditions(Map map);
	
	public String getName() {
		return gamemodeName;
	}
	
	public String getLength() {
		return gamemodeLength;
	}
	
	public String getDescription() {
		return gamemodeDescription;
	}
	
	public Bitmap getPreview() {
		return preview;
	}
	
	public void resume() {

	}
}
