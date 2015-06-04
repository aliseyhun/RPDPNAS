/**
 * 
 */
package nl.vu.feweb.evolution.models.mc.respast;

import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.stat.descriptive.SummaryStatisticsImpl;

import uchicago.src.sim.analysis.DataRecorder;
import uchicago.src.sim.engine.SimInit;


/**
 * @author jgarcia
 *
 */
public class RepastBatchAverage extends RepastGUI {

	/* (non-Javadoc)
	 * @see nl.vu.feweb.evolution.models.mc.respast.RepastGUI#buildModel()
	 */
	@Override
	public void buildModel() {
		this.setSeed(this.getRngSeed());
		super.buildModel();
	}

	int stopAt = 5000;
	
	
	/**
	 * @return the stopAt
	 */
	public int getStopAt() {
		return stopAt;
	}

	/**
	 * @param stopAt the stopAt to set
	 */
	public void setStopAt(int stopAt) {
		this.stopAt = stopAt;
	}

	public RepastBatchAverage() {
		super();
	}
	
	public String[] getInitParam() {
		ArrayList<String> params = new ArrayList<String>();
		CollectionUtils.addAll(params, super.getInitParam());
		String[] initParams = {"evolution", "representation","populationSize", "seed", "mutationProbability", "continuationProbability", "complexityCost", "R", "P", "S", "T", "UpdateGraphsEvery", "FileName", "Measure", "precisionMeasures", "computeComplexity", "stopAt", "W", "Assortment",  "perceptionMistakeProbability", "implementationMistakeProbability"};
		return initParams;
	}
	
	
	SummaryStatisticsImpl fitness = new SummaryStatisticsImpl();
	public double promedioFitness(){
		return fitness.getMean();
	}
	SummaryStatisticsImpl complexity = new SummaryStatisticsImpl();
	public double promedioComplexity(){
		return complexity.getMean();
	}
	SummaryStatisticsImpl cooperation = new SummaryStatisticsImpl();
	public double promedioCooperation(){
		return cooperation.getMean();
	}
	SummaryStatisticsImpl reciprocity = new SummaryStatisticsImpl();
	public double promedioReciprocity(){
		return reciprocity.getMean();
	}
	SummaryStatisticsImpl numberOfRules = new SummaryStatisticsImpl();
	public double promedioNumberOfRules(){
		return numberOfRules.getMean();
	}
	SummaryStatisticsImpl frequencyOfTheMostPopular = new SummaryStatisticsImpl();
	public double promedioFrequencyOfTheMostPopular(){
		return frequencyOfTheMostPopular.getMean();
	}

	
	public void addToAverage(){
		if (recording) {
			fitness.addValue(this.getTabAverageFitness());
			//"averageFitness", this, "getTabAverageFitness");
			if (computeComplexity) {
				//recorder.createNumericDataSource("averageComplexity", this, "getTabAverageFitness");
				complexity.addValue(this.getTabAverageComplexity());
			}
			if (measure != 0) {
				//recorder.createNumericDataSource("avgCoop", this, "getTabAverageCooperativeness");
				cooperation.addValue(getTabAverageCooperativeness());
				//recorder.createNumericDataSource("avgRec", this, "");
				reciprocity.addValue(getTabAverageReciprocity());
			}
			//recorder.createNumericDataSource("numberOfRules", this, "getTabNumberOfRules");
			numberOfRules.addValue(getTabNumberOfRules());
			//recorder.createNumericDataSource("frecMostPopular", this, "getTabFrequencyMostPopular");
			frequencyOfTheMostPopular.addValue(getTabFrequencyMostPopular());
			//recorder.createObjectDataSource("population", this, "getTabPopulationPrint");	
		}
	}
	
	
	
	protected void buildRecorder() {
		if (StringUtils.isEmpty(this.fileName)) {
			this.recording = false;
			return;
		}else{
			this.recording= true;
			recorder = new DataRecorder("./"+this.fileName, this);
			recorder.setDelimeter("\t");
			recorder.createNumericDataSource("averageFitness", this, "promedioFitness");
			if (computeComplexity) {
				recorder.createNumericDataSource("averageComplexity", this, "promedioComplexity");
			}
			if (measure != 0) {
				recorder.createNumericDataSource("avgCoop", this, "promedioCooperation");
				recorder.createNumericDataSource("avgRec", this, "promedioReciprocity");
			}
			recorder.createNumericDataSource("numberOfRules", this, "promedioNumberOfRules");
			recorder.createNumericDataSource("frecMostPopular", this, "promedioFrequencyOfTheMostPopular");
		}
	}
	
	
	@Override
	public void buildSchedule() {
		schedule.scheduleActionBeginning(0, this.evolutionAction());
		if (recording) {
			//write at the end
			schedule.scheduleActionAt(this.stopAt, recorder, "record");
			schedule.scheduleActionAt(this.stopAt, recorder, "write");
		}
		schedule.scheduleActionAt(this.stopAt, this, "stop");
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
		RepastBatchAverage model = new RepastBatchAverage();
		init.loadModel(model, args[0], true);

	}

	@Override
	public void step() {
		this.getSimulation().evaluateFitness();
		if (getTickCount() % updateGraphsEvery == 0) {
			this.computeValues();	
			if (recording) {
				addToAverage();
			}
		}
		this.getSimulation().evolutionStep();
		this.getSimulation().mutate();
	}

}
