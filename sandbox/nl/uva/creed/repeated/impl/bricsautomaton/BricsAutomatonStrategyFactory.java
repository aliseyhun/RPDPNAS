package nl.uva.creed.repeated.impl.bricsautomaton;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.vu.feweb.utils.MyStringUtils;
/**
 * @deprecated
 * @author jgarcia
 *
 */
public class BricsAutomatonStrategyFactory {

	public static final BricsAutomatonStrategy randomStrategy() {
		BricsAutomatonStrategy ans;
		boolean tryMore = true;
		while (tryMore) {
			try {
				ans = new BricsAutomatonStrategy(MyStringUtils.randomString(Alphabet.ALPHABETE, 0.9));
				return ans;
			} catch (InvalidStrategyException e) {
				tryMore = true;
			}
		}
		return null;

	}

	public static final BricsAutomatonStrategy alwaysCooperate()
			throws InvalidStrategyException {
		return new BricsAutomatonStrategy(".*");
	}

	public static final BricsAutomatonStrategy alwaysDefect()
			throws InvalidStrategyException {
		//return new RegularExpressionStrategy(".^");
		return new BricsAutomatonStrategy("#");
	}

	public static final BricsAutomatonStrategy titForTat()
			throws InvalidStrategyException {
		return new BricsAutomatonStrategy("()|.*C");
	}

	public static final BricsAutomatonStrategy titForTwoTats()
			throws InvalidStrategyException {
		return new BricsAutomatonStrategy("()|.|.*CC|.*CD|.*DC");
	}

	public static final BricsAutomatonStrategy titForThreeTats()
			throws InvalidStrategyException {
		return new BricsAutomatonStrategy("()|.|..|.*CCC|.*CDC|.*CCD|.*DCC|.*CDD|.*DCD|.*DDC");
	}

	public static final BricsAutomatonStrategy tatForTit()
			throws InvalidStrategyException {
		return new BricsAutomatonStrategy(".*C");
	}
	
	public static final BricsAutomatonStrategy trigger()
	throws InvalidStrategyException {
			return new BricsAutomatonStrategy("C*");
}

}
