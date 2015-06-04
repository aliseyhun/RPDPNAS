package nl.uva.creed.repeated;

import nl.uva.creed.game.Game;
import nl.uva.creed.repeated.exceptions.GameException;
import nl.uva.creed.repeated.impl.RepeatedGameResult;

public interface RepeatedGame {
	
	public Game oneShotGame();
	public RepeatedGameResult payOff(RepeatedGameStrategy strategy1, RepeatedGameStrategy strategy2, 
			double implementationMistakeProbability, double perceptionMistakeProbability) throws GameException;
	

}
