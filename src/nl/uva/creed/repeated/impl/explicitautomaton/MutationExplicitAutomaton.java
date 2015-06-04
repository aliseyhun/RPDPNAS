package nl.uva.creed.repeated.impl.explicitautomaton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.Mutation;
import nl.uva.creed.evolution.Population;
import nl.vu.feweb.simulation.PseudoRandomSequence;
import nl.vu.feweb.simulation.SimulationUtils;

public class MutationExplicitAutomaton implements Mutation {
	// add, delete, change
	private static Double[] distribution = { 0.30, 0.35, 0.45 };

	public Individual mutate(Individual individual) {
		if (individual.getStrategy() instanceof ExplicitAutomatonStrategy) {
			ExplicitAutomatonStrategy automatonStrategy = ((ExplicitAutomatonStrategy) individual
					.getStrategy());
			automatonStrategy = this.mutateAutomaton(automatonStrategy);
			return new IndividualImpl(automatonStrategy);

		} else {
			throw new IllegalArgumentException(
					"This mutation only works with individuals whose strategies are  implementations of ExplicitAutomatonStrategy");
		}

	}

	public Population mutate(Population population, double mutationProbability) {
		for (int j = 0; j < population.getSize(); j++) {
			if (SimulationUtils.bernoullTrial(mutationProbability)) {
				Individual mutated = this.mutate(population.getIndividual(j));
				population.setIndividual(mutated, j);

			}
		}
		return population;
	}

	private ExplicitAutomatonStrategy mutateAutomaton(
			ExplicitAutomatonStrategy automatonStrategy) {

		// determine randomly type of operation
		List<Integer> integer = new ArrayList<Integer>();
		integer.add(SimulationUtils.simulateDiscreteDistribution(distribution));
		while (SimulationUtils.bernoullTrial(0.5)) {
			integer.add(SimulationUtils
					.simulateDiscreteDistribution(distribution));
		}

		for (Iterator<Integer> iter = integer.iterator(); iter.hasNext();) {
			Integer mutationType = iter.next();
			automatonStrategy = mutateAutomaton(automatonStrategy, mutationType);
			
		}
		return automatonStrategy;

	}

	private ExplicitAutomatonStrategy mutateAutomaton(
			ExplicitAutomatonStrategy automatonStrategy, Integer mutationType) {
		switch (mutationType) {
		case 0: // add
			return add(automatonStrategy);
		case 1: // delete something
			return remove(automatonStrategy);
		case 2: // changes a state
			return change(automatonStrategy);
		}
		throw new RuntimeException("Error en el codigo");
	}

	private ExplicitAutomatonStrategy change(
			ExplicitAutomatonStrategy automatonStrategy) {
		int stateIndex = PseudoRandomSequence.nextInt(automatonStrategy.size());
		if (PseudoRandomSequence.nextBoolean()) {
			// change state
			automatonStrategy.getState(stateIndex).flip();

		} else {
			// change arrows
			if (PseudoRandomSequence.nextBoolean()) {
				// change nextCoop
				automatonStrategy.getState(stateIndex).setNextCooperate(
						PseudoRandomSequence.nextInt(automatonStrategy.size()));
			} else {
				// change nextDef
				automatonStrategy.getState(stateIndex).setNextDefect(
						PseudoRandomSequence.nextInt(automatonStrategy.size()));
			}

		}
		return automatonStrategy;
	}

	private ExplicitAutomatonStrategy remove(
			ExplicitAutomatonStrategy automatonStrategy) {
		if (automatonStrategy.size() == 1) {
			return automatonStrategy;
		}
		int stateIndex = PseudoRandomSequence.nextInt(automatonStrategy.size());
		automatonStrategy.removeState(stateIndex);
		repairArrows(automatonStrategy, stateIndex);
		return automatonStrategy;
	}

	private void repairArrows(ExplicitAutomatonStrategy automatonStrategy,
			int deletedIndex) {
		for (int i = 0; i < automatonStrategy.size(); i++) {
			if (automatonStrategy.getState(i).getNextCooperate() == deletedIndex) {
				automatonStrategy.getState(i).setNextCooperate(
						PseudoRandomSequence.nextInt(automatonStrategy.size()));
			}
			if (automatonStrategy.getState(i).getNextDefect() == deletedIndex) {
				automatonStrategy.getState(i).setNextDefect(
						PseudoRandomSequence.nextInt(automatonStrategy.size()));
			}
			if (automatonStrategy.getState(i).getNextCooperate() > deletedIndex) {
				int newPointer = automatonStrategy.getState(i)
						.getNextCooperate() - 1;
				automatonStrategy.getState(i).setNextCooperate(newPointer);
			}
			if (automatonStrategy.getState(i).getNextDefect() > deletedIndex) {
				int newPointer = automatonStrategy.getState(i).getNextDefect() - 1;
				automatonStrategy.getState(i).setNextDefect(newPointer);
			}
		}
		if (!automatonStrategy.isValid()) {
			throw new RuntimeException("Mutation failed at reparing arrows");
		}
	}

	private ExplicitAutomatonStrategy add(
			ExplicitAutomatonStrategy automatonStrategy) {
		automatonStrategy.addState(AutomatonState.random(automatonStrategy
				.size() + 1));
		// make it reachable
		int stateIndex = PseudoRandomSequence
				.nextInt(automatonStrategy.size() - 1);
		if (PseudoRandomSequence.nextBoolean()) {
			// modify nextCoop
			automatonStrategy.getState(stateIndex).setNextCooperate(
					automatonStrategy.size() - 1);
		} else {
			// modify nextDef
			automatonStrategy.getState(stateIndex).setNextDefect(
					automatonStrategy.size() - 1);
		}
		return automatonStrategy;
	}

}
