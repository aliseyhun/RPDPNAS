

import static org.junit.Assert.assertTrue;
import nl.vu.feweb.utils.FileRepair;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileRepairTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testProcessPopulationString() {
		String test = " Frac: 100 Str: [D 0 0] Avg fit: 9.43";
		String ans = FileRepair.processPopulationString(test);
		System.out.println(ans);
		assertTrue(true);
	}

}
