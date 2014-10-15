package combatgame.graphics;

public class GPoint {

	public int row;
	public int col;
	
	public GPoint(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public GPoint(GPoint GP)
	{
		this.row = GP.row;
		this.col = GP.col;
	}
	
}
