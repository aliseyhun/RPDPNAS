package nl.vu.feweb.evolution.models.automata;

import java.util.Iterator;
import java.util.Set;

import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.impl.explicitautomaton.AutomatonState;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

public class BricsExplicitConverter {

	public static Automaton convert(
			ExplicitAutomatonStrategy explicitAutomatonStrategy) {

		State[] stateArray = new State[explicitAutomatonStrategy
				.getComplexity()];

		for (int i = 0; i < explicitAutomatonStrategy.getComplexity(); i++) {
			stateArray[i] = new State();
			stateArray[i].setAccept(explicitAutomatonStrategy.getState(i)
					.isCooperate());
		}

		for (int i = 0; i < explicitAutomatonStrategy.getComplexity(); i++) {
			AutomatonState explicitState = explicitAutomatonStrategy
					.getState(i);
			int indexNextCooperate = explicitState.getNextCooperate();
			int indexNextDefect = explicitState.getNextDefect();
			stateArray[i].addTransition(new Transition(Action.COOPERATE_CHAR,
					stateArray[indexNextCooperate]));
			stateArray[i].addTransition(new Transition(Action.DEFECT_CHAR,
					stateArray[indexNextDefect]));
		}
		Automaton ans = new Automaton();
		ans.setInitialState(stateArray[0]);
		// ans.restoreInvariant();
		ans.setDeterministic(true);
		return ans;
	}

	public static ExplicitAutomatonStrategy convert(Automaton bricsAutomaton) {
		Set<State> bricsStatesSet = bricsAutomaton.getStates();
		State[] bricsStatesArray = new State[bricsStatesSet.size()];
		bricsStatesArray[0] = bricsAutomaton.getInitialState();
		int i = 1;
		for (Iterator<State> iterator = bricsStatesSet.iterator(); iterator
				.hasNext();) {
			State state = (State) iterator.next();
			if (!state.equals(bricsStatesArray[0])) {
				bricsStatesArray[i] = state;
				i++;
			}
		}

		AutomatonState initialState = new AutomatonState();
		initialState.setCooperate(bricsStatesArray[0].isAccept());
		Set<Transition> transitionsSet = bricsStatesArray[0].getTransitions();
		for (Iterator<Transition> iterator = transitionsSet.iterator(); iterator
				.hasNext();) {
			Transition transition = (Transition) iterator.next();
			if (isCooperate(transition)) {
				initialState.setNextCooperate(index(bricsStatesArray,
						transition.getDest()));
			} else if (isDefect(transition)) {
				initialState.setNextDefect(index(bricsStatesArray, transition
						.getDest()));
			} else if (isBoth(transition)) {
				int index = index(bricsStatesArray, transition.getDest());
				initialState.setNextDefect(index);
				initialState.setNextCooperate(index);
			} else {
				throw new IllegalArgumentException("Wrong machine!!");
			}
		}
		ExplicitAutomatonStrategy ans = new ExplicitAutomatonStrategy(
				initialState);
		for (int j = 1; j < bricsStatesArray.length; j++) {
			AutomatonState state = new AutomatonState();
			state.setCooperate(bricsStatesArray[j].isAccept());
			Set<Transition> transitions = bricsStatesArray[j].getTransitions();
			for (Iterator<Transition> iterator = transitions.iterator(); iterator
					.hasNext();) {
				Transition transition = (Transition) iterator.next();
				if (isCooperate(transition)) {
					state.setNextCooperate(index(bricsStatesArray,
							transition.getDest()));
				} else if (isDefect(transition)) {
					state.setNextDefect(index(bricsStatesArray, transition
							.getDest()));
				} else if (isBoth(transition)) {
					int index = index(bricsStatesArray, transition.getDest());
					state.setNextDefect(index);
					state.setNextCooperate(index);
				} else {
					throw new IllegalArgumentException("Wrong machine!!");
				}
			}
			ans.addState(state);
		}
		return ans;

	}

	private static int index(State[] array, State state) {
		for (int i = 0; i < array.length; i++) {
			if (state.equals(array[i]))
				return i;
		}
		throw new IllegalArgumentException("Array does not contain such state");
	}

	private static boolean isCooperate(Transition transition) {
		if (transition.getMin() == transition.getMax()
				&& transition.getMax() == Action.COOPERATE_CHAR) {
			return true;
		}
		return false;
	}

	private static boolean isDefect(Transition transition) {
		if (transition.getMin() == transition.getMax()
				&& transition.getMax() == Action.DEFECT_CHAR) {
			return true;
		}
		return false;
	}

	private static boolean isBoth(Transition transition) {
		if (transition.getMin() == Action.COOPERATE_CHAR
				&& transition.getMax() == Action.DEFECT_CHAR) {
			return true;
		}
		if (transition.getMin() == Action.DEFECT_CHAR
				&& transition.getMax() == Action.COOPERATE_CHAR) {
			return true;
		}
		return false;
	}

}
