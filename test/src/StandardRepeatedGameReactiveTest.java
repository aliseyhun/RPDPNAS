

import junit.framework.TestCase;
import nl.uva.creed.repeated.impl.StandardRepeatedGame;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategyFactory;
import nl.vu.feweb.simulation.PseudoRandomSequence;

public class StandardRepeatedGameReactiveTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		PseudoRandomSequence.seed(null);
	}

	public void testPayOffPlayer1() {
		try {
			
		    //ExplicitAutomatonStrategy one = ExplicitAutomatonStrategyFactory.alwaysCooperate(); //ExplicitAutomatonStrategy.readFromString("[C 0 0]");
		    //ExplicitAutomatonStrategy two = ExplicitAutomatonStrategyFactory.alwaysDefect(); 
			
		    ExplicitAutomatonStrategy one = ExplicitAutomatonStrategy.readFromString("[D 0 0, D 0 1, D 0 1, D 1 1,D 1 1, D 1 1]");
		    ExplicitAutomatonStrategy two = ExplicitAutomatonStrategy.readFromString("[D 0 0, D 0 1, D 0 0, D 0 0, D 1 1, D 1 1]");
			
		    
		    StandardRepeatedGame game = new StandardRepeatedGame(1.0,0.1);
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
