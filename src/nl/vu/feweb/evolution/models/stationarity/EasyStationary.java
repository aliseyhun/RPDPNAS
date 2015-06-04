package nl.vu.feweb.evolution.models.stationarity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.invasionfinder.PopulationElement;
import nl.uva.creed.invasionfinder.Row;
import nl.uva.creed.invasionfinder.SequentialBatchManipulation;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;

import au.com.bytecode.opencsv.CSVReader;

public class EasyStationary {

	public static Bag	bag	= new HashBag();

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnrecognizableString
	 * @throws InvalidStrategyException
	 * @throws NumberFormatException
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException,
			UnrecognizableString, NumberFormatException,
			InvalidStrategyException {

		if (args.length != 2) {
			System.out.println("Syntax: inputFileName outputFileName");
			System.exit(0);
		}
		String inputFileName = args[0];
		String outputFileName = args[1];

		// String inputFileName = "test_easy.csv";
		// String outputFileName = "facil.txt";

		CSVReader reader = SequentialBatchManipulation.getReader(inputFileName);
		SequentialBatchManipulation.skipPreliminarSection(reader);
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			if (nextLine.length <= 2)
				break;
			if (SequentialBatchManipulation.hasBlanks(nextLine)) {
				return;
			}
			ArrayList<PopulationElement> pop = Row
					.createWithAssortmentAndPopSize(nextLine).getPopulation();

			for (Iterator<PopulationElement> iterator = pop.iterator(); iterator
					.hasNext();) {
				PopulationElement populationElement = (PopulationElement) iterator
						.next();
				// Actual computations
				RepeatedGameStrategy strategy = ExplicitAutomatonStrategy
						.readFromString(populationElement.getRule());
				int copies = populationElement.getNumberOfCopies();
				bag.add(strategy, copies);

			}
		}

		int totalNumberOfStrategies = bag.size();

		for (Iterator<RepeatedGameStrategy> iterator = bag.uniqueSet()
				.iterator(); iterator.hasNext();) {
			RepeatedGameStrategy repeatedGameStrategy = (RepeatedGameStrategy) iterator
					.next();
			int copies = bag.getCount(repeatedGameStrategy);
			double frequency = (double) copies
					/ (double) totalNumberOfStrategies;
			DecimalFormat df = new DecimalFormat("0.000000000000000");
			String result = df.format(frequency) + "\t"
					+ repeatedGameStrategy.toString();
			lineOutToFile(outputFileName, result);

		}

	}
	
	
	
	

	public static void lineOutToFile(String fileName, String output) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName,
					true));
			out.write(output + "\n");
			out.close();
		} catch (IOException e) {
			System.err.println(e.toString());
			System.exit(0);
		}
	}

}
