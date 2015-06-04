package nl.vu.feweb.evolution.models.measurecalculation;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.invasionfinder.SequentialBatchManipulation;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.uva.creed.repeated.measure.Measures;

import org.apache.commons.io.FilenameUtils;

import au.com.bytecode.opencsv.CSVReader;

public class MeasuresCalculation {
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnrecognizableString 
	 * @throws InvalidStrategyException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws IOException, UnrecognizableString, NumberFormatException, InvalidStrategyException {
		if (args.length !=3) {
			System.out.println("Syntax: inputFileName horizon continuationProbability");
			System.exit(0);
		}
		
		String inputFileName = args[0];
		int horizon = Integer.parseInt(args[1]) ;
		double continuationProbability = Double.parseDouble(args[2]);
		
		
		inputFileName = FilenameUtils.normalize(inputFileName);
		FileReader fileReader = new FileReader(inputFileName);
		CSVReader reader = new CSVReader(fileReader, '\t');;
		String[] nextLine;
		
		while ((nextLine = reader.readNext()) != null) {
			if (SequentialBatchManipulation.hasBlanks(nextLine)) {
				return;
			}
			double freq = Double.parseDouble(nextLine[0]);
			String str = nextLine[1];
			StringBuffer popMeasures = new StringBuffer();
			popMeasures.append(freq+"\t");
			RepeatedGameStrategy strategy = ExplicitAutomatonStrategy.readFromString(str);
			double cooperation = Measures.generalCooperativeness(strategy , horizon, continuationProbability);
			double reciprocity = Measures.generalReciprocity(strategy, horizon, continuationProbability);
			String singleStrategyMeasure = cooperation+"\t"+reciprocity+"\t"+str;
			popMeasures.append(singleStrategyMeasure);
			lineOutToFile(inputFileName+".out",popMeasures.toString());
			
		}
		}
		
	public static void lineOutToFile(String fileName, String output){
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
		    out.write(output + "\n");
		    out.close();
		} catch (IOException e) {
			System.err.println(e.toString());
			System.exit(0);
		}
	}
	
	
	}


