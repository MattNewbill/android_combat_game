package combatgame.assets;

import combatgame.main.Game;
import combatgame.util.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
	
	//movement buttons list
	public static Bitmap movementIcon;
	public static Bitmap leftRotateIcon;
	public static Bitmap rightRotateIcon;
	
	//selection overlays
	public static Bitmap selectionOverlay;
	public static Bitmap[] numberOverlays;
	
	//spawn unit
	public static Bitmap spawnUnitIcon;
	
	//player banner
	public static Bitmap playerBanner;
	
	//map tiles
	public static Bitmap dirtSprite;
	public static Bitmap hedgehogSprite;
	public static Bitmap bushSprite;
	public static Bitmap player1BaseSprite;
	public static Bitmap player2BaseSprite;
	
	private GameplayAssets(){}
	
	public static void loadGameplayAssets(AssetManager am) {
		try {
			sniperIcons[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_up.png"));
			sniperIcons[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_right.png"));
			sniperIcons[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_down.png"));
			sniperIcons[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_left.png"));
			assaultIcons[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_up.png"));
			assaultIcons[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_right.png"));
			assaultIcons[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_down.png"));
			assaultIcons[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_left.png"));
			
			unitInfoIcon = BitmapFactory.decodeStream(am.open("sprites/hud/test.png"));
			moveIcon = BitmapFactory.decodeStream(am.open("sprites/hud/move_icon.png"));
			abilityIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_icon.png"));
			deselectIcon = BitmapFactory.decodeStream(am.open("sprites/hud/deselect_icon.png"));
			endTurnIcon = BitmapFactory.decodeStream(am.open("sprites/hud/end_turn_icon.png"));
			
			movementIcon = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/movement_icon.png"));
			leftRotateIcon = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/left_rotate_icon.png"));
			rightRotateIcon = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/right_rotate_icon.png"));
			
			selectionOverlay = BitmapFactory.decodeStream(am.open("sprites/selection_overlay.png"));
			
			numberOverlays = new Bitmap[10];
			Bitmap temp = BitmapFactory.decodeStream(am.open("sprites/number_overlays.png"));
			for(int row = 0; row < 2; row++)
				for(int col = 0; col < 5; col++)
					numberOverlays[row * 5 + col] = Bitmap.createBitmap(temp, col * 70, row * 70, 70, 70);
			temp.recycle();
			
			spawnUnitIcon = BitmapFactory.decodeStream(am.open("sprites/hud/spawn_unit_icon.png"));
			
			playerBanner = BitmapFactory.decodeStream(am.open("sprites/player_banner.png"));
			
			dirtSprite = BitmapFactory.decodeStream(am.open("sprites/dirt.png"));
			hedgehogSprite = BitmapFactory.decodeStream(am.open("sprites/hedgehog.png"));
			bushSprite = BitmapFactory.decodeStream(am.open("sprites/bush.png"));
			player1BaseSprite = BitmapFactory.decodeStream(am.open("sprites/player1_base.png"));
			player2BaseSprite = BitmapFactory.decodeStream(am.open("sprites/player2_base.png"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void resizeHUD() {
		unitInfoIcon = Util.resizeBitmap(unitInfoIcon, (double)(Game.P_WIDTH) / Game.G_WIDTH, (double)(Game.P_HEIGHT) / Game.G_HEIGHT);
		moveIcon = Util.resizeBitmap(moveIcon, (double)(Game.P_WIDTH) / Game.G_WIDTH, (double)(Game.P_HEIGHT) / Game.G_HEIGHT);
		abilityIcon = Util.resizeBitmap(abilityIcon, (double)(Game.P_WIDTH) / Game.G_WIDTH, (double)(Game.P_HEIGHT) / Game.G_HEIGHT);
		deselectIcon = Util.resizeBitmap(deselectIcon, (double)(Game.P_WIDTH) / Game.G_WIDTH, (double)(Game.P_HEIGHT) / Game.G_HEIGHT);
		endTurnIcon = Util.resizeBitmap(endTurnIcon, (double)(Game.P_WIDTH) / Game.G_WIDTH, (double)(Game.P_HEIGHT) / Game.G_HEIGHT);
		
		movementIcon = Util.resizeBitmap(movementIcon, (double)(Game.P_WIDTH) / Game.G_WIDTH, (double)(Game.P_HEIGHT) / Game.G_HEIGHT);
		leftRotateIcon = Util.resizeBitmap(leftRotateIcon, (double)(Game.P_WIDTH) / Game.G_WIDTH, (double)(Game.P_HEIGHT) / Game.G_HEIGHT);
		rightRotateIcon = Util.resizeBitmap(rightRotateIcon, (double)(Game.P_WIDTH) / Game.G_WIDTH, (double)(Game.P_HEIGHT) / Game.G_HEIGHT);
		
		spawnUnitIcon = Util.resizeBitmap(spawnUnitIcon, (double)(Game.P_WIDTH) / Game.G_WIDTH, (double)(Game.P_HEIGHT) / Game.G_HEIGHT);
		
		playerBanner = Util.resizeBitmap(playerBanner, (double)(Game.P_WIDTH) / Game.G_WIDTH, (double)(Game.P_HEIGHT) / Game.G_HEIGHT);
	}
	
	public static void dispose() {
		for(int i = 0; i < sniperIcons.length; i++)
			if(sniperIcons[i] != null)
				sniperIcons[i].recycle();
		
		for(int i = 0; i < assaultIcons.length; i++)
			if(assaultIcons[i] != null)
				assaultIcons[i].recycle();
		
		if(unitInfoIcon != null)
			unitInfoIcon.recycle();
		if(moveIcon != null)
			moveIcon.recycle();
		if(abilityIcon != null)
			abilityIcon.recycle();
		if(deselectIcon != null)
			deselectIcon.recycle();
		if(endTurnIcon != null)
			endTurnIcon.recycle();
		
		if(movementIcon != null)
			movementIcon.recycle();
		if(leftRotateIcon != null)
			leftRotateIcon.recycle();
		if(rightRotateIcon != null)
			rightRotateIcon.recycle();
		
		if(selectionOverlay != null)
			selectionOverlay.recycle();
		
		for(int i = 0; i < numberOverlays.length; i++)
			if(numberOverlays[i] != null)
				numberOverlays[i].recycle();
		
		if(spawnUnitIcon != null)
			spawnUnitIcon.recycle();
		
		if(playerBanner != null)
			playerBanner.recycle();
		
		if(dirtSprite != null)
			dirtSprite.recycle();
		
		if(hedgehogSprite != null)
			hedgehogSprite.recycle();
		
		if(bushSprite != null)
			bushSprite.recycle();
		
		if(player1BaseSprite != null)
			player1BaseSprite.recycle();
		
		if(player2BaseSprite != null)
			player2BaseSprite.recycle();
	}
}
