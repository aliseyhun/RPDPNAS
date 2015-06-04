package nl.uva.creed.repeated.impl.turingmachine;

import static org.junit.Assert.assertTrue;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.repeated.History;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TMStrategyTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNextAction() throws InvalidStrategyException {
		TMStrategy str = TMStrategyFactory.alwaysCooperate();
		History history = History.artificialHistory("CCDCDD");
		System.out.println(str.nextAction(history));
		str = TMStrategyFactory.alwaysDefect();
		System.out.println(str.nextAction(history));
		assertTrue(true);
	}

}
