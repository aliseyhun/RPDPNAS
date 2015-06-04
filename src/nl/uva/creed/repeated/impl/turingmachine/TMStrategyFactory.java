package nl.uva.creed.repeated.impl.turingmachine;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.repeated.impl.turingmachine.machinery.TuringState;

public class TMStrategyFactory {

	public static final TMStrategy alwaysCooperate() throws InvalidStrategyException {
		TuringState state = new TuringState(true);
		return new TMStrategy(state);
		
	}
	
	public static final TMStrategy alwaysDefect() throws InvalidStrategyException {
		TuringState state = new TuringState(false);
		return new TMStrategy(state);
		
	}

}
