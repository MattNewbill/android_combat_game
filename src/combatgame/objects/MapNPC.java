package combatgame.objects;

import combatgame.gamemode.GameMode;
import combatgame.state.GameState;
import android.content.res.AssetManager;

public class MapNPC extends Map {
	private static final long serialVersionUID = 1L;
	
	public MapNPC(GameState gamestate, AssetManager am, String filePath, GameMode gm) {
		super(gamestate, am, filePath, gm, true);
		player2 = new NPC("Computer", false, this, gm.getPlayer2Units());
	}
	
	@Override
	protected void checkWinConditions() {
		if(gamestate.getCheckWin()){
			if(thisPlayersTurn.isPlayerOne){
				int victory = gamemode.checkWinConditions(this);
				
				//both teams wiped out, stalemate
				if(victory == GameMode.STALEMATE) {
					isGameOver = true;
				}
				//player 2 wins
				else if(victory == GameMode.PLAYER_2_WINS) {
					isGameOver = true;
					winningPlayer = player2;
				}
				//player 1 wins
				else if(victory == GameMode.PLAYER_1_WINS) {
					isGameOver = true;
					winningPlayer = player1;
				}
			}
		}
	}
}