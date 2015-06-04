package nl.uva.creed.evolution;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;

public interface Strategy {

	public Strategy getCopy() throws InvalidStrategyException;
	public int hashCode();
	public boolean equals(Object obj);
	public String toString();
	
}
