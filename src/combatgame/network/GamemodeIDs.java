package combatgame.network;

import combatgame.gamemode.*;

public class GamemodeIDs {

	public static final int ASSAULT_BATTLE = 0;
	public static final int CLASSIC = 1;
	public static final int MEAT_GRINDER = 2;
	public static final int SNIPER_BATTLE = 3;
	public static final int SPOTTER = 4;
	public static final int FIGHT_1V1 = 5;
	public static final int CONVOY = 6;
	public static final int RECONNAISSANCE = 7;
	                        
	
	public static GameMode getGamemodeFromID(int id) {
		switch(id) {
			case ASSAULT_BATTLE:
				return new AssaultBattle(null, "");
			case CLASSIC:
				return new Classic(null, "");
			case MEAT_GRINDER:
				return new MeatGrinder(null, "");
			case SNIPER_BATTLE:
				return new SniperBattle(null, "");
			case SPOTTER:
				return new Spotter(null, "");
			case FIGHT_1V1:
				return new Fight1v1(null, "");
			case CONVOY:
				return new Convoy(null, "");
			case RECONNAISSANCE:
				return new Reconnaissance(null, "");
			default:
				return new Classic(null, "");
		}
	}
	
	public static String getGamemodeNameFromID(int id) {
		switch(id) {
			case ASSAULT_BATTLE:
				return "Assault Battle";
			case CLASSIC:
				return "Classic";
			case MEAT_GRINDER:
				return "Meat Grinder";
			case SNIPER_BATTLE:
				return "Sniper Battle";
			case SPOTTER:
				return "Spotter";
			case FIGHT_1V1:
				return "1v1";
			case CONVOY:
				return "Convoy";
			case RECONNAISSANCE:
				return "Reconnaissance";
			default:
				return "Classic";
		}
	}
	
}
