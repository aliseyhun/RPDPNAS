package nl.uva.creed.repeated.impl.bricsautomaton;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
/***
 * 
 * @author jgarcia
 *@deprecated
 */
public class BricsAutomatonStrategy implements RepeatedGameStrategy {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((regularExpression == null) ? 0 : regularExpression
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BricsAutomatonStrategy other = (BricsAutomatonStrategy) obj;
		if (regularExpression == null) {
			if (other.regularExpression != null)
				return false;
		} else if (!regularExpression.equals(other.regularExpression))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.regularExpression;
	}

	protected String regularExpression;
	private Automaton automaton;

	

	public Action nextAction(History history) {
		String inputStr = history.toString();
		boolean matchFound = automaton.run(inputStr);
		if (matchFound) {
			return Action.COOPERATE;
		} else {
			return Action.DEFECT;
		}
	}

	public static boolean isValidSyntax(String string) {
		try {
			RegExp regex = new RegExp(string);
			Automaton automaton = regex.toAutomaton();
			if (!BricsAutomatonStrategy.isJavaValidSyntax(string)) {
				return false;
			}
			return true && automaton.isDeterministic();
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public RepeatedGameStrategy getCopy() throws InvalidStrategyException {
		return new BricsAutomatonStrategy(this.regularExpression.toString());
	}

	public boolean isValidSyntax() {
		try {
			RegExp regex = new RegExp(regularExpression);
			Automaton automaton = regex.toAutomaton();
			if (!BricsAutomatonStrategy.isJavaValidSyntax(this.regularExpression)) {
				return false;
			}
			return true && automaton.isDeterministic();

		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public BricsAutomatonStrategy(String rexp) throws InvalidStrategyException {
		try {
			this.regularExpression = rexp;
			this.automaton = new RegExp(rexp).toAutomaton();
			if (!BricsAutomatonStrategy.isJavaValidSyntax(rexp)) {
				throw new InvalidStrategyException();
			}
		} catch (IllegalArgumentException e) {
			throw new InvalidStrategyException();
		}

	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}
	
	public static boolean isJavaValidSyntax(String string) {
		try {
			Pattern.compile(string);
			return true;
		} catch (PatternSyntaxException e) {
			return false;
		}
	}

	public int getComplexity() {
		return automaton.getNumberOfStates();
	}

	public boolean isCooperateOnFirstMove() {
		return automaton.run("");
	}

	public boolean isEmpty() {
		return automaton.isEmpty();
	}

	public boolean isTotal() {
		return automaton.isTotal();
	}
}