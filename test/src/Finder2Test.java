
import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.Assert;
import nl.uva.creed.invasionfinder.Finder;
import nl.uva.creed.invasionfinder.PopulationState;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class Finder2Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test() throws Exception{
		String fileName = "/home/jgarcia/work/workspace/RepeatedGames/results/popSize500_Cont0.95_Mut0.001.txt";
		Finder finder = new Finder(fileName);
		ArrayList<PopulationState> summary = finder.getRawSummary();
		ArrayList<Integer> listOne = new ArrayList<Integer>(); 
		for (Iterator<PopulationState> iterator = summary.iterator(); iterator.hasNext();) {
			PopulationState equilibrium = iterator.next();
			listOne.add(equilibrium.getTick());
		}
		ArrayList<PopulationState> summary2 = finder.getRawSummary();
		
		ArrayList<Integer> listTwo = new ArrayList<Integer>(); 
		for (Iterator<PopulationState> iterator = summary2.iterator(); iterator.hasNext();) {
			PopulationState equilibrium = iterator.next();
			listTwo.add(equilibrium.getTick());
		}
		Assert.assertEquals(listOne, listTwo);
	
	}

}
