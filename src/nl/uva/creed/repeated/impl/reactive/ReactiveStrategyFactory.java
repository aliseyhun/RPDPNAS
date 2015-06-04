package nl.uva.creed.repeated.impl.reactive;

import java.util.BitSet;

public class ReactiveStrategyFactory {

	public static ReactiveStrategy getStrategy(int code) {
		switch (code) {
		case 0:
			return new ReactiveStrategy(bitSet(false, false, false));
		case 1:
			return new ReactiveStrategy(bitSet(false, false, true));
		case 2:
			return new ReactiveStrategy(bitSet(false, true, false));
		case 3:
			return new ReactiveStrategy(bitSet(true, false, false));
		case 4:
			return new ReactiveStrategy(bitSet(false, true, true));
		case 5:
			return new ReactiveStrategy(bitSet(true, false, true));
		case 6:
			return new ReactiveStrategy(bitSet(true, true, false));
		case 7:
			return new ReactiveStrategy(bitSet(true, true, true));
		default:
			throw new RuntimeException();
		}

	}

	private static BitSet bitSet(boolean b, boolean c, boolean d) {
		BitSet ans = new BitSet(3);
		if (b)
			ans.set(0);
		if (c)
			ans.set(1);
		if (d)
			ans.set(2);
		return ans;
	}

}
