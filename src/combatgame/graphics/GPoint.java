package combatgame.graphics;

import java.io.Serializable;

public class GPoint implements Serializable {

	private static final long serialVersionUID = 1L;
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

	public boolean equals(GPoint point) {
		return this.row == point.row && this.col == point.col;
	}
	
	public GPoint clone() {
		return new GPoint(row, col);
	}
	
}
