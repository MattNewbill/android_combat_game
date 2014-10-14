// Point[] result = Vision.getVision( Map, Unit );

package combatgame.util;

import java.util.*;
import combatgame.objects.Map;
import combatgame.objects.Unit;
import android.graphics.Point;

/**
 * **HAPPY**
 */

public class Vision
{
	private static Point loc, temp;
	private static int view;
	private static int width;
	private static int height;
	private static int direction;
	
	private static Map MAP;
	private static Unit UNIT;

	private static int px, nx, py, ny;
	private static Point[] ans=new Point[0];

	public static Point[] getVision( Map iMAP, Unit iUNIT )
	{
		MAP=iMAP;
		UNIT=iUNIT;
		
		temp= UNIT.getXYCoordinate();
		loc= new Point(temp.x +1, temp.y +1);
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
			for(int j=nx;j<=px;j++)
				if( (direction==0)||(direction==1) ) //up or down
				{
					if( Math.abs(loc.x-j) <= Math.abs(loc.y-i) )
						if( (view+.5) > (Math.sqrt( ((loc.x-j)*(loc.x-j))+((loc.y-i)*(loc.y-i)) )) )
						{
							ans = Arrays.copyOf(ans, ans.length + 1);
							ans[ans.length-1]=new Point(j-1,i-1);
						}
				}
				else //left or right
				{
					if( Math.abs(loc.x-j) >= Math.abs(loc.y-i) )
						if( (view+.5) > (Math.sqrt( ((loc.x-j)*(loc.x-j))+((loc.y-i)*(loc.y-i)) )) )
						{
							ans = Arrays.copyOf(ans, ans.length + 1);
							ans[ans.length-1]=new Point(j-1,i-1);
						}
				}


	}

	private static void limit()
	{
		if(direction=='u')
		{
			nx=loc.x-view;
			if(nx<1)
				nx=1;

			px=loc.x+view;
			if(px>width)
				px=width;

			ny=loc.y-view;
			if(ny<1)
				ny=1;

			py=loc.y-1;
//			if(py<1)
//				py=1;
		}//dir up
		else if(direction=='d')
		{
			nx=loc.x-view;
			if(nx<1)
				nx=1;

			px=loc.x+view;
			if(px>width)
				px=width;

			ny=loc.y+1;
//			if(ny>height)
//				ny=height;

			py=loc.y+view;
			if(py>height)
				py=height;
		}//dir down
		else if(direction=='l')
		{
			nx=loc.x-view;
			if(nx<1)
				nx=1;

			px=loc.x-1;
//			if(px<1)
//				px=1;

			ny=loc.y-view;
			if(ny<1)
				ny=1;

			py=loc.y+view;
			if(py>height)
				py=height;
		}//dir left
		else if(direction=='r')
		{
			nx=loc.x+1;
//			if(nx>width)
//				nx=width;

			px=loc.x+view;
			if(px>width)
				px=width;

			ny=loc.y-view;
			if(ny<1)
				ny=1;

			py=loc.y+view;
			if(py>height)
				py=height;
		}//dir right
	}

}