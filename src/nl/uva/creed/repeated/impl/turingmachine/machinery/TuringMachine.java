package nl.uva.creed.repeated.impl.turingmachine.machinery;

import java.util.ArrayList;
import java.util.Iterator;

import nl.uva.creed.repeated.exceptions.MaximumTransitionsReachedInTheTM;
import nl.uva.creed.repeated.impl.turingmachine.tape.AbstractTape;
import nl.uva.creed.repeated.impl.turingmachine.tape.ReadOnlyTape;
import nl.uva.creed.repeated.impl.turingmachine.tape.ReadWriteTape;

public class TuringMachine {

	public final static int MAXIMUM_TRANSITIONS = 100;

	protected TuringState getCurrentState(){
		return this.states.get(currentState);
	}

	public TuringMachine() {
		super();
		this.states = new ArrayList<TuringState>();
		this.clearRun();
	}

	protected TuringMachine(ArrayList<TuringState> states) {
		super();
		this.states = states;
		this.clearRun();
	}

	/**
	 * reading tape
	 */
	//private ReadOnlyTape readOnlyTape;
	private ReadOnlyTape readOnlyTape;
	/**
	 * read/write tape
	 */
	private ReadWriteTape readWriteTape;

	/**
	 * States of the turing machine
	 */
	protected ArrayList<TuringState> states;
	/**
	 * current state
	 */
	private int currentState = 0;
	
	private int givenSteps = 0;
	
	 

	/**
	 * re-starts the counter, and the tapes, after this methods is called the
	 * readOnlyTape should be initialized
	 */
	protected void clearRun() {
		currentState = 0;
		readOnlyTape = null;
		readWriteTape = new ReadWriteTape();
		givenSteps = 0;
	}
	
	public void initializeReadingTape(ReadOnlyTape tape){
		readOnlyTape = tape;
	}
	
	

	/**
	 * returns the current state
	 * 
	 * @param index
	 * @return
	 */
	public TuringState getState(int index) {
		return this.states.get(index);
	}

	/**
	 * Performs a step of the turing machine
	 * 
	 * @return true if it stops after the state false otherwise
	 * @throws MaximumTransitionsReachedInTheTM 
	 */
	public boolean step() throws MaximumTransitionsReachedInTheTM {
		// reads input and output tapes to determine memory state
		givenSteps++;
		if (givenSteps > MAXIMUM_TRANSITIONS) {
			throw new MaximumTransitionsReachedInTheTM("already performed " + givenSteps);
		} 
		String symbolInputTape = this.readOnlyTape.read();
		String symbolOutputTape = this.readWriteTape.read();
		// find matching transition
		TuringTransition transition = states.get(currentState)
				.findMatchingTransition(symbolInputTape, symbolOutputTape);
		if (transition == null) {
			// halts, no matching transition from current state
			return true;
		} else {
			int destiny = states.get(currentState).getDestiny(transition);
			this.step(transition, destiny);
			if (this.getState(currentState).isHaltDemarcated()) {
				return true;
			}
			return false;

		}

	}

	private void step(TuringTransition transition, int destiny) {
		// action input tape
		actionTape(this.readOnlyTape, transition.actionReadingTape);
		// write output tape
		write(transition.writeWritingTape);
		// action output tape
		actionTape(this.readWriteTape, transition.actionWritingTape);
		currentState = destiny;
	}

	private void write(String writeWritingTape) {
		if (!writeWritingTape.equals(TuringAlphabet.NULL)) {
			this.readWriteTape.write(writeWritingTape);
		}
		return;
	}

	private void actionTape(AbstractTape tape, String actionReadingTape) {
		if (actionReadingTape.equals(TuringAlphabet.RIGHT)) {
			tape.moveRight();
		}
		if (actionReadingTape.equals(TuringAlphabet.LEFT)) {
			tape.moveLeft();
		}
		if (actionReadingTape.equals(TuringAlphabet.STAY)) {
			tape.stay();
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((states == null) ? 0 : states.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TuringMachine))
			return false;
		TuringMachine other = (TuringMachine) obj;
		if (states == null) {
			if (other.states != null)
				return false;
		} else if (!states.equals(other.states))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return states.toString();
	}
	
	public void addState(TuringState state){
		this.states.add(state);
	}
	
	public TuringState getInitialState() {
		return states.get(0);
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
	
	public int numberOfStates(){
		return this.states.size();
	}
	public int numberOfTransitions(){
		int ans = 0;
		for (Iterator<TuringState> iterator = states.iterator(); iterator
				.hasNext();) {
			TuringState state = (TuringState) iterator.next();
			ans = ans + state.numberOfTransitions();
		}
		return ans;
	}
	
}
