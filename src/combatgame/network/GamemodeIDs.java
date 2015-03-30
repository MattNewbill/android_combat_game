package combatgame.network;

import combatgame.gamemode.*;

public class GamemodeIDs {

	public static final int ASSAULT_BATTLE = 0;
	public static final int CLASSIC = 1;
	public static final int HUNTED = 2;
	public static final int MEAT_GRINDER = 3;
	public static final int MEDICCC = 4;
	public static final int SHOOTING_FLIES = 5;
	public static final int SNIPER_BATTLE = 6;
	
	public static GameMode getGamemodeFromID(int id) {
		switch(id) {
			case ASSAULT_BATTLE:
				return new AssaultBattle(null, "");
			case CLASSIC:
				return new Classic(null, "");
			case HUNTED:
				return new Hunted(null, "");
			case MEAT_GRINDER:
				return new MeatGrinder(null, "");
			case MEDICCC:
				return new Mediccc(null, "");
			case SHOOTING_FLIES:
				return new ShootingFlies(null, "");
			case SNIPER_BATTLE:
				return new SniperBattle(null, "");
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
			case HUNTED:
				return "Hunted";
			case MEAT_GRINDER:
				return "Meat Grinder";
			case MEDICCC:
				return "Mediccc";
			case SHOOTING_FLIES:
				return "Shooting Flies";
			case SNIPER_BATTLE:
				return "Sniper Battle";
			default:
				return "Classic";
		}
	}
	
}
