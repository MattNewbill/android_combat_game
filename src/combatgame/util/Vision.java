// List<GPoint> result = Vision.getVision( Map, Unit );

package combatgame.util;

import java.util.*;

import combatgame.graphics.GPoint;
import combatgame.input.LazyPool;
import combatgame.input.LazyPool.LazyPoolObjectFactory;
import combatgame.objects.Map;
import combatgame.objects.Unit;

public class Vision
{
	private static GPoint loc;
	private static int view;
	private static int width;
	private static int height;
	private static int direction;
	
	private static Map MAP;
	private static Unit UNIT;

	private static boolean lineOfSight, LOScheck;
	private static int left, right, top, bottom;
	private static int minR, maxR, minC, maxC, tempX, tempY;
	private static double slope;
	private static boolean[][] notUsed;
	private static List<GPoint> ans= new ArrayList<GPoint>();
	
	private static GPoint point;
	private static LazyPool<GPoint> pool;
	public static final int MAX_POOL_SIZE = 200;
	
	static {
		LazyPoolObjectFactory<GPoint> factory = new LazyPoolObjectFactory<GPoint>() {
			@Override
			public GPoint createObject() {
				return new GPoint(-1, -1);
			}
		};
		pool = new LazyPool<GPoint>(factory, MAX_POOL_SIZE);
	}

	public static List<GPoint> getSprintVision( Map iMAP, Unit iUNIT )
	{
		MAP = iMAP;
		UNIT = iUNIT;
		loc = UNIT.getXYCoordinate();
		view = UNIT.getVisionRadius();
		width = MAP.getNum_horizontal_tiles();
		height = MAP.getNum_vertical_tiles();
		direction = UNIT.getDirectionFacing();
		
		return SprintWork();
	}
	
	public static List<GPoint> getSprintVision( Map iMAP, Unit iUNIT, int range )
	{
		MAP = iMAP;
		UNIT = iUNIT;
		loc = UNIT.getXYCoordinate();
		view = range;
		width = MAP.getNum_horizontal_tiles();
		height = MAP.getNum_vertical_tiles();
		direction = UNIT.getDirectionFacing();
		
		return SprintWork();
	}
	

	private static List<GPoint> SprintWork()
	{
		LOScheck=true;
		notUsed = new boolean[height][width];
		
		for(int a=0; a<notUsed.length; a++)
			for(int b=0; b<notUsed[a].length; b++)
				notUsed[a][b]=true;
		
		ans.clear();
		ans.add(loc);
		
		work();
		return ans;
	}

	public static List<GPoint> getSlowVision( Map iMAP, Unit iUNIT )
	{
		MAP = iMAP;
		UNIT = iUNIT;
		loc = UNIT.getXYCoordinate();
		view = UNIT.getVisionRadius()/2;
		width = MAP.getNum_horizontal_tiles();
		height = MAP.getNum_vertical_tiles();
		
		return SlowWork();
	}
	
	public static List<GPoint> getSlowVision( Map iMAP, Unit iUNIT, int range )
	{
		MAP = iMAP;
		UNIT = iUNIT;
		loc = UNIT.getXYCoordinate();
		view = range;
		width = MAP.getNum_horizontal_tiles();
		height = MAP.getNum_vertical_tiles();
		
		return SlowWork();
	}
	
	private static List<GPoint> SlowWork()
	{
		LOScheck=true;
		notUsed = new boolean[height][width];
		
		for(int a=0; a<notUsed.length; a++)
			for(int b=0; b<notUsed[a].length; b++)
				notUsed[a][b]=true;
		
		ans.clear();
		ans.add(loc);
		
		direction = Unit.FACING_LEFT;
		work();
		direction = Unit.FACING_DOWN;
		work();
		direction = Unit.FACING_RIGHT;
		work();
		direction = Unit.FACING_UP;
		work();
		return ans;
	}
	
	public static List<GPoint> getLaunchVision( Map iMAP, Unit iUNIT, int distance )
	{
		MAP = iMAP;
		UNIT = iUNIT;
		
		loc = UNIT.getXYCoordinate();
		view = distance;
		width = MAP.getNum_horizontal_tiles();
		height = MAP.getNum_vertical_tiles();
		direction = UNIT.getDirectionFacing();
		
		LOScheck=false;
		notUsed = new boolean[height][width];
		
		for(int a=0; a<notUsed.length; a++)
			for(int b=0; b<notUsed[a].length; b++)
				notUsed[a][b]=true;
		
		ans.clear();	
		ans.add(loc);
		
		direction = Unit.FACING_LEFT;
		work();
		direction = Unit.FACING_DOWN;
		work();
		direction = Unit.FACING_RIGHT;
		work();
		direction = Unit.FACING_UP;
		work();
		return ans;
	}

	private static void work()
	{
		limit();
		
		for(int i=left;i<=right;i++)
			for(int j=top;j<=bottom;j++) {
				if( (direction==Unit.FACING_UP)||(direction==Unit.FACING_DOWN) ) //up or down
				{
					if(notUsed[j][i])
						if( Math.abs(loc.row-j) >= Math.abs(loc.col-i) )
							if( (view+.5) > (Math.sqrt( ((loc.row-j)*(loc.row-j))+((loc.col-i)*(loc.col-i)) )) )
								if(LOSchecker(j,i))
								{
									point = pool.newObject();
									point.row = j; point.col = i;
									ans.add(point);
									notUsed[j][i] = false;
								}
				}
				else //left or right
				{
					if(notUsed[j][i])
						if( Math.abs(loc.row-j) <= Math.abs(loc.col-i) )
							if( (view+.5) > (Math.sqrt( ((loc.row-j)*(loc.row-j))+((loc.col-i)*(loc.col-i)) )) )
								if(LOSchecker(j,i))
								{
									point = pool.newObject();
									point.row = j; point.col = i;
									ans.add(point);
									notUsed[j][i] = false;
								}
				}
			}


	}
	
	private static void limit()
	{
		if(direction==Unit.FACING_UP)
		{
			left=loc.col-view;
			if(left<0)
				left=0;

			right=loc.col+view;
			if(right>=width)
				right=width-1;

			top=loc.row-view;
			if(top<0)
				top=0;

			bottom=loc.row-1;

		}//dir up
		else if(direction==Unit.FACING_DOWN)
		{
			left=loc.col-view;
			if(left<0)
				left=0;

			right=loc.col+view;
			if(right>=width)
				right=width-1;

			top=loc.row+1;

			bottom=loc.row+view;
			if(bottom>=height)
				bottom=height-1;
		}//dir down
		else if(direction==Unit.FACING_LEFT)
		{
			left=loc.col-view;
			if(left<0)
				left=0;

			right=loc.col-1;

			top=loc.row-view;
			if(top<0)
				top=0;

			bottom=loc.row+view;
			if(bottom>=height)
				bottom=height-1;
		}//dir left
		else if(direction==Unit.FACING_RIGHT)
		{
			left=loc.col+1;

			right=loc.col+view;
			if(right>=width)
				right=width-1;

			top=loc.row-view;
			if(top<0)
				top=0;

			bottom=loc.row+view;
			if(bottom>=height)
				bottom=height-1;
		}//dir right
	}

	private static boolean LOSchecker(int J, int I)
	{
		lineOfSight=true;

		if(!LOScheck)
			return lineOfSight;
		
		if( (direction==Unit.FACING_UP)||(direction==Unit.FACING_DOWN) ) //up or down
		{
			if(J < loc.row)
			{
				minR= J;
				minC= I;
				maxR= loc.row;
				maxC= loc.col;
			}
			else
			{
				minR= loc.row;
				minC= loc.col;
				maxR= J;
				maxC= I;
			}
			
			slope= (1.0)*(I-loc.col)/(J-loc.row);
			
			for(int k=minR+1; k<maxR; k++)
			{
				if(I > loc.col)
					tempY=(int) ((k-minR)*slope + minC +.49);
				else if(I < loc.col)
					tempY=(int) ((k-minR)*slope + minC +.51);
				else
					tempY=(int) ((k-minR)*slope + minC +.5);
				
				if(!(MAP.getFeature(k, tempY).isSeethrough()))
					lineOfSight=false;
			}
		}
		else //left or right
		{
			if(I < loc.col)
			{
				minR= J;
				minC= I;
				maxR= loc.row;
				maxC= loc.col;
			}
			else
			{
				minR= loc.row;
				minC= loc.col;
				maxR= J;
				maxC= I;
			}
			
			slope= (1.0)*(J-loc.row)/(I-loc.col);
			
			for(int k=minC+1; k<maxC; k++)
			{
				if(J > loc.row)
					tempX=(int) ((k-minC)*slope + minR +.49);
				else if(J < loc.row)
					tempX=(int) ((k-minC)*slope + minR +.51);
				else
					tempX=(int) ((k-minC)*slope + minR +.5);
				
				if(!(MAP.getFeature(tempX, k).isSeethrough()))
					lineOfSight=false;
			}
		}
		return lineOfSight;
	}
}