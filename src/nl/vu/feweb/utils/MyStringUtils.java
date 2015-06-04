package nl.vu.feweb.utils;

import nl.vu.feweb.simulation.PseudoRandomSequence;
import nl.vu.feweb.simulation.SimulationUtils;

public class MyStringUtils {

	public static final String randomString(String[] alphabet, double probabilityAddOneMore) {
		while (true) {
			StringBuffer ansBuffer = new StringBuffer();
			int index = PseudoRandomSequence.nextInt(alphabet.length);
			ansBuffer.append(alphabet[index]);
			while (SimulationUtils.bernoullTrial(probabilityAddOneMore)) {
				index = PseudoRandomSequence.nextInt(alphabet.length);
				ansBuffer.append(alphabet[index]);
				return ansBuffer.toString();
			}
		}

	}
}
