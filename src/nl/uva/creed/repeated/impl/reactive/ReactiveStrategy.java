package nl.uva.creed.repeated.impl.reactive;

import java.util.BitSet;

import nl.uva.creed.evolution.Strategy;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.vu.feweb.simulation.PseudoRandomSequence;

public class ReactiveStrategy implements RepeatedGameStrategy {
	
	BitSet bitSet = new BitSet(3);
	
	public static final Action bitToAction(boolean bit){
		if (bit) {
			return Action.DEFECT;
		}
		return Action.COOPERATE;
	}
	

	public ReactiveStrategy(BitSet bitSet) {
		super();
		this.bitSet = (BitSet) bitSet.clone();
	}

	public ReactiveStrategy() {
		super();
		bitSet.set(0);
		bitSet.set(1);
		bitSet.set(2);
	}

	public Strategy getCopy() throws InvalidStrategyException {
		return new ReactiveStrategy(this.bitSet);
	}

	public Action nextAction(History history) {
		if (history.isEmpty()) {
			return bitToAction(this.bitSet.get(0));
		}
		
		Action last = history.getLastAction();
		if (last.isCooperate()) {
			return bitToAction(this.bitSet.get(1));
		}
		return bitToAction(this.bitSet.get(2));

	}

	public int getComplexity() {
		if (this.bitSet.get(1) == this.bitSet.get(2)) {
			return 0;
		}
		return 1;
	}


	@Override
	public String toString() {
		return "["+ bitSet + "]";
	}
	
	public void flipRandom(){
		this.bitSet.flip(PseudoRandomSequence.nextInt(3));
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bitSet == null) ? 0 : bitSet.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReactiveStrategy))
			return false;
		ReactiveStrategy other = (ReactiveStrategy) obj;
		if (bitSet == null) {
			if (other.bitSet != null)
				return false;
		} else if (!bitSet.equals(other.bitSet))
			return false;
		return true;
	}
	

}
