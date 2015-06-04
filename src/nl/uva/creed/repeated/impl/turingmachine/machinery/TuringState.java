package nl.uva.creed.repeated.impl.turingmachine.machinery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nl.vu.feweb.simulation.PseudoRandomSequence;
import nl.vu.feweb.simulation.SimulationUtils;

public class TuringState {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (transitions.isEmpty()) {
			return "HALT/"+this.accept+" ";
		}
		return transitions.toString()+"/"+this.accept+" ";
	}

	private HashMap<TuringTransition, Integer> transitions;
	private boolean accept = false;
	
	/**
	 * @return the accept
	 */
	public boolean isAccept() {
		return accept;
	}

	/**
	 * @param accept the accept to set
	 */
	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	public TuringState() {
		super();
		transitions = new HashMap<TuringTransition, Integer>();
	}
	
	public TuringState(boolean accept) {
		super();
		transitions = new HashMap<TuringTransition, Integer>();
		this.accept = accept;
	}
	
	public void addTransition(TuringTransition newTransition, Integer destinyState){
		//check that if there is a transition that departs from samte state of tapes, it has to be eliminated
		Set<TuringTransition> keys  = transitions.keySet();
		ArrayList<TuringTransition> removalList = new ArrayList<TuringTransition>();
		for (Iterator<TuringTransition> iterator = keys.iterator(); iterator.hasNext();) {
			TuringTransition turingTransition = (TuringTransition) iterator.next();
			if (turingTransition.departFromTheSameTapesState(newTransition)) {
				removalList.add(turingTransition);
				
			}
		}
		remove(removalList);
		transitions.put(newTransition, destinyState);
	}
	
	private void remove(ArrayList<TuringTransition> removalList) {
		for (Iterator<TuringTransition> iterator = removalList.iterator(); iterator.hasNext();) {
			TuringTransition turingTransition = (TuringTransition) iterator
					.next();
			transitions.remove(turingTransition);
		}
		
	}

	public void removeTransition(TuringTransition transition){
		transitions.remove(transition);
	}
	
	public boolean isHaltDemarcated(){
		return transitions.isEmpty();
	}
	
	
	public void forceHalt(){
		transitions.clear();
	}

	public TuringState(HashMap<TuringTransition, Integer> transitions) {
		super();
		this.transitions = transitions;
	}
	
	public TuringState(HashMap<TuringTransition, Integer> transitions, boolean accept) {
		super();
		this.transitions = transitions;
		this.accept = accept;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((transitions == null) ? 0 : transitions.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TuringState))
			return false;
		TuringState other = (TuringState) obj;
		if (transitions == null) {
			if (other.transitions != null)
				return false;
		} else if (!transitions.equals(other.transitions))
			return false;
		return true;
	}
	
	public TuringTransition findMatchingTransition(String inputSymbol, String outputSymbol){
		//check that if there is a transition that departs from input/output symbol pair
		Set<TuringTransition> keys  = transitions.keySet();
		for (Iterator<TuringTransition> iterator = keys.iterator(); iterator.hasNext();) {
			TuringTransition turingTransition = (TuringTransition) iterator.next();
			if (turingTransition.readReadingTape == inputSymbol && turingTransition.readWritingTape == outputSymbol) {
				return turingTransition;		
			}
		}
		return null;
	} 
	
	public Integer getDestiny(TuringTransition transition){
		return transitions.get(transition);
	}
	
	public int numberOfTransitions(){
		return transitions.size();
	}

	public TuringState getCopy() {
		TuringState ans = new TuringState(this.accept);
		for (Iterator<Map.Entry<TuringTransition, Integer>> it=this.transitions.entrySet().iterator(); it.hasNext(); ) {
	        Map.Entry<TuringTransition, Integer> entry = (Map.Entry<TuringTransition, Integer>)it.next();
	        TuringTransition key = entry.getKey();
	        Integer value = entry.getValue();
	        ans.transitions.put(key.getCopy(), new Integer(value));
	    }
		return ans;
	}

	public boolean isValid(int sizeOfTheMachine) {
		Collection<Integer> destinies = this.transitions.values();
		for (Iterator<Integer> iterator = destinies.iterator(); iterator.hasNext();) {
			Integer object = (Integer) iterator.next();
			if (object.intValue() < 0 || object.intValue() >= sizeOfTheMachine) {
				return false;	
			}	
		}
		return true;
	}

	public static TuringState random(int maximumExclusive) {
		//accepts or rejects with equal probability
		TuringState state = new TuringState(PseudoRandomSequence.nextBoolean());
		double addProbability = 0.5; //probability to add one more transition
		while(SimulationUtils.bernoullTrial(addProbability )){
			state.addTransition(TuringTransition.random(), PseudoRandomSequence.nextInt(maximumExclusive));
		}
		return state;
	}

	public TuringTransition getRandomTransition() {
		if (this.transitions.isEmpty()) {
			return null;
		}
		Object[] keys  = this.transitions.keySet().toArray();
		int index  = PseudoRandomSequence.nextInt(keys.length);
		return (TuringTransition) keys[index];
	}

	public void repairArrows(int deletedIndex, int newTmSize) {
		for (Iterator<Map.Entry<TuringTransition, Integer>> it=this.transitions.entrySet().iterator(); it.hasNext(); ) {
	        Map.Entry<TuringTransition, Integer> entry = (Map.Entry<TuringTransition, Integer>)it.next();
	        TuringTransition key = entry.getKey();
	        Integer value = entry.getValue();
	        if (value == deletedIndex) {
				transitions.put(key, PseudoRandomSequence.nextInt(newTmSize));
			}
	        if (value > deletedIndex) {
	        	transitions.put(key, value-1);
			}
	    }
		
	}
	
	
}
