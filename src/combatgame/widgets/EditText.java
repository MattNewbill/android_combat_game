package combatgame.widgets;

import java.util.List;

import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.util.Util;
import android.graphics.Paint;

public class EditText {

	public static final int TEXT_SIZE = 50;
	
	private String defaultText;
	private int x, y;
	private boolean shouldCenter;
	private StringBuilder text = new StringBuilder();
	private String focusedText = "_";
	private int maxTextLength;
	
	private boolean hasFocus;
	
	private Paint backgroundPaint, textPaint, defaultTextPaint;
	
	public EditText(String defaultString, int xpos, int ypos, boolean center, int maxCharacters, int backgroundColor, int textColor, int defaultTextColor) {
		defaultText = defaultString;
		x = xpos;
		y = ypos;
		shouldCenter = center;
		maxTextLength = maxCharacters;
		
		backgroundPaint = new Paint();
		backgroundPaint.setColor(backgroundColor);
		
		textPaint = new Paint();
		textPaint.setColor(textColor);
		textPaint.setTextSize(TEXT_SIZE);
		
		defaultTextPaint = new Paint();
		defaultTextPaint.setColor(defaultTextColor);
		defaultTextPaint.setTextSize(TEXT_SIZE);
	}
	
	public boolean update(List<TouchEvent> events) {
		for(int i = 0; i < events.size(); i++) {
			if(events.get(i).type == TouchEvent.TOUCH_UP) {
				if(shouldCenter) {
					if(Util.isInBounds(events.get(i), (int)(x - (textPaint.measureText("W") * maxTextLength) / 2), y, (int)(textPaint.measureText("W") * maxTextLength), TEXT_SIZE + 10))
						return true;
				}
				else {
					if(Util.isInBounds(events.get(i), x, y, (int)(textPaint.measureText("W") * maxTextLength), TEXT_SIZE + 10))
						return true;
				}
			}
		}
		return false;
	}
	
	public void render(Graphics2D g) {			
		if(shouldCenter) {
			g.drawRect(x - (textPaint.measureText("W") * maxTextLength) / 2, y, x + (textPaint.measureText("W") * maxTextLength) / 2, y + TEXT_SIZE + 10, backgroundPaint);
			if(text.length() == 0)
				g.drawText(defaultText, x - ((textPaint.measureText("W") * maxTextLength) / 2)+5, y+TEXT_SIZE, defaultTextPaint);
			if(hasFocus)
				g.drawText(focusedText, x - ((textPaint.measureText("W") * maxTextLength) / 2)+5, y+TEXT_SIZE, textPaint);
			else
				g.drawText(text.toString(), x - ((textPaint.measureText("W") * maxTextLength) / 2)+5, y+TEXT_SIZE, textPaint);
		}
		else {
			g.drawRect(x, y, x + textPaint.measureText("W") * maxTextLength, y + TEXT_SIZE + 10, backgroundPaint);
			if(text.length() == 0)
				g.drawText(defaultText, x+5, y+TEXT_SIZE, defaultTextPaint);
			if(hasFocus)
				g.drawText(focusedText, x+5, y+TEXT_SIZE, textPaint);
			else
				g.drawText(text.toString(), x+5, y+TEXT_SIZE, textPaint);
		}
	}
	
	public void addCharacter(char c) {
		if(text.length() < maxTextLength)
			text.append(c);
		setFocusedText();
	}
	
	public void removeCharacter() {
		if(text.length() > 0)
			text = new StringBuilder(text.substring(0, text.length() - 1));
		setFocusedText();
	}
	
	public void setFocus(boolean focus) {
		hasFocus = focus;
	}
	
	public String getText() {
		return text.toString();
	}
	
	public void clear() {
		text = new StringBuilder("");
		setFocusedText();
	}
	
	private void setFocusedText() {
		focusedText = (text.toString() + "_");
	}
	
}
