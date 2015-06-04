package nl.uva.creed.evolution;

import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;



public interface Individual {
	
	public double getFitness() throws UnnasignedFitnessException;
	public void setFitness(double d);
	public Individual getCopy();
	public String toString();
	public boolean equals(Object obj);
	public int hashCode();
	public Strategy getStrategy();
	public double getPayOff();
	public double getPayOffNoComplexity();
	public void setPayOff(double payoff);
	public void setPayOffNoComplexity(double payoffNoComplexity);
		
	

}
