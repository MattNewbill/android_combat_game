package combatgame.assets;

import combatgame.main.Game;
import combatgame.util.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
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
	
	//movement button icons
	public static Bitmap movementIcon;
	public static Bitmap leftRotateIcon;
	public static Bitmap rightRotateIcon;
	
	//ability button icons
	public static Bitmap basicAttackIcon;
	public static Bitmap throwGrenadeIcon;
	
	//selection overlays
	public static Bitmap attackOverlay;
	public static Bitmap selectionOverlay;
	public static Bitmap[] numberOverlays;
	
	//spawn unit
	public static Bitmap spawnUnitIcon;
	public static Bitmap respawnUnitIcon;
	
	//player banner
	public static Bitmap playerBanner;
	
	//map tiles
	public static Bitmap dirtSprite;
	public static Bitmap hedgehogSprite;
	public static Bitmap bushSprite;
	public static Bitmap player1BaseSprite;
	public static Bitmap player2BaseSprite;
	
	//shaded map tiles
	public static Bitmap dirtSpriteShaded;
	public static Bitmap hedgehogSpriteShaded;
	public static Bitmap bushSpriteShaded;
	public static Bitmap player1BaseSpriteShaded;
	public static Bitmap player2BaseSpriteShaded;
	
	//interface buttons
	public static Bitmap okIcon;
	public static Bitmap okArmedIcon;
	public static Bitmap yesIcon;
	public static Bitmap yesArmedIcon;
	public static Bitmap noIcon;
	public static Bitmap noArmedIcon;
	public static Bitmap exitDialogIcon;
	
	//hit indicators
	public static Bitmap hitIndicatorUpIcon;
	public static Bitmap hitIndicatorRightIcon;
	public static Bitmap hitIndicatorDownIcon;
	public static Bitmap hitIndicatorLeftIcon;
	
	private GameplayAssets(){}
	
	public static void loadGameplayAssets(AssetManager am) {
		try {
			Options options = new BitmapFactory.Options();
		    options.inScaled = false;
			
			sniperIcons[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_up.png"), null, options);
			sniperIcons[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_right.png"), null, options);
			sniperIcons[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_down.png"), null, options);
			sniperIcons[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_left.png"), null, options);
			assaultIcons[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_up.png"), null, options);
			assaultIcons[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_right.png"), null, options);
			assaultIcons[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_down.png"), null, options);
			assaultIcons[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_left.png"), null, options);
			
			unitInfoIcon = BitmapFactory.decodeStream(am.open("sprites/hud/unit_info_icon.png"), null, options);
			moveIcon = BitmapFactory.decodeStream(am.open("sprites/hud/move_icon.png"), null, options);
			abilityIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_icon.png"), null, options);
			deselectIcon = BitmapFactory.decodeStream(am.open("sprites/hud/deselect_icon.png"), null, options);
			endTurnIcon = BitmapFactory.decodeStream(am.open("sprites/hud/end_turn_icon.png"), null, options);
			
			movementIcon = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/movement_icon.png"), null, options);
			leftRotateIcon = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/left_rotate_icon.png"), null, options);
			rightRotateIcon = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/right_rotate_icon.png"), null, options);
			
			basicAttackIcon = BitmapFactory.decodeStream(am.open("sprites/hud/action_buttons/standard_attack_icon.png"), null, options);
			throwGrenadeIcon = BitmapFactory.decodeStream(am.open("sprites/hud/action_buttons/throw_grenade_icon.png"), null, options);
			
			attackOverlay = BitmapFactory.decodeStream(am.open("sprites/tiles/attack_overlay.png"), null, options);
			selectionOverlay = BitmapFactory.decodeStream(am.open("sprites/tiles/selection_overlay.png"), null, options);
			
			numberOverlays = new Bitmap[10];
			Bitmap temp = BitmapFactory.decodeStream(am.open("sprites/tiles/number_overlays.png"), null, options);
			for(int row = 0; row < 2; row++)
				for(int col = 0; col < 5; col++)
					numberOverlays[row * 5 + col] = Bitmap.createBitmap(temp, col * 70, row * 70, 70, 70);
			temp.recycle();
			
			spawnUnitIcon = BitmapFactory.decodeStream(am.open("sprites/hud/spawn_unit_icon.png"), null, options);
			respawnUnitIcon = BitmapFactory.decodeStream(am.open("sprites/hud/re-spawn_unit_icon.png"), null, options);
			
			playerBanner = BitmapFactory.decodeStream(am.open("sprites/hud/player_banner.png"), null, options);
			
			dirtSprite = BitmapFactory.decodeStream(am.open("sprites/tiles/dirt.png"), null, options);
			hedgehogSprite = BitmapFactory.decodeStream(am.open("sprites/tiles/hedgehog.png"), null, options);
			bushSprite = BitmapFactory.decodeStream(am.open("sprites/tiles/bush.png"), null, options);
			player1BaseSprite = BitmapFactory.decodeStream(am.open("sprites/tiles/player1_base.png"), null, options);
			player2BaseSprite = BitmapFactory.decodeStream(am.open("sprites/tiles/player2_base.png"), null, options);
			
			okIcon = BitmapFactory.decodeStream(am.open("images/interface_buttons/ok_button.png"), null, options);
			okArmedIcon = BitmapFactory.decodeStream(am.open("images/interface_buttons/ok_button_armed.png"), null, options);
			yesIcon = BitmapFactory.decodeStream(am.open("images/interface_buttons/yes_button.png"), null, options);
			yesArmedIcon = BitmapFactory.decodeStream(am.open("images/interface_buttons/yes_button_armed.png"), null, options);
			noIcon = BitmapFactory.decodeStream(am.open("images/interface_buttons/no_button.png"), null, options);
			noArmedIcon = BitmapFactory.decodeStream(am.open("images/interface_buttons/no_button_armed.png"), null, options);
			exitDialogIcon = BitmapFactory.decodeStream(am.open("images/interface_buttons/confirm_exit_popup.png"), null, options);
			
			hitIndicatorUpIcon = BitmapFactory.decodeStream(am.open("sprites/tiles/indicators/hit_indicator_up.png"), null, options);
			hitIndicatorRightIcon = BitmapFactory.decodeStream(am.open("sprites/tiles/indicators/hit_indicator_right.png"), null, options);
			hitIndicatorDownIcon = BitmapFactory.decodeStream(am.open("sprites/tiles/indicators/hit_indicator_down.png"), null, options);
			hitIndicatorLeftIcon = BitmapFactory.decodeStream(am.open("sprites/tiles/indicators/hit_indicator_left.png"), null, options);
			
			shadeMapTiles();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void shadeMapTiles() {
		int fogOfWar = Color.parseColor("#E64A3F3F");
		
		dirtSpriteShaded = Util.alphaBlendBitmap(dirtSprite, fogOfWar);
		hedgehogSpriteShaded = Util.alphaBlendBitmap(hedgehogSprite, fogOfWar);
		bushSpriteShaded = Util.alphaBlendBitmap(bushSprite, fogOfWar);
		player1BaseSpriteShaded = Util.alphaBlendBitmap(player1BaseSprite, fogOfWar);
		player2BaseSpriteShaded = Util.alphaBlendBitmap(player2BaseSprite, fogOfWar);
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
		if(respawnUnitIcon != null)
			respawnUnitIcon.recycle();
		
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
		
		if(basicAttackIcon != null)
			basicAttackIcon.recycle();
		if(throwGrenadeIcon != null)
			throwGrenadeIcon.recycle();
		
		if(yesIcon != null)
			yesIcon.recycle();
		if(yesArmedIcon != null)
			yesArmedIcon.recycle();
		if(noIcon != null)
			noIcon.recycle();
		if(noArmedIcon != null)
			noArmedIcon.recycle();
		if(okIcon != null)
			okIcon.recycle();
		if(okArmedIcon != null)
			okArmedIcon.recycle();
		if(exitDialogIcon != null)
			exitDialogIcon.recycle();
		
		if(hitIndicatorUpIcon != null)
			hitIndicatorUpIcon.recycle();
		if(hitIndicatorRightIcon != null)
			hitIndicatorRightIcon.recycle();
		if(hitIndicatorDownIcon != null)
			hitIndicatorDownIcon.recycle();
		if(hitIndicatorLeftIcon != null)
			hitIndicatorLeftIcon.recycle();
		
	}
}
