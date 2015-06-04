

import junit.framework.TestCase;
import nl.uva.creed.datastructures.binarytree.BinaryTree;

public class TestStringBinaryTree extends TestCase {

    protected void setUp() throws Exception {
	super.setUp();
    }

    protected void tearDown() throws Exception {
	super.tearDown();
    }

    public void testGetValue() {
	BinaryTree<String> myTree = new BinaryTree<String>("H");
	assertEquals(myTree.getValue(), "H");
    }

    public void testIsLeaf() {
	BinaryTree<String> myTree = new BinaryTree<String>("H");
	assertTrue(myTree.isLeaf());
	BinaryTree<String> myRight = new BinaryTree<String>("J");
	myTree.setRightChild(myRight);
	assertFalse(myTree.isLeaf());
	BinaryTree<String> myLeft = new BinaryTree<String>("S");
	myTree.setRightChild(myLeft);
	assertFalse(myTree.isLeaf());
	
	
    }

        public void testIsValid() {
	
        BinaryTree<String> myTree = new BinaryTree<String>("H");
    	BinaryTree<String> myRight = new BinaryTree<String>("J");
    	myTree.setRightChild(myRight);
    	assertTrue(myTree.isValid());
    	try {
    	myRight.setRightChild(myTree);
    	    
	} catch (IllegalArgumentException e) {
	assertTrue(true);
	}
    	assertFalse(myTree.isValid());

        }

            public void testRead() {
        	BinaryTree<String> myTree = new BinaryTree<String>("H");
        	BinaryTree<String> myLeft = new BinaryTree<String>("J");
        	myTree.setLeftChild(myLeft);
        	BinaryTree<String> myRight = new BinaryTree<String>("S");
        	myTree.setRightChild(myRight);
        	System.out.println(myTree.read());
        	assertEquals(myTree.read(), "JHS");
        	myTree.getLeftChild().setLeftChild(new BinaryTree<String>("T"));
        	assertEquals(myTree.read(), "TJHS");
    }

}
