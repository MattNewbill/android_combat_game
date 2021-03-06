package combatgame.widgets;

import combatgame.gamemode.*;
import combatgame.util.Util;

import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;

public class GamemodeListView extends ListView {

	List<GameMode> gamemodes = new ArrayList<GameMode>();
	
	public GamemodeListView(AssetManager am) {
		super();
		
		//set gamemodes
		gamemodes.add(new Classic(am, "classic.png"));
		gamemodes.add(new Reconnaissance(am, "reconnaissance.png"));
		gamemodes.add(new Spotter(am, "spotter.png"));
		gamemodes.add(new Convoy(am, "convoy.png"));
		gamemodes.add(new SniperBattle(am, "sniper_battle.png"));
		gamemodes.add(new AssaultBattle(am, "assault_battle.png"));
		gamemodes.add(new MeatGrinder(am, "meat_grinder.png"));
		gamemodes.add(new Fight1v1(am, "fight1v1.png"));
		
		
		
		//set listview regions
		for(int i = 0; i < gamemodes.size(); i++) {
			items.add(new ListViewRegion(Util.resizeBitmap(gamemodes.get(i).getPreview(), 0.3, 0.3), gamemodes.get(i).getName(), gamemodes.get(i).getLength()));
		}
	}
	
	public GameMode getSelectedGamemode() {
		return gamemodes.get(selectedItemIndex);
	}
	
	@Override
	public void recycle() {
		super.recycle();
		for(int i = 0; i < gamemodes.size(); i++) {
			if(gamemodes.get(i).getPreview() != null)
				gamemodes.get(i).getPreview().recycle();
		}
	}

}
