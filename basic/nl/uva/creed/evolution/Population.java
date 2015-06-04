package nl.uva.creed.evolution;

import java.util.Collection;
import java.util.List;

import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;




public interface Population {

	public boolean converged();
	public double getFrequency(Individual individual);
	public int getSize();
	public Individual getIndividual(int index);
	public void setIndividual(Individual individual, int index);
	public void add(Individual individual);
	public void removeIndividual(int index);
	public Collection<Individual> getCollection();
	public List<Individual> getList();
	public Individual getRandomIndividual();
	public double averageFitness() throws UnnasignedFitnessException ;
}