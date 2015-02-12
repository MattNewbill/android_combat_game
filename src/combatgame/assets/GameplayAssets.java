package combatgame.assets;

import java.io.IOException;

import combatgame.util.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.content.res.AssetManager;

public class GameplayAssets {

	//class icons
	public static Bitmap[] sniperIconsP1 = new Bitmap[4];
	public static Bitmap[] assaultIconsP1 = new Bitmap[4];
	public static Bitmap[] reconIconsP1 = new Bitmap[4];
	public static Bitmap[] cqcIconsP1 = new Bitmap[4];
	public static Bitmap[] medicIconsP1 = new Bitmap[4];
	public static Bitmap[] sniperIconsP2 = new Bitmap[4];
	public static Bitmap[] assaultIconsP2 = new Bitmap[4];
	public static Bitmap[] reconIconsP2 = new Bitmap[4];
	public static Bitmap[] cqcIconsP2 = new Bitmap[4];
	public static Bitmap[] medicIconsP2 = new Bitmap[4];
	
	public static Bitmap[] presidentIconsP1 = new Bitmap[4];
	public static Bitmap[] presidentIconsP2 = new Bitmap[4];
	
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
	public static Bitmap healOverlay;
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
	public static Bitmap endTurnDialogIcon;
	
	//hit indicators
	public static Bitmap hitIndicatorUpIcon;
	public static Bitmap hitIndicatorRightIcon;
	public static Bitmap hitIndicatorDownIcon;
	public static Bitmap hitIndicatorLeftIcon;
	
	//are the assets loaded
	public static boolean isLoaded = false;
	
	private GameplayAssets(){}
	
	public static void loadGameplayAssets(AssetManager am, String mapSetting) {
		try {
			Options options = new BitmapFactory.Options();
		    options.inScaled = false;
			
			sniperIconsP1[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_up_p1.png"), null, options);
			sniperIconsP1[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_right_p1.png"), null, options);
			sniperIconsP1[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_down_p1.png"), null, options);
			sniperIconsP1[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_left_p1.png"), null, options);
			assaultIconsP1[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_up_p1.png"), null, options);
			assaultIconsP1[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_right_p1.png"), null, options);
			assaultIconsP1[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_down_p1.png"), null, options);
			assaultIconsP1[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_left_p1.png"), null, options);
			reconIconsP1[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_up_p1.png"), null, options);
			reconIconsP1[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_right_p1.png"), null, options);
			reconIconsP1[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_down_p1.png"), null, options);
			reconIconsP1[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_left_p1.png"), null, options);
			cqcIconsP1[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqc_up_p1.png"), null, options);
			cqcIconsP1[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqc_right_p1.png"), null, options);
			cqcIconsP1[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqc_down_p1.png"), null, options);
			cqcIconsP1[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqc_left_p1.png"), null, options);
			medicIconsP1[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_up_p1.png"), null, options);
			medicIconsP1[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_right_p1.png"), null, options);
			medicIconsP1[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_down_p1.png"), null, options);
			medicIconsP1[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_left_p1.png"), null, options);
			sniperIconsP2[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_up_p2.png"), null, options);
			sniperIconsP2[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_right_p2.png"), null, options);
			sniperIconsP2[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_down_p2.png"), null, options);
			sniperIconsP2[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/sniper_left_p2.png"), null, options);
			assaultIconsP2[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_up_p2.png"), null, options);
			assaultIconsP2[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_right_p2.png"), null, options);
			assaultIconsP2[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_down_p2.png"), null, options);
			assaultIconsP2[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/assault_left_p2.png"), null, options);
			reconIconsP2[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_up_p2.png"), null, options);
			reconIconsP2[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_right_p2.png"), null, options);
			reconIconsP2[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_down_p2.png"), null, options);
			reconIconsP2[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/recon_left_p2.png"), null, options);
			cqcIconsP2[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqc_up_p2.png"), null, options);
			cqcIconsP2[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqc_right_p2.png"), null, options);
			cqcIconsP2[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqc_down_p2.png"), null, options);
			cqcIconsP2[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/cqc_left_p2.png"), null, options);
			medicIconsP2[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_up_p2.png"), null, options);
			medicIconsP2[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_right_p2.png"), null, options);
			medicIconsP2[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_down_p2.png"), null, options);
			medicIconsP2[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/medic_left_p2.png"), null, options);
			presidentIconsP1[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/president_up_p1.png"), null, options);
			presidentIconsP1[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/president_right_p1.png"), null, options);
			presidentIconsP1[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/president_down_p1.png"), null, options);
			presidentIconsP1[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/president_left_p1.png"), null, options);
			presidentIconsP2[0] = BitmapFactory.decodeStream(am.open("sprites/class_icons/president_up_p2.png"), null, options);
			presidentIconsP2[1] = BitmapFactory.decodeStream(am.open("sprites/class_icons/president_right_p2.png"), null, options);
			presidentIconsP2[2] = BitmapFactory.decodeStream(am.open("sprites/class_icons/president_down_p2.png"), null, options);
			presidentIconsP2[3] = BitmapFactory.decodeStream(am.open("sprites/class_icons/president_left_p2.png"), null, options);
			
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
			healOverlay = BitmapFactory.decodeStream(am.open("sprites/tiles/heal_overlay.png"), null, options);
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
			endTurnDialogIcon = BitmapFactory.decodeStream(am.open("images/interface_buttons/confirm_endturn_popup.png"), null, options);
			
			hitIndicatorUpIcon = BitmapFactory.decodeStream(am.open("sprites/tiles/indicators/hit_indicator_up.png"), null, options);
			hitIndicatorRightIcon = BitmapFactory.decodeStream(am.open("sprites/tiles/indicators/hit_indicator_right.png"), null, options);
			hitIndicatorDownIcon = BitmapFactory.decodeStream(am.open("sprites/tiles/indicators/hit_indicator_down.png"), null, options);
			hitIndicatorLeftIcon = BitmapFactory.decodeStream(am.open("sprites/tiles/indicators/hit_indicator_left.png"), null, options);
			
			loadMapTiles(am, options, mapSetting);
			
			shadeMapTiles();
			
			isLoaded = true;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void loadMapTiles(AssetManager am, Options options, String mapSetting) {
		if(mapSetting.equals("Woodland"))
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
		for(int i = 0; i < sniperIconsP1.length; i++) {
			if(sniperIconsP1[i] != null) {
				sniperIconsP1[i].recycle();
				sniperIconsP1[i] = null;
			}
			if(assaultIconsP1[i] != null) {
				assaultIconsP1[i].recycle();
				assaultIconsP1[i] = null;
			}
			if(reconIconsP1[i] != null) {
				reconIconsP1[i].recycle();
				reconIconsP1[i] = null;
			}
			if(cqcIconsP1[i] != null) {
				cqcIconsP1[i].recycle();
				cqcIconsP1[i] = null;
			}
			if(medicIconsP1[i] != null) {
				medicIconsP1[i].recycle();
				medicIconsP1[i] = null;
			}
			if(presidentIconsP1[i] != null) {
				presidentIconsP1[i].recycle();
				presidentIconsP1[i] = null;
			}
		}
		for(int i = 0; i < sniperIconsP2.length; i++) {
			if(sniperIconsP2[i] != null) {
				sniperIconsP2[i].recycle();
				sniperIconsP2[i] = null;
			}
			if(assaultIconsP2[i] != null) {
				assaultIconsP2[i].recycle();
				assaultIconsP2[i] = null;
			}
			if(reconIconsP2[i] != null) {
				reconIconsP2[i].recycle();
				reconIconsP2[i] = null;
			}
			if(cqcIconsP2[i] != null) {
				cqcIconsP2[i].recycle();
				cqcIconsP2[i] = null;
			}
			if(medicIconsP2[i] != null) {
				medicIconsP2[i].recycle();
				medicIconsP2[i] = null;
			}
			if(presidentIconsP2[i] != null) {
				presidentIconsP2[i].recycle();
				presidentIconsP2[i] = null;
			}
		}
		
		if(unitInfoIcon != null) {
			unitInfoIcon.recycle();
			unitInfoIcon = null;
		}
		if(moveIcon != null) {
			moveIcon.recycle();
			moveIcon = null;
		}
		if(abilityIcon != null) {
			abilityIcon.recycle();
			abilityIcon = null;
		}
		if(deselectIcon != null) {
			deselectIcon.recycle();
			deselectIcon = null;
		}
		if(endTurnIcon != null) {
			endTurnIcon.recycle();
			endTurnIcon = null;
		}
		
		for(int i = 0; i < movementIcons.length; i++)
			if(movementIcons[i] != null) {
				movementIcons[i].recycle();
				movementIcons[i] = null;
			}
		if(leftRotateIcon != null) {
			leftRotateIcon.recycle();
			leftRotateIcon = null;
		}
		if(rightRotateIcon != null) {
			rightRotateIcon.recycle();
			rightRotateIcon = null;
		}
		if(leftRotateSetupIcon != null) {
			leftRotateSetupIcon.recycle();
			leftRotateSetupIcon = null;
		}
		if(rightRotateSetupIcon != null) {
			rightRotateSetupIcon.recycle();
			rightRotateSetupIcon = null;
		}
		
		if(attackOverlay != null) {
			attackOverlay.recycle();
			attackOverlay = null;
		}
		if(healOverlay != null) {
			healOverlay.recycle();
			healOverlay = null;
		}
		if(selectionOverlay != null) {
			selectionOverlay.recycle();
			selectionOverlay = null;
		}
		
		for(int i = 0; i < numberOverlays.length; i++)
			if(numberOverlays[i] != null) {
				numberOverlays[i].recycle();
				numberOverlays[i] = null;
			}
		
		if(spawnUnitIcon != null) {
			spawnUnitIcon.recycle();
			spawnUnitIcon = null;
		}
		if(respawnUnitIcon != null) {
			respawnUnitIcon.recycle();
			respawnUnitIcon = null;
		}
		
		if(playerBanner != null) {
			playerBanner.recycle();
			playerBanner = null;
		}
		
		for(int i = 0; i < terrainSprites.length; i++) {
			if(terrainSprites[i] != null) {
				terrainSprites[i].recycle();
				terrainSprites[i] = null;
			}
			if(barrierSprites[i] != null) {
				barrierSprites[i].recycle();
				barrierSprites[i] = null;
			}
			if(coverSprites[i] != null) {
				coverSprites[i].recycle();
				coverSprites[i] = null;
			}
			if(waterSprites[i] != null) {
				waterSprites[i].recycle();
				waterSprites[i] = null;
			}
			if(wallSprites[i] != null) {
				wallSprites[i].recycle();
				wallSprites[i] = null;
			}
			if(player1BaseSprites[i] != null) {
				player1BaseSprites[i].recycle();
				player1BaseSprites[i] = null;
			}
			if(player2BaseSprites[i] != null) {
				player2BaseSprites[i].recycle();
				player2BaseSprites[i] = null;
			}
		}
		
		if(rifleFireIcon != null) {
			rifleFireIcon.recycle();
			rifleFireIcon = null;
		}
		if(throwGrenadeIcon != null) {
			throwGrenadeIcon.recycle();
			throwGrenadeIcon = null;
		}
		if(concentratedShotIcon != null) {
			concentratedShotIcon.recycle();
			concentratedShotIcon = null;
		}
		if(wideShotIcon != null) {
			wideShotIcon.recycle();
			wideShotIcon = null;
		}
		if(areaHealIcon != null) {
			areaHealIcon.recycle();
			areaHealIcon = null;
		}
		if(singleHealIcon != null) {
			singleHealIcon.recycle();
			singleHealIcon = null;
		}
		if(pistolShotIcon != null) {
			pistolShotIcon.recycle();
			pistolShotIcon = null;
		}
		if(straightShotIcon != null) {
			straightShotIcon.recycle();
			straightShotIcon = null;
		}
		if(trickShotIcon != null) {
			trickShotIcon.recycle();
			trickShotIcon = null;
		}
		if(chargedShotIcon != null) {
			chargedShotIcon.recycle();
			chargedShotIcon = null;
		}
		if(quickShotIcon != null) {
			quickShotIcon.recycle();
			quickShotIcon = null;
		}
		
		if(yesIcon != null) {
			yesIcon.recycle();
			yesIcon = null;
		}
		if(yesArmedIcon != null) {
			yesArmedIcon.recycle();
			yesArmedIcon = null;
		}
		if(noIcon != null) {
			noIcon.recycle();
			noIcon = null;
		}
		if(noArmedIcon != null) {
			noArmedIcon.recycle();
			noArmedIcon = null;
		}
		if(okIcon != null) {
			okIcon.recycle();
			okIcon = null;
		}
		if(okArmedIcon != null) {
			okArmedIcon.recycle();
			okArmedIcon = null;
		}
		if(exitDialogIcon != null) {
			exitDialogIcon.recycle();
			exitDialogIcon = null;
		}
		if(endTurnDialogIcon != null) {
			endTurnDialogIcon.recycle();
			endTurnDialogIcon = null;
		}
		
		if(hitIndicatorUpIcon != null) {
			hitIndicatorUpIcon.recycle();
			hitIndicatorUpIcon = null;
		}
		if(hitIndicatorRightIcon != null) {
			hitIndicatorRightIcon.recycle();
			hitIndicatorRightIcon = null;
		}
		if(hitIndicatorDownIcon != null) {
			hitIndicatorDownIcon.recycle();
			hitIndicatorDownIcon = null;
		}
		if(hitIndicatorLeftIcon != null) {
			hitIndicatorLeftIcon.recycle();
			hitIndicatorLeftIcon = null;
		}
		
	}
}
