import static org.junit.Assert.assertTrue;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;
import nl.uva.creed.evolution.impl.ParseablePopulationImpl;
import nl.uva.creed.evolution.impl.PopulationOperations;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PopulationOperationsTest {

	ParseablePopulationImpl mockPopulation;
	
	@Before
	public void setUp() throws Exception {
		this.mockPopulation = new ParseablePopulationImpl();
		String strg1 = "[D 0 0]";
		String strg2 = "[D 0 1, D 1 1]";
		int strg1copies = 1;
		int strg2copies = 1;
		for (int i = 0; i < strg1copies; i++) mockPopulation.add(new IndividualImpl(ExplicitAutomatonStrategy.readFromString(strg1)));
		for (int i = 0; i < strg2copies; i++) mockPopulation.add(new IndividualImpl(ExplicitAutomatonStrategy.readFromString(strg2)));
		for (int i = 0; i < mockPopulation.getSize(); i++) {
			mockPopulation.getIndividual(i).setFitness(0);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetFrequenciesPrint() throws UnnasignedFitnessException {
		
		System.out.println("are equal objects?");
		System.out.println(mockPopulation.getIndividual(0).equals(mockPopulation.getIndividual(1)));
		System.out.println("codes");
		System.out.println(mockPopulation.getIndividual(0).hashCode());
		System.out.println(mockPopulation.getIndividual(1).hashCode());
		
		PopulationOperations operations = new PopulationOperations(this.mockPopulation,100,0.9,0,false);
		String ans = operations.getFrequenciesPrint();
		System.out.println(ans);
		assertTrue(true);
	}

}
