package nl.uva.creed.evolution.impl;

import nl.uva.creed.evolution.FitnessEvaluator;
import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.Population;
import nl.uva.creed.repeated.RepeatedGame;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.GameException;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.DescriptiveStatisticsImpl;

public class FitnessEvaluatorImpl implements FitnessEvaluator {

	protected RepeatedGame repeatedGame;
	double averagePayOffPerInteraction = 0;
	double maximumPayOffPerInteraction = 0;
	double minimumPayOffPerInteraction = 0;
	protected double	perceptionMistakeProbability = 0;
	protected double	implementationMistakeProbability = 0;

	
	public FitnessEvaluatorImpl(RepeatedGame repeatedGame, double perceptionMistakeProbability, double implementationMistakeProbability ) {
		super();
		this.repeatedGame = repeatedGame;
		this.perceptionMistakeProbability = perceptionMistakeProbability;
		this.implementationMistakeProbability = implementationMistakeProbability;
	}

	public Population evaluateFitness(Population population) {
		DescriptiveStatistics averagePayOffPerInteractionStatistitcs = new DescriptiveStatisticsImpl();
		try {
			for (int i = 0; i < population.getSize(); i++) {
				Individual individual = population.getIndividual(i);
				//double fitness = this.repeatedGame.payOff(	(RepeatedGameStrategy) individual.getStrategy(), 	(RepeatedGameStrategy) population.getRandomIndividual().getStrategy()).getPayoff();
				double fitness = this.repeatedGame.payOff ((RepeatedGameStrategy) individual.getStrategy(), 	(RepeatedGameStrategy) population.getRandomIndividual().getStrategy(),this.implementationMistakeProbability, this.perceptionMistakeProbability).getPayoff();
				individual.setFitness(fitness);
				//NEGATIVE FITNESS
				if (fitness <0 ) {
					throw new RuntimeException("Negative Fitness!");
				}
				averagePayOffPerInteractionStatistitcs.addValue(fitness);
			}
			this.averagePayOffPerInteraction = averagePayOffPerInteractionStatistitcs.getMean();
			this.maximumPayOffPerInteraction = averagePayOffPerInteractionStatistitcs.getMax();
			this.minimumPayOffPerInteraction = averagePayOffPerInteractionStatistitcs.getMin();
		} catch (GameException e) {
			System.out.println(e);
			throw new RuntimeException("Error in playing the repeated game!!!");
		}
		return population;
	}

	public double getAveragePayOffPerInteraction() {
		return averagePayOffPerInteraction;
	}

	public void setAveragePayOffPerInteraction(double averagePayOffPerInteraction) {
		this.averagePayOffPerInteraction = averagePayOffPerInteraction;
	}

	public double getMaximumPayOffPerInteraction() {
		return maximumPayOffPerInteraction;
	}

	public void setMaximumPayOffPerInteraction(double maximumPayOffPerInteraction) {
		this.maximumPayOffPerInteraction = maximumPayOffPerInteraction;
	}

	public double getMinimumPayOffPerInteraction() {
		return minimumPayOffPerInteraction;
	}

	public void setMinimumPayOffPerInteraction(double minimumPayOffPerInteraction) {
		this.minimumPayOffPerInteraction = minimumPayOffPerInteraction;
	}

}
