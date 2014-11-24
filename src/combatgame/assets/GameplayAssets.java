package combatgame.assets;

import java.io.IOException;

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
	public static Bitmap[] reconIcons = new Bitmap[4];
	public static Bitmap[] cqbIcons = new Bitmap[4];
	public static Bitmap[] medicIcons = new Bitmap[4];
	
	//hud icons
	public static Bitmap unitInfoIcon;
	public static Bitmap moveIcon;
	public static Bitmap abilityIcon;
	public static Bitmap deselectIcon;
	public static Bitmap endTurnIcon;
	
	//movement button icons
	public static Bitmap movementIcons[] = new Bitmap[5];
	public static Bitmap leftRotateIcon;
	public static Bitmap rightRotateIcon;
	public static Bitmap leftRotateSetupIcon;
	public static Bitmap rightRotateSetupIcon;
	
	//ability button icons
	//assault abilities
		public static Bitmap rifleFireIcon;
		public static Bitmap throwGrenadeIcon;
	//cqc abilities
		public static Bitmap concentratedShotIcon;
		public static Bitmap wideShotIcon;
	//medic abilities
		public static Bitmap areaHealIcon;
		public static Bitmap singleHealIcon;
		public static Bitmap pistolShotIcon;
	//recon abilities
		public static Bitmap straightShotIcon;
		public static Bitmap trickShotIcon;
	//sniper abilities
		public static Bitmap chargedShotIcon;
		public static Bitmap quickShotIcon;
		
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
	public static Bitmap[] terrainSprites = new Bitmap[10];
	public static Bitmap[] barrierSprites = new Bitmap[10];
	public static Bitmap[] coverSprites = new Bitmap[10];
	public static Bitmap[] waterSprites = new Bitmap[10];
	public static Bitmap[] wallSprites = new Bitmap[10];
	public static Bitmap[] player1BaseSprites = new Bitmap[10];
	public static Bitmap[] player2BaseSprites = new Bitmap[10];
	
	//shaded map tiles
	public static Bitmap[] terrainSpritesShaded = new Bitmap[10];
	public static Bitmap[] barrierSpritesShaded = new Bitmap[10];
	public static Bitmap[] coverSpritesShaded = new Bitmap[10];
	public static Bitmap[] waterSpritesShaded = new Bitmap[10];
	public static Bitmap[] wallSpritesShaded = new Bitmap[10];
	public static Bitmap[] player1BaseSpritesShaded = new Bitmap[10];
	public static Bitmap[] player2BaseSpritesShaded = new Bitmap[10];
	
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
	
	public static void loadGameplayAssets(AssetManager am, String mapSetting) {
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
			reconIcons[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_up.png"), null, options);
			reconIcons[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_right.png"), null, options);
			reconIcons[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_down.png"), null, options);
			reconIcons[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_left.png"), null, options);
			cqbIcons[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqb_up.png"), null, options);
			cqbIcons[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqb_right.png"), null, options);
			cqbIcons[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqb_down.png"), null, options);
			cqbIcons[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqb_left.png"), null, options);
			medicIcons[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_up.png"), null, options);
			medicIcons[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_right.png"), null, options);
			medicIcons[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_down.png"), null, options);
			medicIcons[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_left.png"), null, options);
			
			unitInfoIcon = BitmapFactory.decodeStream(am.open("sprites/hud/unit_info_icon.png"), null, options);
			moveIcon = BitmapFactory.decodeStream(am.open("sprites/hud/move_icon.png"), null, options);
			abilityIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_icon.png"), null, options);
			deselectIcon = BitmapFactory.decodeStream(am.open("sprites/hud/deselect_icon.png"), null, options);
			endTurnIcon = BitmapFactory.decodeStream(am.open("sprites/hud/end_turn_icon.png"), null, options);
			
			movementIcons[1] = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/move_unit_1.png"), null, options);
			movementIcons[2] = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/move_unit_2.png"), null, options);
			movementIcons[3] = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/move_unit_3.png"), null, options);
			movementIcons[4] = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/move_unit_4.png"), null, options);
			leftRotateIcon = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/rotate_left.png"), null, options);
			rightRotateIcon = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/rotate_right.png"), null, options);
			leftRotateSetupIcon = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/rotate_left_setup.png"), null, options);
			rightRotateSetupIcon = BitmapFactory.decodeStream(am.open("sprites/hud/movement_buttons/rotate_right_setup.png"), null, options);
			
			rifleFireIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Assault/rifle_fire.png"), null, options);
			throwGrenadeIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Assault/throw_grenade.png"), null, options);
			concentratedShotIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Tank/concentrated_shot.png"), null, options);
			wideShotIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Tank/wide_shot.png"), null, options);
			areaHealIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Medic/area_heal.png"), null, options);
			singleHealIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Medic/single_heal.png"), null, options);
			pistolShotIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Medic/pistol_shot.png"), null, options);
			straightShotIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Recon/direct_fire.png"), null, options);
			trickShotIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Recon/ventriloquist_shot.png"), null, options);
			chargedShotIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Sniper/charged_shot.png"), null, options);
			quickShotIcon = BitmapFactory.decodeStream(am.open("sprites/hud/ability_buttons/Sniper/quick_shot.png"), null, options);
			
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
			
			loadMapTiles(am, options, mapSetting);
			
			shadeMapTiles();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void loadMapTiles(AssetManager am, Options options, String mapSetting) {
		if(mapSetting.equals("woodland"))
			loadWoodlandTiles(am, options);
		shadeMapTiles();
	}
	
	private static void shadeMapTiles() {
		int fogOfWar = Color.parseColor("#E64A3F3F");
		
		for(int i = 0; i < terrainSprites.length; i++) {
			if(terrainSprites[i] != null)
				terrainSpritesShaded[i] = Util.alphaBlendBitmap(terrainSprites[i], fogOfWar);
			if(barrierSprites[i] != null)
				barrierSpritesShaded[i] = Util.alphaBlendBitmap(barrierSprites[i], fogOfWar);
			if(coverSprites[i] != null)
				coverSpritesShaded[i] = Util.alphaBlendBitmap(coverSprites[i], fogOfWar);
			if(wallSprites[i] != null)
				wallSpritesShaded[i] = Util.alphaBlendBitmap(wallSprites[i], fogOfWar);
			if(player1BaseSprites[i] != null)
				player1BaseSpritesShaded[i] = Util.alphaBlendBitmap(player1BaseSprites[i], fogOfWar);
			if(player2BaseSprites[i] != null)
				player2BaseSpritesShaded[i] = Util.alphaBlendBitmap(player2BaseSprites[i], fogOfWar);
		}
	}
	
	private static void loadWoodlandTiles(AssetManager am, Options options) {
		try {
			terrainSprites[0] = BitmapFactory.decodeStream(am.open("sprites/tiles/woodland_tiles/dirt.png"), null, options);
			barrierSprites[0] = BitmapFactory.decodeStream(am.open("sprites/tiles/woodland_tiles/hedgehog.png"), null, options);
			coverSprites[0] = BitmapFactory.decodeStream(am.open("sprites/tiles/woodland_tiles/bush.png"), null, options);
			wallSprites[0] = BitmapFactory.decodeStream(am.open("sprites/tiles/woodland_tiles/rock.png"), null, options);
			player1BaseSprites[0] = BitmapFactory.decodeStream(am.open("sprites/tiles/player1_base.png"), null, options);
			player2BaseSprites[0] = BitmapFactory.decodeStream(am.open("sprites/tiles/player2_base.png"), null, options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*private static void resizeHUD() {
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
	}*/
	
	public static void dispose() {
		for(int i = 0; i < sniperIcons.length; i++) {
			if(sniperIcons[i] != null)
				sniperIcons[i].recycle();
			if(assaultIcons[i] != null)
				assaultIcons[i].recycle();
			if(reconIcons[i] != null)
				reconIcons[i].recycle();
			if(cqbIcons[i] != null)
				cqbIcons[i].recycle();
			if(medicIcons[i] != null)
				medicIcons[i].recycle();
		}
		
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
		
		for(int i = 0; i < movementIcons.length; i++)
			if(movementIcons[i] != null)
				movementIcons[i].recycle();
		if(leftRotateIcon != null)
			leftRotateIcon.recycle();
		if(rightRotateIcon != null)
			rightRotateIcon.recycle();
		if(leftRotateSetupIcon != null)
			leftRotateSetupIcon.recycle();
		if(rightRotateSetupIcon != null)
			rightRotateSetupIcon.recycle();
		
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
		
		for(int i = 0; i < terrainSprites.length; i++) {
			if(terrainSprites[i] != null)
				terrainSprites[i].recycle();
			if(barrierSprites[i] != null)
				barrierSprites[i].recycle();
			if(coverSprites[i] != null)
				coverSprites[i].recycle();
			if(waterSprites[i] != null)
				waterSprites[i].recycle();
			if(wallSprites[i] != null)
				wallSprites[i].recycle();
			if(player1BaseSprites[i] != null)
				player1BaseSprites[i].recycle();
			if(player2BaseSprites[i] != null)
				player2BaseSprites[i].recycle();
		}
		
		if(rifleFireIcon != null)
			rifleFireIcon.recycle();
		if(throwGrenadeIcon != null)
			throwGrenadeIcon.recycle();
		if(concentratedShotIcon != null)
			concentratedShotIcon.recycle();
		if(wideShotIcon != null)
			wideShotIcon.recycle();
		if(areaHealIcon != null)
			areaHealIcon.recycle();
		if(singleHealIcon != null)
			singleHealIcon.recycle();
		if(pistolShotIcon != null)
			pistolShotIcon.recycle();
		if(straightShotIcon != null)
			straightShotIcon.recycle();
		if(trickShotIcon != null)
			trickShotIcon.recycle();
		if(chargedShotIcon != null)
			chargedShotIcon.recycle();
		if(quickShotIcon != null)
			quickShotIcon.recycle();
		
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
