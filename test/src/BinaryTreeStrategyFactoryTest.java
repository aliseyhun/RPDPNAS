import junit.framework.TestCase;
import nl.uva.creed.repeated.impl.binarytree.BinaryTreeStrategyFactory;


public class BinaryTreeStrategyFactoryTest extends TestCase {

    public void testAlwaysCooperate() {
	assertEquals(BinaryTreeStrategyFactory.alwaysCooperate().getGenome().read(), ".*");
    }

    public void testAlwaysDefect() {
	assertEquals(BinaryTreeStrategyFactory.alwaysDefect().getGenome().read(), "#");
    }

    public void testTitForTat() {
	assertEquals(BinaryTreeStrategyFactory.titForTat().getGenome().read(), "()|.*C");
    }

    public void testTatForTit() {
	assertEquals(BinaryTreeStrategyFactory.tatForTit().getGenome().read(), ".*C");
    }

    public void testTrigger() {
	assertEquals(BinaryTreeStrategyFactory.trigger().getGenome().read(), "C*");
    }

}
