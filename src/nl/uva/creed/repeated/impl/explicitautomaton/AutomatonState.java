package nl.uva.creed.repeated.impl.explicitautomaton;

import java.util.StringTokenizer;

import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.vu.feweb.simulation.PseudoRandomSequence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class AutomatonState {

	private boolean cooperate;
	private Integer nextCooperate;
	private Integer nextDefect;

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof AutomatonState))
			return false;
		AutomatonState castOther = (AutomatonState) other;
		return new EqualsBuilder().append(cooperate, castOther.cooperate)
				.append(nextCooperate, castOther.nextCooperate).append(
						nextDefect, castOther.nextDefect).isEquals();
	}

	
	public void flip(){
		this.cooperate = !cooperate;
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(cooperate).append(nextCooperate)
				.append(nextDefect).toHashCode();
	}

	public AutomatonState(boolean cooperate, Integer nextCooperate,
			Integer nextDefect) {
		super();
		this.cooperate = cooperate;
		this.nextCooperate = nextCooperate;
		this.nextDefect = nextDefect;
	}

	@Override
	public String toString() {
		if (cooperate) {
			return "C " + this.nextCooperate + " " + this.nextDefect;
		} else {
			return "D " + this.nextCooperate + " " + this.nextDefect;
		}

	}

	public AutomatonState() {
		super();
	}

	public boolean isCooperate() {
		return cooperate;
	}

	public void setCooperate(boolean cooperate) {
		this.cooperate = cooperate;
	}

	public Integer getNextCooperate() {
		return nextCooperate;
	}

	public void setNextCooperate(Integer nextCooperate) {
		this.nextCooperate = nextCooperate;
	}

	public Integer getNextDefect() {
		return nextDefect;
	}

	public void setNextDefect(Integer nextDefect) {
		this.nextDefect = nextDefect;
	}

	public AutomatonState getCopy() {
		AutomatonState ans = new AutomatonState();
		ans.setCooperate(this.cooperate);
		ans.setNextCooperate(this.nextCooperate);
		ans.setNextDefect(this.nextDefect);
		return ans;
	}

	public boolean isValid(int sizeOfMachine) {
		if (this.nextCooperate == null || this.nextDefect == null) {
			return false;
		}
		if (this.nextCooperate < 0  || this.nextCooperate >= sizeOfMachine) {
			return false;
		}
		if (this.nextDefect < 0  || this.nextDefect >= sizeOfMachine) {
			return false;
		}
		return true;
	}


	public static AutomatonState random(int nextInt) {
		return new AutomatonState(PseudoRandomSequence.nextBoolean(), PseudoRandomSequence.nextInt(nextInt), PseudoRandomSequence.nextInt(nextInt));
	}
	
	public static AutomatonState convert(String stateString) throws UnrecognizableString{
		StringTokenizer tokenizer = new StringTokenizer(stateString);
		String isCooperate = tokenizer.nextToken();
		AutomatonState automatonState = new AutomatonState();
		if (isCooperate.equalsIgnoreCase(Action.COOPERATE_STRING)) {
			automatonState.setCooperate(true);
		}else if (isCooperate.equalsIgnoreCase(Action.DEFECT_STRING)) {
			automatonState.setCooperate(false);
		}else{
			throw new UnrecognizableString("isCoop is malformed:" + isCooperate);
		}
		String nextCoop = tokenizer.nextToken();
		String nextDef = tokenizer.nextToken();
		try {
			automatonState.nextCooperate = Integer.parseInt(nextCoop);
			automatonState.nextDefect = Integer.parseInt(nextDef);
		} catch (NumberFormatException e) {
			throw new UnrecognizableString("malformed state:" + e.toString());
		}
		return automatonState;
	}
	
	public Action action(){
		if (this.isCooperate()) {
			return Action.COOPERATE;
		}else{
			return Action.DEFECT;
		}
	}

}
