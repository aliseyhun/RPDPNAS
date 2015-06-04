/**
 * 
 */
package nl.uva.creed.repeated.impl.binarytree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.uva.creed.evolution.Individual;
import nl.uva.creed.evolution.IndividualImpl;
import nl.uva.creed.evolution.Mutation;
import nl.uva.creed.evolution.Population;
import nl.vu.feweb.simulation.PseudoRandomSequence;
import nl.vu.feweb.simulation.SimulationUtils;

/**
 * @author julian
 * 
 */
public class MutationBinaryTreeStrategy implements Mutation {
	//add, delete, replace, copy -- delete chops off complete leaves
	private static Double[] distribution = { 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0.0 };
	
	public static RegularExpressionBinaryTree randomSubtreeWithoutRoot(
			RegularExpressionBinaryTree tree) {
		if (tree.isLeaf()) {
			return tree;
		}
		int size = tree.walk(0);
		// excluding root
		int picked = PseudoRandomSequence
				.nextIntBetweenOneAndInclusive(size - 1);
		return tree.getSubtree(picked);
	}

	public static RegularExpressionBinaryTree randomSubtreeWithRoot(
			RegularExpressionBinaryTree tree) {
		if (tree.isLeaf()) {
			return tree;
		}
		int size = tree.walk(0);
		int picked = PseudoRandomSequence.nextIntBetweenOneAndInclusive(size);
		return tree.getSubtree(picked);
	}

	public static RegularExpressionBinaryTree randomLeaf(
			RegularExpressionBinaryTree tree) {
		if (tree.isLeaf()) {
			return tree;
		}
		tree.clearCodes();
		int size = tree.walkLeafs(0);
		// excluding root
		int picked = PseudoRandomSequence.nextIntBetweenOneAndInclusive(size);
		return tree.getSubtree(picked);
	}

	private RegularExpressionBinaryTree mutateTree(
			RegularExpressionBinaryTree genome, Integer mutationType) {
		switch (mutationType) {
		case 0: // add
			return add(genome);
		case 1: // delete something
			return remove(genome);
			/*
			 * case 2: // switch return switchSubtrees(genome);
			 */
		case 2: // replace
			return replace(genome);
		case 3: // copy
			return copy(genome);
		}
		throw new RuntimeException("Error en el codigo");
	}

	public RegularExpressionBinaryTree copy(RegularExpressionBinaryTree genome) {
		if (genome.isLeaf()) {
			return genome;
		}
		if (genome.numberOfNodes() - genome.numberOfLeaves() <= 1) {
			return genome;
		}
		RegularExpressionBinaryTree subtree = randomSubtreeWithoutRoot(genome);
		while (subtree.isLeaf()) {
			subtree = randomSubtreeWithoutRoot(genome);
		}
		RegularExpressionBinaryTree leaf = randomLeaf(genome);
		RegularExpressionBinaryTree parent = (RegularExpressionBinaryTree) leaf.getParent();
		if (parent.getLeftChild() != null && parent.getLeftChild() == leaf) {
			parent.setLeftChild(subtree.getCopy());
		} else {
			parent.setRightChild(subtree.getCopy());
		}

		return genome;
	}

	@SuppressWarnings("unchecked")
	public RegularExpressionBinaryTree replace(
			RegularExpressionBinaryTree genome) {
		RegularExpressionBinaryTree random = randomSubtreeWithRoot(genome);
		if (random.isBinaryOperator((String) random.getValue())) {
			random.setValue(RegularExpressionBinaryTree
					.randomBinaryOperatorValue());
			return genome;
		}
		if (random.isUnaryOperator((String) random.getValue())) {
			random.setValue(RegularExpressionBinaryTree
					.randomUnaryOperatorValue());
			return genome;
		}
		if (random.isTerminal((String) random.getValue())) {
			random.setValue(RegularExpressionBinaryTree.randomTerminalValue());
			return genome;
		}
		return genome;
	}

	/*
	 * public RegularExpressionBinaryTree switchSubtrees(
	 * RegularExpressionBinaryTree genome) { if (genome.getLeftChild() == null ||
	 * genome.getRightChild() == null) { return genome; }
	 * RegularExpressionBinaryTree subtree1 = null; RegularExpressionBinaryTree
	 * subtree2 = null; while (subtree1 == subtree2) { subtree1 =
	 * randomSubtreeWithoutRoot(genome); subtree2 =
	 * randomSubtreeWithoutRoot(genome);
	 *  }
	 * 
	 * 
	 * 
	 * RegularExpressionBinaryTree parent1 = genome.getParentTree(subtree1);
	 * RegularExpressionBinaryTree parent2 = genome.getParentTree(subtree2);
	 * 
	 * try { if (parent1.getRightChild() == subtree1) {
	 * parent1.setRightChild(subtree2); } if (parent1.getLeftChild() ==
	 * subtree1) { parent1.setLeftChild(subtree2); }
	 * 
	 * if (parent2.getRightChild() == subtree2) {
	 * parent2.setRightChild(subtree1); } if (parent2.getLeftChild() ==
	 * subtree2) { parent2.setLeftChild(subtree1); }
	 *  } catch (IllegalArgumentException e) { return genome; } catch
	 * (StackOverflowError e) { return genome; } return genome; }
	 */

	@SuppressWarnings("unchecked")
	public RegularExpressionBinaryTree remove(RegularExpressionBinaryTree genome) {
		if (genome.isLeaf()) {
			return genome;
		}
		RegularExpressionBinaryTree randomLeaf = randomLeaf(genome);
		RegularExpressionBinaryTree leafParent = (RegularExpressionBinaryTree) randomLeaf.getParent();
		leafParent.setValue(randomLeaf.getValue());
		leafParent.setLeftChild(null);
		leafParent.setRightChild(null);
		return genome;
	}

	@SuppressWarnings("unchecked")
	public RegularExpressionBinaryTree add(RegularExpressionBinaryTree genome) {
		RegularExpressionBinaryTree leaf = randomLeaf(genome);
		if (PseudoRandomSequence.nextBoolean()) {
			// binary
			String randomTerminal = RegularExpressionBinaryTree
					.randomTerminalValue();
			String randomOperation = RegularExpressionBinaryTree
					.randomBinaryOperatorValue();
			leaf.setLeftChild(new RegularExpressionBinaryTree((String) leaf
					.getValue()));
			leaf.setRightChild(new RegularExpressionBinaryTree(randomTerminal));
			leaf.setValue(randomOperation);
			return genome;
		} else {
			// unary
			String randomOperation = RegularExpressionBinaryTree
					.randomUnaryOperatorValue();
			leaf.setLeftChild(new RegularExpressionBinaryTree((String) leaf
					.getValue()));
			leaf.setValue(randomOperation);
			return genome;
		}
	}

	public RegularExpressionBinaryTree mutateTree(
			RegularExpressionBinaryTree genome) {

		// add delete replace copy
		
		// determine randomly type of operation
		List<Integer> integer = new ArrayList<Integer>();
		integer.add(SimulationUtils.simulateDiscreteDistribution(distribution));
		while (SimulationUtils.bernoullTrial(0.5)) {
			integer.add(SimulationUtils
					.simulateDiscreteDistribution(distribution));
		}

		for (Iterator<Integer> iter = integer.iterator(); iter.hasNext();) {
			Integer mutationType = (Integer) iter.next();
			genome = mutateTree(genome, mutationType);
			// System.out.println(mutationType + " " + genome.read());

		}
		return genome;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.uva.creed.evolution.Mutation#mutate(nl.uva.creed.evolution.Individual)
	 */
	public Individual mutate(Individual individual) {
		if (individual.getStrategy() instanceof BinaryTreeStrategy) {
			BinaryTreeStrategy treeStrategy = ((BinaryTreeStrategy) individual
					.getStrategy());
			RegularExpressionBinaryTree newTree = this.mutateTree(treeStrategy
					.getGenome());
			return new IndividualImpl(new BinaryTreeStrategy(newTree));
		} else {
			throw new IllegalArgumentException(
					"This mutation operator can only work with BinaryTreeStrategy");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.uva.creed.evolution.Mutation#mutate(nl.uva.creed.evolution.Population,
	 *      double)
	 */
	public Population mutate(Population population, double mutationProbability) {
		for (int j = 0; j < population.getSize(); j++) {
			if (SimulationUtils.bernoullTrial(mutationProbability)) {
				Individual mutated = this.mutate(population.getIndividual(j));
				population.setIndividual(mutated, j);

			}
		}
		return population;
	}

}
