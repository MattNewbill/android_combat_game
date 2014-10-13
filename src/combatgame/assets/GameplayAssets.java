package combatgame.assets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.AssetManager;

/**
 * **HAPPY**
 */

public class GameplayAssets {

	//class icons
	public static Bitmap[] sniperIcons = new Bitmap[4];
	public static Bitmap[] assaultIcons = new Bitmap[4];
	
	//hud icons
	public static Bitmap unitInfoIcon;
	public static Bitmap moveIcon;
	public static Bitmap abilityIcon;
	public static Bitmap deselectIcon;
	public static Bitmap endTurnIcon;
	
	//selection overlays
	public static Bitmap selectionOverlay;
	
	private GameplayAssets(){}
	
	public static void loadGameplayAssets(AssetManager am) {
		try {
			sniperIcons[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_resized.png"));
			assaultIcons[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_resized.png"));
			
			unitInfoIcon = BitmapFactory.decodeStream(am.open("sprites/hud/test.png"));
			moveIcon = BitmapFactory.decodeStream(am.open("sprites/hud/move_icon.png"));
			abilityIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_icon.png"));
			deselectIcon = BitmapFactory.decodeStream(am.open("sprites/hud/deselect_icon.png"));
			endTurnIcon = BitmapFactory.decodeStream(am.open("sprites/hud/end_turn_icon.png"));
			
			selectionOverlay = BitmapFactory.decodeStream(am.open("sprites/selection_overlay.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void dispose() {
		for(int i = 0; i < sniperIcons.length; i++) {
			if(sniperIcons[i] != null)
				sniperIcons[i].recycle();
		}
		
		for(int i = 0; i < assaultIcons.length; i++) {
			if(assaultIcons[i] != null)
				assaultIcons[i].recycle();
		}
		
		if(endTurnIcon != null)
			endTurnIcon.recycle();
	}
}
