import java.util.ArrayList;

import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.vu.feweb.evolution.models.automata.BricsExplicitConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.brics.automaton.Automaton;


public class MinimizationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testMinimization() throws UnrecognizableString{
		ArrayList<Automaton> out = new ArrayList<Automaton>();
		
		ExplicitAutomatonStrategy str = ExplicitAutomatonStrategy.readFromString("[C 2 3, D 0 1, C 2 1, C 1 0]");
		Automaton automaton = BricsExplicitConverter.convert(str);
		Automaton.setMinimization(Automaton.MINIMIZE_BRZOZOWSKI);
		//System.out.println(automaton);
		for (int i = 0; i < 100; i++) {
			Automaton copy = automaton.clone();
			copy.minimize();
			out.add(copy.clone());
		}
		for (int i = 1; i < out.size(); i++) {
			if (!out.get(i).equals(out.get(i-1))) {
				System.out.println("foutje");
			}else{
				System.out.println("OK");
			}
		}
		
		
		
		
		
	}

}
