package combatgame.widgets;

import combatgame.network.GameStub;

public class ServerListView extends ListView {

	private GameStub[] games;
	
	public ServerListView(GameStub[] games) {
		super();
		
		this.games = games;
		
		for(int i = 0; i < games.length; i++) {
			if(games[i].password != null)
				items.add(new ListViewRegion(null, games[i].hostName, "Password Protected: Yes"));
			else
				items.add(new ListViewRegion(null, games[i].hostName, "Password Protected: No"));
		}
	}
	
	public GameStub getSelectedGame() {
		return games[selectedItemIndex];
	}
	
	@Override
	public void recycle() {
		super.recycle();
		for(int i = 0; i < games.length; i++)
			games[i] = null;
		
		games = null;
	}
	
}
