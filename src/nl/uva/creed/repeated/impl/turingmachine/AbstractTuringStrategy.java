package nl.uva.creed.repeated.impl.turingmachine;

import java.util.ArrayList;

import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.MaximumTransitionsReachedInTheTM;
import nl.uva.creed.repeated.impl.turingmachine.machinery.TuringMachine;
import nl.uva.creed.repeated.impl.turingmachine.machinery.TuringState;
import nl.uva.creed.repeated.impl.turingmachine.tape.ReadOnlyTape;

public abstract class AbstractTuringStrategy extends TuringMachine implements
		RepeatedGameStrategy {

	public AbstractTuringStrategy() {
		super();

	}

	/**
	 * Number of transitions in the whole machine
	 */
	public int getComplexity() {
		return this.numberOfTransitions();
	}

	public AbstractTuringStrategy(ArrayList<TuringState> states) {
		super(states);
	}

	public Action nextAction(History history) {
		this.load(history);
		try {
			while (!this.step())
				;
			return onHalt();
		} catch (MaximumTransitionsReachedInTheTM e) {
			return onMaximumNumberOfTransitionsReached();
		}
	}

	abstract protected Action onMaximumNumberOfTransitionsReached();

	abstract protected Action onHalt();

	private void load(History history) {
		this.clearRun();
		ReadOnlyTape tape = ReadOnlyTape.toTape(history);
		this.initializeReadingTape(tape);
	}

	protected static ArrayList<TuringState> deepCopy(
			ArrayList<TuringState> states) {
		ArrayList<TuringState> ans = new ArrayList<TuringState>();
		for (int i = 0; i < states.size(); i++) {
			ans.add(states.get(i).getCopy());
		}

		return ans;
	}

}
