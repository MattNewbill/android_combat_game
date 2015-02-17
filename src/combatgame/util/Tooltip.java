package combatgame.util;
import java.util.List;
import android.graphics.Color;
import android.graphics.Paint;
import combatgame.assets.GameplayAssets;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.widgets.Button;
import combatgame.widgets.CheckBox;

public class Tooltip
{
	public static final int PLACE_UNIT = 1;
	public static final int EDIT_UNIT = 2;
	public static final int END_TURN = 3;
	public static final int FIRST_TURN = 4;
	public static final int MOVE_UNIT = 5;
	public static final int DAMAGED_UNIT = 6;
	public static final int ATTACK_UNIT = 10;
	public static final int ASSAULT_ATTACKS = 11;
	public static final int SNIPER_ATTACKS = 12;
	public static final int RECON_ATTACKS = 13;
	public static final int JUGGERNAUT_ATTACKS = 14;
	public static final int MEDIC_ATTACKS = 15;
	
	private static Paint painRectIn = new Paint();
	private static Paint painRectOut = new Paint();
	private static Paint painText = new Paint();
	private static Paint painTextCB = new Paint();
	
	static{
		painRectIn.setColor(Color.GRAY);
		painRectOut.setColor(Color.WHITE);
		painText.setColor(Color.WHITE);
		painText.setTextSize(40);
		painTextCB.setColor(Color.WHITE);
		painTextCB.setTextSize(25);
	}
	
	private static String text0 = "Tooltip!";
	private static String text1;
	private static String text2;
	private static String text3;
	private static String textCB = "Stop showing tooltips";
	
	private static transient CheckBox checkBox = new CheckBox(GameplayAssets.TTcheckbox0,GameplayAssets.TTcheckbox1, 50, 287);
	private static transient Button okButton = new Button(GameplayAssets.TTok0,GameplayAssets.TTok1, 900, 275);
	
	private static boolean TTon = false;
	
	private static boolean TT_all[] = {true,true};
	private static boolean TT_place[] = {true,true};
	private static boolean TT_edit[] = {true,true};
	private static boolean TT_end[] = {true,true};
	private static boolean TT_first[] = {true,true};
	private static boolean TT_move[] = {true,true};
	private static boolean TT_damaged[] = {true,true};
	private static boolean TT_attack[] = {true,true};
	private static boolean TT_assault[] = {true,true};
	private static boolean TT_sniper[] = {true,true};
	private static boolean TT_recon[] = {true,true};
	private static boolean TT_juggernaut[] = {true,true};
	private static boolean TT_medic[] = {true,true};
	private static int index;
	
	public static void reset()
	{
		TTon = false;
		
		TT_all[0] = true;
		TT_place[0] = true;
		TT_edit[0] = true;
		TT_end[0] = true;
		TT_first[0] = true;
		TT_move[0] = true;
		TT_damaged[0] = true;
		TT_attack[0] = true;
		TT_assault[0] = true;
		TT_sniper[0] = true;
		TT_recon[0] = true;
		TT_juggernaut[0] = true;
		TT_medic[0] = true;
		
		TT_all[1] = true;
		TT_place[1] = true;
		TT_edit[1] = true;
		TT_end[1] = true;
		TT_first[1] = true;
		TT_move[1] = true;
		TT_damaged[1] = true;
		TT_attack[1] = true;
		TT_assault[1] = true;
		TT_sniper[1] = true;
		TT_recon[1] = true;
		TT_juggernaut[1] = true;
		TT_medic[1] = true;
	}
	
	public static void showTooltip ( boolean isPlayerOne, int tooltipName )
	{
		if(isPlayerOne)
			index = 0;
		else
			index = 1;
			
		if(TT_all[index])
		{
			if(tooltipName==PLACE_UNIT)
			{
				if(TT_place[index])
				{
					TT_place[index] = false;
					TTon = true;
					text1 = "The [Place Unit] button allows you to set up your units.";
					text2 = "When clicked, the name of the current unit will be shown in the";
					text3 = "info box on the left. Units can only be placed in spawn areas.";
				}
			}
			else if(tooltipName==EDIT_UNIT)
			{
				if(TT_edit[index])
				{
					TT_edit[index] = false;
					TTon = true;
					text1 = "Now that a unit has been placed, you can edit it or place more units.";
					text2 = "The [Movement] button allows you to rotate units.";
					text3 = "The [Replace Unit] button allows you to move units to another spot.";
				}
			}
			else if(tooltipName==END_TURN)
			{
				if(TT_end[index])
				{
					TT_end[index] = false;
					TTon = true;
					text1 = "Now that all your units have been placed, you can continue to";
					text2 = "edit your units. Once you have nothing left to do that turn,";
					text3 = "hit the [End Turn] button to finish your turn.";
				}
			}
			else if(tooltipName==FIRST_TURN)
			{
				if(TT_first[index])
				{
					TT_first[index] = false;
					TTon = true;
					text1 = "[Movement] and [Action] buttons move, rotate, and use unit's";
					text2 = "abilities at the cost of AP (Action Points). If a unit's AP is used up,";
					text3 = "it is done for that turn.";
				}
			}
			else if(tooltipName==MOVE_UNIT)
			{
				if(TT_move[index])
				{
					TT_move[index] = false;
					TTon = true;
					text1 = "A unit can only move in the direction it is facing.";
					text2 = "If there is an obstacle directly in front of the unit, it cannot move.";
					text3 = "Movement AP cost is different for each unit.";
				}
			}
			else if(tooltipName==DAMAGED_UNIT)
			{
				if(TT_damaged[index])
				{
					TT_damaged[index] = false;
					TTon = true;
					text1 = "Your unit has been attacked and lost HP (Health Points). The hit";
					text2 = "indicator points in the direction of the attack. Your AR (Armor)";
					text3 = "absorbed a portion of the damage. HP can be healed, AR cannot.";
				}
			}
			else if(tooltipName==ATTACK_UNIT)
			{
				if(TT_attack[index])
				{
					TT_attack[index] = false;
					TTon = true;
					text1 = "A Unit can only attack enemies in its own view range. When hit,";
					text2 = "a unit's AR (Armor) will absorb a portion of the damage. The rest of";
					text3 = "the damage will be deducted from the unit's HP (Health Points).";
				}
			}
			else if(tooltipName==ASSAULT_ATTACKS)
			{
				if(TT_assault[index])
				{
					TT_assault[index] = false;
					TTon = true;
					text1 = "The assault is the generic soldier. Rifle Fire does damage";
					text2 = "to one enemy. Throw Grenade does a 3x3 box of damage where";
					text3 = "thrown. Grenades can damage ally units.";
				}
			}
			else if(tooltipName==SNIPER_ATTACKS)
			{
				if(TT_sniper[index])
				{
					TT_sniper[index] = false;
					TTon = true;
					text1 = "The sniper is the long range class. Quick Shot does less damage";
					text2 = "but allows some movement. Charged shot does more damage";
					text3 = "at the expense of all AP points that turn.";
				}
			}
			else if(tooltipName==RECON_ATTACKS)
			{
				if(TT_recon[index])
				{
					TT_recon[index] = false;
					TTon = true;
					text1 = "The recon is the scout. Direct Fire is a simple attack.";
					text2 = "Ventriloquist Shot randomly spins the damage direction indicator";
					text3 = "allowing the recon a chance to not give away his position.";
				}
			}
			else if(tooltipName==JUGGERNAUT_ATTACKS)
			{
				if(TT_juggernaut[index])
				{
					TT_juggernaut[index] = false;
					TTon = true;
					text1 = "The juggernaut is the heavy class.";
					text2 = "Wide Shot can hit adjacent enemies.";
					text3 = "Concentrated Shot hits one enemy with more damage.";
				}
			}
			else if(tooltipName==MEDIC_ATTACKS)
			{
				if(TT_medic[index])
				{
					TT_medic[index] = false;
					TTon = true;
					text1 = "The medic is the healer. Pistol Shot is a simple attack.";
					text2 = "Single Heal heals one adjacent ally.";
					text3 = "Area Heal heals all adjacent allies.";
				}
			}
		}
	}
	
	public static void render( Graphics2D g)
	{
		if(TTon)
		{
			g.drawRoundRect(0, 40, 1280, 400, 45, 45, painRectOut);
			g.drawRoundRect(10, 50, 1270, 390, 45, 45, painRectIn);
			g.drawText(text0, 50, 100, painText);
			g.drawText(text1, 50, 150, painText);
			g.drawText(text2, 50, 200, painText);
			g.drawText(text3, 50, 250, painText);
			g.drawText(textCB, 150, 350, painTextCB);
			checkBox.render(g);
			okButton.render(g);
		}
	}
	
	public static List<TouchEvent> updateEvents(List<TouchEvent> events)
	{
		if(TTon)
		{
			if( TT_all[index] && (checkBox.state == CheckBox.ACTIVATED) )
				checkBox.disarm();
			
			okButton.update(events);
			checkBox.update(events);
			
			if(okButton.state == Button.ACTIVATED)
			{	
				TTon = false;
				okButton.disarm();
			}
			
			if(checkBox.state == CheckBox.ACTIVATED)
				TT_all[index] = false;
			if(checkBox.state == CheckBox.DISARMED)
				TT_all[index] = true;
			
			events.clear();
		}
		return events;
	}
	
	public static void onResume()
	{
		okButton = new Button(GameplayAssets.TTok0,GameplayAssets.TTok1, 900, 275);
		checkBox = new CheckBox(GameplayAssets.TTcheckbox0,GameplayAssets.TTcheckbox1, 50, 287);
	}
}