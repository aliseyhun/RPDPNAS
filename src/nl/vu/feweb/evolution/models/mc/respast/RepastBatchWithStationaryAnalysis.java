package nl.vu.feweb.evolution.models.mc.respast;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;
import nl.uva.creed.evolution.impl.PopulationOperations;
import nl.vu.feweb.evolution.models.stationarity.StationarityAnalysis;

import org.apache.commons.lang.StringUtils;

import uchicago.src.sim.engine.SimInit;

public class RepastBatchWithStationaryAnalysis extends RepastBatch {

	protected StationarityAnalysis analysis;
	protected double minimumThreshold = 0.20;
	protected String stationaryFileName = "stationary.txt";
	
	
	/**
	 * @return the minimumThreshold
	 */
	public double getMinimumThreshold() {
		return minimumThreshold;
	}

	/**
	 * @param minimumThreshold the minimumThreshold to set
	 */
	public void setMinimumThreshold(double minimumThreshold) {
		this.minimumThreshold = minimumThreshold;
	}

	public RepastBatchWithStationaryAnalysis() {
		analysis = new StationarityAnalysis();
		this.computeComplexity = false;
		this.measure = 0;
	}
	
	

	public String[] getInitParam() {
		String[] initParams = {"evolution", "representation","populationSize", "seed", "mutationProbability", "continuationProbability", "complexityCost", "R", "P", "S", "T", "UpdateGraphsEvery", "FileName", "Measure", "precisionMeasures", "computeComplexity", "stopAt", "minimumThreshold", "stationaryFileName",  "perceptionMistakeProbability", "implementationMistakeProbability"};
		return initParams;
	}
	
	
	
	/* (non-Javadoc)
	 * @see nl.vu.feweb.evolution.models.mc.respast.RepastGUI#computeValues()
	 */
	@Override
	public void computeValues() {
	try {
			
			PopulationOperations operations = new PopulationOperations(this.getSimulation().getPopulation(), horizon, continuationProbability, this.measure, this.computeComplexity);
			this.tabAverageComplexity = operations.getAverageComplexity();
			this.tabAverageFitness = operations.getAverageFitness();
			this.tabNumberOfRules= operations.getNumberOfRules();
			this.tabFrecuenciesPrint = operations.getFrequenciesPrint();
			this.tabPopulationPrint = StringUtils.remove(operations.getFrequenciesPrint(), "\n");
			this.tabAverageCooperativeness = operations.averageCooperativeness();
			this.tabAverageReciprocity = operations.averageReciprocity();
			this.tabFrequencyMostPopular = operations.frequencyMostPopular();
			this.tabAveragePayOffPerInteraction = this.simulation.getAveragePayOffPerInteraction();
			this.tabMinimumPayOffPerInteraction = this.simulation.getMinimumPayOffPerInteraction();
			this.tabMaximumPayOffPerInteraction = this.simulation.getMaximumPayOffPerInteraction();
			this.analysis.add(StationarityAnalysis.populationStateWithMostRepresentedStrategies(operations.getFrequenciesTable(), minimumThreshold));
			
		} catch (UnnasignedFitnessException e) {
			e.printStackTrace();
			throw new RuntimeException("Error computing values", e);
		}
	}

	
	@Override
	public void stop() {
		this.writeOrAppendToFile(stationaryFileName, this.analysis.frequenciesTable());
		this.fireStopSim();
	}

	/**
	 * @return the stationaryFileName
	 */
	public String getStationaryFileName() {
		return stationaryFileName;
	}

	/**
	 * @param stationaryFileName the stationaryFileName to set
	 */
	public void setStationaryFileName(String stationaryFileName) {
		this.stationaryFileName = stationaryFileName;
	}

	
	
	private void writeOrAppendToFile(String fileName, String frequenciesTable) {
		 try {
		        BufferedWriter out = new BufferedWriter(new FileWriter(fileName,true));
		        out.write(frequenciesTable);
		        out.close();
		    } catch (IOException e) {
		    System.out.println(e);
		    }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimInit init = new SimInit();
		RepastBatchWithStationaryAnalysis model = new RepastBatchWithStationaryAnalysis();
		init.loadModel(model, "", false);

	}



}
