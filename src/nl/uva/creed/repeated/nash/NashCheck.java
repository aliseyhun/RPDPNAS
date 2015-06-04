package nl.uva.creed.repeated.nash;

import java.util.ArrayList;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.game.Action;
import nl.uva.creed.game.Game;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGame;
import nl.uva.creed.repeated.exceptions.GameException;
import nl.uva.creed.repeated.impl.StandardRepeatedGame;
import nl.uva.creed.repeated.impl.explicitautomaton.AutomatonState;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.vu.feweb.numerical.KahanSummation;


public class NashCheck {

	private class ValueAction{
		double value = 0;
		Action action;
		public ValueAction(double value, Action action) {
			super();
			this.value = value;
			this.action = action;
		}
		@Override
		public String toString() {
			return value + " " + action;
		}
		
		
	}

	private static final double	epsilon	= 0.00000000001;
	private static final int	horizon	= 200;
	private ArrayList<ValueAction>[] table;
	
	private double valueOfState(int i){
		if(table[i].size() ==0) return 0;
		return table[i].get(table[i].size()-1).value;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private NashCheck(int numberOfStates) {
		super();
		table = new ArrayList[numberOfStates];
		for (int i = 0; i < table.length; i++) {
			table[i] = new ArrayList<ValueAction>();
		}
	}




	public static boolean isNash(ExplicitAutomatonStrategy automaton, RepeatedGame game) throws GameException, InvalidStrategyException{
		NashCheck nashCheck = new NashCheck(automaton.getComplexity());
		ValueAction[] ans = nashCheck.fillTable(automaton, game);
		double selfPlay = ((StandardRepeatedGame)game).expectedPayoff1Sum(automaton, automaton, horizon);
		double bestResponse = nashCheck.expectedPayoffAgainstBestResponse(ans, (ExplicitAutomatonStrategy) automaton.getCopy(), horizon, (StandardRepeatedGame) game);
		return selfPlay == bestResponse;
	}
	
	
	
	
	private ValueAction[] fillTable(ExplicitAutomatonStrategy automaton,RepeatedGame game) throws GameException {
		int i = 0;
		Game oneShot = game.oneShotGame();
		while(!this.hasConverged()){
			for (int j = 0; j < table.length; j++) {
				AutomatonState state = automaton.getState(j);
				double discount = ((StandardRepeatedGame)game).getContinuationProbability();
				double valueCooperate = discount*valueOfState(state.getNextCooperate()) +  oneShot.payoffPlayer1(Action.COOPERATE, state.action());
				double  valueDefect = 	discount*valueOfState(state.getNextDefect()) + oneShot.payoffPlayer1(Action.DEFECT, state.action());
				if (valueCooperate >= valueDefect) {
					table[j].add(new ValueAction(valueCooperate, Action.COOPERATE));
				}else{
					table[j].add(new ValueAction(valueDefect, Action.DEFECT));
				}
			}
		i++;
		}
		ValueAction[] ans = new ValueAction[automaton.size()]; 
		for (int j = 0; j < ans.length; j++) {
			ans[j] = table[j].get(table[j].size()-1);
		}
		return ans;
	}




	private double[] lastValues(){
		int index = table[0].size()-1;
		return valuesAtTime(index);
	}
	
	private  double[] valuesAtTime(int index){
		double[] ans = new double[table.length];
		for (int i = 0; i < table.length; i++) {
			ans[i] = table[i].get(index).value;
		}
		return ans;
	}
	
	private  double[] secondTolastValues(){
		int index = table[0].size()-2;
		return valuesAtTime(index);
	}




	private boolean hasConverged() {
		if (table[0].size() <= 2) {
			return false;
		}
		return areArraysEqual(secondTolastValues(), lastValues());
	}


	private boolean equal(double d1, double d2){
		double min = d1 - epsilon;
		double max = d1 + epsilon;
		if (d2> min && d2 < max) {
			return true;
		}
		return false;
	}
	

	private boolean areArraysEqual(double[] secondTolastValues,double[] lastValues) {
		if (lastValues.length != secondTolastValues.length) {
			throw new RuntimeException("arrays not the same size");
		}
		for (int i = 0; i < lastValues.length; i++) {
			if (!equal(secondTolastValues[i], lastValues[i])) {
				return false;
			}
		}
		return true;
	}



	
	
	private double expectedPayoffAgainstBestResponse(ValueAction[] strategy1,  ExplicitAutomatonStrategy strategy2, int horizon, StandardRepeatedGame game) throws GameException{
		double continuationProbability = game.getContinuationProbability();
		Game oneShotGame = game.getOneShotGame();
		History history1 = new History();
		History history2 = new History();
		KahanSummation summation = new KahanSummation();
		Action action1  = strategy1[strategy2.getCurrentState()].action;
		//Action action2  = strategy2.nextAction(history1);
		Action action2  = strategy2.nextAction(history1);
		
		double payoff = oneShotGame.payoffPlayer1(action1, action2);
		summation.add(payoff);
		history1.addMove(action1);
		history2.addMove(action2);
		for (int i = 1; i <= horizon; i++) {
			action2  = strategy2.nextAction(history1);
			action1  = strategy1[strategy2.getCurrentState()].action;
			payoff = oneShotGame.payoffPlayer1(action1, action2)*(Math.pow(continuationProbability, new Double(i)));
			summation.add(payoff);
			history1.addMove(action1);
			history2.addMove(action2);	
		}
		return summation.value();
	}

}
