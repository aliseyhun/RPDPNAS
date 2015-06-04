package nl.uva.creed.evolution.impl;


import nl.uva.creed.evolution.Evolver;
import nl.uva.creed.evolution.FitnessEvaluator;
import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.Mutation;
import nl.uva.creed.evolution.Population;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.evolution.factories.EvolutionFactory;
import nl.uva.creed.evolution.factories.RepresentationFactory;
import nl.uva.creed.game.impl.PDGame;
import nl.uva.creed.repeated.RepeatedGame;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.StandardRepeatedGame;
import nl.vu.feweb.simulation.PseudoRandomSequence;

import org.apache.commons.lang.StringUtils;

public class Simulation {

	// PARAMETERS
	private int populationSize;
	private Long seed;
	private double mutationProbability;
	private double continuationProbability;
	private double complexityCost;
	private double r = 3.0;
	private double p = 1.0;
	private double s = Double.MIN_VALUE;
	private double t = 5.0;

	// CHANGING THINGS
	private Population population;

	// OTHER THINGS
	private Individual incumbent;
	private Mutation mutation;
	private Evolver evolver;
	//private FitnessEvaluatorImpl fitness;
	private StochasticFitnessEvaluatorImpl fitness;
	private double averagePayOffPerInteraction = 0;
	private double minimumPayOffPerInteraction = 0;
	private double maximumPayOffPerInteraction = 0;
	private double	w;
	private double	relatedness;
	
	protected double	perceptionMistakeProbability = 0;
	protected double	implementationMistakeProbability = 0;


	public Simulation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Simulation(int populationSize, Long seed,
			double mutationProbability, double continuationProbability, double complexityCost,
			double r, double p, double s, double t, int representationCode, int evolverCode, double w, double relatedness, String pop, double perceptionMistakeProbability, double implementationMistakeProbability)
			throws InvalidStrategyException, NumberFormatException, UnrecognizableString {
		super();
		this.populationSize = populationSize;
		this.seed = seed;
		this.mutationProbability = mutationProbability;
		this.continuationProbability = continuationProbability;
		this.complexityCost = complexityCost;
		this.r = r;
		this.p = p;
		this.s = s;
		this.t = t;
		this.w = w;
		this.relatedness = relatedness;
		this.perceptionMistakeProbability = perceptionMistakeProbability;
		this.implementationMistakeProbability = implementationMistakeProbability;
		if (StringUtils.isBlank(pop)){
		this.setUp(representationCode, evolverCode);
		}else{
		this.setUp(representationCode, evolverCode, pop);
		}
	}

	public void setUp(int representationCode, int evolverCode) throws InvalidStrategyException {
		//Seeding
		if (seed == null){
			seed =  new Long(System.currentTimeMillis());
		}
		PseudoRandomSequence.seed(seed);
		
		RepeatedGameStrategy strategy = RepresentationFactory.getIncumbent(representationCode);
		mutation = RepresentationFactory.getMutation(representationCode);
		incumbent = new IndividualImpl(strategy);
		population = new ParseablePopulationImpl(incumbent, populationSize);
		RepeatedGame repeatedGame = new StandardRepeatedGame(new PDGame(r, s,
				t, p), this.continuationProbability, this.complexityCost);
		fitness = new StochasticFitnessEvaluatorImpl(repeatedGame, this.w, this.relatedness, this.perceptionMistakeProbability, this.implementationMistakeProbability);
		evolver = EvolutionFactory.getEvolver(evolverCode);
	    
	}
	
	public void setUp(int representationCode, int evolverCode, String pop) throws InvalidStrategyException, NumberFormatException, UnrecognizableString {
		setUp(representationCode, evolverCode);
		population = new ParseablePopulationImpl(pop);	    
	}

	public void evolve() {
		this.population = fitness.evaluateFitness(this.population);
		this.population = evolver.evolve(this.population, this.mutation, mutationProbability);
		//this.population = this.mutation.mutate(population, mutationProbability);
		
	}

	public Individual getIncumbent() {
		return incumbent;
	}

	public void setIncumbent(Individual incumbent) {
		this.incumbent = incumbent;
	}

	public Population getPopulation() {
		return population;
	}

	public Mutation getMutation() {
		return mutation;
	}

	public void setMutation(Mutation mutation) {
		this.mutation = mutation;
	}

	public Evolver getEvolver() {
		return evolver;
	}

	public void setEvolver(Evolver evolver) {
		this.evolver = evolver;
	}

	public FitnessEvaluator getFitness() {
		return fitness;
	}

	public void setFitness(StochasticFitnessEvaluatorImpl fitness) {
		this.fitness = fitness;
	}



	public Long getSeed() {
		return seed;
	}

	public void setSeed(Long seed) {
		this.seed = seed;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public double getMutationProbability() {
		return mutationProbability;
	}

	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	public double getContinuationProbability() {
		return continuationProbability;
	}

	public void setContinuationProbability(double continuationProbability) {
		this.continuationProbability = continuationProbability;
	}

	public void evaluateFitness() {
		this.population = fitness.evaluateFitness(this.population);
		this.averagePayOffPerInteraction = fitness.getAveragePayOffPerInteraction();
		this.minimumPayOffPerInteraction = fitness.getMaximumPayOffPerInteraction();
		this.maximumPayOffPerInteraction = fitness.getMinimumPayOffPerInteraction();

	}

	public void evolutionStep() {
		this.population = evolver.evolve(this.population, this.mutation, mutationProbability);

	}

	public void mutate() {
		this.population = this.mutation.mutate(population, mutationProbability);

	}

	public double getR() {
		return r;
	}

	public double getP() {
		return p;
	}

	public double getS() {
		return s;
	}

	public double getT() {
		return t;
	}

	public double getAveragePayOffPerInteraction() {
		return averagePayOffPerInteraction;
	}

	public double getMinimumPayOffPerInteraction() {
		return minimumPayOffPerInteraction;
	}

	public double getMaximumPayOffPerInteraction() {
		return maximumPayOffPerInteraction;
	}

	public double getComplexityCost() {
		return complexityCost;
	}

	public void setComplexityCost(double complexityCost) {
		this.complexityCost = complexityCost;
	}

}
