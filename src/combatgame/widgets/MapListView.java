package combatgame.widgets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import combatgame.objects.PartialMap;
import combatgame.state.MapSelectionState;
import combatgame.util.Util;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MapListView extends ListView {

	List<PartialMap> partialMaps = new ArrayList<PartialMap>();
	
	public MapListView(AssetManager am, String[] mapPaths) {
		super();
		
		BufferedReader reader;
		try {
			for(int i = 0; i < mapPaths.length; i++) {
				reader = new BufferedReader(new InputStreamReader((am.open(MapSelectionState.mapPath+"/"+mapPaths[i]))));
				String[] mapData = reader.readLine().split(" ");
				
				//load map preview and scale to thumbnail
				String path = "maps/thumbnails/"+mapPaths[i].substring(0, mapPaths[i].length()-4)+".png";
				Bitmap preview = BitmapFactory.decodeStream(am.open(path));
				Bitmap thumbnail = Util.resizeBitmap(preview, 0.3, 0.3);
				
				//read in the description of the map
				for(int rows = 0; rows < Integer.parseInt(mapData[1]); rows++)
					reader.readLine();
				StringBuilder description = new StringBuilder();
				String line = "";
				while((line = reader.readLine()) != null) {
					description.append(line);
				}
				items.add(new ListViewRegion(thumbnail, mapPaths[i].substring(0, mapPaths[i].length()-4), mapData[3]));
				partialMaps.add(new PartialMap(preview, MapSelectionState.mapPath+"/"+mapPaths[i], mapPaths[i].substring(0, mapPaths[i].length()-4), mapData[3], description.toString().split(" "), mapData[2])); //TODO: load description of map
				reader.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public PartialMap getSelectedMap() {
		return partialMaps.get(selectedItemIndex);
	}
	
	@Override
	public void recycle() {
		super.recycle();
		for(int i = 0; i < partialMaps.size(); i++) {
			if(partialMaps.get(i).getPreview() != null)
				partialMaps.get(i).getPreview().recycle();
		}
	}
	
}
