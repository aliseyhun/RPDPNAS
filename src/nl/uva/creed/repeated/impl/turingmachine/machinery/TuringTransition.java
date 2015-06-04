package nl.uva.creed.repeated.impl.turingmachine.machinery;

import nl.vu.feweb.simulation.PseudoRandomSequence;

public class TuringTransition {

	/**
	 * Action to read from the reading tape
	 */
	public String readReadingTape;
	/**
	 * Action to perform on the reading tape
	 */
	public String actionReadingTape;
	/**
	 * Action to read on the writing tape
	 */
	public String readWritingTape;
	/**
	 * Symbol to write on the writing tape
	 */
	public String writeWritingTape;
	/**
	 * Action to perform on the writing tape
	 */
	public String actionWritingTape;

	public TuringTransition(String actionReadingTape, String actionWritingTape,
			String readReadingTape, String readWritingTape,
			String writeWritingTape) {
		super();
		this.actionReadingTape = actionReadingTape;
		this.actionWritingTape = actionWritingTape;
		this.readReadingTape = readReadingTape;
		this.readWritingTape = readWritingTape;
		this.writeWritingTape = writeWritingTape;
	}
	
	public TuringTransition getCopy(){
		return new TuringTransition(this.actionReadingTape, this.actionWritingTape, this.readReadingTape, this.readWritingTape, this.writeWritingTape);
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
		result = prime
				* result
				+ ((actionReadingTape == null) ? 0 : actionReadingTape
						.hashCode());
		result = prime
				* result
				+ ((actionWritingTape == null) ? 0 : actionWritingTape
						.hashCode());
		result = prime * result
				+ ((readReadingTape == null) ? 0 : readReadingTape.hashCode());
		result = prime * result
				+ ((readWritingTape == null) ? 0 : readWritingTape.hashCode());
		result = prime
				* result
				+ ((writeWritingTape == null) ? 0 : writeWritingTape.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return readReadingTape + actionReadingTape + "/" + readWritingTape
				+ writeWritingTape + actionWritingTape;
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
		if (!(obj instanceof TuringTransition))
			return false;
		TuringTransition other = (TuringTransition) obj;
		if (actionReadingTape == null) {
			if (other.actionReadingTape != null)
				return false;
		} else if (!actionReadingTape.equals(other.actionReadingTape))
			return false;
		if (actionWritingTape == null) {
			if (other.actionWritingTape != null)
				return false;
		} else if (!actionWritingTape.equals(other.actionWritingTape))
			return false;
		if (readReadingTape == null) {
			if (other.readReadingTape != null)
				return false;
		} else if (!readReadingTape.equals(other.readReadingTape))
			return false;
		if (readWritingTape == null) {
			if (other.readWritingTape != null)
				return false;
		} else if (!readWritingTape.equals(other.readWritingTape))
			return false;
		if (writeWritingTape == null) {
			if (other.writeWritingTape != null)
				return false;
		} else if (!writeWritingTape.equals(other.writeWritingTape))
			return false;
		return true;
	}

	/**
	 * Use to avoid non-deterministic turing machines
	 * 
	 * @param transition
	 * @return true if both transitions get activated with the same state of the
	 *         tapes
	 */
	public boolean departFromTheSameTapesState(TuringTransition transition) {
		if (this.readReadingTape.equals(transition.readReadingTape)
				&& this.readWritingTape.equals(transition.readWritingTape)) {
			return true;
		}
		return false;
	}

	/**
	 * @return the readReadingTape
	 */
	public String getReadReadingTape() {
		return readReadingTape;
	}

	/**
	 * @param readReadingTape
	 *            the readReadingTape to set
	 */
	public void setReadReadingTape(String readReadingTape) {
		this.readReadingTape = readReadingTape;
	}

	/**
	 * @return the actionReadingTape
	 */
	public String getActionReadingTape() {
		return actionReadingTape;
	}

	/**
	 * @param actionReadingTape
	 *            the actionReadingTape to set
	 */
	public void setActionReadingTape(String actionReadingTape) {
		this.actionReadingTape = actionReadingTape;
	}

	/**
	 * @return the readWritingTape
	 */
	public String getReadWritingTape() {
		return readWritingTape;
	}

	/**
	 * @param readWritingTape
	 *            the readWritingTape to set
	 */
	public void setReadWritingTape(String readWritingTape) {
		this.readWritingTape = readWritingTape;
	}

	/**
	 * @return the writeWritingTape
	 */
	public String getWriteWritingTape() {
		return writeWritingTape;
	}

	/**
	 * @param writeWritingTape
	 *            the writeWritingTape to set
	 */
	public void setWriteWritingTape(String writeWritingTape) {
		this.writeWritingTape = writeWritingTape;
	}

	/**
	 * @return the actionWritingTape
	 */
	public String getActionWritingTape() {
		return actionWritingTape;
	}

	/**
	 * @param actionWritingTape
	 *            the actionWritingTape to set
	 */
	public void setActionWritingTape(String actionWritingTape) {
		this.actionWritingTape = actionWritingTape;
	}

	public static TuringTransition random() {
		String actionReadingTape = pickRandom(TuringAlphabet.ACTIONS);
		String actionWritingTape = pickRandom(TuringAlphabet.ACTIONS); 
		String readReadingTape = pickRandom(TuringAlphabet.CAN_READ_READING_TAPE);
		String readWritingTape = pickRandom(TuringAlphabet.CAN_READ_WRITING_TAPE);
		String writeWritingTape = pickRandom(TuringAlphabet.CAN_WRITE_WRITING_TAPE);
		return new TuringTransition(actionReadingTape, actionWritingTape, readReadingTape, readWritingTape, writeWritingTape);
	}

	private static String pickRandom(String[] array) {
		int index = PseudoRandomSequence.nextInt(array.length);
		return array[index];
	}

}
