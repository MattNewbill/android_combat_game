// Point[][] result = movement.getMovement( Map, Unit );

package combatgame.util;

import java.util.*;

import combatgame.objects.*;
import combatgame.objects.Map;
import combatgame.graphics.GPoint;

/**
 * **HAPPY**
 */

public class Movement
{
//	private static int[][]map;
	
	private static Map MAP;
	private static Unit UNIT;
	private static GPoint loc;
	private static int distance;
	
	private static int height;
	private static int width;
	
	private static boolean[][] notUsed;
	private static GPoint tempPoint;
	private static GPoint[][] ans;

	public static GPoint[][] getMovement( Map iMAP, Unit iUNIT )
//	public static GPoint[][] getMovement( int[][]Imap, GPoint Iloc, int Idistance )
	{
		//map=Imap;
		//loc=Iloc;
		//distance=Idistance;
		//width = map[0].length;
		//height = map.length;
		MAP=iMAP;
		UNIT=iUNIT;
		loc =UNIT.getXYCoordinate();
		distance = UNIT.getPointsLeft()/UNIT.getMovementCost();
		
		width = MAP.getNum_horizontal_tiles();
		height = MAP.getNum_vertical_tiles();
		
		notUsed = new boolean[height][width];
		
		for(int a=0; a<notUsed.length; a++)
			for(int b=0; b<notUsed[a].length; b++)
				notUsed[a][b]=true;
		
		ans = new GPoint[distance+1][0];
		
		setM();
		return ans;
	}
	
	 private static void setM()
	{
		ans[0] = Arrays.copyOf(ans[0], ans[0].length + 1);
		ans[0][0] = new GPoint(loc);
		notUsed[loc.row][loc.col]=false;
		
		for(int i=1; i <= distance; i++)
		{
			for(int j=0; j < ans[i-1].length; j++)
			{
				
				tempPoint = new GPoint(ans[i-1][j].row +1,ans[i-1][j].col);
				if(tempPoint.row < height)
					if(notUsed[tempPoint.row][tempPoint.col])
						if( !(MAP.getTile(tempPoint.row, tempPoint.col).hasUnit()) )
							if(MAP.getFeature(tempPoint.row, tempPoint.col).isPassable())
						//if(map[tempPoint.row][tempPoint.col] == 0)
							{
								ans[i] = Arrays.copyOf(ans[i], ans[i].length + 1);
								ans[i][ans[i].length-1]= new GPoint(tempPoint);
								notUsed[tempPoint.row][tempPoint.col] = false;
							}
					
				
				
				tempPoint = new GPoint(ans[i-1][j].row -1,ans[i-1][j].col);
				if(tempPoint.row >= 0)
					if(notUsed[tempPoint.row][tempPoint.col])
						if( !(MAP.getTile(tempPoint.row, tempPoint.col).hasUnit()) )
							if(MAP.getFeature(tempPoint.row, tempPoint.col).isPassable())
							//if(map[tempPoint.row][tempPoint.col] == 0)
							{
								ans[i] = Arrays.copyOf(ans[i], ans[i].length + 1);
								ans[i][ans[i].length-1]= new GPoint(tempPoint);
								notUsed[tempPoint.row][tempPoint.col] = false;
							}
				
				tempPoint = new GPoint(ans[i-1][j].row,ans[i-1][j].col +1);
				if(tempPoint.col < width)
					if(notUsed[tempPoint.row][tempPoint.col])
						if( !(MAP.getTile(tempPoint.row, tempPoint.col).hasUnit()) )
							if(MAP.getFeature(tempPoint.row, tempPoint.col).isPassable())
							//if(map[tempPoint.row][tempPoint.col] == 0)
							{
								ans[i] = Arrays.copyOf(ans[i], ans[i].length + 1);
								ans[i][ans[i].length-1]= new GPoint(tempPoint);
								notUsed[tempPoint.row][tempPoint.col] = false;
							}
				
				tempPoint = new GPoint(ans[i-1][j].row,ans[i-1][j].col -1);
				if(tempPoint.col >= 0)
					if(notUsed[tempPoint.row][tempPoint.col])
						if( !(MAP.getTile(tempPoint.row, tempPoint.col).hasUnit()) )
							if(MAP.getFeature(tempPoint.row, tempPoint.col).isPassable())
							//if(map[tempPoint.row][tempPoint.col] == 0)
							{
								ans[i] = Arrays.copyOf(ans[i], ans[i].length + 1);
								ans[i][ans[i].length-1]= new GPoint(tempPoint);
								notUsed[tempPoint.row][tempPoint.col] = false;
							}
			}
		}
	}
	
}