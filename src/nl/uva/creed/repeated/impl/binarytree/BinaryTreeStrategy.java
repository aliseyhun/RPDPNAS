package nl.uva.creed.repeated.impl.binarytree;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

public class BinaryTreeStrategy implements RepeatedGameStrategy {

	
    private RegularExpressionBinaryTree genome;

    private Automaton automaton;

    public BinaryTreeStrategy(RegularExpressionBinaryTree genome) {
	super();
	this.genome = genome;
	this.automaton = new RegExp(genome.read()).toAutomaton();

    }

    

    public void setAutomaton(Automaton automaton) {
	this.automaton = automaton;
    }

    public RegularExpressionBinaryTree getGenome() {
	return genome;
    }

    public void setGenome(RegularExpressionBinaryTree genome) {
	this.genome = genome;
    }

    public RepeatedGameStrategy getCopy() throws InvalidStrategyException {
	return new BinaryTreeStrategy(this.genome.getCopy());
    }

    public Action nextAction(History history) {
	String inputStr = history.toString();
	boolean matchFound = automaton.run(inputStr);
	if (matchFound) {
	    return Action.COOPERATE;
	} else {
	    return Action.DEFECT;
	}
    }

    public static boolean isValidSyntax(BinaryTreeStrategy strategy) {
	try {
	    String string = strategy.genome.read();
	    RegExp regex = new RegExp(string);
	    Automaton automaton = regex.toAutomaton();
	    if (!BinaryTreeStrategy.isJavaValidSyntax(strategy)) {
		return false;
	    }
	    return true && automaton.isDeterministic();
	} catch (IllegalArgumentException e) {
	    return false;
	}
    }

    public static boolean isJavaValidSyntax(BinaryTreeStrategy strategy) {
	try {
	    String string = strategy.genome.read();
	    Pattern.compile(string);
	    return true;
	} catch (PatternSyntaxException e) {
	    return false;
	}
    }
    
    
    ///-----------------------------
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.getGenome().read() == null) ? 0 : this.getGenome().read()
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
		final BinaryTreeStrategy other = (BinaryTreeStrategy) obj;
		if (this.getGenome().read() == null) {
			if (other.getGenome().read() != null)
				return false;
		} else if (!this.getGenome().read().equals(other.getGenome().read()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getGenome().read();
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
