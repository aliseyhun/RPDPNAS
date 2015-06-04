package nl.uva.creed.invasionfinder;

import nl.uva.creed.invasionfinder.PopulationState.Cardinality;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;

public class RowEqulibriumTransformer {

	//ASSUMES EXPLICIT REPRESENTATION
	
	public static PopulationState transform(Row row, Cardinality type,
			Integer popSize) throws UnrecognizableString {
		PopulationState ans = new PopulationState();
		ans.setCardinality(type);
		ans.setNumberOfRules(row.getNumberOfRules());
		ans.setRun(row.getRun());
		ans.setTick(row.getTick());
		ans.setFrecMostPopular(row.getFrecMostPopular());
		ans.setFrecSecondMostPopular(row.getFrecSecondMostPopular());
		ans.setFrecThirdMostPopular(row.getFrecThirdMostPopular());
		//NOTE that the population is already ordered.
		for (int i = 0; i < row.getPopulation().size(); i++) {
			ans.getStrategies().add(ExplicitAutomatonStrategy.minimize(
					ExplicitAutomatonStrategy.readFromString(row
							.getPopulation().get(i).getRule())));
			ans.getFrequencies().add(
					new Double(row.getPopulation().get(i).getNumberOfCopies())
							/ new Double(popSize));
		}
		return ans;

	}

}
