package combatgame.network;

import org.json.JSONObject;

public class PostWrapper {

	public String url;
	public JSONObject obj;
	
	public PostWrapper(String url, JSONObject obj) {
		this.url = url;
		this.obj = obj;
	}
	
}
