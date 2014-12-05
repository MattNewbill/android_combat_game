package combatgame.widgets;

import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.main.Game;

public abstract class ListView {

	protected List<ListViewRegion> items = new ArrayList<ListViewRegion>();
	protected int selectedItemIndex = 0;
	
	protected int softSelectedItemIndex = 0;
	protected TouchEvent previousTouchEvent;
	
	protected int offset = 0;
	public static final int SCROLL_SPEED = 10;
	
	protected Paint selectedPaint;
	protected Paint titlePaint;
	protected Paint subtitlePaint;
	
	
	public ListView(AssetManager am) {		
		selectedPaint = new Paint();
		selectedPaint.setColor(Color.parseColor("#AAFFFFFF"));
		
		titlePaint = new Paint();
		titlePaint.setTextSize(34);
		titlePaint.setColor(Color.BLACK);
		
		subtitlePaint = new Paint();
		subtitlePaint.setTextSize(28);
		subtitlePaint.setColor(Color.BLACK);
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
								offset += SCROLL_SPEED;
							else if(previousTouchEvent.y < events.get(i).y)
								offset -= SCROLL_SPEED;
						}
				}
			}
			previousTouchEvent = events.get(i);
		}
		
		//make sure we aren't going out of bounds
		if(offset < 0)
			offset = 0;
		//else if(offset > (items.size() - 1) * ListViewRegion.HEIGHT)
		//	if(items.size() * ListViewRegion.HEIGHT > )
		//	offset = items.size() * ListViewRegion.HEIGHT;
		else if(offset > ListViewRegion.HEIGHT * (items.size() / (Game.G_HEIGHT / ListViewRegion.HEIGHT))) {
			offset = ListViewRegion.HEIGHT * (items.size() / (Game.G_HEIGHT / ListViewRegion.HEIGHT));
		}
			
	}
	
	public void render(Graphics2D g) {
		for(int i = 0; i < items.size(); i++) {
			if(i == selectedItemIndex)
				g.drawRect(0, i * ListViewRegion.HEIGHT - offset, ListViewRegion.WIDTH, (i+1) * ListViewRegion.HEIGHT - offset, selectedPaint);
			
			g.drawBitmap(items.get(i).getThumbnail(), 20, i * ListViewRegion.HEIGHT + 10 - offset, null);
			
			g.drawText(items.get(i).getTitle().substring(0, items.get(i).getTitle().length() - 4), 130, i * ListViewRegion.HEIGHT + 45 - offset, titlePaint);
			g.drawText(items.get(i).getSubtitle(), 130, i * ListViewRegion.HEIGHT + 85 - offset, subtitlePaint);			
		}
	}
	
	public String getSelectedItem() {
		return items.get(selectedItemIndex).getTitle();
	}
	
	public void recycle() {
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getThumbnail() != null)
				items.get(i).getThumbnail().recycle();
		}
	}
	
}
