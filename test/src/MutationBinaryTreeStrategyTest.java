import java.util.Random;

import junit.framework.TestCase;
import nl.uva.creed.repeated.impl.binarytree.BinaryTreeStrategyFactory;
import nl.uva.creed.repeated.impl.binarytree.MutationBinaryTreeStrategy;
import nl.uva.creed.repeated.impl.binarytree.RegularExpressionBinaryTree;
import nl.vu.feweb.simulation.PseudoRandomSequence;


public class MutationBinaryTreeStrategyTest extends TestCase {

    protected void setUp() throws Exception {
	super.setUp();
	int seed = new Random().nextInt();
	PseudoRandomSequence.seed(new Long(seed));
	System.out.println(PseudoRandomSequence.getSeed());
	
}
    
    /*public void testRandomSubtreeWithoutRoot() {
	RegularExpressionBinaryTree test = BinaryTreeStrategyFactory.titForTat().getGenome();
	System.out.println(test.read());
	System.out.println(MutationBinaryTreeStrategy.randomSubtreeWithoutRoot(test).read());
	assertTrue(true);
    }

    public void testRandomLeaf() {
	RegularExpressionBinaryTree test = BinaryTreeStrategyFactory.titForTat().getGenome();
	System.out.println(test.read());
	System.out.println(MutationBinaryTreeStrategy.randomLeaf(test).read());
	assertTrue(true);
    }

    public void testRandomLeafParent() {
	RegularExpressionBinaryTree test = BinaryTreeStrategyFactory.titForTat().getGenome();
	System.out.println(test.read());
	System.out.println(MutationBinaryTreeStrategy.randomLeafParent(test).read());
	assertTrue(true);
    }*/
    
    public void testCopy() {
	RegularExpressionBinaryTree test = BinaryTreeStrategyFactory.titForTat().getGenome();
	MutationBinaryTreeStrategy mutationBinaryTreeStrategy = new MutationBinaryTreeStrategy();
	System.out.println(test.read());
	RegularExpressionBinaryTree mutated = mutationBinaryTreeStrategy.mutateTree(test);
	System.out.println(mutated.read());
	assertTrue(true);
    }

}
