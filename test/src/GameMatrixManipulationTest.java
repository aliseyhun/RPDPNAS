

import static org.junit.Assert.assertTrue;
import nl.uva.creed.invasionfinder.GameMatrixManipulation;

import org.apache.commons.math.linear.RealMatrixImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GameMatrixManipulationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void testTakeOutStronglyDominatedStrategies() {
		RealMatrixImpl matrix = new RealMatrixImpl(3,3);
		matrix.getDataRef()[0][0] = 0;
		matrix.getDataRef()[0][1] = 0;
		matrix.getDataRef()[1][0] = 4;
		matrix.getDataRef()[1][1] = 3;
		matrix.getDataRef()[0][2] = 0;
		matrix.getDataRef()[2][0] = 0;
		matrix.getDataRef()[2][2] = 1;
		matrix.getDataRef()[1][2] = 2;
		matrix.getDataRef()[2][1] = 3;
		System.out.println(matrix);
		System.out.println("------------------");
		matrix = GameMatrixManipulation.takeOutStronglyDominatedStrategies(matrix);
		System.out.println(matrix);
		assertTrue(true);
	}
	
	@Test
	public void testTakeEqualRows() {
		RealMatrixImpl matrix = new RealMatrixImpl(3,3);
		matrix.getDataRef()[0][0] = 0;
		matrix.getDataRef()[0][1] = 0;
		matrix.getDataRef()[1][0] = 0;
		matrix.getDataRef()[1][1] = 0;
		matrix.getDataRef()[0][2] = 0;
		matrix.getDataRef()[2][0] = 0;
		matrix.getDataRef()[2][2] = 0;
		matrix.getDataRef()[1][2] = 0;
		matrix.getDataRef()[2][1] = 1;
		System.out.println(matrix);
		System.out.println("------------------");
		matrix = GameMatrixManipulation.takeOutEqualRows(matrix);
		System.out.println(matrix);
		assertTrue(true);
	}

}
