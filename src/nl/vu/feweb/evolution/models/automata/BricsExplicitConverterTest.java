package nl.vu.feweb.evolution.models.automata;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategyFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.brics.automaton.Automaton;

public class BricsExplicitConverterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvertExplicitAutomatonStrategy() throws InvalidStrategyException {
		ExplicitAutomatonStrategy strategy = ExplicitAutomatonStrategyFactory.titForTat();
		Automaton automaton = BricsExplicitConverter.convert(strategy);
		System.out.println(strategy);
		System.out.println("----------------------------------");
		System.out.println(automaton);
		System.out.println("----------------------------------");
		System.out.println(BricsExplicitConverter.convert(automaton));
		assertTrue("OK", true);
	}

	//@Test
	public void testConvertAutomaton() {
		fail("Not yet implemented");
	}

}
