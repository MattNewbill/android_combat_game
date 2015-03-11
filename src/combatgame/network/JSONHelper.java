package combatgame.network;

import org.json.JSONArray;
import org.json.JSONObject;

import combatgame.objects.Map;
import combatgame.objects.Player;
import combatgame.objects.Unit;

public class JSONHelper {

	public static JSONObject turnToJSON(long gameID, Map map) {
		JSONObject turn = new JSONObject();
		JSONObject wrapper = new JSONObject();
		
		//write player 1 to JSON object
		Player p1 = map.getPlayer1();
		JSONObject p1JSON = writePlayerToJSON(p1);
		
		//write player 2 to JSON object
		Player p2 = map.getPlayer2();
		JSONObject p2JSON = writePlayerToJSON(p2);
		
		//write data to turn object
		try {
			wrapper.put("game_id",  gameID);
			wrapper.put("player1", p1JSON);
			wrapper.put("player2", p2JSON);
			if(map.getCurrentPlayersTurn() == p1)
				wrapper.put("is_player_ones_turn", true);
			else
				wrapper.put("is_player_ones_turn", false);
			
			turn.put("turn", wrapper);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return turn;
	}
	
	private static JSONObject writePlayerToJSON(Player player) {
		Unit[] pUnits = player.getUnits();
		JSONObject pJSON = new JSONObject();
		JSONArray pUnitsJSON = new JSONArray();
		try {
			for(int i = 0; i < pUnits.length; i++) {
				//write each unit to their own JSON object
				JSONObject pUnitJSON = new JSONObject();
				pUnitJSON.put("name", pUnits[i].getName());
				if(pUnits[i].getXYCoordinate() == null) {
					pUnitJSON.put("row", -1);
					pUnitJSON.put("col", -1);
				}
				else {
					pUnitJSON.put("row", pUnits[i].getXYCoordinate().row);
					pUnitJSON.put("col", pUnits[i].getXYCoordinate().col);
				}
				pUnitJSON.put("health", pUnits[i].getHealth());
				pUnitJSON.put("armor", pUnits[i].getArmor());
				pUnitJSON.put("is_dead", pUnits[i].isDead());
				pUnitJSON.put("direction_facing", pUnits[i].getDirectionFacing());
				
				//add each unit to the "units" JSON object
				pUnitsJSON.put(pUnitJSON);
			}
			pJSON.put("units", pUnitsJSON);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return pJSON;
	}
	
	public static void JSONToTurn(JSONObject object) {
		
	}
	
}
