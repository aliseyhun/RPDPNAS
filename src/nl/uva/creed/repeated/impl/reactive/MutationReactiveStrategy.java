package nl.uva.creed.repeated.impl.reactive;

import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.Mutation;
import nl.uva.creed.evolution.Population;
import nl.vu.feweb.simulation.SimulationUtils;

public class MutationReactiveStrategy implements Mutation {

	public Individual mutate(Individual individual) {
		if (individual.getStrategy() instanceof ReactiveStrategy) {
			ReactiveStrategy strategy = ((ReactiveStrategy) individual.getStrategy());
			strategy.flipRandom();
			return new IndividualImpl(strategy);

		} else {
			throw new IllegalArgumentException(
					"This mutation only works with individuals whose strategies are  implementations of RestrictedStrategy");
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

}
