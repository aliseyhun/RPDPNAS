

package nl.uva.creed.evolution;
import java.util.ArrayList;

import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;
import nl.vu.feweb.simulation.PseudoRandomSequence;
import nl.vu.feweb.simulation.SimulationUtils;

public class MoranEvolverImpl implements Evolver {

	public MoranEvolverImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Population evolve(Population population, Mutation mutation, double mutationProbability) {
		try {
			population = moran(population, mutation, mutationProbability);
			return population;
		} catch (UnnasignedFitnessException e) {
			e.printStackTrace();
			throw new RuntimeException("Plop");
		}
	}

	private Population moran(Population population, Mutation mutation, double mutationProbability) throws UnnasignedFitnessException {
		double sum = 0.0;
		for (int i = 0; i < population.getSize(); i++) {
			sum = sum + population.getIndividual(i).getFitness();
		}
		ArrayList<Double> distribution = new ArrayList<Double>();
		for (int i = 0; i < population.getSize(); i++) {
			distribution.add((population.getIndividual(i).getFitness()) / sum);
		}
		int fitGuyIndex = SimulationUtils.simulateDiscreteDistribution(distribution.toArray());
		Individual theChosenOne = population.getIndividual(fitGuyIndex).getCopy();
		if (SimulationUtils.bernoullTrial(mutationProbability)) {
			theChosenOne = mutation.mutate(theChosenOne);
		}
		population.add(theChosenOne);
		population.removeIndividual(PseudoRandomSequence.nextInt(population.getSize()));
		return population;
	}

}
