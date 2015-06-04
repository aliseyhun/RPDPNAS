package nl.uva.creed.repeated.impl;

import nl.uva.creed.game.Action;
import nl.uva.creed.game.Game;
import nl.uva.creed.game.impl.PDGame;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGame;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.GameException;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.vu.feweb.numerical.KahanSummation;
import nl.vu.feweb.simulation.SimulationUtils;

public class StandardRepeatedGame implements RepeatedGame {

	private Game oneShotGame;
	private double continuationProbability;
	private boolean verbose = false;
	private double	complexityCost;
	
	public StandardRepeatedGame(Game oneShotGame, double continuationProbability, double complexityCost) {
		super();
		this.oneShotGame = oneShotGame;
		this.continuationProbability = continuationProbability;
		this.complexityCost = complexityCost;
	}
	
	public StandardRepeatedGame() {
		super();
		this.oneShotGame = new PDGame();
		this.continuationProbability = 0.9;
		this.complexityCost = 0.0;
	}
	
	public StandardRepeatedGame(double continuationProbability, double complexityCost) {
		super();
		this.oneShotGame = new PDGame();
		this.continuationProbability = continuationProbability;
		this.complexityCost = complexityCost;
	}

	public Game oneShotGame() {
		return oneShotGame;
	}

	


	public double expectedPayoff1(RepeatedGameStrategy strategy1, RepeatedGameStrategy strategy2, int horizon) throws GameException{
		//return expectedPayoff1Sum(strategy1, strategy2, horizon)*((1.0-continuationProbability)/(1.0-Math.pow(continuationProbability, new Double(horizon+1))));
		//Now includes complexity cost - AFTER THE SUM
		return expectedPayoff1Sum(strategy1, strategy2, horizon)*((1.0-continuationProbability)/(1.0-Math.pow(continuationProbability, new Double(horizon+1))))- complexityCost*(double)strategy1.getComplexity();
		
	}
	
	public double expectedPayoff1Sum(RepeatedGameStrategy strategy1, RepeatedGameStrategy strategy2, int horizon) throws GameException{
		History history1 = new History();
		History history2 = new History();
		KahanSummation summation = new KahanSummation();
		Action action1  = strategy1.nextAction(history2);
		Action action2  = strategy2.nextAction(history1);
		double payoff = oneShotGame.payoffPlayer1(action1, action2);
		summation.add(payoff);
		history1.addMove(action1);
		history2.addMove(action2);
		for (int i = 1; i <= horizon; i++) {
			action1  = strategy1.nextAction(history2);
			action2  = strategy2.nextAction(history1);
			payoff = oneShotGame.payoffPlayer1(action1, action2)*(Math.pow(continuationProbability, new Double(i)));
			summation.add(payoff);
			history1.addMove(action1);
			history2.addMove(action2);	
		}
		return summation.value();
	}
	

	public Game getOneShotGame() {
		return oneShotGame;
	}

	public double getContinuationProbability() {
		return continuationProbability;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	
	public RepeatedGameResult payOff(RepeatedGameStrategy strategy1, RepeatedGameStrategy strategy2, 
			double implementationMistakeProbability, double perceptionMistakeProbability) throws GameException {
		if (this.continuationProbability != 1.0) {
			History history1 = new History();
			History history2 = new History();
			double sum = 0.0;
			double sum2 = 0.0;
			
			
			Action action1  = strategy1.nextAction(history2);
			Action action2  = strategy2.nextAction(history1);
			
			if (implementationMistakeProbability > 0.0) {
			if(SimulationUtils.bernoullTrial(implementationMistakeProbability)){
				action1 = oppositeAction(action1);
			}
			if(SimulationUtils.bernoullTrial(implementationMistakeProbability)){
				action2 = oppositeAction(action2);
			}	
			}
			
			
			double payoff = oneShotGame.payoffPlayer1(action1, action2);
			double payoff2 = oneShotGame.payoffPlayer2(action1, action2);
			
			if (verbose) System.out.println("("+action1+","+action2+","+payoff+"/"+payoff2+")");			
			sum = sum + payoff;
			sum2 = sum2 + payoff2;
			
			
			if (perceptionMistakeProbability > 0.0) {
				if(SimulationUtils.bernoullTrial(perceptionMistakeProbability)){
					action1 = oppositeAction(action1);
				}
				if(SimulationUtils.bernoullTrial(perceptionMistakeProbability)){
					action2 = oppositeAction(action2);
				}	
			}
			history1.addMove(action1);
			history2.addMove(action2);
			
			while (SimulationUtils.bernoullTrial(continuationProbability)) {
				action1  = strategy1.nextAction(history2);
				action2  = strategy2.nextAction(history1);
				
				
				if (implementationMistakeProbability > 0.0) {
					if(SimulationUtils.bernoullTrial(implementationMistakeProbability)){
						action1 = oppositeAction(action1);
					}
					if(SimulationUtils.bernoullTrial(implementationMistakeProbability)){
						action2 = oppositeAction(action2);
					}	
				}
				
				payoff = oneShotGame.payoffPlayer1(action1, action2);
				payoff2 = oneShotGame.payoffPlayer2(action1, action2);
				
				if (verbose) System.out.println("("+action1+","+action2+","+payoff+"/"+payoff2+")");			
				sum = sum + payoff;
				sum2 = sum2 + payoff2;
				
				if (perceptionMistakeProbability > 0.0) {
					if(SimulationUtils.bernoullTrial(perceptionMistakeProbability)){
						action1 = oppositeAction(action1);
					}
					if(SimulationUtils.bernoullTrial(perceptionMistakeProbability)){
						action2 = oppositeAction(action2);
					}	
				}
				
				history1.addMove(action1);
				history2.addMove(action2);
			}
			
			//COMPLEXITY COST
			double discount = (1-continuationProbability);
			return new RepeatedGameResult(discount*sum- complexityCost*(double)strategy1.getComplexity(), discount*sum2- complexityCost*(double)strategy2.getComplexity(), discount*sum, discount*sum2);
		
		}else{
			//Delta = 1 REQUIRES FSA REPRESENTATION AND NO MISTAKES
			if (perceptionMistakeProbability > 0.0 || implementationMistakeProbability >0){
				throw new GameException("For delta 1 we require no mistakes");
			}
			return Delta1Helper.payoffDelta1((ExplicitAutomatonStrategy)strategy1, (ExplicitAutomatonStrategy)strategy2, this);
		}
		
		
		}

	private Action oppositeAction(Action action) {
		if (action.equals(Action.COOPERATE)) {
			return Action.DEFECT;
		}
		return Action.COOPERATE;
	}

	public double getComplexityCost() {
		return complexityCost;
	}
	
	

}
