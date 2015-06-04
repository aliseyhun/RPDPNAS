package nl.uva.creed.evolution;

public interface Evolver {

	Population evolve(Population population, Mutation mutation, double mutationProbability);
}
