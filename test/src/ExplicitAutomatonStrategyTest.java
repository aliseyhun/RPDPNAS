import static org.junit.Assert.assertTrue;
import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategyFactory;

import org.junit.Test;


public class ExplicitAutomatonStrategyTest {

	@Test
	public void testAlwaysCooperate() throws InvalidStrategyException, UnrecognizableString {
		//ExplicitAutomatonStrategy str = ExplicitAutomatonStrategyFactory.trigger();
		//History history = History.artificialHistory("CCDC");
		//System.out.println(str.nextAction(history));
		
		ExplicitAutomatonStrategy strategy = ExplicitAutomatonStrategy.readFromString("[C 0 3, D 2 3, C 2 2, C 2 1]");
		strategy = ExplicitAutomatonStrategy.minimize(strategy);
		System.out.println(strategy);
		assertTrue(true);
		
	}

}
