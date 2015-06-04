package nl.uva.creed.game;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Action {
	
	private String code;
	
	public static final String COOPERATE_STRING = "C";
	public static final char COOPERATE_CHAR = 'C';
	public static final String DEFECT_STRING = "D";
	public static final char DEFECT_CHAR = 'D';
	
	public static final String[] ACTION_ALPHABET= {COOPERATE_STRING, DEFECT_STRING};
	
	public static final Action COOPERATE = new Action(COOPERATE_STRING);
	public static final Action DEFECT = new Action(DEFECT_STRING);
	
	
	@Override
	public boolean equals(Object obj) {
		   if (obj == null) { return false; }
		   if (obj == this) { return true; }
		   if (obj.getClass() != getClass()) {
		     return false;
		   }
		   Action rhs = (Action) obj;
		   return new EqualsBuilder()
		                 .appendSuper(super.equals(obj))
		                 .append(code, rhs.code).isEquals();
		  }
		 

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).
	       append(code).toHashCode();
	}
	
	

	@Override
	public String toString() {
		return code;
	}

	private Action(String code) {
		super();
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public boolean isCooperate(){
		return this.code.equalsIgnoreCase(Action.COOPERATE_STRING);
	}
}
