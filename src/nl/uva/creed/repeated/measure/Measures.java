package nl.uva.creed.repeated.measure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.uva.creed.game.Action;
import nl.uva.creed.game.strategyvisualizer.HistoryGenerator;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.vu.feweb.numerical.KahanSummation;

public class Measures {

	public static double cooperativeness(RepeatedGameStrategy strategy, History initialHistory, int horizon, double continuationProbability) {
		double ans = 0.0;
		for (int s = 0; s <= horizon; s++) {
			double fraction = (Math.pow(continuationProbability/2.0, s)*
					((1.0-continuationProbability)/(1.0 -Math.pow(continuationProbability, horizon+1.0))));
			List<History> allPossibleHistories = allPossibleHistoriesFrom(initialHistory, s);
			for (Iterator<History> iterator = allPossibleHistories.iterator(); iterator.hasNext();) {
				History history = (History) iterator.next();
				if (strategy.nextAction(history).isCooperate()) {
					ans = ans + fraction;
				}
			}
		}
		return ans;
	}
	
	public static double cooperativenessUsingKahan(RepeatedGameStrategy strategy, History initialHistory, int horizon, double continuationProbability) {
		KahanSummation summation = new KahanSummation();
		summation.clear();
		for (int s = 0; s <= horizon; s++) {
			List<History> allPossibleHistories = allPossibleHistoriesFrom(initialHistory, s);
			for (Iterator<History> iterator = allPossibleHistories.iterator(); iterator.hasNext();) {
				History history = (History) iterator.next();
				if (strategy.nextAction(history).isCooperate()) {
					summation.add(Math.pow(continuationProbability/2.0, s)*(1.0-continuationProbability));

				}
			}
		}
		return summation.value();
	}
	
	public static double generalCooperativeness(RepeatedGameStrategy strategy, int horizon, double continuationProbability){
		return cooperativeness(strategy, new History(), horizon, continuationProbability);
	}
	
	public static double generalCooperativenessUsingKahan(RepeatedGameStrategy strategy, int horizon, double continuationProbability){
		return cooperativenessUsingKahan(strategy, new History(), horizon, continuationProbability);
	}
	
	public static double reciprocity(RepeatedGameStrategy strategy, History initialHistory, int horizon, double continuationProbability) {
		History historyPlusCooperation = initialHistory.getCopy();
		historyPlusCooperation.addMove(Action.COOPERATE);
		History historyPlusDefection = initialHistory.getCopy();
		historyPlusDefection.addMove(Action.DEFECT);
		return cooperativeness(strategy, historyPlusCooperation, horizon, continuationProbability) - cooperativeness(strategy, historyPlusDefection, horizon, continuationProbability);
	}
	
	public static double reciprocityUsingKahan(RepeatedGameStrategy strategy, History initialHistory, int horizon, double continuationProbability) {
		History historyPlusCooperation = initialHistory.getCopy();
		historyPlusCooperation.addMove(Action.COOPERATE);
		History historyPlusDefection = initialHistory.getCopy();
		historyPlusDefection.addMove(Action.DEFECT);
		return cooperativenessUsingKahan(strategy, historyPlusCooperation, horizon, continuationProbability) - cooperativeness(strategy, historyPlusDefection, horizon, continuationProbability);
	}
	
	public static double generalReciprocity(RepeatedGameStrategy strategy, int horizon, double continuationProbability){
		double ans = 0.0;
		for (int t = 0; t <= horizon; t++) {
			List<History> allPossibleHistories = HistoryGenerator.getAllHistoriesOfSize(t);
			for (Iterator<History> iterator = allPossibleHistories.iterator(); iterator.hasNext();) {
				History history = (History) iterator.next();
				ans = ans + reciprocity(strategy, history, horizon, continuationProbability)*(Math.pow(continuationProbability/2.0, new Double(t)));
			}
		}
		return ans;
	}
	
	public static double generalReciprocityUsingKahan(RepeatedGameStrategy strategy, int horizon, double continuationProbability){
		double ans = 0.0;
		for (int t = 0; t <= horizon; t++) {
			List<History> allPossibleHistories = HistoryGenerator.getAllHistoriesOfSize(t);
			for (Iterator<History> iterator = allPossibleHistories.iterator(); iterator.hasNext();) {
				History history = (History) iterator.next();
				ans = ans + reciprocityUsingKahan(strategy, history, horizon, continuationProbability)*(Math.pow(continuationProbability/2.0, new Double(t)));
			}
		}
		return ans;
	}
	
	
	

	public static List<History> allPossibleHistoriesFrom(
			History initialHistory, int additionalRounds) {
		List<History> ans = new ArrayList<History>();
		if (additionalRounds < 0) {
			throw new IllegalArgumentException(
					"additionalRounds must be positive");
		}

		List<History> all = HistoryGenerator.getAllHistoriesOfSize(additionalRounds);
		for (Iterator<History> iterator = all.iterator(); iterator.hasNext();) {
			History history = (History) iterator.next();
			ans.add(History.concatenate(initialHistory, history));
		}
		return ans;
	}

}
