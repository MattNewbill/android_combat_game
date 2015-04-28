package combatgame.widgets;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.main.Game;

public abstract class ListView {

	protected List<ListViewRegion> items = new ArrayList<ListViewRegion>();
	protected int selectedItemIndex = 0;
	
	protected int softSelectedItemIndex = 0;
	protected TouchEvent previousTouchEvent;
	
	protected int offset = 0;
	public static final int SCROLL_SPEED = 3;
	
	protected Paint selectedPaint;
	protected Paint titlePaint;
	protected Paint subtitlePaint;
	
	
	public ListView() {		
		selectedPaint = new Paint();
		selectedPaint.setColor(Color.parseColor("#AAFFFFFF"));
		
		titlePaint = new Paint();
		titlePaint.setTextSize(38);
		
		subtitlePaint = new Paint();
		subtitlePaint.setTextSize(32);
	}
	
	public void update(List<TouchEvent> events) {
		//update position of list view and selected data because of touch events
		for(int i = 0; i < events.size(); i++) {
			if(events.get(i).x < ListViewRegion.WIDTH) {
				if(events.get(i).type == TouchEvent.TOUCH_DOWN) {
					int index = (events.get(i).y + offset) / ListViewRegion.HEIGHT;
					if(index < items.size())
						softSelectedItemIndex = index;
				}
				if(events.get(i).type == TouchEvent.TOUCH_UP) {
					int index = (events.get(i).y + offset) / ListViewRegion.HEIGHT;
					if(index == softSelectedItemIndex)
						selectedItemIndex = softSelectedItemIndex;
				}
				if(events.get(i).type == TouchEvent.TOUCH_DRAGGED) {
					if(previousTouchEvent != null)
						if(previousTouchEvent.type == TouchEvent.TOUCH_DRAGGED) {
							if(previousTouchEvent.y > events.get(i).y)
								offset += SCROLL_SPEED * (previousTouchEvent.y - events.get(i).y);
							else if(previousTouchEvent.y < events.get(i).y)
								offset -= SCROLL_SPEED * (events.get(i).y - previousTouchEvent.y);
						}
				}
			}
			previousTouchEvent = events.get(i);
		}
		
		//make sure we aren't going out of bounds
		if(offset < 0)
			offset = 0;
		else if(offset > ((items.size() - (Game.G_HEIGHT / ListViewRegion.HEIGHT)) * ListViewRegion.HEIGHT) && offset > 0)
			offset = (items.size() - (Game.G_HEIGHT / ListViewRegion.HEIGHT)) * ListViewRegion.HEIGHT;
			
	}
	
	public void render(Graphics2D g) {
		for(int i = 0; i < items.size(); i++) {
			if(i == selectedItemIndex) {
				titlePaint.setColor(Color.BLACK);
				subtitlePaint.setColor(Color.BLACK);
				g.drawRect(0, i * ListViewRegion.HEIGHT - offset, ListViewRegion.WIDTH, (i+1) * ListViewRegion.HEIGHT - offset, selectedPaint);
			}
			else {
				titlePaint.setColor(Color.WHITE);
				subtitlePaint.setColor(Color.WHITE);
			}
			
			Bitmap thumbnail = items.get(i).getThumbnail();
			if(thumbnail != null)
				g.drawBitmap(thumbnail, 20, i * ListViewRegion.HEIGHT + 10 - offset, null);
			
			g.drawText(items.get(i).getTitle(), 130, i * ListViewRegion.HEIGHT + 45 - offset, titlePaint);
			g.drawText(items.get(i).getSubtitle(), 130, i * ListViewRegion.HEIGHT + 85 - offset, subtitlePaint);			
		}
	}
	
	public String getSelectedItem() {
		return items.get(selectedItemIndex).getTitle();
	}
	
	public void recycle() {
		for(int i = 0; i < items.size(); i++)
			if(items.get(i).getThumbnail() != null)
				items.get(i).recycle();
		
		selectedPaint = null;
		titlePaint = null;
		subtitlePaint = null;
		previousTouchEvent = null;
	}
	
}
