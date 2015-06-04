package nl.uva.creed.evolution.impl;

import nl.uva.creed.evolution.FitnessEvaluator;
import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.Population;
import nl.uva.creed.repeated.RepeatedGame;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.GameException;
import nl.uva.creed.repeated.impl.RepeatedGameResult;
import nl.vu.feweb.simulation.CollectionsRandom;
import nl.vu.feweb.simulation.PseudoRandomSequence;
import nl.vu.feweb.simulation.SimulationUtils;


public class StochasticFitnessEvaluatorImpl implements FitnessEvaluator  {
	
	protected RepeatedGame repeatedGame;
	double averagePayOffPerInteraction = 0;
	double maximumPayOffPerInteraction = 0;
	double minimumPayOffPerInteraction = 0;
	protected double	perceptionMistakeProbability = 0;
	protected double	implementationMistakeProbability = 0;

	private double	w = 1.0;
	private double	relatedness =0.0;

	public StochasticFitnessEvaluatorImpl(RepeatedGame repeatedGame, double w, double relatedness, double perceptionMistakeProbability, double implementationMistakeProbability ) {
		super();
		this.repeatedGame = repeatedGame;
		this.perceptionMistakeProbability = perceptionMistakeProbability;
		this.implementationMistakeProbability = implementationMistakeProbability;
		this.w = w;
		this.relatedness = relatedness;
	}

	public Population evaluateFitness(Population population) {
		//check that popSize is even
		if (population.getSize()%2!=0) {
			throw new IllegalArgumentException("population size must be even!");
		}
		//re-assign mutations
		CollectionsRandom.shuffle(population.getList(), PseudoRandomSequence.getRandom());
		for (int i = 1; i < population.getList().size(); i=i+2) {
			if (SimulationUtils.bernoullTrial(this.relatedness )) {
				population.setIndividual(population.getIndividual(i-1).getCopy(), i);
			}
		}
		
		
		try {
			for (int i = 0; i < population.getSize()-1; i=i+2) {
				Individual individual = population.getIndividual(i);
				Individual oponent = population.getIndividual(i+1);
				//RepeatedGameResult result = this.repeatedGame.payOff((RepeatedGameStrategy) individual.getStrategy(),(RepeatedGameStrategy) oponent.getStrategy()); 
				RepeatedGameResult result = this.repeatedGame.payOff((RepeatedGameStrategy) individual.getStrategy(),(RepeatedGameStrategy) oponent.getStrategy(), this.implementationMistakeProbability, this.perceptionMistakeProbability);
				double payoff1 = result.getPayoff();
				double payoff2 = result.getPayoff2();
				double payoff1NoComplexityCost = result.getPayoff1WithoutComplexityCost();
				double payoff2NoComplexityCost = result.getPayoff2WithoutComplexityCost();
			
				
				double fitness1 = (1-this.w)+(this.w*payoff1);
				double fitness2 = (1-this.w)+(this.w *payoff2);
				
				individual.setFitness(fitness1);
				individual.setPayOff(payoff1);
				individual.setPayOffNoComplexity(payoff1NoComplexityCost);
				
				
				oponent.setFitness(fitness2);
				oponent.setPayOff(payoff2);
				oponent.setPayOffNoComplexity(payoff2NoComplexityCost);
				
				//NEGATIVE FITNESS
				if (fitness1 <0 || fitness2 < 0) {
					System.out.println("NEGATIVE FITNESS - ABORTING ");
					System.exit(0);
					
				}
				
			}
			
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
