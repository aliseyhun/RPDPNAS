package nl.uva.creed.evolution;

import java.util.ArrayList;

import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;
import nl.uva.creed.evolution.impl.ParseablePopulationImpl;
import nl.vu.feweb.simulation.SimulationUtils;

public class WrightFisherEvolverImpl implements Evolver {
 

	public WrightFisherEvolverImpl() {
		super();
		
	}

	public Population evolve(Population population, Mutation mutation, double mutationProbability) {
		try {
			population = addInProportionToFitness(population);
			population = mutation.mutate(population, mutationProbability);
			return population;
		} catch (UnnasignedFitnessException e) {
			e.printStackTrace();
			throw new RuntimeException("Plop");
		}

	}

	

	private Population addInProportionToFitness(Population population)
			throws UnnasignedFitnessException {
		double sum = 0.0;
		for (int i = 0; i < population.getSize(); i++) {
			sum = sum + population.getIndividual(i).getFitness();
		}
		ArrayList<Double> distribution = new ArrayList<Double>();
		for (int i = 0; i < population.getSize(); i++) {
			distribution.add((population.getIndividual(i).getFitness()) / sum);
		}
		Population ans = new ParseablePopulationImpl();
		for (int i = 0; i < population.getSize(); i++) {
			int fitGuyIndex = SimulationUtils
					.simulateDiscreteDistribution(distribution.toArray());
			ans.add(population.getIndividual(fitGuyIndex).getCopy());
		}
		return ans;

	}

}
