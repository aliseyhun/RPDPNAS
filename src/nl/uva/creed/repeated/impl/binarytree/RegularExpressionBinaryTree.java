package nl.uva.creed.repeated.impl.binarytree;

import nl.uva.creed.datastructures.binarytree.BinaryTree;
import nl.uva.creed.game.Action;
import nl.vu.feweb.simulation.PseudoRandomSequence;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RegularExpressionBinaryTree extends BinaryTree {

    @Override
    public String toString() {
	if (value != null) {
	    return value.toString();
	}
	return "NULL";
    }

    /**
         * all codes are initialize by default in zero
         */
    private int code = 0;

    public static final String STAR = "*";

    public static final String PARENTHESES = "()";

    public static String[] UNARY_OPERATORS = { STAR, PARENTHESES };

    public static String randomUnaryOperatorValue(){
	return UNARY_OPERATORS[PseudoRandomSequence.nextInt(2)];
    }
    
    
    public static final String CONCATENATION = "0";

    public static final String OR = "|";

    public static String[] BINARY_OPERATORS = { CONCATENATION, OR };

    public static String randomBinaryOperatorValue(){
	return BINARY_OPERATORS[PseudoRandomSequence.nextInt(2)];
    }
    
    public static final String EMPTY_LANGUAGE = "#";

    public static final String EMPTY_STRING = "E";

    public static final String ANY = ".";

    public static final String DEFECT = Action.DEFECT_STRING;

    public static final String COOEPRATE = Action.COOPERATE_STRING;

    public static String[] TERMINALS = { EMPTY_LANGUAGE, EMPTY_STRING, ANY, DEFECT,
	    COOEPRATE };
    
    public static String randomTerminalValue(){
	return TERMINALS[PseudoRandomSequence.nextInt(5)];
    }

    /**
         * Useful to look up words in dictionary arrays
         * 
         * @param character
         * @param dictionary
         * @return
         */
    private static boolean contains(String character, String[] dictionary) {
	return StringUtils.contains(StringUtils.join(dictionary), character);
    }

    public boolean isBinaryOperator(String word) {
	return contains(word, BINARY_OPERATORS);
    }

    public boolean isUnaryOperator(String word) {
	return contains(word, UNARY_OPERATORS);
    }

    public boolean isTerminal(String word) {
	return contains(word, TERMINALS);
    }

    public boolean isOperator(String word) {
	return isBinaryOperator(word) || isUnaryOperator(word);
    }

    public RegularExpressionBinaryTree(String value, BinaryTree leftChild,
	    BinaryTree rightChild) {
	super(value, leftChild, rightChild);

    }

    RegularExpressionBinaryTree getCopy() {
	if (this.leftChild != null && this.rightChild != null) {
	    return new RegularExpressionBinaryTree((String) this.value,
		    ((RegularExpressionBinaryTree) this.leftChild).getCopy(),
		    ((RegularExpressionBinaryTree) this.rightChild).getCopy());
	} else if (this.leftChild != null && this.rightChild == null) {
	    return new RegularExpressionBinaryTree((String) this.value,
		    ((RegularExpressionBinaryTree) this.leftChild).getCopy(),
		    null);
	} else if (this.leftChild == null && this.rightChild != null) {
	    return new RegularExpressionBinaryTree((String) this.value, null,
		    ((RegularExpressionBinaryTree) this.rightChild).getCopy());
	} else {
	    return new RegularExpressionBinaryTree((String) this.value);
	}
    }

    public RegularExpressionBinaryTree(String value) {
	super(value);

    }

    /**
         * Reads the regular expression with explicit concatenations
         * 
         * @return
         */
    private String preRead() {
	if (isLeaf()) {
	    if (!isTerminal((String) this.value)) {
		throw new IllegalRegularExpressionBinaryTree(
			"Leaf is not terminal");
	    }
	    return value.toString();
	} else {
	    if (!isOperator((String) this.value)) {
		throw new IllegalRegularExpressionBinaryTree(
			"Tree with  children must be an operator");
	    }
	    if (this.leftChild != null & this.rightChild != null) {
		if (!isBinaryOperator((String) this.value)) {
		    throw new IllegalRegularExpressionBinaryTree(
			    "Tree with two children must be a binary operator");
		}
		return this.leftChild.read() + this.value
			+ this.rightChild.read();

	    } else if (this.leftChild != null & this.rightChild == null) {
		if (!isUnaryOperator((String) this.value)) {
		    throw new IllegalRegularExpressionBinaryTree(
			    "Tree with one child must be a unary operator");
		}
		if (this.value.equals(PARENTHESES)) {
		    return "(" + this.leftChild.read() + ")";
		}
		return this.leftChild.read() + this.value;

	    } else {
		throw new IllegalRegularExpressionBinaryTree(
			"Tree with one child must keep the child on the left");
	    }
	}

    }

    /**
         * reads a string
         */
    public String read() {
	String ans = StringUtils.remove(preRead(), CONCATENATION);
	return StringUtils.replace(ans, EMPTY_STRING, "()");
    }

    @Override
    public BinaryTree getLeftChild() {
	// TODO Auto-generated method stub
	return super.getLeftChild();
    }

    @Override
    public BinaryTree getRightChild() {
	// TODO Auto-generated method stub
	return super.getRightChild();
    }

    @Override
    public void setLeftChild(BinaryTree leftChild) {
	// TODO Auto-generated method stub
	super.setLeftChild(leftChild);

    }

    @Override
    public void setRightChild(BinaryTree rightChild) {
	// TODO Auto-generated method stub
	super.setRightChild(rightChild);

    }

    @Override
    public boolean isValid() {
	return super.isValid();
    }

    public boolean isValidSyntax() {
	if (super.isValid()) {
	    try {
		this.preRead();
		return true;
	    } catch (IllegalRegularExpressionBinaryTree e) {
		return false;
	    }
	} else {
	    return false;
	}
    }

    @Override
    public int numberOfNodes() {
	// return this.readToList().size();
	return this.walk(0);

    }

    public int walk(int counter) {
	if (isLeaf()) {
	    counter++;
	    this.code = counter;
	    return counter;
	} else if (this.leftChild != null & this.rightChild != null) {
	    counter = ((RegularExpressionBinaryTree) this.leftChild)
		    .walk(counter);
	    counter = ((RegularExpressionBinaryTree) this.rightChild)
		    .walk(counter);
	    counter++;
	    this.code = counter;
	    return counter;

	} else if (this.leftChild != null & this.rightChild == null) {

	    counter = 1 + ((RegularExpressionBinaryTree) this.leftChild)
		    .walk(counter);
	    this.code = counter;
	    return counter;

	} else {
	    counter = 1 + ((RegularExpressionBinaryTree) this.rightChild)
		    .walk(counter);
	    this.code = counter;
	    return counter;
	}
    }

    /**
         * MAKE SURE THE WALK HAS BEEN CALLED BEFORE
         */

    public RegularExpressionBinaryTree getSubtree(int code) {

	if (this.code == code) {
	    return this;
	} else {
	    if (this.leftChild != null) {

		RegularExpressionBinaryTree found = ((RegularExpressionBinaryTree) this
			.getLeftChild()).getSubtree(code);
		if (found != null) {
		    return found;
		}
	    }
	    if (this.rightChild != null) {

		RegularExpressionBinaryTree found = ((RegularExpressionBinaryTree) this
			.getRightChild()).getSubtree(code);
		if (found != null) {
		    return found;
		}
	    }
	    return null;
	}
    }

    public void clearCodes() {
	if (isLeaf()) {
	    this.code = 0;
	    return;
	} else if (this.leftChild != null & this.rightChild != null) {
	    ((RegularExpressionBinaryTree) this.leftChild).clearCodes();
	    ((RegularExpressionBinaryTree) this.rightChild).clearCodes();
	    this.code = 0;
	    return;

	} else if (this.leftChild != null & this.rightChild == null) {

	    ((RegularExpressionBinaryTree) this.leftChild).clearCodes();
	    this.code = 0;
	    return;

	} else {
	    ((RegularExpressionBinaryTree) this.rightChild).clearCodes();
	    this.code = 0;
	    return;
	}
    }

    public int walkLeafs(int counter) {
	if (isLeaf()) {
	    counter++;
	    this.code = counter;
	    return counter;
	} else if (this.leftChild != null & this.rightChild != null) {
	    counter = ((RegularExpressionBinaryTree) this.leftChild)
		    .walkLeafs(counter);
	    counter = ((RegularExpressionBinaryTree) this.rightChild)
		    .walkLeafs(counter);

	    return counter;

	} else if (this.leftChild != null & this.rightChild == null) {
	    counter = ((RegularExpressionBinaryTree) this.leftChild)
		    .walkLeafs(counter);
	    return counter;

	} else {
	    counter = ((RegularExpressionBinaryTree) this.rightChild)
		    .walkLeafs(counter);
	    return counter;
	}
    }

    public int numberOfLeaves() {
	this.clearCodes();
	return this.walkLeafs(0);
    }

    public RegularExpressionBinaryTree getLeaf(int code) {
	int leaves = this.numberOfLeaves();
	if (code > leaves || code <= 0) {
	    throw new IllegalArgumentException("Wrong labeling");
	}
	return this.getSubtree(code);
    }
/*
    // AQUI
    public RegularExpressionBinaryTree getParentTree(
	    RegularExpressionBinaryTree son) {

	if (this.isLeaf()) {
	    return null;
	} else if (this.leftChild != null && this.rightChild != null) {
	    if (son == this.leftChild) {
		return this;
	    }
	    if (son == this.rightChild) {
		return this;
	    }

	    RegularExpressionBinaryTree foundLeft = ((RegularExpressionBinaryTree) this.leftChild)
		    .getParentTree(son);
	    RegularExpressionBinaryTree foundRight = ((RegularExpressionBinaryTree) this.rightChild)
		    .getParentTree(son);

	    if (foundLeft == null && foundRight == null) {
		return null;
	    } else if (foundLeft != null) {
		return foundLeft;
	    } else if (foundRight != null) {
		return foundRight;
	    }
	    return null;
	}

	else if (this.leftChild != null) {

	    if (son == this.leftChild) {
		return this;
	    }

	    RegularExpressionBinaryTree foundLeft = ((RegularExpressionBinaryTree) this.leftChild)
		    .getParentTree(son);

	    if (foundLeft == null) {
		return null;
	    } else {
		return foundLeft;
	    }
	} else if (this.rightChild != null) {

	    if (son == this.rightChild) {
		return this;
	    }

	    RegularExpressionBinaryTree foundRight = ((RegularExpressionBinaryTree) this.rightChild)
		    .getParentTree(son);

	    if (foundRight == null) {
		return null;
	    } else {
		return foundRight;
	    }
	}
	return null;

    }*/

}
