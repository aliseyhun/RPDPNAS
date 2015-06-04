package nl.uva.creed.repeated.impl.turingmachine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.Mutation;
import nl.uva.creed.evolution.Population;
import nl.uva.creed.repeated.impl.turingmachine.machinery.TuringState;
import nl.uva.creed.repeated.impl.turingmachine.machinery.TuringTransition;
import nl.vu.feweb.simulation.PseudoRandomSequence;
import nl.vu.feweb.simulation.SimulationUtils;

public class MutationTMStrategy implements Mutation {

	// add delete change
	private static Double[] distribution = { 0.31, 0.34, 0.35 };

	public Individual mutate(Individual individual) {
		if (individual.getStrategy() instanceof TMStrategy) {
			TMStrategy tmStrategy = ((TMStrategy) individual.getStrategy());
			tmStrategy = this.mutateTM(tmStrategy);
			return new IndividualImpl(tmStrategy);

		} else {
			throw new IllegalArgumentException(
					"This mutation only works with individuals whose strategies are  implementations of ExplicitAutomatonStrategy");
		}

	}

	private TMStrategy mutateTM(TMStrategy tmStrategy) {

		// determine randomly type of operation
		List<Integer> integer = new ArrayList<Integer>();
		integer.add(SimulationUtils.simulateDiscreteDistribution(distribution));
		while (SimulationUtils.bernoullTrial(0.5)) {
			integer.add(SimulationUtils
					.simulateDiscreteDistribution(distribution));
		}

		for (Iterator<Integer> iter = integer.iterator(); iter.hasNext();) {
			Integer mutationType = (Integer) iter.next();
			tmStrategy = mutateTM(tmStrategy, mutationType);

		}
		return tmStrategy;
	}

	private TMStrategy mutateTM(TMStrategy tmStrategy, Integer mutationType) {
		switch (mutationType) {
		case 0: // add
			return add(tmStrategy);
		case 1: // delete something
			return remove(tmStrategy);
		case 2: // changes a state
			return change(tmStrategy);
		}
		throw new RuntimeException("Error en el codigo");
	}

	private TMStrategy change(TMStrategy tmStrategy) {
		int statetoChange = PseudoRandomSequence.nextInt(tmStrategy
				.numberOfStates());
		int typeOfChange = PseudoRandomSequence.nextInt(4);
		switch (typeOfChange) {
		case 0:
			// change accept value
			tmStrategy.getState(statetoChange).setAccept(
					!tmStrategy.getState(statetoChange).isAccept());
			break;
		case 1:
			// add a random transition
			tmStrategy.getState(statetoChange).addTransition(
					TuringTransition.random(),
					PseudoRandomSequence.nextInt(tmStrategy.numberOfStates()));
			break;
		case 2:
			// delete a transition
			TuringTransition transition = tmStrategy.getState(statetoChange)
					.getRandomTransition();
			if (transition != null) {
				tmStrategy.getState(statetoChange).removeTransition(transition);
			}
			break;
		case 3:
			// change destiny of a transition
			TuringTransition trans = tmStrategy.getState(statetoChange)
					.getRandomTransition();
			if (trans != null) {
				tmStrategy.getState(statetoChange).addTransition(
						trans,
						PseudoRandomSequence.nextInt(tmStrategy
								.numberOfStates()));
			}
			break;
		}

		return tmStrategy;
	}

	private TMStrategy remove(TMStrategy tmStrategy) {
		if (tmStrategy.numberOfStates() == 1) {
			return tmStrategy;
		}
		int stateIndex = PseudoRandomSequence.nextInt(tmStrategy
				.numberOfStates());
		tmStrategy.removeState(stateIndex);
		repairArrows(tmStrategy, stateIndex);
		return tmStrategy;
	}

	private void repairArrows(TMStrategy tmStrategy, int deletedIndex) {
		for (int i = 0; i < tmStrategy.numberOfStates(); i++) {
			tmStrategy.getState(i).repairArrows(deletedIndex,
					tmStrategy.numberOfStates());
		}
		if (!tmStrategy.isValid()) {
			throw new RuntimeException("Mutation failed at reparing arrows");
		}

	}

	private TMStrategy add(TMStrategy tmStrategy) {
		tmStrategy
				.addState(TuringState.random(tmStrategy.numberOfStates() + 1));
		// make it reachable
		int stateIndex = PseudoRandomSequence.nextInt(tmStrategy
				.numberOfStates() - 1);
		// modify a or add random transition to point to the new state
		if (tmStrategy.getState(stateIndex).isHaltDemarcated()) {
			// add - if state is empty
			tmStrategy.getState(stateIndex).addTransition(
					TuringTransition.random(), tmStrategy.numberOfStates() - 1);
		} else {
			// modify
			TuringTransition transition = tmStrategy.getState(stateIndex)
					.getRandomTransition();
			tmStrategy.getState(stateIndex).addTransition(transition,
					tmStrategy.numberOfStates() - 1);
		}
		return tmStrategy;
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
