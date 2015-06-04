package nl.vu.feweb.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;


/**
 * Checks a file and changes the population so that the fractions are ordered from most common to least, does other things too on the file
 * @author jgarcia
 *
 */
public class FileRepair {

	public static void main(String[] args) throws IOException {
		String filename = "/home/jgarcia/SimulationResults/RepeatedGames/bigGirl/part09";
		String outFilename = "/home/jgarcia/SimulationResults/RepeatedGames/bigGirl/part09.txt";
		BufferedReader in = new BufferedReader(new FileReader(filename));
		CSVReader csv = new CSVReader(in, '\t');
		String[] line = csv.readNext();
		while (line != null) {
			String[] processed = process(line);
			appendtoOutput(processed, outFilename);
			line = csv.readNext();
		}
		csv.close();
	}

	private static void appendtoOutput(String[] processed, String outFilename) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(outFilename,
					true));
			out.write(addTabs(processed));
			out.close();
		} catch (IOException e) {
		}

	}

	private static String addTabs(String[] processed) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < processed.length; i++) {
			buffer.append(processed[i]);
			if (i < processed.length - 1) {
				buffer.append("\t");
			}
		}
		return buffer.toString() + "\n";
	}

	private static String[] process(String[] line) {
		String[] ans = new String[line.length];
		for (int i = 0; i < line.length; i++) {
			if (i <= 4) {
				ans[i] = line[i];
			} else if (i == 5) {
				ans[i] = processPopulationString(line[i]);
			} else {
				throw new RuntimeException("Wrong!");
			}
		}
		return ans;
	}

	public static String processPopulationString(String string) {
		string = StringUtils.removeStart(string, " ");
		string = StringUtils.replace(string, "Frac: ", "/");
		StringTokenizer tokenizer = new StringTokenizer(string, "/", false);
		ArrayList<String> list = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}
		NumericStartComparator numericStartComparator = new NumericStartComparator();
		Collections.sort(list, numericStartComparator);
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String string2 = (String) iterator.next();
			buffer.append("N: " + string2 + ";");
		}
		String ans = buffer.toString();
		return StringUtils.removeEnd(ans, ";");
	}

}
