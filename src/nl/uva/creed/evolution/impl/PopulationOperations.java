package nl.uva.creed.evolution.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.Population;
import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.measure.Measures;
import nl.uva.creed.repeated.measure.frequencydependent.FrequencyDependentMeasuresFixed;
import nl.uva.creed.repeated.measure.frequencydependent.HistoryMetaSet3D;
import nl.vu.feweb.utils.NumericStartComparator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.DescriptiveStatisticsImpl;

public class PopulationOperations {

	private double averageFitness;
	
	private double	averagePayoff;

	private double	averagePayoffNoComplexity;

	private double averageComplexity;

	private double numberOfRules;

	private Set<RepeatedGameStrategy> strategySet;

	private String frequenciesPrint;

	private double averageReciprocity;

	private double averageCooperativeness;

	private double continuationProbability;

	private int horizon;

	private double frequencyMostPopular;

	private Hashtable<RepeatedGameStrategy, Double> frequenciesTable;

	

	
	/***
	 * measure 0 no measure, 1 frequency dependent, 2 absolute
	 */
	public PopulationOperations(Population population, int horizon,double continuationProbability, int measure, boolean computeAverageComplexity)
	throws UnnasignedFitnessException {
	super();

	HistoryMetaSet3D metaSet = null;

	if (measure == 1) {
			metaSet = new HistoryMetaSet3D(population, horizon);
	}

	this.horizon = horizon;
	this.continuationProbability = continuationProbability;
	
	DescriptiveStatistics fitnessStatistics = new DescriptiveStatisticsImpl();
	DescriptiveStatistics payoffStatistics = new DescriptiveStatisticsImpl();
	DescriptiveStatistics payoffNCStatistics = new DescriptiveStatisticsImpl();
	
	DescriptiveStatistics automatonSizeStatistics = new DescriptiveStatisticsImpl();
		
	//strategySet = new LinkedHashSet<RepeatedGameStrategy>();
	strategySet = new HashSet<RepeatedGameStrategy>();
		for (int i = 0; i < population.getSize(); i++) {
			Individual individual = population.getIndividual(i);
			RepeatedGameStrategy strategy = (RepeatedGameStrategy) individual.getStrategy();
			fitnessStatistics.addValue(individual.getFitness());
			payoffStatistics.addValue(individual.getPayOff());
			payoffNCStatistics.addValue(individual.getPayOffNoComplexity());
			
			if (computeAverageComplexity) {
				automatonSizeStatistics.addValue(strategy.getComplexity());
			}
			strategySet.add(strategy);
		
		}
		averageFitness = fitnessStatistics.getMean();
		averagePayoff = payoffStatistics.getMean();
		averagePayoffNoComplexity = payoffNCStatistics.getMean();
		
		//complexity
		if (computeAverageComplexity) {
			averageComplexity = automatonSizeStatistics.getMean();
		}
		
		
		numberOfRules = strategySet.size();
		ArrayList<String> list = new ArrayList<String>();
		this.averageCooperativeness = 0.0;
		this.averageReciprocity = 0.0;
		frequenciesTable = new Hashtable<RepeatedGameStrategy, Double>();
		for (@SuppressWarnings("rawtypes")
		Iterator iter = strategySet.iterator(); iter.hasNext();) {
			RepeatedGameStrategy element = (RepeatedGameStrategy) iter.next();
			int count = CollectionUtils.countMatches(population.getCollection(), new EqualRepeatedGameStrategyPredicate(element));

			if (measure == 1) {
				this.averageCooperativeness = this.averageCooperativeness + FrequencyDependentMeasuresFixed.generalCooperativeness(element, this.horizon,
										this.continuationProbability, metaSet)* count;

			} else if (measure == 2){
				this.averageCooperativeness = this.averageCooperativeness
						+ Measures.generalCooperativeness(element,
								this.horizon, this.continuationProbability)
						* count;
				this.averageReciprocity = this.averageReciprocity
						+ Measures.generalReciprocity(element, this.horizon,
								this.continuationProbability) * count;

			}
			double fraction = new Double(new Double(count)/ new Double(population.getSize()));
			frequenciesTable.put(element, fraction);
			list.add(count + " Str: " + element.toString() + " Avg fit: "+ averageFitnessOfStrategy(element, population));
		}
		Collections.sort(list, new NumericStartComparator());
		
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String string2 = (String) iterator.next();
			buffer.append("N: " + string2 + ";");
		}
		String ans = buffer.toString();
		this.frequenciesPrint = StringUtils.removeEnd(ans, ";");
		
		 StringUtils.join(list.toArray());

		this.averageCooperativeness = this.averageCooperativeness
		/ (new Double(population.getSize()));

		
		
		String mostPop = list.get(0);
		this.frequencyMostPopular = Double.valueOf(StringUtils
				.substringBetween(mostPop, "", " Str: "))/new Double(population.getSize());

		// reciprocity
		if (measure==1) {
			Object[] strategyArray = strategySet.toArray();
			this.averageReciprocity = 0.0;
			for (int i = 0; i < strategyArray.length; i++) {
				for (int j = 0; j < strategyArray.length; j++) {
					this.averageReciprocity = averageReciprocity
							+ FrequencyDependentMeasuresFixed
									.globalReciprocity(i, j,
											continuationProbability, metaSet)
							* frequenciesTable.get(strategyArray[i])
							* frequenciesTable.get(strategyArray[j]);
				}
			}
		}

	}

	/**
	 * @return the frequenciesTable
	 */
	public Hashtable<RepeatedGameStrategy, Double> getFrequenciesTable() {
		return frequenciesTable;
	}

	private double averageFitnessOfStrategy(RepeatedGameStrategy element,
			Population population) {
		Individual individual = new IndividualImpl(element);
		int matches = 0;
		double fitnessSum = 0;
		for (int i = 0; i < population.getSize(); i++) {

			if (population.getIndividual(i).equals(individual)) {
				matches++;
				try {
					fitnessSum = fitnessSum
							+ population.getIndividual(i).getFitness();
				} catch (UnnasignedFitnessException e) {
					e.printStackTrace();
					fitnessSum = 0;
				}
			}

		}
		return fitnessSum / new Double(matches);
	}

	public double getAverageFitness() {
		return averageFitness;
	}
	
	public double getAveragePayoff() {
		return averagePayoff;
	}

	public double getAveragePayoffNoComplexity() {
		return averagePayoffNoComplexity;
	}

	public double getNumberOfRules() {
		return numberOfRules;
	}


	/**
	 * @return the averageComplexity
	 */
	public double getAverageComplexity() {
		return averageComplexity;
	}

	/**
	 * 
	 * @return
	 * @author
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("PopulationOperations[");
		buffer.append("averageAutomatonSize = ").append(averageComplexity);
		buffer.append(" averageFitness = ").append(averageFitness);
		buffer.append(" averagePayoff = ").append(averagePayoff);
		buffer.append(" averagePayoffNoComplexityCost = ").append(averagePayoffNoComplexity);
		
		buffer.append(" numberOfRules = ").append(numberOfRules);
		buffer.append(" averageCooperativeness = ").append(
				averageCooperativeness);
		buffer.append(" averageReciprocity = ").append(averageReciprocity);
		buffer.append("]");
		return buffer.toString();
	}

	public Set<RepeatedGameStrategy> getStrategySet() {
		return strategySet;
	}

	public void setStrategySet(Set<RepeatedGameStrategy> strategySet) {
		this.strategySet = strategySet;
	}

	public String getFrequenciesPrint() {
		return frequenciesPrint;
	}

	public double averageReciprocity() {
		return averageReciprocity;
	}

	public double averageCooperativeness() {
		return averageCooperativeness;
	}

	public double frequencyMostPopular() {
		return this.frequencyMostPopular;
	}

	

}
