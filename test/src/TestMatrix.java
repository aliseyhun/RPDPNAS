
import nl.uva.creed.invasionfinder.InvasionClassifier;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;

import org.apache.commons.math.linear.RealMatrixImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestMatrix {

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
		ExplicitAutomatonStrategy str1 = ExplicitAutomatonStrategy.readFromString("[D 1 0, C 1 0]");
		ExplicitAutomatonStrategy str2 = ExplicitAutomatonStrategy.readFromString("[D 1 2, D 1 0, C 2 0]");
		//ExplicitAutomatonStrategy str3 = ExplicitAutomatonStrategy.readFromString("");
		RealMatrixImpl m = InvasionClassifier.build2x2Matrix(str1, str2, 0.75,0.0, 3.0, 0.0, 4.0, 1.0, 200);
		System.out.println(m);
		
		
	}

}
