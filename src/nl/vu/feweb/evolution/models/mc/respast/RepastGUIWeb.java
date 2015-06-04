package nl.vu.feweb.evolution.models.mc.respast;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;
import nl.uva.creed.evolution.impl.PopulationOperations;
import nl.uva.creed.evolution.impl.Simulation;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import uchicago.src.reflector.ListPropertyDescriptor;
import uchicago.src.sim.analysis.DataRecorder;
import uchicago.src.sim.analysis.DataSource;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.analysis.plot.OpenGraph;
import uchicago.src.sim.engine.AbstractGUIController;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimpleModel;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.TextDisplay;

public class RepastGUIWeb extends SimpleModel{


	protected int measure = 0;
	protected String initialPopulation = "";


	/**
	 * @return the frequencydependentMeasure
	 */
	public int getMeasure() {
		return measure;
	}
	/**
	 * @param frequencydependentMeasure the frequencydependentMeasure to set
	 */
	public void setMeasure(int measure) {
		this.measure = measure;
	}
	@SuppressWarnings("unchecked")
	public RepastGUIWeb() {
		super(); 
		Hashtable<Integer, String> h1 = new Hashtable<Integer, String>();
		//h1.put(new Integer(0), "String genome");
		h1.put(new Integer(1), "Regular expressions");
		h1.put(new Integer(2), "Finite automata");
		h1.put(new Integer(3), "Turing Machines");
		
		ListPropertyDescriptor pd = new ListPropertyDescriptor("Representation", h1);
		
		Hashtable<Integer, String> h2 = new Hashtable<Integer, String>();
		h2.put(new Integer(0), "Wright-Fisher process");
		h2.put(new Integer(1), "Moran process - mutation must be very small");
		ListPropertyDescriptor pd2 = new ListPropertyDescriptor("Evolution", h2);
		
		Hashtable<Integer, String> h3 = new Hashtable<Integer, String>();
		h3.put(new Integer(0), "No measures");
		h3.put(new Integer(1), "Frequency dependent - time consuming! ");
		h3.put(new Integer(2), "Absolute  - very time consuming!");
		ListPropertyDescriptor pd3 = new ListPropertyDescriptor("Measure", h3);
		
		descriptors.put("Representation", pd);
		descriptors.put("Evolution", pd2);
		descriptors.put("Measure", pd3);
	}
	
	protected Simulation simulation; 
	

	
	//key things
	protected Schedule schedule;
	// display
	

	

	
	protected OpenSequenceGraph averageFitness;
	protected OpenSequenceGraph measures;
	protected OpenSequenceGraph averageComplexity;
	protected OpenSequenceGraph numberOfRules;
	protected OpenSequenceGraph frecuencyMostPopular;
	
	protected DisplaySurface displaySurface; protected TextDisplay text; 

	protected int populationSize = 100;
	protected Long seed =  new Long(System.currentTimeMillis());
	protected double mutationProbability = 0.002;
	protected double continuationProbability = 0.9;
	protected double complexityCost = 0.0;
	protected double r = 3.0;
	protected double p = 1.0;
	protected double s = 0.0;
	protected double t = 4.0;
	protected double precisionMeasures = 0.01;
	protected int horizon; 
	protected int updateGraphsEvery = 1;
	protected DataRecorder recorder;
	protected boolean recording;



	protected int representation = 2;



	protected int evolution;



	protected double	w = 1.0;



	protected double	assortment;
	
	protected double	perceptionMistakeProbability = 0;
	protected double	implementationMistakeProbability = 0;
	
	
	
	
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	public OpenSequenceGraph getCurrentFrequencies() {
		return averageFitness;
	}
	/**
	 * @return the measures
	 */
	public OpenSequenceGraph getMeasures() {
		return measures;
	}
	/**
	 * @param measures the measures to set
	 */
	public void setMeasures(OpenSequenceGraph measures) {
		this.measures = measures;
	}
	public void setCurrentFrequencies(OpenSequenceGraph currentFrequencies) {
		this.averageFitness = currentFrequencies;
	}
	public OpenSequenceGraph getAverageComplexity() {
		return averageComplexity;
	}
	public void setAverageGroupSize(OpenSequenceGraph averageGroupSize) {
		this.averageComplexity = averageGroupSize;
	}
	public OpenSequenceGraph getAverageFrequencyAfterMixing() {
		return numberOfRules;
	}
	/**
	 * @return the frecuencyMostPopular
	 */
	public OpenSequenceGraph getFrecuencyMostPopular() {
		return frecuencyMostPopular;
	}
	/**
	 * @param frecuencyMostPopular the frecuencyMostPopular to set
	 */
	public void setFrecuencyMostPopular(OpenSequenceGraph frecuencyMostPopular) {
		this.frecuencyMostPopular = frecuencyMostPopular;
	}
	public void setAverageFrequencyAfterMixing(
			OpenSequenceGraph averageFrequencyAfterMixing) {
		this.numberOfRules = averageFrequencyAfterMixing;
	}
	public DisplaySurface getDisplaySurface() {
		return displaySurface;
	}
	public void setDisplaySurface(DisplaySurface displaySurface) {
		this.displaySurface = displaySurface;
	}
	public TextDisplay getText() {
		return text;
	}
	public void setText(TextDisplay text) {
		this.text = text;
	}
	
	
	public double getMutationProbability() {
		return mutationProbability;
	}
	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}
	
	
	//----------------------------------------------
	
	
	private void tearDownDisplay(OpenGraph openGraph){
		if (openGraph != null) {
			openGraph.dispose();
		}
		openGraph = null;
	}
	
	private void tearDownDisplay(DisplaySurface displaySurface){
		if (displaySurface != null) {
			displaySurface.dispose();
		}
		displaySurface = null;
	}
	
	public void setup() {
		
		recorder = null;
		
		schedule = new Schedule(1);
		// Tear down Displays
		
		tearDownDisplay(averageFitness);
		tearDownDisplay(measures);
		tearDownDisplay(averageComplexity);
		tearDownDisplay(numberOfRules);
		tearDownDisplay(displaySurface);
		tearDownDisplay(frecuencyMostPopular);
		
		// Create Displays
		averageFitness = new OpenSequenceGraph("Average fitness", this);
		averageComplexity = new OpenSequenceGraph("Average complexity", this);
		numberOfRules = new OpenSequenceGraph("Number of strategies", this);
		displaySurface = new DisplaySurface(new Dimension(250, 250), this, "Additional information");
		measures = new OpenSequenceGraph("Measures", this);
		frecuencyMostPopular = new OpenSequenceGraph("Frequency of the most popular strategy", this);
		
		// Register Displays
		this.registerMediaProducer("averageFitness", averageFitness);
		averageFitness.setYRange(0.0, 1.0);
		
		this.registerMediaProducer("measures", measures);
		averageFitness.setYRange(0.0, 1.0);
		
		this.registerMediaProducer("mostPopular", frecuencyMostPopular);
		frecuencyMostPopular.setYRange(0.0, 1.0);
		
		this.registerMediaProducer("averageComplexity", averageComplexity);
		averageComplexity.setYRange(0.0, 1.0);
		
		this.registerMediaProducer("numberOfRules", numberOfRules);
		numberOfRules.setYRange(0.0, 1.0);
				
		this.registerMediaProducer("AdditionalInformation", displaySurface);
		
		

		
	}
	
	public void begin() { 
		buildModel();
		buildRecorder();
		buildSchedule();
		buildDisplay();
		averageFitness.display();
		if (measure != 0) {
			measures.display();
		}
		if (computeComplexity) {
			averageComplexity.display();
		}
		
		averageFitness.setLocation(50, 100);
		frecuencyMostPopular.setLocation(50, 600);
		numberOfRules.setLocation(500, 100);
		displaySurface.setLocation(500, 600);
		displaySurface.setSize(new Dimension(100,10));
		displaySurface.display();
		numberOfRules.display();
		frecuencyMostPopular.display();
		
		
		
	}
	
	protected void buildRecorder() {
		if (StringUtils.isEmpty(this.fileName)) {
			this.recording = false;
			return;
		}else{
			this.recording= true;
			// recorder uses the compute* methods as sources for its data
			recorder = new DataRecorder("./"+this.fileName, this);
			recorder.setDelimeter("\t");
			// As of Repast 1.3 this is just as fast as creating your own
			// inner classes. (If this means nothing to you, don't worry about it).
			//recorder.createNumericDataSource("gen", this, "getTabGeneration", -1, 0);
			recorder.createNumericDataSource("averageFitness", this, "getTabAverageFitness");
			if (computeComplexity) {
				recorder.createNumericDataSource("averageComplexity", this, "getTabAverageComplexity");
			}
			if (measure != 0) {
				recorder.createNumericDataSource("avgCoop", this, "getTabAverageCooperativeness");
				recorder.createNumericDataSource("avgRec", this, "getTabAverageReciprocity");
			}
			recorder.createNumericDataSource("numberOfRules", this, "getTabNumberOfRules");
			recorder.createNumericDataSource("frecMostPopular", this, "getTabFrequencyMostPopular");
			recorder.createObjectDataSource("population", this, "getTabPopulationPrint");
			
			
			
		}
	}

	
	
	/**
	 * @return the tabAverageCooperativeness
	 */
	public double getTabAverageCooperativeness() {
		return tabAverageCooperativeness;
	}
	/**
	 * @return the tabAverageReciprocity
	 */
	public double getTabAverageReciprocity() {
		return tabAverageReciprocity;
	}
	protected Sequence averageFitness() {

		class averageFitness implements DataSource, Sequence {

			public Object execute() {
				return new Double(getSValue());
			}
			public double getSValue() {
				return  tabAverageFitness;
			}
		}
		return new averageFitness();
	}
	
	protected Sequence averagePayOffPerInteraction() {

		class averagePayOffPerInteraction implements DataSource, Sequence {

			public Object execute() {
				return new Double(getSValue());
			}
			public double getSValue() {
				return  tabAveragePayOffPerInteraction;
			}
		}
		return new averagePayOffPerInteraction();
	}
	
	protected Sequence minimumPayOffPerInteraction() {

		class minimumPayOffPerInteraction implements DataSource, Sequence {

			public Object execute() {
				return new Double(getSValue());
			}
			public double getSValue() {
				return  tabMinimumPayOffPerInteraction;
			}
		}
		return new minimumPayOffPerInteraction();
	}
	
	protected Sequence maximumPayOffPerInteraction() {

		class maximumPayOffPerInteraction implements DataSource, Sequence {

			public Object execute() {
				return new Double(getSValue());
			}
			public double getSValue() {
				return  tabMaximumPayOffPerInteraction;
			}
		}
		return new maximumPayOffPerInteraction();
	}
	
	protected Sequence averageComplexity() {

		class averageComplexity implements DataSource, Sequence {

			public Object execute() {
				return new Double(getSValue());
			}
			public double getSValue() {
				return  tabAverageComplexity;
			}
		}
		return new averageComplexity();
	}
	
	protected Sequence numberOfRules() {

		class numberOfRules implements DataSource, Sequence {

			public Object execute() {
				return new Double(getSValue());
			}
			public double getSValue() {
				return  tabNumberOfRules;
			}
		}
		return new numberOfRules();
	}
	
	protected Sequence frequencyMostPopular() {

		class frequencyMostPopular implements DataSource, Sequence {

			public Object execute() {
				return new Double(getSValue());
			}
			public double getSValue() {
				return  tabFrequencyMostPopular;
			}
		}
		return new frequencyMostPopular();
	}
	protected Sequence averageReciprocity() {

		class averageReciprocity implements DataSource, Sequence {

			public Object execute() {
				return new Double(getSValue());
			}
			public double getSValue() {
				return  tabAverageReciprocity;
			}
		}
		return new averageReciprocity();
	}
	protected Sequence averageCooperativeness() {

		class averageCooperativeness implements DataSource, Sequence {

			public Object execute() {
				return new Double(getSValue());
			}
			public double getSValue() {
				return  tabAverageCooperativeness;
			}
		}
		return new averageCooperativeness();
	}
	protected void buildDisplay() {
		averageFitness.addSequence("Average fitness",this.averageFitness(), Color.blue);
		
		if (computeComplexity) {
			averageComplexity.addSequence("Average complexity", this.averageComplexity(), Color.red);
		}
		
		numberOfRules.addSequence("Number of rules",this.numberOfRules(), Color.black);
		
		frecuencyMostPopular.addSequence("frec. most pop", this.frequencyMostPopular(), Color.darkGray);
		
		if (measure != 0) {
			measures.addSequence("Average coop", this.averageCooperativeness(), Color.red);
			measures.addSequence("Average reciprocity", this.averageReciprocity(), Color.green);	
		}
		
		
		text = new TextDisplay(400, 400, 5, 5, Color.green);
		displaySurface.setBackground(Color.black);
		displaySurface.addDisplayable(text, "info", 1);
		frecuencyMostPopular.setYAutoExpand(false);
		frecuencyMostPopular.setYRange(0.0, 1.0);
	}

	public void buildSchedule() {
		schedule.scheduleActionBeginning(0, this.evolutionAction());
		
		schedule.scheduleActionAtInterval(updateGraphsEvery, new UpdateGraphs());
		schedule.scheduleActionAtInterval(updateGraphsEvery, new updateTextAction());
		
		if (!StringUtils.isEmpty(this.fileName)) {
			schedule.scheduleActionAtEnd(recorder, "writeToFile");	
		}
	}
	
	class UpdateGraphs extends BasicAction {
		public void execute() {
			
			averageFitness.step();
			if (computeComplexity) {
				averageComplexity.step();
			}
			if (measure !=0) {
				measures.step();
			}
			numberOfRules.step();
			frecuencyMostPopular.step();
		}
	} 
	
	class updateTextAction extends BasicAction{ 
		
		public void execute() {
			text.clearLines();
			text.addLine(tabFrecuenciesPrint);
			displaySurface.updateDisplay();
		}
	}
	
	public String getName() {
		return "Evolution of cooperation: repeated games and population structure";
	}
	
	public void buildModel() {
		super.buildModel();
		try {
		if (!StringUtils.isBlank(this.initialPopulation)) {
		File file = new File(this.initialPopulation);
		this.initialPopulation = (String) FileUtils.readLines(file).get(0);
		}
		
		this.horizon = new Double(Math.log(precisionMeasures)/Math.log(continuationProbability)).intValue();
		
			this.simulation = new Simulation(this.populationSize, this.seed,
					this.mutationProbability, this.continuationProbability, this.complexityCost,
					this.r, this.p, this.s, this.t, this.representation, this.evolution, this.w, this.assortment, this.initialPopulation,  this.perceptionMistakeProbability, this.implementationMistakeProbability);
		} catch (InvalidStrategyException e) {
			e.printStackTrace();
			throw new RuntimeException("Error building model" , e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new RuntimeException("Error building model" , e);
		} catch (UnrecognizableString e) {
			e.printStackTrace();
			throw new RuntimeException("Error building model" , e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error building model" , e);
		} 
		
	}
	
	/**
	 * @return the precisionMeasures
	 */
	public double getPrecisionMeasures() {
		return precisionMeasures;
	}
	/**
	 * @param precisionMeasures the precisionMeasures to set
	 */
	public void setPrecisionMeasures(double precisionMeasures) {
		this.precisionMeasures = precisionMeasures;
	}
	protected BasicAction evolutionAction() {

		class EvolutionStep extends BasicAction {
			public void execute() {
				try {
					step();
					
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return new EvolutionStep();
	}
	public void step() {
		this.getSimulation().evaluateFitness();
		if (getTickCount() % updateGraphsEvery == 0) {
			this.computeValues();	
			if (recording) {
				recorder.record();
			}
		}
		this.getSimulation().evolutionStep();
		this.getSimulation().mutate();
	}
	
	public String[] getInitParam() {
		String[] initParams = {"assortment", "representation","populationSize", "seed", "mutationProbability", "continuationProbability", "complexityCost","R", "P", "S", "T", "Measure", "precisionMeasures", "perceptionMistakeProbability", "implementationMistakeProbability"};
		return initParams;
	}
	public static void main(String[] args) {
		AbstractGUIController.CONSOLE_ERR = false;
		AbstractGUIController.CONSOLE_OUT = false;
		SimInit init = new SimInit();
		RepastGUIWeb model = new RepastGUIWeb();
		init.loadModel(model, "", false);
	}
	public int getUpdateGraphsEvery() {
		return updateGraphsEvery;
	}
	public void setUpdateGraphsEvery(int updateGraphsEvery) {
		this.updateGraphsEvery = updateGraphsEvery;
	}
	///----- VARIABLES FOR WRITING IN FILES
	protected double tabGeneration=0;
	protected double tabAverageFitness;
	protected double tabAverageComplexity;
	protected double tabNumberOfRules;
	protected double tabAveragePayOffPerInteraction=0;
	protected double tabMinimumPayOffPerInteraction=0;
	protected double tabMaximumPayOffPerInteraction=0;
	protected double tabAverageCooperativeness;
	protected double tabAverageReciprocity;
	protected double tabFrequencyMostPopular;
	/**
	 * @return the tabFrequencyMostPopular
	 */
	public double getTabFrequencyMostPopular() {
		return tabFrequencyMostPopular;
	}

	protected String tabFrecuenciesPrint = "";
	protected String fileName = "";
	protected String tabPopulationPrint;



	protected boolean computeComplexity = false;
	
	public void computeValues(){
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
		} catch (UnnasignedFitnessException e) {
			e.printStackTrace();
			throw new RuntimeException("Error computing values", e);
		}
	}
	/**
	 * @return the computeComplexity
	 */
	public boolean isComputeComplexity() {
		return computeComplexity;
	}
	/**
	 * @param computeComplexity the computeComplexity to set
	 */
	public void setComputeComplexity(boolean computeComplexity) {
		this.computeComplexity = computeComplexity;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Simulation getSimulation() {
		return simulation;
	}
	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}
	public int getPopulationSize() {
		return populationSize;
	}
	public void setPopulationSize(int populationSize) {
		//add one if population size is odd
		this.populationSize = populationSize;
		if (this.populationSize%2!=0) {
			this.populationSize++;
		}
	}
	public Long getSeed() {
		return seed;
	}
	public void setSeed(Long seed) {
		this.seed = seed;
	}
	public double getContinuationProbability() {
		return continuationProbability;
	}
	public void setContinuationProbability(double continuationProbability) {
		this.continuationProbability = continuationProbability;
	}
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	public double getS() {
		return s;
	}
	public void setS(double s) {
		this.s = s;
	}
	public double getT() {
		return t;
	}
	public void setT(double t) {
		this.t = t;
	}
	public double getTabGeneration() {
		return tabGeneration;
	}
	public void setTabGeneration(double tabGeneration) {
		this.tabGeneration = tabGeneration;
	}
	public double getTabAverageFitness() {
		return tabAverageFitness;
	}
	public String getTabPopulationPrint() {
		return tabPopulationPrint;
	}
	public void setTabAverageFitness(double tabAverageFitness) {
		this.tabAverageFitness = tabAverageFitness;
	}
	
	/**
	 * @return the tabAverageComplexity
	 */
	public double getTabAverageComplexity() {
		return tabAverageComplexity;
	}
	/**
	 * @param tabAverageComplexity the tabAverageComplexity to set
	 */
	public void setTabAverageComplexity(double tabAverageComplexity) {
		this.tabAverageComplexity = tabAverageComplexity;
	}
	public double getTabNumberOfRules() {
		return tabNumberOfRules;
	}
	public void setTabNumberOfRules(double tabNumberOfRules) {
		this.tabNumberOfRules = tabNumberOfRules;
	}
	public double getTabAveragePayOffPerInteraction() {
		return tabAveragePayOffPerInteraction;
	}
	public double getTabMinimumPayOffPerInteraction() {
		return tabMinimumPayOffPerInteraction;
	}
	public double getTabMaximumPayOffPerInteraction() {
		return tabMaximumPayOffPerInteraction;
	}
	public void setTabPopulationPrint(String tabPopulationPrint) {
	    this.tabPopulationPrint = tabPopulationPrint;
	}
	public int getRepresentation() {
	    return representation;
	}
	public void setRepresentation(int representation) {
	    this.representation = representation;
	}
	public int getEvolution() {
		return evolution;
	}
	public void setEvolution(int evolver) {
		this.evolution = evolver;
	}
	public double getW() {
		return w;
	}
	public void setW(double w) {
		this.w = w;
	}
	public double getAssortment() {
		return assortment;
	}
	public void setAssortment(double relatedness) {
		this.assortment = relatedness;
	}
	public double getPerceptionMistakeProbability() {
		return perceptionMistakeProbability;
	}
	public void setPerceptionMistakeProbability(double perceptionMistakeProbability) {
		this.perceptionMistakeProbability = perceptionMistakeProbability;
	}
	public double getImplementationMistakeProbability() {
		return implementationMistakeProbability;
	}
	public void setImplementationMistakeProbability(
			double implementationMistakeProbability) {
		this.implementationMistakeProbability = implementationMistakeProbability;
	}
	public double getComplexityCost() {
		return complexityCost;
	}
	public void setComplexityCost(double complexityCost) {
		this.complexityCost = complexityCost;
	}
	
}
