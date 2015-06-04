import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import nl.uva.creed.invasionfinder.PopulationElement;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class PopulationElementTest {

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
	public void testOrderByNumberOfCopies() {
		PopulationElement element = new PopulationElement(10, "A");
		PopulationElement element2 = new PopulationElement(50, "A");
		ArrayList<PopulationElement> array= new ArrayList<PopulationElement>();
		array.add(element);
		array.add(element2);
		PopulationElement.orderByNumberOfCopies(array);
		assertTrue(array.get(0).getNumberOfCopies() > array.get(1).getNumberOfCopies());
	}

}
