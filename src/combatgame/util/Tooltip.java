

package combatgame.util;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import combatgame.assets.GameplayAssets;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;



public class Tooltip
{
	static Paint painRectIn = new Paint();
	static Paint painRectOut = new Paint();
	static Paint painText = new Paint();
	static Paint painTextCB = new Paint();
	
	static{
		painRectIn.setColor(Color.GRAY);
		painRectOut.setColor(Color.WHITE);
		painText.setColor(Color.WHITE);
		painText.setTextSize(40);
		painTextCB.setColor(Color.WHITE);
		painTextCB.setTextSize(25);
	}
	
	static String text0 = "Tooltip!";
	static String text1;
	static String text2;
	static String text3;
	static String textCB = "Stop showing tooltips";
	
	static boolean p1TTon = true;
	static boolean p2TTon = true;
	
	static boolean p1TTonSpawn = true;
	static boolean p2TTonSpawn = true;
	
	public static void showTooltip ( boolean isPlayerOne, String tooltipName )
	{
		if( p1TTon && p1TTonSpawn )
		{
			p1TTonSpawn = false;
			
			if( tooltipName.equals("PLACE_UNIT_TT"))
				placeUnitTT();
			
		}
		
		if( p2TTon && p2TTonSpawn )
		{
			p2TTonSpawn = false;
			
			if( tooltipName.equals("PLACE_UNIT_TT"))
				placeUnitTT();
			
		}
	}
	
	private static void placeUnitTT()
	{
		text1 = "111111111111111111111111111111111111111";
		text2 = "7777777777777777777777777777777777777777777";
		text3 = "33333333333333333333333333333333333333333333333";
	}
	
	public static void render( Graphics2D g)
	{
		g.drawRoundRect(0, 40, 1280, 400, 45, 45, painRectOut);
		g.drawRoundRect(10, 50, 1270, 390, 45, 45, painRectIn);
		g.drawText(text0, 50, 100, painText);
		g.drawText(text1, 50, 150, painText);
		g.drawText(text2, 50, 200, painText);
		g.drawText(text3, 50, 250, painText);
		g.drawBitmap(GameplayAssets.TTcheckbox1, 50, 287, null);
		g.drawText(textCB, 150, 350, painTextCB);
		g.drawBitmap(GameplayAssets.TTok0, 900, 275, null);
	}
	
	public static List<TouchEvent> updateEvents(List<TouchEvent> events)
	{
		//events.clear();
		return events;
	}
}