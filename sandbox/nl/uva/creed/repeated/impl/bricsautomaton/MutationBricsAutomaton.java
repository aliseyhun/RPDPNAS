package nl.uva.creed.repeated.impl.bricsautomaton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.Mutation;
import nl.uva.creed.evolution.Population;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.vu.feweb.simulation.PseudoRandomSequence;
import nl.vu.feweb.simulation.SimulationUtils;

import org.apache.commons.lang.StringUtils;
/**
 * @deprecated
 * @author jgarcia
 *
 */
public class MutationBricsAutomaton implements Mutation {



	private String mutateString(String regularExpression) {
		
	    	Double[] distribution = {1.0/3.0, 1.0/3.0, 1.0/3.0};
		// determine randomly type of operation
	    	List<Integer> integer = new ArrayList<Integer>();
	    	integer.add(SimulationUtils.simulateDiscreteDistribution(distribution ));
	    	while (SimulationUtils.bernoullTrial(0.8)) {
	    	    integer.add(SimulationUtils.simulateDiscreteDistribution(distribution ));
		}
	    	
	    	for (Iterator<Integer> iter = integer.iterator(); iter.hasNext();) {
		    Integer mutationType = (Integer) iter.next();
		    regularExpression = mutateString(regularExpression, mutationType);
		}
	    	return regularExpression;
		
	}
	
	private String mutateString(String regularExpression, int type) {
		switch (type) {
		case 0: // add Something
			return addSomething(regularExpression);
		case 1: // remove something
			return removeSomething(regularExpression);
		case 2: // switch
			return switchSomething(regularExpression);
		}
		throw new RuntimeException("Error en el codigo");
	}

	private String switchSomething(String regularExpression) {
		if (regularExpression.length()<=1) {
			return regularExpression;
		}
		StringBuffer buffer = new StringBuffer(regularExpression);
		int index1 = PseudoRandomSequence.nextInt(regularExpression.length());
		int index2 = PseudoRandomSequence.nextInt(regularExpression.length());
		int iterations =  0;
		while ((Alphabet.doesItNeedMathing(String.valueOf(regularExpression
				.charAt(index1))) || Alphabet.doesItNeedMathing(String
				.valueOf(regularExpression.charAt(index2))))
				|| index1 == index2) {
			index1 = PseudoRandomSequence.nextInt(regularExpression.length());
			index2 = PseudoRandomSequence.nextInt(regularExpression.length());
			iterations++;
			if (iterations > 1000) {
				return regularExpression;
				//nothing was switch, i was waiting too long
			}
		}

		buffer.setCharAt(index1, regularExpression.charAt(index2));
		buffer.setCharAt(index2, regularExpression.charAt(index1));
		return buffer.toString();
	}

	private String removeSomething(String regularExpression) {
		if (regularExpression.length()<=1) {
			return regularExpression;
		}
		StringBuffer buffer = new StringBuffer(regularExpression);
		int index;
		try {
		    index = PseudoRandomSequence.nextInt(regularExpression.length());
		} catch (IllegalArgumentException e) {
		   index = 0;
		}
		
		
		
		
		if (Alphabet.doesItNeedMathing(String.valueOf(regularExpression
				.charAt(index)))) {
			String matchingChar = String.valueOf(regularExpression
					.charAt(index));
			buffer.deleteCharAt(index);
			String matchingChar2 = Alphabet.getMatcher(matchingChar);
			int index2 = StringUtils.indexOf(buffer.toString(), matchingChar2
					.charAt(0));
			if (index2 !=-1) {
			    buffer.deleteCharAt(index2);
				    
			}

		} else {
			buffer.deleteCharAt(index);
		}
		return buffer.toString();
	}

	private String addSomething(String regularExpression) {
		StringBuffer buffer = new StringBuffer(regularExpression);
		String add = Alphabet.randomElementFromTheAlphabet();

		
		
		if (Alphabet.isInconditional(add)) {
			int index;
			try {
			    index = PseudoRandomSequence
			    		.nextInt(regularExpression.length());
			} catch (IllegalArgumentException e) {
			    index =0;
			}
			buffer.insert(index, add);
			return buffer.toString();
		}

		if (Alphabet.isNotToBePlacedAtBeginning(add)) {
			int index = 0;
			if (regularExpression.length() ==1) {
				index=1;
			}
			while (index == 0) {
				try {
				    index = PseudoRandomSequence
				    		.nextInt(regularExpression.length());
				} catch (IllegalArgumentException e) {
				    index = 0;
				}
			}
			buffer.insert(index, add);
			return buffer.toString();
		}

		if (Alphabet.isInBetween(add)) {
			int index = regularExpression.length();
			if (regularExpression.length()<=1) {
				return add;
			}else{
				while (index == 1 || index  == regularExpression.length()) {
					try {
					    index = PseudoRandomSequence
					    		.nextInt(regularExpression.length());
					} catch (IllegalArgumentException e) {
					    index =0;
					}
				}
			}
			
			buffer.insert(index, add);
			return buffer.toString();
		}

		if (Alphabet.doesItNeedMathing(add)) {

			int index1 = 0;
			int index2 = 0;

			if (regularExpression.length()!=1) {
				while (index1 == index2) {
					index1 = PseudoRandomSequence.nextInt(regularExpression
							.length());
					index2 = PseudoRandomSequence.nextInt(regularExpression
							.length());
				}
			}else {
				index1 = 0;
				index2=1;
			}
			
			if (index1 > index2) {
				if (Alphabet.isLeft(add)) {
					buffer.insert(index1, add);
					buffer.insert(index2 + 1, Alphabet.getMatcher(add));
				} else {
					buffer.insert(index1, Alphabet.getMatcher(add));
					buffer.insert(index2 + 1, add);
				}
			} else {
				if (Alphabet.isLeft(add)) {
					buffer.insert(index2, add);
					buffer.insert(index1 + 1, Alphabet.getMatcher(add));
				} else {
					buffer.insert(index2, Alphabet.getMatcher(add));
					buffer.insert(index1 + 1, add);
				}
			}

			return buffer.toString();
		}

		throw new IllegalArgumentException("Nothing added!");
	}
	
	public Individual mutate(Individual individual) {
		if (individual.getStrategy() instanceof BricsAutomatonStrategy) {
			BricsAutomatonStrategy regularExpressionBasedStrategy = ((BricsAutomatonStrategy) individual
					.getStrategy());
			boolean tryMore = true;
			while (tryMore) {
				try {
					String newString = this.mutateString(regularExpressionBasedStrategy.getRegularExpression());
					tryMore = false;
					return new IndividualImpl(new BricsAutomatonStrategy(newString));
				} catch (InvalidStrategyException e) {
					// e.printStackTrace();
					tryMore = true;
				}
			}
			
		} else {
			throw new IllegalArgumentException(
					"This mutation only works with individuals whose strategies are  implementations of RegularExpressionBasedStrategy");
		}
		return null;
	}

	public Population mutate(Population population, double mutationProbability) {
		for (int j = 0; j < population.getSize(); j++) {
			if (SimulationUtils.bernoullTrial(mutationProbability)) {
				Individual mutated = this.mutate(population.getIndividual(j));
				population.setIndividual(mutated, j);
				
			}
		}
		return population;
	}
	
	

}
