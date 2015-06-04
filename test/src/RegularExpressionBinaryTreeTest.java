
import junit.framework.TestCase;
import nl.uva.creed.datastructures.binarytree.BinaryTree;
import nl.uva.creed.repeated.impl.binarytree.IllegalRegularExpressionBinaryTree;
import nl.uva.creed.repeated.impl.binarytree.RegularExpressionBinaryTree;

public class RegularExpressionBinaryTreeTest extends TestCase {

    protected void setUp() throws Exception {
	super.setUp();
    }

    protected void tearDown() throws Exception {
	super.tearDown();
    }

    public void testReadOkTree() {
	RegularExpressionBinaryTree myTree = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.CONCATENATION);

	RegularExpressionBinaryTree myLeft = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.OR);
	myLeft.setLeftChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT));
	myLeft.setRightChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.COOEPRATE));
	myTree.setLeftChild(myLeft);
	RegularExpressionBinaryTree myRight = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.STAR);
	myRight.setLeftChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT));
	myTree.setRightChild(myRight);
	System.out.println(myTree.read());
	System.out.println(myTree.numberOfNodes());
	assertEquals(myTree.read(), "D|CD*");

    }

    public void testReadNotOkTree() {
	RegularExpressionBinaryTree myTree = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.CONCATENATION);

	RegularExpressionBinaryTree myLeft = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.OR);
	myLeft.setLeftChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT));
	myLeft.setRightChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.COOEPRATE));
	myTree.setLeftChild(myLeft);
	RegularExpressionBinaryTree myRight = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.STAR);
	myRight.setLeftChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.OR));
	myTree.setRightChild(myRight);
	try {
	    myTree.read();
	    assertTrue(false);
	} catch (IllegalRegularExpressionBinaryTree e) {
	    assertTrue(true);
	}

    }

    @SuppressWarnings("unchecked")
	public void testReadNotOkTreeRightUnaryChild() {
	RegularExpressionBinaryTree myTree = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.CONCATENATION);

	BinaryTree<String> myLeft = new BinaryTree<String>(
		RegularExpressionBinaryTree.OR);
	myLeft.setLeftChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT));
	myLeft.setRightChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.COOEPRATE));
	myTree.setLeftChild(myLeft);
	BinaryTree<String> myRight = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.STAR);
	myRight.setRightChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT));
	myTree.setRightChild(myRight);
	try {
	    myTree.read();
	    assertTrue(false);
	} catch (IllegalRegularExpressionBinaryTree e) {
	    assertTrue(true);
	}

    }

    @SuppressWarnings("unchecked")
	public void testReadOkTreeParentheses() {
	RegularExpressionBinaryTree myTree = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.CONCATENATION);

	BinaryTree<String> myLeft = new BinaryTree<String>(
		RegularExpressionBinaryTree.OR);
	myLeft.setLeftChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT));
	myLeft.setRightChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.COOEPRATE));
	myTree.setLeftChild(myLeft);
	BinaryTree<String> myRight = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.PARENTHESES);
	myRight.setLeftChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT));
	myTree.setRightChild(myRight);
	System.out.println(myTree.read());
	assertEquals(myTree.read(), "D|C(D)");

    }
    
    
    public void testWalk()
    {
	RegularExpressionBinaryTree myTree = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.CONCATENATION);

	RegularExpressionBinaryTree myLeft = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.OR);
	myLeft.setLeftChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT));
	myLeft.setRightChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.COOEPRATE));
	myTree.setLeftChild(myLeft);
	RegularExpressionBinaryTree myRight = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT);
	myTree.setRightChild(myRight);
	System.out.println(myTree.read());
	System.out.println(myTree.numberOfNodes());
	System.out.println(myTree.walk(0));
	System.out.println("bien ok?");
	assertTrue(true);	
    }
    
    public void testGet()
    {
	RegularExpressionBinaryTree myTree = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.CONCATENATION);
	RegularExpressionBinaryTree myLeft = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.OR);
	myLeft.setLeftChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT));
	myLeft.setRightChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.COOEPRATE));
	myTree.setLeftChild(myLeft);
	RegularExpressionBinaryTree myRight = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT);
	myTree.setRightChild(myRight);
	myTree.walk(0);
	System.out.println(myTree.read());
	System.out.println(myTree.getSubtree(3).read());
	assertEquals("D|C", myTree.getSubtree(3).read());
	System.out.println(myTree.getSubtree(5).read());
	assertEquals(myTree.read(), myTree.getSubtree(5).read());
	System.out.println(myTree.getSubtree(4).read());
	assertEquals("D", myTree.getSubtree(4).read());
	System.out.println(myTree.getSubtree(1).read());
	assertEquals("D", myTree.getSubtree(1).read());
	
	System.out.println("bien ok?");
	assertTrue(true);	
    }
    
    public void testWalkLeafs(){
	RegularExpressionBinaryTree myTree = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.CONCATENATION);
	RegularExpressionBinaryTree myLeft = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.OR);
	myLeft.setLeftChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT));
	myLeft.setRightChild(new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.COOEPRATE));
	myTree.setLeftChild(myLeft);
	RegularExpressionBinaryTree myRight = new RegularExpressionBinaryTree(
		RegularExpressionBinaryTree.DEFECT);
	myTree.setRightChild(myRight);
	System.out.println(myTree.read());
	assertEquals("D",myTree.getLeaf(1).read());
	RegularExpressionBinaryTree mom = (RegularExpressionBinaryTree) myTree.getLeaf(1).getParent();
	System.out.println(mom);
	assertEquals("D|C", mom.read());
	myTree.walk(0);
	RegularExpressionBinaryTree daddies_son = myTree.getSubtree((4));
	RegularExpressionBinaryTree dad =  (RegularExpressionBinaryTree) daddies_son.getParent();
	System.out.println(dad);
	assertEquals("D|CD", dad.read());
	
    }
}
