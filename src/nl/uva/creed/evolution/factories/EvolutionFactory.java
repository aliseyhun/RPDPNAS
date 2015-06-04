package nl.uva.creed.evolution.factories;

import nl.uva.creed.evolution.Evolver;
import nl.uva.creed.evolution.MoranEvolverImpl;
import nl.uva.creed.evolution.WrightFisherEvolverImpl;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;

public class EvolutionFactory {
	// CODE 0 - Wright Fisher
	// CODE 1 - Moran

	public static Evolver getEvolver(int representationCode)
			throws InvalidStrategyException {
		switch (representationCode) {
		case 0:
			return new WrightFisherEvolverImpl();
		case 1:
			return new MoranEvolverImpl();
		default:
			return null;
		}
	}

	
}
