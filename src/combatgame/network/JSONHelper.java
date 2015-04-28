package combatgame.network;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import combatgame.alerts.HitIndicator;
import combatgame.objects.Map;
import combatgame.objects.Player;
import combatgame.objects.Unit;

public class JSONHelper {

	public static JSONObject turnToJSON(long gameID, Map map, int turnNumber) {
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
			wrapper.put("host_user", p1JSON);
			wrapper.put("client_user", p2JSON);
			wrapper.put("turn_number", turnNumber);
			
			turn.put("Turn", wrapper);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return turn;
	}
	
	private static JSONObject writePlayerToJSON(Player player) {
		Unit[] pUnits = player.getUnits();
		List<HitIndicator> pHitIndicators = player.getHitIndicators();
		JSONObject pJSON = new JSONObject();
		JSONArray pUnitsJSON = new JSONArray();
		JSONArray pHitIndicatorsJSON = new JSONArray();
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
				pUnitJSON.put("hp", pUnits[i].getHealth());
				pUnitJSON.put("arm", pUnits[i].getArmor());
				pUnitJSON.put("is_dead", pUnits[i].isDead());
				pUnitJSON.put("direction_facing", pUnits[i].getDirectionFacing());
				
				//add each unit to the "units" JSON object
				pUnitsJSON.put(pUnitJSON);
			}
			pJSON.put("units", pUnitsJSON);
			
			for(int i = 0; i < pHitIndicators.size(); i++) {
				JSONObject pHitIndicatorJSON = new JSONObject();
				pHitIndicatorJSON.put("row", pHitIndicators.get(i).tile.row);
				pHitIndicatorJSON.put("col", pHitIndicators.get(i).tile.col);
				pHitIndicatorJSON.put("direction", pHitIndicators.get(i).direction);
				
				//add each indicator to the "indicators" JSON object
				pHitIndicatorsJSON.put(pHitIndicatorJSON);
			}
			pJSON.put("hit_indicators", pHitIndicatorsJSON);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return pJSON;
	}
	
}
