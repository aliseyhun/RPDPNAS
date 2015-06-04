package nl.uva.creed.game.strategyvisualizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.uva.creed.repeated.History;

public class HistoryGenerator {

	private static ArrayList<String> getStrings(int maxLenght) {
		ArrayList<String> ans = new ArrayList<String>();
		if (maxLenght == 1) {
			ans.add("C");
			ans.add("D");
			return ans;
		} else {
			// ArrayList<String> copy = getStrings(maxLenght-1);
			ans = getStrings(maxLenght - 1);
			ans = doubleSize(ans);
			ans = postProcess(ans);
			// copy.addAll(ans);
			return ans;
		}

	}

	private static ArrayList<String> postProcess(ArrayList<String> ans) {
		for (int i = 0; i < ans.size(); i++) {
			if (i % 2 == 0) {
				ans.set(i, ans.get(i) + "C");
			} else {
				ans.set(i, ans.get(i) + "D");
			}
		}
		return ans;
	}

	private static ArrayList<String> doubleSize(ArrayList<String> param) {
		ArrayList<String> ans = new ArrayList<String>();
		for (Iterator<String> iterator = param.iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			ans.add(name);
			ans.add(name);
		}
		return ans;
	}

	private static ArrayList<String> getAllStrings(int maxLenght) {
		ArrayList<String> ans = new ArrayList<String>();
		ans.add("");
		for (int i = 1; i <= maxLenght; i++) {
			ans.addAll(getStrings(i));
		}
		return ans;
	}

	private static ArrayList<String> getAllStringsOfSize(int lenght) {
		ArrayList<String> ans = new ArrayList<String>();
		if (lenght == 0) {
			ans.add("");
		} else {
			ans.addAll(getStrings(lenght));
		}
		return ans;
	}

	static public List<History> getAllHistories(int maxLenght) {
		ArrayList<History> ans = new ArrayList<History>();
		ArrayList<String> strings = getAllStrings(maxLenght);
		for (int i = 0; i < strings.size(); i++) {
			ans.add(History.artificialHistory(strings.get(i)));
		}
		return ans;
	}

	static public List<History> getAllHistoriesOfSize(int lenght) {
		ArrayList<History> ans = new ArrayList<History>();
		ArrayList<String> strings = getAllStringsOfSize(lenght);
		for (int i = 0; i < strings.size(); i++) {
			ans.add(History.artificialHistory(strings.get(i)));
		}
		return ans;
	}
	
	/*
	 * public static void main(String[] args) {
	 * System.out.println(HistoryGenerator.getAllHistoriesOfSize(4)); }
	 */
}
