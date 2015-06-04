package nl.vu.feweb.utils;

import java.util.Comparator;
import java.util.StringTokenizer;

public class NumericStartComparator implements Comparator<String> {

	public int compare(String string1, String string2) {
		Double one = extractFirstNumber(string1);
		Double two = extractFirstNumber(string2);
		if (one == null || two == null) {
			return string1.compareTo(string2);
		}
		if (one < two) {
			return +1;
		}
		if (one == two) {
			return 0;
		} else {
			return -1;
		}
	}

	private Double extractFirstNumber(String string) {
		try {
			String firstToken = getFirstToken(string);
			double ans = Double.parseDouble(firstToken);
			return ans;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private String getFirstToken(String string) {
		StringTokenizer tokenizer = new StringTokenizer(string);
		return tokenizer.nextToken();
	}

}
