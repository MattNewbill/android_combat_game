package combatgame.widgets;

import java.util.ArrayList;
import java.util.List;

import combatgame.input.TouchEvent;
import combatgame.util.Util;

import android.graphics.Bitmap;

public class CheckBox extends Button{

	protected int tempState = 0;
	
	public CheckBox(Bitmap disarmedImage, Bitmap armedImage, int x, int y) {
		super(disarmedImage, armedImage, x, y);
	}
	
	@Override
	public List<TouchEvent> update(List<TouchEvent> events) {
		//image is disabled so we shouldn't handle touch events
		if(state == DISABLED)
			return events;
		List<TouchEvent> unusedEvents = new ArrayList<TouchEvent>();
		for(int i = 0; i < events.size(); i++) {
			if(events.get(i).type == TouchEvent.TOUCH_DOWN) {
				if(Util.isInBounds(events.get(i), x, y, images[DISARMED].getWidth(), images[DISARMED].getHeight())) {
					if(state==DISARMED)
						tempState=DISARMED;
					if(state==ACTIVATED)
						tempState=ACTIVATED;
					state = ARMED;
				}
				else {
					unusedEvents.add(events.get(i));
				}
			}
			else if(events.get(i).type == TouchEvent.TOUCH_UP) {
				if(Util.isInBounds(events.get(i), x, y, images[DISARMED].getWidth(), images[DISARMED].getHeight()) && state == ARMED) {
					if(tempState==DISARMED)
						state = ACTIVATED;
					if(tempState==ACTIVATED)
						state = DISARMED;
				}
				else {
					
					if(tempState==DISARMED)
						state = DISARMED;
					if(tempState==ACTIVATED)
						state = ACTIVATED;
					unusedEvents.add(events.get(i));
				}
				tempState = ARMED;
			}
			else if(events.get(i).type == TouchEvent.TOUCH_DRAGGED) {
				if(!Util.isInBounds(events.get(i), x, y, images[DISARMED].getWidth(), images[DISARMED].getHeight())) {
					unusedEvents.add(events.get(i));
				}
			}
		}
		return unusedEvents;
	}
}