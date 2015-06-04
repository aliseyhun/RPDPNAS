package nl.uva.creed.invasionfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PopulationElement {

	

	private Integer	numberOfCopies;
	private String	rule;

	public Integer getNumberOfCopies() {
		return numberOfCopies;
	}

	public PopulationElement() {
		super();

	}

	public PopulationElement(Integer numberOfCopies, String rule) {
		super();

		this.numberOfCopies = numberOfCopies;
		this.rule = rule;
	}

	public void setNumberOfCopies(Integer numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numberOfCopies == null) ? 0 : numberOfCopies.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PopulationElement))
			return false;
		PopulationElement other = (PopulationElement) obj;
		if (numberOfCopies == null) {
			if (other.numberOfCopies != null)
				return false;
		} else if (!numberOfCopies.equals(other.numberOfCopies))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		return true;
	}

	public static void orderByNumberOfCopies(ArrayList<PopulationElement> array) {
		Collections.sort(array, comparator);
		
	}
	
	public static NumberOfCopiesComparator comparator = new NumberOfCopiesComparator();
	
	public static class NumberOfCopiesComparator implements Comparator<PopulationElement> {
		public int compare(PopulationElement o1, PopulationElement o2) {
			PopulationElement o1s = (PopulationElement) o1;
			PopulationElement o2s = (PopulationElement) o2;
			Double n1 = new Double(o1s.getNumberOfCopies());
			Double n2 = new Double(o2s.getNumberOfCopies());
			return Double.compare(n2, n1);
		}		

	}

}
