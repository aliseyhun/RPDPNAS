import static org.junit.Assert.assertTrue;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.uva.creed.repeated.measure.Measures;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MeasuresTest {
	private RepeatedGameStrategy strategy;
	private double discount;
	private int horizon;

	@Before
	public void setUp() throws Exception {
		strategy = ExplicitAutomatonStrategy.readFromString("[D 1 0, C 1 1]");
		this.discount = 0.1;
		this.horizon = (int) Math.ceil(1.0/(1.0-this.discount))+4;
		
		//this.horizon = 10;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCooperativeness() throws InvalidStrategyException {

		double coop = Measures.cooperativeness(strategy, new History(), horizon, discount);
		
		System.out.println("Cooperativeness kahan "+ coop);
		assertTrue(coop<=1.0);
	}

	@Test
	public void testReciprocity() throws InvalidStrategyException {
		// System.out.println("Reciprocity " +
		// Measures.generalReciprocity(strategy, 3, 0.1));
		
		
		System.out.println("Reciprocity kahan "
				+ Measures.generalReciprocity(strategy, this.horizon, this.discount));

		assertTrue(true);
	}

	public void testAllPossibleHistoriesFrom() {
		// History testHistory = History.artificialHistory("");
		// List<History> list = Measures.allPossibleHistoriesFrom(testHistory,
		// 4);
		// System.out.println(list);
		assertTrue(true);
	}

}
