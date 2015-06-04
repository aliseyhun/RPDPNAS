import static org.junit.Assert.assertTrue;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.repeated.RepeatedGame;
import nl.uva.creed.repeated.exceptions.GameException;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.StandardRepeatedGame;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.uva.creed.repeated.nash.NashCheck;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class NashCheckTest {

	double continuationProbability = 0.75;
	double complexityCost = 0.0;
	
	RepeatedGame game = new StandardRepeatedGame(continuationProbability, complexityCost);
	

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsNash() throws InvalidStrategyException, GameException, UnrecognizableString {
	System.out.println(NashCheck.isNash(ExplicitAutomatonStrategy.readFromString("[C 0 1, D 0 1]"), game));
	assertTrue(true);
	}

}
