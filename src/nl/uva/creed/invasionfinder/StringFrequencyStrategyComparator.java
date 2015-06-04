package nl.uva.creed.invasionfinder;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

public class StringFrequencyStrategyComparator implements Comparator<Object> {

	public int compare(Object o1, Object o2) {
		String o1s = o1.toString();
		String o2s = o2.toString();
		o1s= StringUtils.substringBefore(o1s, "[");
		o2s= StringUtils.substringBefore(o2s, "[");
		Double n1 = new Double(o1s);
		Double n2 = new Double(o2s);
		return Double.compare(n1, n2);
	}

}
