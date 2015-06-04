package nl.uva.creed.repeated.impl.explicitautomaton;

import java.util.ArrayList;
import java.util.StringTokenizer;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.game.Action;
import nl.uva.creed.game.Game;
import nl.uva.creed.game.impl.PDGame;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.GameException;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.StandardRepeatedGame;
import nl.vu.feweb.evolution.models.automata.BricsExplicitConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.linear.RealMatrixImpl;

import dk.brics.automaton.Automaton;

public class ExplicitAutomatonStrategy implements RepeatedGameStrategy {

	private ArrayList<AutomatonState>	states;
	private int							currentState	= 0;

	@Override
	public String toString() {
		return states.toString();
	}

	@Override
	public int hashCode() {
		/*
		 * final int prime = 31; int result = 1; result = prime * result +
		 * ((states == null) ? 0 : states.hashCode()); return result;
		 */
		Automaton automaton = BricsExplicitConverter.convert(this);
		return automaton.hashCode();

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ExplicitAutomatonStrategy other = (ExplicitAutomatonStrategy) obj;
		Automaton automaton = BricsExplicitConverter.convert(this);
		Automaton automaton2 = BricsExplicitConverter.convert(other);
		return automaton.equals(automaton2);
		/*
		 * 
		 * if (states == null) { if (other.states != null) return false; } else
		 * if (!states.equals(other.states)) return false; return true;
		 */
	}

	private void clearRun() {
		currentState = 0;
	}

	public AutomatonState getState(int index) {
		return this.states.get(index);
	}

	private void nextState(Action action) {
		if (action.getCode().equalsIgnoreCase(Action.COOPERATE_STRING)) {
			currentState = this.states.get(currentState).getNextCooperate();
		} else if (action.getCode().equalsIgnoreCase(Action.DEFECT_STRING)) {
			currentState = this.states.get(currentState).getNextDefect();
		} else {
			throw new RuntimeException("Wrong history!");
		}
	}

	public ExplicitAutomatonStrategy(AutomatonState initialState) {
		super();
		states = new ArrayList<AutomatonState>();
		states.add(initialState);
	}

	public int size() {
		return this.states.size();
	}

	public RepeatedGameStrategy getCopy() throws InvalidStrategyException {
		ExplicitAutomatonStrategy ans = new ExplicitAutomatonStrategy(this
				.getInitialState().getCopy());
		for (int i = 1; i < this.getComplexity(); i++) {
			ans.addState(this.states.get(i).getCopy());
		}
		return ans;
	}

	public void addState(AutomatonState automatonState) {
		this.states.add(automatonState);
	}

	public AutomatonState getInitialState() {
		return states.get(0);
	}

	public int getComplexity() {
		return states.size();
	}

	public boolean isCooperateOnFirstMove() {
		return this.getInitialState().isCooperate();
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	public Action nextAction(History history) {
		clearRun();
		if (!this.isValid()) {
			throw new RuntimeException("Invalid strategy!");
		}
		for (int i = 0; i < history.getActions().size(); i++) {
			nextState(history.getActions().get(i));
		}
		if (this.states.get(currentState).isCooperate()) {
			
			return Action.COOPERATE;
		} else {
			return Action.DEFECT;
		}
	}

	public boolean isValid() {
		if (states == null || states.size() <= 0)
			return false;
		for (int i = 0; i < states.size(); i++) {
			if (!states.get(i).isValid(states.size())) {
				return false;
			}
		}
		return true;
	}

	public void removeState(int stateIndex) {
		this.states.remove(stateIndex);
	}

	public static ExplicitAutomatonStrategy minimize(
			ExplicitAutomatonStrategy strategy) {
		Automaton.setMinimization(Automaton.MINIMIZE_HUFFMAN);
		Automaton automaton = BricsExplicitConverter.convert(strategy);
		// automaton.reduce();
		// automaton.removeDeadTransitions();
		automaton.minimize();
		// System.out.println(automaton);
		return restoreDeadTranstions(BricsExplicitConverter.convert(automaton));
	}

	private static ExplicitAutomatonStrategy restoreDeadTranstions(
			ExplicitAutomatonStrategy explicitStrategy) {
		if (explicitStrategy.isSingleState()
				&& !explicitStrategy.getInitialState().isCooperate()
				&& explicitStrategy.getInitialState().getNextCooperate() == null
				& explicitStrategy.getInitialState().getNextDefect() == null) {
			explicitStrategy.getInitialState().setNextCooperate(0);
			explicitStrategy.getInitialState().setNextDefect(0);
		}
		boolean foundDead = false;
		int numberOfStates = explicitStrategy.getComplexity();
		for (int i = 0; i < explicitStrategy.getComplexity(); i++) {
			if (explicitStrategy.getState(i).getNextCooperate() == null) {
				explicitStrategy.getState(i).setNextCooperate(numberOfStates);
				foundDead = true;
			}
			if (explicitStrategy.getState(i).getNextDefect() == null) {
				explicitStrategy.getState(i).setNextDefect(numberOfStates);
				foundDead = true;
			}
		}
		if (foundDead) {
			AutomatonState dead = new AutomatonState(false, numberOfStates,
					numberOfStates);
			explicitStrategy.addState(dead);
		}
		if (!explicitStrategy.isValid()) {
			throw new IllegalArgumentException("failed restoring dead states");
		}
		return explicitStrategy;
	}

	public boolean isSingleState() {
		return this.getComplexity() == 1;
	}

	public static ExplicitAutomatonStrategy readFromString(String string)
			throws UnrecognizableString {
		string = StringUtils.removeStart(string, "[");
		string = StringUtils.removeEnd(string, "]");
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		ExplicitAutomatonStrategy automatonStrategy = new ExplicitAutomatonStrategy(
				AutomatonState.convert(tokenizer.nextToken()));
		while (tokenizer.hasMoreElements()) {
			automatonStrategy.addState(AutomatonState.convert(tokenizer
					.nextToken()));
		}
		if (!automatonStrategy.isValid()) {
			throw new IllegalArgumentException("no valid automata "
					+ automatonStrategy);
		}
		return automatonStrategy;
	}

	public static ArrayList<ExplicitAutomatonStrategy> readCollectionFromString(
			String string) throws UnrecognizableString {
		string = StringUtils.removeStart(string, "[");
		string = StringUtils.removeEnd(string, "]");
		string = StringUtils.replace(string, "], ", "];");
		StringTokenizer tokenizer = new StringTokenizer(string, ";");
		ArrayList<ExplicitAutomatonStrategy> ans = new ArrayList<ExplicitAutomatonStrategy>();
		while (tokenizer.hasMoreElements()) {
			ans.add(readFromString(tokenizer.nextToken()));
		}
		return ans;
	}

	/**
	 * Test a string against an oponent, shows self-play and matrix to check if
	 * they form a mixed populationState
	 * 
	 * @param args
	 * @throws UnrecognizableString
	 * @throws GameException
	 */
	public static void main(String[] args) throws UnrecognizableString,
			GameException {
		int rounds = 100;

		String string = "[C 0 2, D 2 3, D 0 1, C 2 0]";
		String oponent = "[C 0 3, D 3 2, C 3 0, D 0 1]";
		ExplicitAutomatonStrategy strategy = ExplicitAutomatonStrategy
				.readFromString(string);

		System.out.println("Original strategy");
		System.out.println(string);
		System.out.println("Minimal version");
		System.out.println(ExplicitAutomatonStrategy.minimize(strategy));
		System.out.println("Self-play");
		History history = new History();
		for (int i = 0; i < rounds; i++) {
			Action action = strategy.nextAction(history);
			System.out.print(action);
			history.addMove(action);
		}
		if (!StringUtils.isEmpty(oponent)) {
			ExplicitAutomatonStrategy oponentS = ExplicitAutomatonStrategy
					.minimize(ExplicitAutomatonStrategy.readFromString(oponent));

			System.out.println("\n\nOpponent strategy");
			System.out.println(oponent);
			System.out.println("Minimal Opponent");
			System.out.println(oponentS);
			System.out.println("Self-play");
			history = new History();
			for (int i = 0; i < rounds; i++) {

				Action action = oponentS.nextAction(history);
				System.out.print(action);
				history.addMove(action);
			}

			System.out.println("\n\n\nAgainst....");

			History historyFoc = new History();
			History historyOponent = new History();

			for (int i = 0; i < rounds; i++) {
				Action action = strategy.nextAction(historyOponent);
				Action actionOponent = oponentS.nextAction(historyFoc);
				historyFoc.addMove(action);
				historyOponent.addMove(actionOponent);
			}
			System.out.println(historyFoc);
			System.out.println(historyOponent);

			Game oneShot = new PDGame(3.0, 0.0, 4.0, 1.0);
			double continuationProbability = 0.95;
			double complexityCost = 0.0;
			
			StandardRepeatedGame repeatedGame = new StandardRepeatedGame(oneShot, continuationProbability,complexityCost);
			// MATRIX
			RealMatrixImpl matrix = new RealMatrixImpl(2, 2);
			matrix.getDataRef()[0][0] = repeatedGame.expectedPayoff1(strategy,
					strategy, rounds);
			matrix.getDataRef()[0][1] = repeatedGame.expectedPayoff1(strategy,
					oponentS, rounds);
			matrix.getDataRef()[1][0] = repeatedGame.expectedPayoff1(oponentS,
					strategy, rounds);
			matrix.getDataRef()[1][1] = repeatedGame.expectedPayoff1(oponentS,
					oponentS, rounds);
			System.out.println("");
			System.out.println(matrix);

			System.out.println("Equal? " + oponentS.equals(strategy));
			;

		}

	}

	public int getCurrentState() {
		return currentState;
	}

}