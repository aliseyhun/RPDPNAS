package nl.uva.creed.repeated.measure.frequencydependent;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;

import org.apache.commons.lang.StringUtils;

public class FrequencyDependentMeasuresFixed {

	public static double cooperativeness(RepeatedGameStrategy strategy,
			History initialHistory, int horizon,
			double continuationProbability, HistoryMetaSet3D metaSet) {
		double ans = 0.0;
		for (int s = 0; s <= horizon; s++) {
			String initialPiece = initialHistory.toString();
			int exactLenght = initialPiece.length() + s;
			Hashtable<String, Double> allPresentHistories = allPresentHistoriesAndFrequenciesFrom(
					initialPiece, exactLenght, metaSet, strategy);
			for (Enumeration<String> iterator = allPresentHistories.keys(); iterator
					.hasMoreElements();) {
				String historyString = (String) iterator.nextElement();
				History history = History.artificialHistory(historyString);
				if (strategy.nextAction(history).isCooperate()) {
					Double frequency = allPresentHistories.get(historyString);
					ans = ans
							+ (Math.pow(continuationProbability, s) * frequency * ((1.0 - continuationProbability) / (1.0 - Math
									.pow(continuationProbability, horizon + 1.0))));
				}
			}
		}
		return ans;
	}

	public static double generalCooperativeness(RepeatedGameStrategy strategy, int horizon,
			double continuationProbability, HistoryMetaSet3D metaSet) {
		return cooperativeness(strategy, new History(), horizon,
				continuationProbability, metaSet);
	}

	private static Hashtable<String, Double> allPresentHistoriesAndFrequenciesFrom(
			String initialPiece, int exactLenght, HistoryMetaSet3D metaSet,
			RepeatedGameStrategy strategy) {
		ArrayList<String> matchingHistories = new ArrayList<String>();
		ArrayList<Double> frequenciesMatchingHistories = new ArrayList<Double>();
		int indexCurrentStrategy = -1;
		for (int i = 0; i < metaSet.getStrategies().size(); i++) {
			if (metaSet.getStrategies().get(i).equals(strategy)) {
				indexCurrentStrategy = i;
				break;
			}
		}

		for (int i = 0; i < metaSet.numberOfStrategies(); i++) {
			// look at 0 depth only!!!!!!!!!!!!!!!
			String matching = match(initialPiece, exactLenght, metaSet
					.getCube()[i][indexCurrentStrategy][0]);
			if (matching != null) {
				matchingHistories.add(matching);
				frequenciesMatchingHistories.add(new Double(metaSet
						.getFrequencies().get(i)));
			}
		}

		// build the actual matching map
		Hashtable<String, Double> ans = new Hashtable<String, Double>();
		for (int i = 0; i < matchingHistories.size(); i++) {
			String history = matchingHistories.get(i);
			Double frequency = frequenciesMatchingHistories.get(i);
			if (ans.containsKey(history)) {
				ans.put(history, ans.get(history) + frequency);
			} else {
				ans.put(history, frequency);
			}

		}
		return ans;
	}

	private static String match(String initialPiece, int exactLenght,
			String string) {
		String subString = StringUtils.mid(string, 0, exactLenght);
		if (StringUtils.isEmpty(initialPiece)) {
			return subString;

		} else {
			if (subString.startsWith(initialPiece)) {
				return subString;
			} else {
				return null;
			}
		}
	}

	// RECIPROCITY MEASURE

	public static double globalReciprocity(int strategyIndex,
			int oponentStrategyIndex, double continuationProbability,
			HistoryMetaSet3D metaSet) {
		double ans = 0.0;
		for (int s = 1; s <= metaSet.getHorizon(); s++) {
			ans = ans
					+ localReciprocity(strategyIndex, oponentStrategyIndex, s,
							continuationProbability, metaSet)
					* (Math.pow(continuationProbability, s-1.0));
		}
		return ans;
	}
	


	//


	
	public static double localReciprocity(int strategyIndex,
			int oponentStrategyIndex, int pointInTime,
			double continuationProbability, HistoryMetaSet3D metaSet) {
		String factualHistory = metaSet.getCube()[strategyIndex][oponentStrategyIndex][0];
		String counterFactualHistory = metaSet.getCube()[strategyIndex][oponentStrategyIndex][pointInTime];
		if (pointInTime <= 0) {
			throw new IllegalArgumentException(
					"pointInTime should never be 0 or less");
		}

		RepeatedGameStrategy strategy = metaSet.getStrategies().get(strategyIndex);
		double sumFactual = 0.0;
		for (int i = pointInTime; i <= pointInTime + metaSet.getHorizon(); i++) {
			if (strategy.nextAction(
					History.artificialHistory(factualHistory.substring(0, i)))
					.isCooperate()) {
				double s = new Integer(i - pointInTime).doubleValue();
				sumFactual = sumFactual
						+ Math.pow(continuationProbability, s)
						* ((1 - continuationProbability) / (1 - Math.pow(
								continuationProbability,
								metaSet.getHorizon() + 1)));
			}
		}

		double sumCounterFactual = 0.0;
		for (int i = pointInTime; i <= pointInTime + metaSet.getHorizon(); i++) {
			if (strategy.nextAction(
					History.artificialHistory(counterFactualHistory.substring(
							0, i))).isCooperate()) {
				double s = new Integer(i - pointInTime).doubleValue();
				sumCounterFactual = sumCounterFactual
						+ Math.pow(continuationProbability, s)
						* ((1 - continuationProbability) / (1 - Math.pow(
								continuationProbability,
								metaSet.getHorizon() + 1)));
			}
		}

		if (Action.COOPERATE_STRING.equalsIgnoreCase(Character
				.toString(factualHistory.charAt(pointInTime)))) {
			return sumFactual - sumCounterFactual;
		} else {
			return sumCounterFactual - sumFactual;
		}

	}

}
