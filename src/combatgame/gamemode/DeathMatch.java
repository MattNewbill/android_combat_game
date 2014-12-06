package combatgame.gamemode;

import android.content.res.AssetManager;
import combatgame.objects.Map;
import combatgame.objects.Unit;

public abstract class DeathMatch extends GameMode {

	public DeathMatch(AssetManager am, String path) {
		super(am, path);
	}
	
	@Override
	public int checkWinConditions(Map map) {
		Unit[] player1Units = map.getPlayer1().getUnits();
		Unit[] player2Units = map.getPlayer2().getUnits();
		boolean isPlayer1Destroyed = true;
		boolean isPlayer2Destroyed = true;
		for(int i = 0; i < player1Units.length; i++) {
			if(!player1Units[i].isDead())
				isPlayer1Destroyed = false;
		}
		for(int i = 0; i < player2Units.length; i++) {
			if(!player2Units[i].isDead())
				isPlayer2Destroyed = false;
		}
		//both teams wiped out, stalemate
		if(isPlayer1Destroyed && isPlayer2Destroyed) {
			return STALEMATE;
		}
		//player 2 wins
		else if(isPlayer1Destroyed && !isPlayer2Destroyed) {
			return PLAYER_2_WINS;
		}
		//player 1 wins
		else if(!isPlayer1Destroyed && isPlayer2Destroyed) {
			return PLAYER_1_WINS;
		}
		else {
			return IN_PROGRESS;
		}
	}
	
}
