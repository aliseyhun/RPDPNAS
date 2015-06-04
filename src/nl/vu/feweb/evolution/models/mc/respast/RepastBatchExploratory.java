package nl.vu.feweb.evolution.models.mc.respast;

import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;
import nl.uva.creed.evolution.impl.PopulationOperations;

import org.apache.commons.lang.StringUtils;

import uchicago.src.sim.engine.SimInit;

public class RepastBatchExploratory extends RepastBatchWithStationaryAnalysis {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.vu.feweb.evolution.models.mc.respast.RepastBatchWithStationaryAnalysis
	 * #computeValues()
	 */
	@Override
	public void computeValues() {
		try {

			PopulationOperations operations = new PopulationOperations(this
					.getSimulation().getPopulation(), horizon,
					continuationProbability, this.measure,
					this.computeComplexity);
			this.tabAverageComplexity = operations.getAverageComplexity();
			this.tabAverageFitness = operations.getAverageFitness();
			this.tabNumberOfRules = operations.getNumberOfRules();
			this.tabFrecuenciesPrint = operations.getFrequenciesPrint();
			this.tabPopulationPrint = StringUtils.remove(operations
					.getFrequenciesPrint(), "\n");
			this.tabAverageCooperativeness = operations
					.averageCooperativeness();
			this.tabAverageReciprocity = operations.averageReciprocity();
			this.tabFrequencyMostPopular = operations.frequencyMostPopular();
			this.tabAveragePayOffPerInteraction = this.simulation
					.getAveragePayOffPerInteraction();
			this.tabMinimumPayOffPerInteraction = this.simulation
					.getMinimumPayOffPerInteraction();
			this.tabMaximumPayOffPerInteraction = this.simulation
					.getMaximumPayOffPerInteraction();

		} catch (UnnasignedFitnessException e) {
			e.printStackTrace();
			throw new RuntimeException("Error computing values", e);
		}
	}

	/* (non-Javadoc)
	 * @see nl.vu.feweb.evolution.models.mc.respast.RepastBatchWithStationaryAnalysis#getInitParam()
	 */
	@Override
	public String[] getInitParam() {
		String[] initParams = {"evolution", "representation","populationSize", "seed", "mutationProbability", "continuationProbability", "complexityCost", "R", "P", "S", "T", "UpdateGraphsEvery", "FileName", "Measure", "precisionMeasures", "computeComplexity", "stopAt",  "perceptionMistakeProbability", "implementationMistakeProbability"};
		return initParams;
	}

	/* (non-Javadoc)
	 * @see nl.vu.feweb.evolution.models.mc.respast.RepastBatchWithStationaryAnalysis#stop()
	 */
	@Override
	public void stop() {
		this.fireStopSim();
	}
	
	public static void main(String[] args) {
		SimInit init = new SimInit();
		RepastBatchExploratory model = new RepastBatchExploratory();
		init.loadModel(model,args[0], true);

	}

	/* (non-Javadoc)
	 * @see nl.vu.feweb.evolution.models.mc.respast.RepastBatch#buildSchedule()
	 */
	@Override
	public void buildSchedule() {
		schedule.scheduleActionBeginning(0, this.evolutionAction());
		if (recording) {
			if (this.updateGraphsEvery == 1) {
				schedule.scheduleActionBeginning(1, recorder, "write");
			}else{
				schedule.scheduleActionAtInterval(this.updateGraphsEvery, recorder, "write");
			}
			schedule.scheduleActionAtEnd(recorder, "writeToFile");	
		}
		schedule.scheduleActionAt(this.stopAt, this, "stop");
	}
	
	

}
