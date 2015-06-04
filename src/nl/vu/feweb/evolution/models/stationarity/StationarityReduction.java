package nl.vu.feweb.evolution.models.stationarity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import dk.brics.automaton.Automaton;

public class StationarityReduction {

	private ArrayList<Double> frequency;
	private ArrayList<String> state;

	private Hashtable<Set<ExplicitAutomatonStrategy>, Double> table;

	public StationarityReduction() {
		super();
		frequency = new ArrayList<Double>();
		state = new ArrayList<String>();
		table = new Hashtable<Set<ExplicitAutomatonStrategy>, Double>();
	}

	public void readStationary(String filename) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String str;
			while ((str = in.readLine()) != null) {
				readLine(str);
			}
			in.close();
		} catch (IOException e) {
		}
	}

	private void readLine(String str) {
		StringTokenizer stringTokenizer = new StringTokenizer(str, "\t");
		if (stringTokenizer.countTokens() != 2) {
			return;
		}
		frequency.add(Double.parseDouble(stringTokenizer.nextToken()));
		state.add(stringTokenizer.nextToken());
	}

	private void process() throws UnrecognizableString {
		Automaton.setMinimization(Automaton.MINIMIZE_HOPCROFT);
		int size = frequency.size();
		for (int i = 0; i < size; i++) {
			System.out.print(".");
			HashSet<ExplicitAutomatonStrategy> hashSet = minimizeStrategySet(ExplicitAutomatonStrategy
					.readCollectionFromString(this.state.get(i)));
			Double currentFrequency = table.get(hashSet);
			if (currentFrequency != null) {
				table.put(hashSet, currentFrequency + this.frequency.get(i));
			} else {
				table.put(hashSet, this.frequency.get(i));
			}

		}
	}

	private HashSet<ExplicitAutomatonStrategy> minimizeStrategySet(
			ArrayList<ExplicitAutomatonStrategy> list) {
		HashSet<ExplicitAutomatonStrategy> ans = new HashSet<ExplicitAutomatonStrategy>();
		for (Iterator<ExplicitAutomatonStrategy> iterator = list.iterator(); iterator
				.hasNext();) {
			ExplicitAutomatonStrategy explicitAutomatonStrategy = (ExplicitAutomatonStrategy) iterator
					.next();
			ans.add(ExplicitAutomatonStrategy
					.minimize(explicitAutomatonStrategy));
		}
		return ans;
	}
	
	public String print(){
		ArrayList<String> printList = new ArrayList<String>();
		for (Iterator<Set<ExplicitAutomatonStrategy>> iterator = table.keySet().iterator(); iterator.hasNext();) {
			Set<ExplicitAutomatonStrategy> state = iterator.next();
			DecimalFormat format = new DecimalFormat("0.0000000000000000000000000000000");
			printList.add( format.format(table.get(state))+ "\t" + state);
		}
		Collections.sort(printList); //, Collections.reverseOrder()
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = printList.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			buffer.append(string + "\n");
		}
		return buffer.toString();
	}


	public static void main(String[] args) throws UnrecognizableString {
		String filename = "/media/LaCie/SimulationResults/RepeatedGames/secondMCAnalysis/stationarySecondTry.txt";
		String output = "/media/LaCie/SimulationResults/RepeatedGames/secondMCAnalysis/reduced.csv";
		
		StationarityReduction reduction = new StationarityReduction();
		reduction.readStationary(filename);
		reduction.process();
		String result = reduction.print();
		System.out.println(result);
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter(output));
	        out.write(result);
	        out.close();
	    } catch (IOException e) {
	    }

	}
}
