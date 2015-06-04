package nl.uva.creed.repeated.impl.restrictedbasic;

import nl.uva.creed.evolution.Strategy;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategyFactory;

public class RestrictedStrategy implements RepeatedGameStrategy {

	// 0 ALLD
	// 1 DTFT
	// 2 TFT
	// 3 ALLC

	protected int	genotype	= 0;

	public RestrictedStrategy(int genotype) {
		this.genotype = genotype;
	}

	public Strategy getCopy() throws InvalidStrategyException {
		return new RestrictedStrategy(this.genotype);
	}

	public Action nextAction(History history) {
		try {

			switch (this.genotype) {
			case 0:
				return ExplicitAutomatonStrategyFactory.alwaysDefect()
						.nextAction(history);
			case 1:
				return ExplicitAutomatonStrategyFactory.negativeHandshake()
						.nextAction(history);
			case 2:
				return ExplicitAutomatonStrategyFactory.titForTat().nextAction(
						history);
			case 3:
				return ExplicitAutomatonStrategyFactory.alwaysCooperate()
						.nextAction(history);
			default:
				throw new RuntimeException("Wrong strategy!");
			}
		} catch (InvalidStrategyException e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		}
	}

	public int getComplexity() {
		try {

			switch (this.genotype) {
			case 0:
				return ExplicitAutomatonStrategyFactory.alwaysDefect().getComplexity();
						
			case 1:
				return ExplicitAutomatonStrategyFactory.negativeHandshake().getComplexity();
			case 2:
				return ExplicitAutomatonStrategyFactory.titForTat().getComplexity();
			case 3:
				return ExplicitAutomatonStrategyFactory.alwaysCooperate().getComplexity();
			default:
				throw new RuntimeException("Wrong strategy!");
			}
		} catch (InvalidStrategyException e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		}

	}

	public int getGenotype() {
		return genotype;
	}

	@Override
	public String toString() {
		switch (genotype) {
		case 0:
		return "ALLD";
		case 1:
			return "DTFT";
		case 2:
			return "TFT";
		case 3:
			return "ALLC";
		default:
			return "KAPUT";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + genotype;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RestrictedStrategy))
			return false;
		RestrictedStrategy other = (RestrictedStrategy) obj;
		if (genotype != other.genotype)
			return false;
		return true;
	}

}
