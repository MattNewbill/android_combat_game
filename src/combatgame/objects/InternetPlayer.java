package combatgame.objects;

public class InternetPlayer extends Player {

	private static final long serialVersionUID = 1L;

	public InternetPlayer(String gamertag, boolean isPlayerOne, Map map, Unit[] units) {
		super(gamertag, isPlayerOne, map, units);
	}

}
