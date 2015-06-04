import static org.junit.Assert.assertTrue;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.impl.ParseablePopulationImpl;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategyFactory;
import nl.uva.creed.repeated.measure.frequencydependent.FrequencyDependentMeasures;
import nl.uva.creed.repeated.measure.frequencydependent.HistoryMetaSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FrequencyDependentMeasuresTest {

	HistoryMetaSet metaSet;
	RepeatedGameStrategy strategy;
	int horizon = 100;
	double discount = 0.99;
	
	@Before
	public void setUp() throws Exception {
	ParseablePopulationImpl population = new ParseablePopulationImpl();
	for (int i = 0; i < 25; i++) {
		population.add(new IndividualImpl(ExplicitAutomatonStrategyFactory.alwaysDefect()));
	}
	for (int i = 0; i < 50; i++) {
		population.add(new IndividualImpl(ExplicitAutomatonStrategyFactory.titForTat()));
	}
	for (int i = 0; i < 25; i++) {
		population.add(new IndividualImpl(ExplicitAutomatonStrategyFactory.alwaysCooperate()));
	}
	metaSet = new HistoryMetaSet(population, horizon);
	strategy = ExplicitAutomatonStrategyFactory.titForTat();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCooperativeness() {
		double coop = FrequencyDependentMeasures.cooperativeness(strategy, new History(),
				horizon, discount, metaSet);
		System.out.println("Cooperativeness " + coop);
		assertTrue(coop <= 1.0);
	}

}
