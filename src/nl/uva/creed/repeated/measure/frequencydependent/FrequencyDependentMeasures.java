package nl.uva.creed.repeated.measure.frequencydependent;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;

import org.apache.commons.lang.StringUtils;

public class FrequencyDependentMeasures {
	
	
	public static double cooperativeness(RepeatedGameStrategy strategy, History initialHistory, int horizon, double continuationProbability, HistoryMetaSet metaSet) {
		double ans = 0.0;
		for (int s = 0; s <= horizon; s++) {
			String initialPiece = initialHistory.toString();
			int exactLenght = initialPiece.length() + s;
			Hashtable<String, Double> allPresentHistories = allPresentHistoriesAndFrequenciesFrom(initialPiece, exactLenght, metaSet, strategy);
			for (Enumeration<String> iterator = allPresentHistories.keys(); iterator.hasMoreElements();) {
				String historyString = (String) iterator.nextElement();
				History history = History.artificialHistory(historyString);
				if (strategy.nextAction(history).isCooperate()) {
					Double frequency = allPresentHistories.get(historyString);
					ans = ans + (Math.pow(continuationProbability, s)*frequency*
							((1.0-continuationProbability)/(1.0 -Math.pow(continuationProbability, horizon+1.0))));
				}
			}
		}
		return ans;
	}
	
	public static double generalCooperativeness(RepeatedGameStrategy strategy, int horizon, double continuationProbability, HistoryMetaSet metaSet){
		return cooperativeness(strategy, new History(), horizon, continuationProbability, metaSet);
	}
	
	
	public static double reciprocity(RepeatedGameStrategy strategy, History initialHistory, int horizon, double continuationProbability, HistoryMetaSet metaSet) {
		History historyPlusCooperation = initialHistory.getCopy();
		historyPlusCooperation.addMove(Action.COOPERATE);
		History historyPlusDefection = initialHistory.getCopy();
		historyPlusDefection.addMove(Action.DEFECT);
		return cooperativeness(strategy, historyPlusCooperation, horizon, continuationProbability, metaSet) - cooperativeness(strategy, historyPlusDefection, horizon, continuationProbability, metaSet);
	}

	public static double generalReciprocity(RepeatedGameStrategy strategy, int horizon, double continuationProbability, HistoryMetaSet metaSet){
		double ans = 0.0;
		for (int t = 0; t <= horizon; t++) {
			History initialHistory = new History();
			String initialPiece = initialHistory.toString();
			int exactLenght = t;
			Hashtable<String, Double> allPresentHistories = allPresentHistoriesAndFrequenciesFrom(initialPiece, exactLenght, metaSet, strategy);
			for (Enumeration<String> iterator = allPresentHistories.keys(); iterator.hasMoreElements();) {
				String historyString = (String) iterator.nextElement();
				History history = History.artificialHistory(historyString);
				Double frequency = allPresentHistories.get(historyString);
				ans = ans + reciprocity(strategy, history, horizon, continuationProbability, metaSet)*Math.pow(continuationProbability, t)*frequency;
			}
		}
		return ans;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static Hashtable<String, Double> allPresentHistoriesAndFrequenciesFrom(String initialPiece, int exactLenght, HistoryMetaSet metaSet, RepeatedGameStrategy strategy) {
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
				String matching = match(initialPiece, exactLenght, metaSet.getLongestHistoryString()[i][indexCurrentStrategy]);
				if (matching != null) {
					matchingHistories.add(matching);
					frequenciesMatchingHistories.add(new Double(metaSet.getFrequencies().get(i)));
			}
		}
		
		//build the actual matching map
		Hashtable<String, Double> ans = new Hashtable<String, Double>();
		for (int i = 0; i < matchingHistories.size(); i++) {
			String history = matchingHistories.get(i);
			Double frequency = frequenciesMatchingHistories.get(i);
			if (ans.containsKey(history)) {
				ans.put(history, ans.get(history)+frequency);
			}else{
				ans.put(history, frequency);
			}
			
		}
		return ans;
	}

	private static String match(String initialPiece, int exactLenght, String string) {
		String subString = StringUtils.mid(string, 0, exactLenght);
		if (StringUtils.isEmpty(initialPiece)) {
				return subString;
			
		}else{
			if (subString.startsWith(initialPiece)) {
				return subString;
			}else{
				return null;
			}	
		}
	}

	

}
