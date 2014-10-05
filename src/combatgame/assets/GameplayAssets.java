package combatgame.assets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.AssetManager;

/**
 * **NOT HAPPY**
 * TODO: sniper and assault icons should be arrays with four sprites representing the different directions units can be facing
 */

public class GameplayAssets {

	//class icons
	public static Bitmap sniperIcon; //TODO
	public static Bitmap assaultIcon; //TODO
	
	//hud icons
	public static Bitmap endTurnIcon;
	
	private GameplayAssets(){}
	
	public static void loadGameplayAssets(AssetManager am) {
		try {
			sniperIcon = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_resized.png")); //TODO
			assaultIcon = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_resized.png")); //TODO
			
			endTurnIcon = BitmapFactory.decodeStream(am.open("sprites/hud/end_turn_original.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
