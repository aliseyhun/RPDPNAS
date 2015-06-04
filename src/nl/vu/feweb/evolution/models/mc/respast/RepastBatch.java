/**
 * 
 */
package nl.vu.feweb.evolution.models.mc.respast;

import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uchicago.src.sim.engine.SimInit;

/**
 * @author jgarcia
 * 
 */
public class RepastBatch extends RepastGUI {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.vu.feweb.evolution.models.mc.respast.RepastGUI#buildModel()
	 */
	@Override
	public void buildModel() {
		this.setSeed(this.getRngSeed());
		super.buildModel();
	}

	int	stopAt	= 5000;

	/**
	 * @return the stopAt
	 */
	public int getStopAt() {
		return stopAt;
	}

	/**
	 * @param stopAt
	 *            the stopAt to set
	 */
	public void setStopAt(int stopAt) {
		this.stopAt = stopAt;
	}

	public RepastBatch() {
		super();
	}

	public RepastBatch(String initialPopulationFile) {
		super();
		this.initialPopulation = initialPopulationFile;
	}

	public String[] getInitParam() {
		ArrayList<String> params = new ArrayList<String>();
		CollectionUtils.addAll(params, super.getInitParam());
		String[] initParams = { "evolution", "representation",
				"populationSize", "seed", "mutationProbability",
				"continuationProbability", "complexityCost",  "R", "P", "S", "T",
				"UpdateGraphsEvery", "FileName", "Measure",
				"precisionMeasures", "computeComplexity", "stopAt", "W",
				"Assortment",  "perceptionMistakeProbability", "implementationMistakeProbability" };
		return initParams;
	}

	@Override
	public void buildSchedule() {
		schedule.scheduleActionBeginning(0, this.evolutionAction());
		if (recording) {
			schedule.scheduleActionAtInterval(updateGraphsEvery, this, "write");
			schedule.scheduleActionAtEnd(recorder, "writeToFile");
		}
		schedule.scheduleActionAt(this.stopAt, this, "stop");
	}

	public void record() {
		recorder.record();
	}

	public void write() {
		this.recorder.write();
	}

	@Override
	public void begin() {
		buildModel();
		buildRecorder();
		buildSchedule();
	}

	public void stop() {
		this.fireStopSim();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimInit init = new SimInit();
		RepastBatch model;
		/*if (StringUtils.isBlank(args[1])) {
			model = new RepastBatch();
		} else {*/
			model = new RepastBatch();
		/*}*/
		init.loadModel(model, args[0], true);

	}

}
