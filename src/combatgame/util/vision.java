package combatgame.util;
/* vision v1 = new vision(Point location, int view, int width, int height, char direction);
 * Point[] result1 = v1.getVision();
 */

import java.util.*;
import android.graphics.Point;

public class vision
{
	private Point loc;
	private int view;
	private int width;
	private int height;
	private char direction;

	private int px, nx, py, ny;
	private Point[] ans=new Point[0];

	public vision( Point locIn, int viewIn, int widthIn, int heightIn, char directionIn)
	{
		loc= new Point(locIn.x+1,locIn.y+1);
		view=viewIn;
		width=widthIn;
		height=heightIn;
		direction=directionIn;
	}//public vision( Point locIn, int viewIn, int widthIn, int heightIn, char directionIn)

	public Point[] getVision()
	{
		work();
		return ans;
	}//public Point getVision()

	private void work()
	{
		limit();

		for(int i=ny;i<=py;i++)
			for(int j=nx;j<=px;j++)
				if( (direction=='u')||(direction=='d') )
				{
					if( Math.abs(loc.x-j) <= Math.abs(loc.y-i) )
						if( (view+.5) > (Math.sqrt( ((loc.x-j)*(loc.x-j))+((loc.y-i)*(loc.y-i)) )) )
						{
							ans = Arrays.copyOf(ans, ans.length + 1);
							ans[ans.length-1]=new Point(j-1,i-1);
						}
				}
				else
				{
					if( Math.abs(loc.x-j) >= Math.abs(loc.y-i) )
						if( (view+.5) > (Math.sqrt( ((loc.x-j)*(loc.x-j))+((loc.y-i)*(loc.y-i)) )) )
						{
							ans = Arrays.copyOf(ans, ans.length + 1);
							ans[ans.length-1]=new Point(j-1,i-1);
						}
				}


	}//private void work()

	private void limit()
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
	}//private void limit()

}//public class vision