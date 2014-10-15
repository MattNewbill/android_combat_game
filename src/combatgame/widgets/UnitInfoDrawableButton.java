package combatgame.widgets;

import java.util.List;
import combatgame.graphics.Graphics2D;
import combatgame.input.TouchEvent;
import combatgame.objects.Unit;
import android.graphics.Bitmap;
import android.graphics.Paint;

public class UnitInfoDrawableButton extends Button {
	private String ability_points = "";
	private String hp = "";
	private String unit_type = "";

	@Override
	public List<TouchEvent> update(List<TouchEvent> events) {
		// TODO Auto-generated method stub
		return super.update(events);
	}

	@Override
	public void render(Graphics2D g) {
		super.render(g);
		g.drawText(unit_type, x + 10, y + 30, new Paint());
		g.drawText(hp, x + 10, y + 60, new Paint());
		g.drawText(ability_points, x + 10, y + 90, new Paint());
		
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

	public UnitInfoDrawableButton(Bitmap disarmedImage, Bitmap armedImage, int x, int y) {
		super(disarmedImage, armedImage, x, y);
		
	}

	public void updateTextInfo(Unit unit) {
		ability_points = unit.getPointsLeft() + " / " + Unit.POINTS_PER_TURN;
		hp = unit.getHealth() + " / " + unit.getMaxHealth();
		unit_type = unit.getName();
	}

}
