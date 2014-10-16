// Point[] result = Vision.getVision( Map, Unit );

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
	private static GPoint loc, temp;
	private static int view;
	private static int width;
	private static int height;
	private static int direction;
	
	private static Map MAP;
	private static Unit UNIT;

	private static int px, nx, py, ny;
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
	
	public static List<GPoint> getVision( Map iMAP, Unit iUNIT )
	{
		MAP=iMAP;
		UNIT=iUNIT;
		
		temp= UNIT.getXYCoordinate();
		loc= new GPoint(temp.row +1, temp.col +1);
		view=UNIT.getVisionRadius();
		width = MAP.getNum_horizontal_tiles();
		height = MAP.getNum_vertical_tiles();
		direction=UNIT.getDirectionFacing();
		
		work();
		return ans;
	}

	private static void work()
	{
		limit();

		for(int i=ny;i<=py;i++)
			for(int j=nx;j<=px;j++) {
				GPoint point = pool.newObject();
				if( (direction==Unit.FACING_UP)||(direction==Unit.FACING_DOWN) ) //up or down
				{
					if( Math.abs(loc.row-j) <= Math.abs(loc.col-i) )
						if( (view+.5) > (Math.sqrt( ((loc.row-j)*(loc.row-j))+((loc.col-i)*(loc.col-i)) )) )
						{
							//ans = Arrays.copyOf(ans, ans.length + 1);
							point.row = j-1; point.col = i-1;
							ans.add(point);
						}
				}
				else //left or right
				{
					if( Math.abs(loc.row-j) >= Math.abs(loc.col-i) )
						if( (view+.5) > (Math.sqrt( ((loc.row-j)*(loc.row-j))+((loc.col-i)*(loc.col-i)) )) )
						{
							//ans = Arrays.copyOf(ans, ans.length + 1);
							point.row = j-1; point.col = i-1;
							ans.add(point);
						}
				}
			}


	}

	private static void limit()
	{
		if(direction==Unit.FACING_UP)
		{
			nx=loc.row-view;
			if(nx<1)
				nx=1;

			px=loc.row+view;
			if(px>width)
				px=width;

			ny=loc.col-view;
			if(ny<1)
				ny=1;

			py=loc.col-1;
//			if(py<1)
//				py=1;
		}//dir up
		else if(direction==Unit.FACING_DOWN)
		{
			nx=loc.row-view;
			if(nx<1)
				nx=1;

			px=loc.row+view;
			if(px>width)
				px=width;

			ny=loc.col+1;
//			if(ny>height)
//				ny=height;

			py=loc.col+view;
			if(py>height)
				py=height;
		}//dir down
		else if(direction==Unit.FACING_LEFT)
		{
			nx=loc.row-view;
			if(nx<1)
				nx=1;

			px=loc.row-1;
//			if(px<1)
//				px=1;

			ny=loc.col-view;
			if(ny<1)
				ny=1;

			py=loc.col+view;
			if(py>height)
				py=height;
		}//dir left
		else if(direction==Unit.FACING_RIGHT)
		{
			nx=loc.row+1;
//			if(nx>width)
//				nx=width;

			px=loc.row+view;
			if(px>width)
				px=width;

			ny=loc.col-view;
			if(ny<1)
				ny=1;

			py=loc.col+view;
			if(py>height)
				py=height;
		}//dir right
	}

}