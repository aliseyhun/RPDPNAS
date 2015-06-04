package nl.uva.creed.repeated.impl.binarytree;


public class BinaryTreeStrategyFactory {
    public static final BinaryTreeStrategy alwaysCooperate() {
	//".*"
	RegularExpressionBinaryTree alwaysCooperate = new RegularExpressionBinaryTree(RegularExpressionBinaryTree.STAR);
	alwaysCooperate.setLeftChild(new RegularExpressionBinaryTree(RegularExpressionBinaryTree.ANY));
	return new BinaryTreeStrategy(alwaysCooperate);
	
    }

    public static final BinaryTreeStrategy alwaysDefect() {
	return new BinaryTreeStrategy(new RegularExpressionBinaryTree(RegularExpressionBinaryTree.EMPTY_LANGUAGE));
    }

    public static final BinaryTreeStrategy titForTat() {
	//return new BinaryTreeStrategy("()|.*C");
	RegularExpressionBinaryTree titForThat = new RegularExpressionBinaryTree(RegularExpressionBinaryTree.OR);
	titForThat.setLeftChild(new RegularExpressionBinaryTree(RegularExpressionBinaryTree.EMPTY_STRING));
	RegularExpressionBinaryTree rightChild = new RegularExpressionBinaryTree(RegularExpressionBinaryTree.CONCATENATION);
	rightChild.setLeftChild(BinaryTreeStrategyFactory.alwaysCooperate().getGenome());
	rightChild.setRightChild(new RegularExpressionBinaryTree(RegularExpressionBinaryTree.COOEPRATE));
	titForThat.setRightChild(rightChild);
	return new BinaryTreeStrategy(titForThat);
    }

  

    public static final BinaryTreeStrategy tatForTit() {
	RegularExpressionBinaryTree tatForTit = new RegularExpressionBinaryTree(RegularExpressionBinaryTree.CONCATENATION);
	tatForTit.setLeftChild(BinaryTreeStrategyFactory.alwaysCooperate().getGenome());
	tatForTit.setRightChild(new RegularExpressionBinaryTree(RegularExpressionBinaryTree.COOEPRATE));
	//return new BinaryTreeStrategy(".*C");
	return new BinaryTreeStrategy(tatForTit);
    }

    public static final BinaryTreeStrategy trigger() {
	RegularExpressionBinaryTree alwaysCooperate = new RegularExpressionBinaryTree(RegularExpressionBinaryTree.STAR);
	alwaysCooperate.setLeftChild(new RegularExpressionBinaryTree(RegularExpressionBinaryTree.COOEPRATE));
	return new BinaryTreeStrategy(alwaysCooperate);
    }

}
