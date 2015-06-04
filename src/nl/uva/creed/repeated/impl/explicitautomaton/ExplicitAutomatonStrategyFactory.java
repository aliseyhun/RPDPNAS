package nl.uva.creed.repeated.impl.explicitautomaton;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;

public class ExplicitAutomatonStrategyFactory {
	
	public static final ExplicitAutomatonStrategy alwaysCooperate()
			throws InvalidStrategyException {
		AutomatonState state = new AutomatonState(true, 0, 0);
		return new ExplicitAutomatonStrategy(state);
	}
	
	public static final ExplicitAutomatonStrategy conditionOnTheFirstMove()
	throws InvalidStrategyException {
		AutomatonState state1 = new AutomatonState(true, 1, 2);
		AutomatonState state2 = new AutomatonState(true, 1, 1);
		AutomatonState state3 = new AutomatonState(false, 2, 2);
		
		ExplicitAutomatonStrategy ans = new ExplicitAutomatonStrategy(state1);
		ans.addState(state2);
		ans.addState(state3);
		return ans;
}
	
	

	public static final ExplicitAutomatonStrategy alwaysDefect()
			throws InvalidStrategyException {
		AutomatonState state = new AutomatonState(false, 0, 0);
		return new ExplicitAutomatonStrategy(state);
	}

	public static final ExplicitAutomatonStrategy titForTat()
			throws InvalidStrategyException {
		AutomatonState state1 = new AutomatonState(true, 0, 1);
		AutomatonState state2 = new AutomatonState(false, 0, 1);
		ExplicitAutomatonStrategy ans = new ExplicitAutomatonStrategy(state1);
		ans.addState(state2);
		return ans;
	}

	public static final ExplicitAutomatonStrategy titForTwoTats()
			throws InvalidStrategyException {
		AutomatonState state1 = new AutomatonState(true, 0, 1);
		AutomatonState state2 = new AutomatonState(true, 0, 2);
		AutomatonState state3 = new AutomatonState(false, 0, 2);
		ExplicitAutomatonStrategy ans = new ExplicitAutomatonStrategy(state1);
		ans.addState(state2);
		ans.addState(state3);
		return ans;
	}

	

	public static final ExplicitAutomatonStrategy tatForTit()
			throws InvalidStrategyException {
		AutomatonState state1 = new AutomatonState(false, 1, 0);
		AutomatonState state2 = new AutomatonState(true, 1, 0);
		ExplicitAutomatonStrategy ans = new ExplicitAutomatonStrategy(state1);
		ans.addState(state2);
		return ans;
	}
	
	public static final ExplicitAutomatonStrategy trigger()
	throws InvalidStrategyException {
		AutomatonState state1 = new AutomatonState(true, 0, 1);
		AutomatonState state2 = new AutomatonState(false, 1, 1);
		ExplicitAutomatonStrategy ans = new ExplicitAutomatonStrategy(state1);
		ans.addState(state2);
		return ans;
	}
	
	
	public static final ExplicitAutomatonStrategy wsls()
	throws InvalidStrategyException {
		AutomatonState state1 = new AutomatonState(true, 0, 1);
		AutomatonState state2 = new AutomatonState(false, 1, 0);
		ExplicitAutomatonStrategy ans = new ExplicitAutomatonStrategy(state1);
		ans.addState(state2);
		return ans;
	}
	
	public static final ExplicitAutomatonStrategy negativeHandshake()
	throws InvalidStrategyException {
		AutomatonState state1 = new AutomatonState(false, 1,2);
		AutomatonState state2 = new AutomatonState(false, 1, 1);
		AutomatonState state3 = new AutomatonState(true, 2, 3);
		AutomatonState state4 = new AutomatonState(false, 2, 3);
		
		ExplicitAutomatonStrategy ans = new ExplicitAutomatonStrategy(state1);
		ans.addState(state2);
		ans.addState(state3);
		ans.addState(state4);
		return ans;
	}
}
