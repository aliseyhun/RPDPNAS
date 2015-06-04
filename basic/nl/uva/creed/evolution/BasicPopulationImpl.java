package nl.uva.creed.evolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;
import nl.vu.feweb.simulation.PseudoRandomSequence;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.DescriptiveStatisticsImpl;

public class BasicPopulationImpl implements Population {

	protected ArrayList<Individual> populationArray;
	
	public BasicPopulationImpl(ArrayList<Individual> populationArray) {
		super();
		this.populationArray =  populationArray;
		
	}
	
	public BasicPopulationImpl(Individual exampleIndividual, int size) {
		super();
		populationArray = new ArrayList<Individual>();
		for (int i = 0; i < size; i++) {
			populationArray.add(exampleIndividual.getCopy());
		}		
	}
	
	
	public BasicPopulationImpl(Population population) {
		this.populationArray = new ArrayList<Individual>();
		for (int i = 0; i < population.getSize(); i++) {
			this.populationArray.add(population.getIndividual(i).getCopy());
		}
	}

	public BasicPopulationImpl() {
		populationArray = new ArrayList<Individual>();
	}
	
	
	public boolean converged() {
		for (int i = 0; i < populationArray.size()-1; i++) {
			if(!populationArray.get(i).equals(populationArray.get(i+1))) return false;
		}
		return true;
	}

	public double getFrequency(Individual individual) {
		int copies = 0;
		for (int i = 0; i < populationArray.size(); i++) {
			if (populationArray.get(i).equals(individual)) {
				copies++;
			}
		}
		
		return new Double(new Double(copies)/new Double(populationArray.size())).doubleValue();
	}

	@Override
	public String toString() {
		return this.populationArray.toString();
	}

	public Individual getIndividual(int index) {
		return this.populationArray.get(index);
	}

	public int getSize() {
		return populationArray.size();
	}

	public void setIndividual(Individual individual, int index) {
		populationArray.set(index, individual);
	}

	public void removeIndividual(int index) {
		this.populationArray.remove(index);
	}

	public Collection<Individual> getPopulationArray() {
		return populationArray;
	}

	public void add(Individual individual) {
		this.populationArray.add(individual);
		
	}

	public String print() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < populationArray.size(); i++) {
			buffer.append(populationArray.get(i).toString()+"\n");
		}
		return buffer.toString();
	}

	public Collection<Individual> getCollection() {
		return this.populationArray;
	}

	public Individual getRandomIndividual() {
		int randomIndex = PseudoRandomSequence.nextInt(this.getSize());
		return this.getIndividual(randomIndex);
	}

	public double averageFitness() throws UnnasignedFitnessException {
		DescriptiveStatistics statistics = new DescriptiveStatisticsImpl();
		for (Iterator<Individual> iterator = this.populationArray.iterator(); iterator.hasNext();) {
			Individual individual = (Individual) iterator.next();
			statistics.addValue(individual.getFitness());
		}
		return statistics.getMean();
	}

	public List<Individual> getList() {
		return this.populationArray;
	}

}
