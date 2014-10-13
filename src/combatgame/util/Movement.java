// Point[][] result = movement.getMovement( Map, Unit );

package combatgame.util;

import java.util.*;
import combatgame.objects.*;
import combatgame.objects.Map;
import android.graphics.Point;

/**
 * **HAPPY**
 */

public class Movement
{
	private static Map MAP;
	private static Unit UNIT;
	private static Point loc;
	private static int distance;
	
	private static int length;
	private static int width;
	
	private static boolean[][] notUsed;
	private static Point tempPoint;
	private static Point[][] ans;

	public static Point[][] getMovement( Map iMAP, Unit iUNIT )
	{
		MAP=iMAP;
		UNIT=iUNIT;
		loc =UNIT.getXYCoordinate();
		distance = UNIT.getPointsLeft()/UNIT.getMovementCost();
		
		width = MAP.getNum_horizontal_tiles();
		length = MAP.getNum_vertical_tiles();
		
		notUsed = new boolean[length][width];
		
		for(int a=0; a<notUsed.length; a++)
			for(int b=0; b<notUsed[a].length; b++)
				notUsed[a][b]=true;
		
		ans = new Point[distance+1][0];
		
		setM();
		return ans;
	}
	
	private static void setM()
	{
		ans[0] = Arrays.copyOf(ans[0], ans[0].length + 1);
		ans[0][0] = new Point(loc);
		notUsed[loc.x][loc.y]=false;
		
		for(int i=1; i <= distance; i++)
		{
			for(int j=0; j < ans[i-1].length; j++)
			{
				
				tempPoint = new Point(ans[i-1][j].x +1,ans[i-1][j].y);
				if(tempPoint.x < width)
					if(notUsed[tempPoint.x][tempPoint.y])
						if( !(MAP.getTile(tempPoint.x, tempPoint.y).hasUnit()) )
							if(MAP.getFeature(tempPoint.x, tempPoint.y).isPassable())
							{
								ans[i] = Arrays.copyOf(ans[i], ans[i].length + 1);
								ans[i][ans[i].length-1]= new Point(tempPoint);
								notUsed[tempPoint.x][tempPoint.y] = false;
							}
					
				
				
				tempPoint = new Point(ans[i-1][j].x -1,ans[i-1][j].y);
				if(tempPoint.x >= 0)
					if(notUsed[tempPoint.x][tempPoint.y])
						if( !(MAP.getTile(tempPoint.x, tempPoint.y).hasUnit()) )
							if(MAP.getFeature(tempPoint.x, tempPoint.y).isPassable())
							{
								ans[i] = Arrays.copyOf(ans[i], ans[i].length + 1);
								ans[i][ans[i].length-1]= new Point(tempPoint);
								notUsed[tempPoint.x][tempPoint.y] = false;
							}
				
				tempPoint = new Point(ans[i-1][j].x,ans[i-1][j].y +1);
				if(tempPoint.y < length)
					if(notUsed[tempPoint.x][tempPoint.y])
						if( !(MAP.getTile(tempPoint.x, tempPoint.y).hasUnit()) )
							if(MAP.getFeature(tempPoint.x, tempPoint.y).isPassable())
							{
								ans[i] = Arrays.copyOf(ans[i], ans[i].length + 1);
								ans[i][ans[i].length-1]= new Point(tempPoint);
								notUsed[tempPoint.x][tempPoint.y] = false;
							}
				
				tempPoint = new Point(ans[i-1][j].x,ans[i-1][j].y -1);
				if(tempPoint.y >= 0)
					if(notUsed[tempPoint.x][tempPoint.y])
						if( !(MAP.getTile(tempPoint.x, tempPoint.y).hasUnit()) )
							if(MAP.getFeature(tempPoint.x, tempPoint.y).isPassable())
							{
								ans[i] = Arrays.copyOf(ans[i], ans[i].length + 1);
								ans[i][ans[i].length-1]= new Point(tempPoint);
								notUsed[tempPoint.x][tempPoint.y] = false;
							}
			}
		}
	}
	
}