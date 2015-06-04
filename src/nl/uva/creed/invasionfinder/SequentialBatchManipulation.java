package nl.uva.creed.invasionfinder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.linear.RealMatrixImpl;
import org.apache.commons.math.stat.descriptive.DescriptiveStatisticsImpl;

import au.com.bytecode.opencsv.CSVReader;

public class SequentialBatchManipulation {

	public static CSVReader getReader(String fullFileName) {
		try {
			fullFileName = FilenameUtils.normalize(fullFileName);
			FileReader fileReader;
			fileReader = new FileReader(fullFileName);
			return new CSVReader(fileReader, '\t');
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static CSVReader getReader(String fullFileName, int startingLine) {
		try {
			fullFileName = FilenameUtils.normalize(fullFileName);
			FileReader fileReader;
			fileReader = new FileReader(fullFileName);
			return new CSVReader(fileReader, '\t',
					CSVReader.DEFAULT_QUOTE_CHARACTER, startingLine) {
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void skipPreliminarSection(CSVReader reader)
			throws IOException {
		String[] nextLine;
		// read header
		nextLine = reader.readNext();
		while (!nextLine[0].equalsIgnoreCase("Run")) {
			nextLine = reader.readNext();
		}
		// done with reader
	}

	public static String getHeader(String fullFileName) throws IOException {
		CSVReader reader = getReader(fullFileName);
		String[] nextLine;
		// read header
		nextLine = reader.readNext();
		while (!nextLine[0].equalsIgnoreCase("Run")) {
			nextLine = reader.readNext();
		}
		ArrayList<String> header = new ArrayList<String>();
		for (int i = 0; i < nextLine.length; i++) {
			header.add(nextLine[i]);
		}
		return PrintCSV.printNice(header);
	}

	public static String getDescription(String fullFileName) {
		try {
			CSVReader reader = getReader(fullFileName);
			String[] nextLine;
			// read header
			nextLine = reader.readNext();
			StringBuffer desc = new StringBuffer();
			while (!nextLine[0].equalsIgnoreCase("Run")) {
				desc.append(nextLine[0] + "\n");
				nextLine = reader.readNext();
			}
			// done with reader

			// store header
			ArrayList<String> header = new ArrayList<String>();
			for (int i = 0; i < nextLine.length; i++) {
				header.add(nextLine[i]);
			}

			return desc.toString() + "\n" + PrintCSV.printNice(header) + "\n";

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("FileNotFoundException");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static double[][] getRun(String fullFileName, int run)
			throws IOException {
		int startingLine = (run - 1) * linesPerRun(fullFileName);
		CSVReader reader = getReader(fullFileName, startingLine);
		if (run == 1) {
			skipPreliminarSection(reader);
		}
		ArrayListStringBuffer buffer = new ArrayListStringBuffer();
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {

			if (Integer.parseInt(nextLine[0]) == run) {
				buffer.add(nextLine);
			}
			if (Integer.parseInt(nextLine[0]) > run) {
				return buffer.process().getData();
			}
		}
		return null;
	}

	public static RealMatrixImpl getRunAsMatrix(String fullFileName, int run)
			throws IOException {
		int startingLine = (run - 1) * linesPerRun(fullFileName);
		CSVReader reader = getReader(fullFileName, startingLine);
		if (run == 1) {
			skipPreliminarSection(reader);
		}
		ArrayListStringBuffer buffer = new ArrayListStringBuffer();
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {

			try {
				if (Integer.parseInt(nextLine[0]) == run) {
					buffer.add(nextLine);
				}
				if (Integer.parseInt(nextLine[0]) > run) {
					return buffer.process();
				}
			} catch (NumberFormatException e) {

			}
		}
		return null;
	}

	public static double[][] getSummary(String fullFileName) throws IOException {

		CSVReader reader = getReader(fullFileName);
		skipPreliminarSection(reader);
		ArrayListStringBuffer buffer = new ArrayListStringBuffer();
		String[] nextLine;
		int lastAdded = 0;
		while ((nextLine = reader.readNext()) != null) {
			try {
				if (Integer.parseInt(nextLine[0]) != lastAdded) {
					buffer.add(nextLine);
					lastAdded = Integer.parseInt(nextLine[0]);
				}
			} catch (NumberFormatException e) {

			}
		}
		return buffer.process().getData();
	}

	public static void printSummary(String fullFileName) throws IOException {

		CSVReader reader = getReader(fullFileName);
		skipPreliminarSection(reader);
		String[] nextLine;
		int lastAdded = 0;
		while ((nextLine = reader.readNext()) != null) {
			if (Integer.parseInt(nextLine[0]) != lastAdded) {
				System.out.println(PrintCSV.printNice(nextLine));
				lastAdded = Integer.parseInt(nextLine[0]);
			}
		}
	}

	public static void printRun(String fullFileName, int run)
			throws IOException {
		int startingLine = (run - 1) * linesPerRun(fullFileName);
		CSVReader reader = getReader(fullFileName, startingLine);
		if (run == 1) {
			skipPreliminarSection(reader);
		}
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {

			if (Integer.parseInt(nextLine[0]) == run) {
				System.out.println(PrintCSV.printNice(nextLine));
			}
			if (Integer.parseInt(nextLine[0]) > run) {
				return;
			}
		}

	}

	/**
	 * Based on the first run
	 * 
	 * @param fullFileName
	 * @param run
	 * @param maxRuns
	 * @return
	 * @throws IOException
	 */
	public static int linesPerRun(String fullFileName) throws IOException {
		CSVReader reader = getReader(fullFileName);
		skipPreliminarSection(reader);
		String[] nextLine;
		int i = 0;
		while ((nextLine = reader.readNext()) != null) {
			if (Integer.parseInt(nextLine[0]) < 2) {
				i++;
			} else {
				return i;
			}
		}
		return i;
	}

	public static double[] getAverageRun(String fileName, int run)
			throws IOException {
		RealMatrixImpl matrix = getRunAsMatrix(fileName, run);
		return average(matrix);
	}

	public static double[] getAverageRun(String fileName, int run,
			int startingIndex, int endIndex) throws IOException {
		RealMatrixImpl matrix = getRunAsMatrix(fileName, run);
		return average(matrix, startingIndex, endIndex);

	}

	private static double[] average(RealMatrixImpl matrix, int startingIndex,
			int endIndex) {
		double[] ans = new double[matrix.getColumnDimension()];
		for (int i = 0; i < ans.length; i++) {
			DescriptiveStatisticsImpl statisticsColumnI = new DescriptiveStatisticsImpl();
			double[] columnI = matrix.getColumn(i);
			for (int j = startingIndex; j <= endIndex; j++) {
				statisticsColumnI.addValue(columnI[j]);
			}
			ans[i] = statisticsColumnI.getMean();
		}
		return ans;
	}

	private static double[] average(RealMatrixImpl matrix) {
		double[] ans = new double[matrix.getColumnDimension()];
		for (int i = 0; i < ans.length; i++) {
			DescriptiveStatisticsImpl statisticsColumnI = new DescriptiveStatisticsImpl();
			double[] columnI = matrix.getColumn(i);
			for (int j = 0; j < columnI.length; j++) {
				statisticsColumnI.addValue(columnI[j]);
			}
			ans[i] = statisticsColumnI.getMean();
		}
		return ans;
	}

	public static void printAverages(String fullFileName) throws IOException {
		CSVReader reader = getReader(fullFileName);
		skipPreliminarSection(reader);
		ArrayListStringBuffer buffer = new ArrayListStringBuffer();
		String[] nextLine;
		int run = 1;
		while ((nextLine = reader.readNext()) != null) {
			if (hasBlanks(nextLine)) {
				return;
			}
			
			if (Integer.parseInt(nextLine[0]) == run) {
				buffer.add(nextLine);
			}

			if (Integer.parseInt(nextLine[0]) > run) {
				run++;
				System.out.println(PrintCSV
						.printNice(average(buffer.process())));
				buffer.clear();
			}
		}
	}

	public static void printAverages(String fullFileName,
			int runsPerParameterSet) throws IOException {
		CSVReader reader = getReader(fullFileName);
		skipPreliminarSection(reader);
		ArrayListStringBuffer masterBuffer = new ArrayListStringBuffer();
		ArrayListStringBuffer buffer = new ArrayListStringBuffer();
		String[] nextLine;
		int run = 1;
		while ((nextLine = reader.readNext()) != null) {
			if (hasBlanks(nextLine)) {
				return;
			}
			
			if (Integer.parseInt(nextLine[0]) == run) {
				buffer.add(nextLine);
			}
			if (Integer.parseInt(nextLine[0]) > run) {
				run++;
				masterBuffer.add(transform(average(buffer.process())));
				buffer.clear();
			}
			if (masterBuffer.size() == runsPerParameterSet) {
				System.out.println(PrintCSV.printNice(average(masterBuffer
						.process())));
				masterBuffer.clear();
			}
		}
	}

	public static void printAverages(String fullFileName,
			int runsPerParameterSet, int startingIndex, int endingIndex)
			throws IOException {
		CSVReader reader = getReader(fullFileName);
		skipPreliminarSection(reader);
		ArrayListStringBuffer masterBuffer = new ArrayListStringBuffer();
		ArrayListStringBuffer buffer = new ArrayListStringBuffer();
		String[] nextLine;
		int run = 1;
		while ((nextLine = reader.readNext()) != null) {
			if (hasBlanks(nextLine)) {
				return;
			}
			if (Integer.parseInt(nextLine[0]) == run) {
				buffer.add(nextLine);
			}
			if (Integer.parseInt(nextLine[0]) > run) {
				run++;
				masterBuffer.add(transform(average(buffer.process(),
						startingIndex, endingIndex)));
				buffer.clear();
			}
			if (masterBuffer.size() == runsPerParameterSet) {
				System.out.println(PrintCSV.printNice(average(masterBuffer
						.process())));
				masterBuffer.clear();
			}
		}
	}

	private static String[] transform(double[] average) {
		String[] ans = new String[average.length];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = new Double(average[i]).toString();
		}
		return ans;
	}
	
	public static boolean hasBlanks(String[] average) {
		for (int i = 0; i < average.length; i++) {
			if (StringUtils.isBlank(average[i])) {
				return true;
			}
		}
		return false;
	}

	public static void printAverages(String fullFileName, int startingIndex,
			int endingIndex) throws IOException {
		CSVReader reader = getReader(fullFileName);
		skipPreliminarSection(reader);
		ArrayListStringBuffer buffer = new ArrayListStringBuffer();
		String[] nextLine;
		int run = 1;
		while ((nextLine = reader.readNext()) != null) {
			if (hasBlanks(nextLine)) {
				return;
			}
			if (Integer.parseInt(nextLine[0]) == run) {
				buffer.add(nextLine);
			}
			if (Integer.parseInt(nextLine[0]) > run) {
				run++;
				System.out.println(PrintCSV.printNice(average(buffer.process(),
						startingIndex, endingIndex)));
				buffer.clear();
			}
		}
	}
}
