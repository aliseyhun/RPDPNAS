package nl.vu.feweb.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class MixedDetector {

	static double mustBeBelow = 0.80;
	static int forThisLong = 200;

	/**
	 * Looks for periods of mixed strategies
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String filename = "/media/WORK UNIT/SimulationResults/RepeatedGames/bigMama/piece00";
		//String filename = "/home/jgarcia/work/workspace/RepeatedGames/futura2.txt";
		BufferedReader in = new BufferedReader(new FileReader(filename));
		CSVReader csv = new CSVReader(in, '\t');
		String[] line = csv.readNext();
		int i = 0;
		double startingLine = 0.0;
		boolean mixed = false;
		while (line != null) {
			if (Double.parseDouble(line[3]) <= mustBeBelow) {
				i++;
				if (startingLine ==0.0 )startingLine = Double.parseDouble(line[0]);
				if (i >= forThisLong) {
					if (i == forThisLong) {
						System.out.println("Mixed populationState from line " + startingLine);
						mixed = true;
					}
				}
			} else {
				i = 0;
				if (mixed == true) {
					System.out.println(" to " + line[0] + " --  " +  new Double(Double.parseDouble(line[0])-startingLine).toString());
					mixed = false;
				}
				startingLine =0.0;
				
			}
			line = csv.readNext();
		}
		csv.close();
	}

}
