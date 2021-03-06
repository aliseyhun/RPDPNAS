package nl.uva.creed.repeated.impl.turingmachine;

import java.util.ArrayList;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.impl.turingmachine.machinery.TuringState;

public class TmDefectOnHaltStrategy extends AbstractTuringStrategy {

	@Override
	protected Action onHalt() {
		return Action.DEFECT;
	}

	@Override
	protected Action onMaximumNumberOfTransitionsReached() {
		return Action.COOPERATE;
	}

	public RepeatedGameStrategy getCopy() throws InvalidStrategyException {
		return new TmDefectOnHaltStrategy(this.states);
	}

	public TmDefectOnHaltStrategy(ArrayList<TuringState> states) {
		super(deepCopy(states));
	}


	



	

	
}
