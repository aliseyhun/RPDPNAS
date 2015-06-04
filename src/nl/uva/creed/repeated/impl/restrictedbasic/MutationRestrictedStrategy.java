package nl.uva.creed.repeated.impl.restrictedbasic;

import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.Mutation;
import nl.uva.creed.evolution.Population;
import nl.vu.feweb.simulation.SimulationUtils;

public class MutationRestrictedStrategy implements Mutation {

	
	// 0 ALLD
	// 1 DTFT
	// 2 TFT
	// 3 ALLC

	
	
	//NEIGHBOUR DISTRIBUTION
/*	private static Double[] distribution0 = { 0.0, 1.0, 0.0, 0.0};
	private static Double[] distribution1 = { 0.5, 0.0, 0.5, 0.0 };
	private static Double[] distribution2 = { 0.0, 0.5, 0.0, 0.5 };
	private static Double[] distribution3 = { 0.0, 0.0, 1.0, 0.0 };*/

	
	//UNIFORM
	private static Double[] distribution0 = { 0.0, 1.0/3.0, 1.0/3.0, 1.0/3.0};
	private static Double[] distribution1 = { 1.0/3.0, 0.0, 1.0/3.0, 1.0/3.0 };
	private static Double[] distribution2 = { 1.0/3.0, 1.0/3.0, 0.0,1.0/3.0};
	private static Double[] distribution3 = { 1.0/3.0, 1.0/3.0,1.0/3.0, 0.0 };	
	
	
	public MutationRestrictedStrategy() {
		

		
	}

	public Individual mutate(Individual individual) {
		if (individual.getStrategy() instanceof RestrictedStrategy) {
			RestrictedStrategy strategy = ((RestrictedStrategy) individual.getStrategy());
			int nextGenotype = -1;
			switch (strategy.getGenotype()) {
			case 0:
				nextGenotype = SimulationUtils.simulateDiscreteDistribution(distribution0);
			break;
			case 1:
				nextGenotype = SimulationUtils.simulateDiscreteDistribution(distribution1);
			break;
			case 2:
				nextGenotype = SimulationUtils.simulateDiscreteDistribution(distribution2);
			break;
			case 3:
				nextGenotype = SimulationUtils.simulateDiscreteDistribution(distribution3);
			break;
			}
			strategy = new RestrictedStrategy(nextGenotype);
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
