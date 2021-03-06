package combatgame.widgets;

import android.graphics.Bitmap;

public class ListViewRegion {

	private Bitmap thumbnail; //100x100
	private String title = "";
	private String subtitle = "";
	
	public static final int WIDTH = 640; //half the screen's width
	public static final int HEIGHT = 120; //90 = 8 rows per screen, 120 = 6 rows
	
	public ListViewRegion(Bitmap thumbnail, String title, String subtitle) {
		this.thumbnail = thumbnail;
		if(title != null)
			this.title = title;
		if(subtitle != null)
			this.subtitle = subtitle;
	}
	
	public Bitmap getThumbnail() {
		return thumbnail;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getSubtitle() {
		return subtitle;
	}
	
	public void recycle() {
		if(thumbnail != null) {
			thumbnail.recycle();
			thumbnail = null;
		}
	}
	
}
