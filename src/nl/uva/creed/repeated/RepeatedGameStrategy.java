package nl.uva.creed.repeated;

import nl.uva.creed.evolution.Strategy;
import nl.uva.creed.game.Action;

public interface RepeatedGameStrategy extends Strategy{
	
	public Action nextAction(History history);
	public int getComplexity();


}
