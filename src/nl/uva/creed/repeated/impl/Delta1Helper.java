package nl.uva.creed.repeated.impl;

import java.util.ArrayList;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.game.Action;
import nl.uva.creed.game.Game;
import nl.uva.creed.game.impl.PDGame;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.exceptions.GameException;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategyFactory;

public class Delta1Helper {
	
	private static final int	HORIZON	= 200;


	private static Integer[] getCurrentStatesArray(ExplicitAutomatonStrategy strategy1, ExplicitAutomatonStrategy strategy2){
		Integer state1  = strategy1.getCurrentState();
		Integer state2  = strategy2.getCurrentState();
		Integer[] ans = {state1, state2};
		return ans;
	}
	
	
	public static RepeatedGameResult payoffDelta1(ExplicitAutomatonStrategy strategy1, ExplicitAutomatonStrategy strategy2, StandardRepeatedGame game ) throws GameException{
		strategy1 = ExplicitAutomatonStrategy.minimize(strategy1);
		strategy2 = ExplicitAutomatonStrategy.minimize(strategy2);
		History history1 = new History();
		History history2 = new History();
		ArrayList<Double> sum = new ArrayList<Double>();
		ArrayList<Double> sum2 = new ArrayList<Double>();
		int counter = 0;
		ArrayList<Integer[]> estados = new ArrayList<Integer[]>();
		estados.add(getCurrentStatesArray(strategy1, strategy2));
		counter++;
		Action action1  = strategy1.nextAction(history2);
		Action action2  = strategy2.nextAction(history1);
		double payoff = game.getOneShotGame().payoffPlayer1(action1, action2);
		double payoff2 = game.getOneShotGame().payoffPlayer2(action1, action2);
		sum.add(payoff);
		sum2.add(payoff2);
		history1.addMove(action1);
		history2.addMove(action2);
		
		int cycle = -1;
		while (counter < HORIZON) {
			action1  = strategy1.nextAction(history2);
			action2  = strategy2.nextAction(history1);
			payoff = game.getOneShotGame().payoffPlayer1(action1, action2);
			payoff2 = game.getOneShotGame().payoffPlayer2(action1, action2);
			sum.add(payoff);
			sum2.add(payoff2);
			history1.addMove(action1);
			history2.addMove(action2);
			estados.add(getCurrentStatesArray(strategy1, strategy2));
			cycle = detectCycle(estados);
			if (cycle !=-1) {
				//cycle detected
				break;
			}
			counter++;
		}
		if (counter > HORIZON) throw new RuntimeException("Error finding cycle");
		
		double sum_d = 0.0;
		double sum2_d = 0.0;
		// there is cycle from cycle to counter
		for (int i = cycle; i < counter; i++) {
			sum_d = sum_d + sum.get(i);
			sum2_d = sum2_d + sum2.get(i);
		}
		double lenght = (double)counter - (double)cycle; 
		return new RepeatedGameResult((sum_d/lenght) - game.getComplexityCost()*(double)strategy1.getComplexity(), (sum2_d/lenght) - game.getComplexityCost()*(double)strategy2.getComplexity(), sum_d/lenght, sum2_d/lenght);
		
	}


	private static int detectCycle(ArrayList<Integer[]> estados) {
		int last_index = estados.size() -1;
		int i = last_index -1;
		while (i>0){
			if (compare(estados.get(i),estados.get(last_index))) return i;
			i = i-1;
		}
		return -1;
	}


	private static boolean compare(Integer[] integers, Integer[] integers2) {
		if (integers[0]==integers2[0] && integers[1]==integers2[1]) return true; 
		return false;
	}
	
/*	*//**
	 * @param args
	 * @throws GameException 
	 * @throws InvalidStrategyException 
	 *//*
	public static void main(String[] args) throws GameException, InvalidStrategyException {
		Game oneShot = new PDGame(3.0, 0.0, 4.0, 1.0);
		double continuationProbability = 0.95;
		double complexityCost = 0.0;
		ExplicitAutomatonStrategy strategy1 = ExplicitAutomatonStrategyFactory.wsls();
		ExplicitAutomatonStrategy strategy2 = ExplicitAutomatonStrategyFactory.wsls();
		StandardRepeatedGame repeatedGame = new StandardRepeatedGame(oneShot, continuationProbability,complexityCost);
		System.out.println(Delta1Helper.payoffDelta1(strategy1, strategy2, repeatedGame).toString());
		
	}*/

}
