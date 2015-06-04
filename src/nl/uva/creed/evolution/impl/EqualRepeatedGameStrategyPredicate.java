package nl.uva.creed.evolution.impl;

import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.repeated.RepeatedGameStrategy;

import org.apache.commons.collections.Predicate;

public class EqualRepeatedGameStrategyPredicate implements Predicate {

	RepeatedGameStrategy comparisonElement;

	public EqualRepeatedGameStrategyPredicate(RepeatedGameStrategy element) {
		comparisonElement = element;
	}

	public boolean evaluate(Object arg0) {
		IndividualImpl individual = (IndividualImpl) arg0;
		return individual.getStrategy().equals(comparisonElement);
	}

}
