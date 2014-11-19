package combatgame.widgets;

import java.util.List;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.objects.Unit;
import android.graphics.Bitmap;
import android.graphics.Paint;

public class UnitInfoDrawableButton extends Button {
	
	private Paint paint;
	
	private String ability_points = "";
	private String hp = "";
	private String unit_type = "";
	
	public UnitInfoDrawableButton(Paint paint, Bitmap disarmedImage, Bitmap armedImage, int x, int y) {
		super(disarmedImage, armedImage, x, y);
		this.paint = paint;
		
	}
	
	@Override
	public List<TouchEvent> update(List<TouchEvent> events) {
		return super.update(events);
	}

	@Override
	public void render(Graphics2D g) {
		super.render(g);
		g.drawText(unit_type, x + 10, y + 30, paint);
		g.drawText(hp, x + 10, y + 65, paint);
		g.drawText(ability_points, x + 10, y + 100, paint);
		
	}

	@Override
	public void disable() {
		super.disable();
		ability_points = "";
		hp = "";
		unit_type = "";
	}

	@Override
	public void enable() {
		super.enable();
	}

	public void updateTextInfo(Unit unit) {
		ability_points = "AP: " + unit.getPointsLeft() + " / " + Unit.POINTS_PER_TURN;
		hp = "HP: " + unit.getHealth() + " / " + unit.getMaxHealth();
		unit_type = unit.getName();
	}
	
	public void updateTextSetupInfo(Unit unit) {
		unit_type = unit.getName();
	}

}
