// List<GPoint> result = Vision.getVision( Map, Unit );

package combatgame.util;

import java.util.*;

import combatgame.graphics.GPoint;
import combatgame.input.Pool;
import combatgame.input.TouchEvent;
import combatgame.input.Pool.PoolObjectFactory;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import android.util.Log;

/**
 * **HAPPY**
 */

public class Vision
{
	private static GPoint loc;
	private static int view;
	private static int width;
	private static int height;
//	private static char direction;
	private static int direction;
	
	private static Map MAP;
	private static Unit UNIT;

	private static int left, right, top, bottom;
	private static boolean[][] notUsed;
	private static List<GPoint> ans= new ArrayList<GPoint>();
	

	private static Pool<GPoint> pool;
	public static final int MAX_POOL_SIZE = 120;
	
	static {
		PoolObjectFactory<GPoint> factory = new PoolObjectFactory<GPoint>() {
			@Override
			public GPoint createObject() {
				return new GPoint(-1, -1);
			}
		};
		pool = new Pool<GPoint>(factory, MAX_POOL_SIZE);
	}

	
//	public static List<GPoint> getVision( GPoint locIn, int viewIn, int widthIn, int heightIn, char directionIn)
	public static List<GPoint> getSprintVision( Map iMAP, Unit iUNIT )
	{
//		loc= new GPoint(locIn);
//		view=viewIn;
//		width=widthIn;
//		height=heightIn;
//		direction=directionIn;
		
		MAP = iMAP;
		UNIT = iUNIT;
		
		loc = UNIT.getXYCoordinate();
		view = UNIT.getVisionRadius();
		width = MAP.getNum_horizontal_tiles();
		height = MAP.getNum_vertical_tiles();
		direction = UNIT.getDirectionFacing();
		
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
//		loc= new GPoint(locIn);
//		view=viewIn;
//		width=widthIn;
//		height=heightIn;
//		direction=directionIn;
		
		MAP = iMAP;
		UNIT = iUNIT;
		
		loc = UNIT.getXYCoordinate();
		view = UNIT.getVisionRadius()/2;
		width = MAP.getNum_horizontal_tiles();
		height = MAP.getNum_vertical_tiles();
//		direction = UNIT.getDirectionFacing();
		
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
				GPoint point = pool.newObject();
				if( (direction==Unit.FACING_UP)||(direction==Unit.FACING_DOWN) ) //up or down
//				if( (direction=='u')||(direction=='d') )
				{
					if(notUsed[j][i])
						if( Math.abs(loc.row-j) >= Math.abs(loc.col-i) )
							if( (view+.5) > (Math.sqrt( ((loc.row-j)*(loc.row-j))+((loc.col-i)*(loc.col-i)) )) )
							{
								point.row = j; point.col = i;
								ans.add(point);
								notUsed[j][i] = false;
//								ans.add(new GPoint(j,i));
							}
				}
				else //left or right
				{
					if(notUsed[j][i])
						if( Math.abs(loc.row-j) <= Math.abs(loc.col-i) )
							if( (view+.5) > (Math.sqrt( ((loc.row-j)*(loc.row-j))+((loc.col-i)*(loc.col-i)) )) )
							{
								point.row = j; point.col = i;
								ans.add(point);
								notUsed[j][i] = false;
//								ans.add(new GPoint(j,i));
							}
				}
			}


	}

	private static void limit()
	{
//		if(direction=='u')
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
		
//		else if(direction=='d')
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
		
//		else if(direction=='l')
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
		
//		else if(direction=='r')
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

}