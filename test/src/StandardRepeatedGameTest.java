

import junit.framework.TestCase;
import nl.uva.creed.repeated.impl.StandardRepeatedGame;
import nl.uva.creed.repeated.impl.reactive.ReactiveStrategy;
import nl.uva.creed.repeated.impl.reactive.ReactiveStrategyFactory;
import nl.vu.feweb.simulation.PseudoRandomSequence;

public class StandardRepeatedGameTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		PseudoRandomSequence.seed(null);
	}

	public void testPayOffPlayer1() {
		try {
			
		    ReactiveStrategy one = ReactiveStrategyFactory.getStrategy(7);
		    ReactiveStrategy two = ReactiveStrategyFactory.getStrategy(7); 
			StandardRepeatedGame game = new StandardRepeatedGame(0.95,0.0);
			System.out.println(one);
			System.out.println(two);	
			game.setVerbose(true);
			//System.out.println(game.payOff(one, two));	
			System.out.println(game.payOff(one, two, 0.0,0.0));
		} catch (Exception e) {
			System.out.println(e.toString());
			fail("Exception");
		}
		
		
	}

}
