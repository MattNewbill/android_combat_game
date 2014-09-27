package combatgame.state;

import combatgame.main.*;
import combatgame.graphics.*;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.Log;

public class TestState extends State {

	int width = 96;
	int height = 96;
	int[][] map = {{1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
				   {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
				   {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
				   {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0}};
	Paint paint;
	
	public TestState(StateManager stateManager) {
		super(stateManager);
		paint = new Paint();
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(Graphics2D g, float delta) {
		for(int row = 0; row < map.length; row++) {
			for(int col = 0; col < map[row].length; col++) {
				if(map[row][col] == 1) {
					paint.setColor(Color.RED);
				}
				else {
					paint.setColor(Color.GREEN);
				}
				g.drawRect(col * width, row * height, col * width + width, row * height + height, paint);
				//Log.i("combatgame", "left: "+col*width+" right: "+(col*width+width)+" top: "+row*height+" bottom: "+(row*height+height));
				//c.drawRect(10, 10, 10, 10, paint);
			}
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}
