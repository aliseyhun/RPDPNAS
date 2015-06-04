package nl.uva.creed.evolution.factories;

import nl.uva.creed.evolution.Mutation;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.impl.binarytree.BinaryTreeStrategyFactory;
import nl.uva.creed.repeated.impl.binarytree.MutationBinaryTreeStrategy;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategyFactory;
import nl.uva.creed.repeated.impl.explicitautomaton.MutationExplicitAutomaton;
import nl.uva.creed.repeated.impl.reactive.MutationReactiveStrategy;
import nl.uva.creed.repeated.impl.reactive.ReactiveStrategy;
import nl.uva.creed.repeated.impl.restrictedbasic.MutationRestrictedStrategy;
import nl.uva.creed.repeated.impl.restrictedbasic.RestrictedStrategy;
import nl.uva.creed.repeated.impl.turingmachine.MutationTMStrategy;
import nl.uva.creed.repeated.impl.turingmachine.TMStrategyFactory;

public class RepresentationFactory {

	// CODE 1 - Tree genome
	// CODE 2 - Explicit genome
	// CODE 3 - Turing machine
	// CODE 4 - Restricted
	
	public static RepeatedGameStrategy getIncumbent(int representationCode)
			throws InvalidStrategyException {
		switch (representationCode) {
		case 1:
			return BinaryTreeStrategyFactory.alwaysDefect();
		case 2:
			return ExplicitAutomatonStrategyFactory.alwaysDefect();
		case 3:
			return TMStrategyFactory.alwaysDefect();
		case 4:
			return new RestrictedStrategy(0);
		case 5:
			return new ReactiveStrategy();
		default:
			return null;
		}
	}

	public static Mutation getMutation(int representationCode) {
		switch (representationCode) {
		case 1:
			return new MutationBinaryTreeStrategy();
		case 2:
			return new MutationExplicitAutomaton();
		case 3:
			return new MutationTMStrategy();
		case 4:
			return new MutationRestrictedStrategy();
		case 5:
			return new MutationReactiveStrategy();
		default:
			return null;
		}
	}

}
