package nl.uva.creed.evolution.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import nl.uva.creed.evolution.BasicPopulationImpl;
import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.Population;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.explicitautomaton.PopulationParser;



public class ParseablePopulationImpl extends BasicPopulationImpl implements Population  {

	
	
	
	
	
	
	public ParseablePopulationImpl() {
		super();
	}





	public ParseablePopulationImpl(ArrayList<Individual> populationArray) {
		super(populationArray);
	}
	public ParseablePopulationImpl(Individual exampleIndividual, int size) {
		super(exampleIndividual, size);
	}

	public ParseablePopulationImpl(Population population) {
		super(population);
	}





	/**
	 * Half and half
	 * @param exampleIndividual1
	 * @param exampleIndividual2
	 * @param size
	 */
	    
	public ParseablePopulationImpl(Individual exampleIndividual1, Individual exampleIndividual2, int size) {
		super();
		populationArray = new ArrayList<Individual>();
		for (int i = 0; i < size/2; i++) {
			populationArray.add(exampleIndividual1.getCopy());
		}
		for (int i = 0; i < size/2; i++) {
			populationArray.add(exampleIndividual2.getCopy());
		}
	}

	


	
	public ParseablePopulationImpl(String pop) throws InvalidStrategyException, NumberFormatException, UnrecognizableString {
		super();
		populationArray = new ArrayList<Individual>();
		Hashtable<RepeatedGameStrategy, Integer> table = PopulationParser.parse(pop);
		Set<Entry<RepeatedGameStrategy, Integer>> set = table.entrySet();
		
		for (Iterator<Entry<RepeatedGameStrategy, Integer>> iterator = set.iterator(); iterator.hasNext();) {
			Entry<RepeatedGameStrategy, Integer> entry = (Entry<RepeatedGameStrategy, Integer>) iterator.next();
			RepeatedGameStrategy strategy = entry.getKey();
			for (int i = 0; i < entry.getValue(); i++) {
				populationArray.add(new IndividualImpl(strategy.getCopy()));
			}
		}		
	}

	
	
	

}
