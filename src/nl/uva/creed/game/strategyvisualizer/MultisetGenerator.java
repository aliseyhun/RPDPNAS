package nl.uva.creed.game.strategyvisualizer;

import java.util.ArrayList;
import java.util.List;

/*
 * A multiset of natural numbers generator
 * 
 * @author JuanCarlos
 * 
 * @version created on 20/03/2005
 */

public class MultisetGenerator {
	/**
	 * It is assumed that the multisets generated take their values from the
	 * domain set {@code X = {1, ..., maximum}}
	 */
	private int maximum;

	/** *** PUBLIC INTERFACE **** */

	// Constructors
	/**
	 * Creates a new generator of multisets
	 * 
	 * @param maximumOfDomain
	 *            maximum element in the domain set
	 * @see #maximum
	 */
	public MultisetGenerator(int maximumOfDomain) {
		setMaximum(maximumOfDomain);
	}

	// Another Methods

	/**
	 * Set the maximum of Domain value
	 * 
	 * @param anInteger
	 *            the new value for the maximum of domain
	 */
	public void setMaximum(int anInteger) {
		maximum = anInteger;
	}

	/**
	 * Return the maximum of domain value
	 * 
	 * @return the current maximum of domain
	 */
	public int getMaximum() {
		return maximum;
	}

	/**
	 * Generates all the possible multisets of size <b>multisetSize </b> whose
	 * elements come from the set {@code X = {1, ..., maximum}}
	 * 
	 * @param multisetSize
	 *            the size of multisets to be generated
	 */
	public List<List<Integer>> generate(int multisetSize) {
		ArrayList<List<Integer>> multisets;
		List<Integer> currentMultiset;

		multisets = new ArrayList<List<Integer>>();

		currentMultiset = getFirst(multisetSize);

		multisets.add(copy(currentMultiset));

		while (!last(currentMultiset)) {
			currentMultiset = (ArrayList<Integer>) successor(currentMultiset);
			multisets.add(copy(currentMultiset));
		}
		multisets.trimToSize();
		return multisets;
	}

	/** *** PRIVATE METHODS **** */

	// Another Methods
	/**
	 * @return the "first" multiset of size <b>size </b>
	 */
	private List<Integer> getFirst(int size) {
		ArrayList<Integer> first;
		int i;

		first = new ArrayList<Integer>(size);

		for (i = 0; i < size; i++) {
			first.add(new Integer(1));
		}
		first.trimToSize();
		return first;
	}

	/**
	 * creates a copy of a specified array
	 * 
	 * @param anArray
	 *            the array to be copied
	 * 
	 * @return a copy of <b>anArray </b>
	 */
	private List<Integer> copy(List<Integer> anArray) {
		ArrayList<Integer> newArray;
		int element;
		int i;

		newArray = new ArrayList<Integer>();

		for (i = 0; i < anArray.size(); i++) {
			element = ((Integer) (anArray.get(i))).intValue();
			newArray.add(new Integer(element));
		}
		newArray.trimToSize();
		return newArray;
	}

	/**
	 * tests if <b>anArray </b> is the "last" multiset, this is, if every
	 * element of <b>anArray </b> is the "maximum"
	 * 
	 * @param anArray
	 *            the array to test
	 * 
	 * @return true if anArray is the last multiset
	 */
	private boolean last(List<Integer> anArray) {
		boolean ans;
		int i;

		ans = true;
		i = 0;

		while (ans && i < anArray.size()) {
			if (!((Integer) anArray.get(i)).equals(new Integer(maximum))) {
				ans = false;
			}

			i = i + 1;
		}

		return ans;
	}

	/**
	 * Returns the successor, in lexicographic order, of a multiset
	 * 
	 * @param anArray
	 *            a multiset
	 * 
	 * @return the successor of anArray
	 */
	private List<Integer> successor(List<Integer> anArray) {
		List<Integer> theSuccessor;
		int i;
		int j;
		int element;
		boolean done;

		theSuccessor = copy(anArray);
		i = theSuccessor.size() - 1;
		done = false;

		while (!done && i >= 0) {
			element = ((Integer) (theSuccessor.get(i))).intValue();
			if (element < maximum) {
				element = element + 1;
				theSuccessor.set(i, new Integer(element));

				for (j = i + 1; j < theSuccessor.size(); j++) {
					theSuccessor.set(j, new Integer(element));
				}

				done = true;
			}

			i = i - 1;
		}

		return theSuccessor;
	}

	/** *** TEST ZONE **** */

	/*
	public static void main(String args[]) {
		MultisetGenerator generator = new MultisetGenerator(2);
		System.out.println(generator.generate(3));
	}*/
}