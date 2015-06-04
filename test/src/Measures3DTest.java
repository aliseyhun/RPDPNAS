import static org.junit.Assert.assertTrue;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.impl.ParseablePopulationImpl;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategyFactory;
import nl.uva.creed.repeated.measure.frequencydependent.FrequencyDependentMeasuresFixed;
import nl.uva.creed.repeated.measure.frequencydependent.HistoryMetaSet3D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Measures3DTest {

	HistoryMetaSet3D metaSet;
	RepeatedGameStrategy strategy;
	int horizon = 100;
	double continuationProb = 0.8;
	
	@Before
	public void setUp() throws Exception {
	ParseablePopulationImpl population = new ParseablePopulationImpl();
	
	for (int i = 0; i < 100; i++) {
		population.add(new IndividualImpl(ExplicitAutomatonStrategyFactory.titForTat()));
	}
	metaSet = new HistoryMetaSet3D(population, horizon);
	System.out.println(metaSet.getStrategies());
	//strategy = ExplicitAutomatonStrategyFactory.titForTat();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLocalReciprocity() {
		//double ans = FrequencyDependentMeasuresFixed.globalReciprocity(0, 1, continuationProb, metaSet); 
		
		double coop = FrequencyDependentMeasuresFixed.globalReciprocity(0, 0, continuationProb, metaSet);
		//(strategy, new History(),	horizon, continuationProb, metaSet);
		System.out.println(coop);
		assertTrue(true);
	}

}


