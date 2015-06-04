package nl.uva.creed.repeated.impl.turingmachine;

import java.util.ArrayList;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.impl.turingmachine.machinery.TuringState;

public class TMStrategy extends AbstractTuringStrategy {

	@Override
	protected Action onHalt() {
		if (this.getCurrentState().isAccept()) {
			return Action.COOPERATE;
		}else{
			return Action.DEFECT;
		}
		
	}

	@Override
	protected Action onMaximumNumberOfTransitionsReached() {
		return Action.COOPERATE;
	}

	public RepeatedGameStrategy getCopy() throws InvalidStrategyException {
		return new TMStrategy(deepCopy(states));
	}
	
	protected TMStrategy(ArrayList<TuringState> states) {
		super(deepCopy(states));
	}
	
	public TMStrategy(TuringState initialState) {
		super();
		this.states.add(initialState);
	}

}
