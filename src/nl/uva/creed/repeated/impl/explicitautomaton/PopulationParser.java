package nl.uva.creed.repeated.impl.explicitautomaton;

import java.util.Hashtable;
import java.util.StringTokenizer;

import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;

import org.apache.commons.lang.StringUtils;

public class PopulationParser {

	public static Hashtable<RepeatedGameStrategy, Integer> parse(String pop) throws NumberFormatException, UnrecognizableString {
		Hashtable<RepeatedGameStrategy, Integer> ans = new Hashtable<RepeatedGameStrategy, Integer>();
		StringTokenizer tokenizer = new StringTokenizer(pop, ";", false);
		while (tokenizer.hasMoreElements()) {
			String element = (String) tokenizer.nextElement();
			String strategy = StringUtils.substringBetween(element, "Str: ", " Avg fit:");
			String number = StringUtils.substringBetween(element,"N: ", " Str:");
			ans.put(ExplicitAutomatonStrategy.readFromString(strategy), new Integer(number));
		}
		return ans;
	}

}
