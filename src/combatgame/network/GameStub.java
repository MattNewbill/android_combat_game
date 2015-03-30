package combatgame.network;

import org.json.JSONObject;

public class GameStub {

	public String hostName;
	public long gameID;
	public String password = null;
	public int mapID;
	public int gamemodeID;
	
	public GameStub(String hostName, long gameID, int mapID, int gamemodeID) {
		this.hostName = hostName;
		this.gameID = gameID;
		this.mapID = mapID;
		this.gamemodeID = gamemodeID;
	}
	
	public GameStub(String hostName, long gameID, int mapID, int gamemodeID, String password) {
		this.hostName = hostName;
		this.gameID = gameID;
		this.mapID = mapID;
		this.gamemodeID = gamemodeID;
		this.password = password;
	}
	
	public JSONObject toJSON(long clientID) {
		JSONObject object = new JSONObject();
		try {
			object.put("client_player_id", clientID);
			object.put("game_id", gameID);
			
			if(password != null)
				object.put("password", password);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return object;
	}
}
