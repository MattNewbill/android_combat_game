package combatgame.widgets;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import combatgame.state.MapSelectionState;

import android.content.res.AssetManager;

public class MapListView extends ListView {

	public MapListView(AssetManager am, String[] mapPaths) {
		super(am);
		
		BufferedReader reader;
		try {
			for(int i = 0; i < mapPaths.length; i++) {
				reader = new BufferedReader(new InputStreamReader((am.open(MapSelectionState.mapPath+"/"+mapPaths[i]))));
				String[] mapData = reader.readLine().split(" ");
				items.add(new ListViewRegion(null, mapPaths[i], mapData[3])); //TODO: get thumbnail of map
				reader.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
