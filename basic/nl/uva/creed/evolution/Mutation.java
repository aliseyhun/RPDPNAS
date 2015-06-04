package nl.uva.creed.evolution;

public interface Mutation {
	
	public Individual mutate(Individual individual);
	public Population mutate(Population population, double rate);
}
