package combatgame.objects;

import java.io.Serializable;

import android.graphics.Bitmap;

public class PartialMap implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient Bitmap preview; //300x300
	
	private String path;
	private String name;
	private String size;
	private String[] description;
	private String tileset;
	
	
	public PartialMap(Bitmap preview, String path, String name, String size, String[] description, String tileset) {
		this.preview = preview;
		this.path = path;
		this.name = name;
		this.size = size;
		this.description = description;
		this.tileset = tileset;
	}
	
	public Bitmap getPreview() {
		return preview;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getName() {
		if(name == null)
			return "";
		return name;
	}
	
	public String getSize() {
		if(size == null)
			return "";
		return size;
	}
	
	public String[] getDescription() {
		return description;
	}
	
	public String getTileset() {
		return tileset;
	}
	
}
