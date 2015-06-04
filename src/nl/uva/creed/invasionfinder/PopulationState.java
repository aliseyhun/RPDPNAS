package nl.uva.creed.invasionfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import nl.uva.creed.repeated.RepeatedGameStrategy;

import org.apache.commons.lang.StringUtils;

public class PopulationState {

	private int timeSpent = 0;
	
	public int getTimeSpent() {
		return timeSpent;
	}

	public PopulationState() {
		super();
	}

	public enum Cardinality {
		UNDEFINED, ONE_DIMENSION, TWO_DIMENSION, THREE_DIMENSION, DARK_ZONE
	}
	
	public int cardinality(){
		switch (this.cardinality) {
		case UNDEFINED: return -1;
		case ONE_DIMENSION: return 1;
		case TWO_DIMENSION: return 2;
		case THREE_DIMENSION: return 3;
		case DARK_ZONE: return 4;
		default: return -1;
		}
	}
	
	
	//This one corresponds to the colours
	public enum PopulationStateStatus {
		PURE_NASH, //Green
		PURE_NOT_NASH,  //Red
		
		TWO_BOTH_NASH_MIX_NASH,  //Green
		TWO_BOTH_NASH_MIX_NOT_NASH, //RED
		TWO_ONE_NASH_ONE_NOT_NASH, //Orange 
		TWO_ONE_NASH_ONE_NOT_NASH_MIX_NOT_NASH, //Red 
		
		
		TWO_BOTH_NOT_NASH, //Orange 
		TWO_BOTH_NOT_NASH_MIX_NOT_NASH, //RED
		TWO_BOTH_NOT_NASH_MIX_COULD_BE_NASH, //blue
		
		THREE_ALL_NASH_MIX_NASH, //GREEN
		THREE_ALL_NASH_MIX_NOT_NASH,// RED
		THREE_TWO_NASH_ONE_NOT_NASH, //ORANGE
		THREE_TWO_NASH_ONE_NOT_NASH_MIX_NOT_NASH, //RED
		THREE_ONE_NASH_TWO_NOT_NASH, //ORANGE
		THREE_ONE_NASH_TWO_NOT_NASH_MIX_NOT_NASH, // RED
		THREE_ALL_NOT_NASH_NEUTRAL, //ORANGE
		THREE_ALL_NOT_NASH_MIX_COULD_BE_NASH, // BLUE
		THREE_ALL_NOT_NASH, //ORANGE
		THREE_ALL_NOT_NASH_MIX_NOT_NASH, //RED
		
		HYPERDIMENSIONAL 
	}
	
	
	

	public Cardinality getCardinality() {
		return cardinality;
	}

	/*public PopulationState(boolean flag, Double frecMostPopular, Double frecSecondMostPopular, Double frecThirdMostPopular,
			ArrayList<Double> frequencies, Integer numberOfRules, Integer run,
			ArrayList<RepeatedGameStrategy> strategies, Integer tick, Cardinality type) {
		super();
		this.flag = flag;
		this.frecMostPopular = frecMostPopular;
		this.frecSecondMostPopular = frecSecondMostPopular;
		this.frecThirdMostPopular = frecThirdMostPopular;
		this.frequencies = frequencies;
		this.numberOfRules = numberOfRules;
		this.run = run;
		this.strategies = strategies;
		this.tick = tick;
		this.cardinality = type;
	}*/

	public void setCardinality(Cardinality type) {
		this.cardinality = type;
	}

	public ArrayList<RepeatedGameStrategy> getStrategies() {
		return strategies;
	}
	
	public HashSet<RepeatedGameStrategy> getStrategiesSet() {
		HashSet<RepeatedGameStrategy> ans = new HashSet<RepeatedGameStrategy>();
		for (int i = 0; i < strategies.size(); i++) {
			ans.add(strategies.get(i));
		}
		return ans;
	}
	
	public HashSet<RepeatedGameStrategy> getCoreStrategiesSet() {
		HashSet<RepeatedGameStrategy> ans = new HashSet<RepeatedGameStrategy>();
		for (int i = 0; i < this.cardinality(); i++) {
			ans.add(strategies.get(i));
		}
		return ans;
	}
	
	

	public void setStrategies(ArrayList<RepeatedGameStrategy> strategies) {
		this.strategies = strategies;
	}

	public Integer getRun() {
		return run;
	}

	public void setRun(Integer run) {
		this.run = run;
	}

	public Integer getTick() {
		return tick;
	}

	public void setTick(Integer tick) {
		this.tick = tick;
	}

	public Integer getNumberOfRules() {
		return numberOfRules;
	}

	public void setNumberOfRules(Integer numberOfRules) {
		this.numberOfRules = numberOfRules;
	}

	public Double getFrecMostPopular() {
		return frecMostPopular;
	}

	public void setFrecMostPopular(Double frecMostPopular) {
		this.frecMostPopular = frecMostPopular;
	}

	private ArrayList<RepeatedGameStrategy> strategies = new ArrayList<RepeatedGameStrategy>();
	private ArrayList<Double> frequencies = new ArrayList<Double>();
	public ArrayList<Double> getFrequencies() {
		return frequencies;
	}

	public void setFrequencies(ArrayList<Double> frequencies) {
		this.frequencies = frequencies;
	}

	private Integer run = null;
	private Integer tick = null;
	private Integer numberOfRules = null;
	private Double frecMostPopular = null;
	private Double frecSecondMostPopular = null;
	private Double frecThirdMostPopular = null;
	
	private Cardinality cardinality = null;
	private boolean flag = false;
	private PopulationStateStatus status = null;
	
	
	@Override
	public String toString() {
		return "Run: " + this.run + "/ Tick: " + this.tick + "/ Car: " + this.cardinality + "/ Status: " + this.status + " Pop:  " + this.nicePopPrint();  
	}

	private String nicePopPrint() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < strategies.size(); i++) {
			list.add(frequencies.get(i).toString() + " " + strategies.get(i).toString());
		}
		Collections.sort(list, new StringFrequencyStrategyComparator());
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		int i = list.size()-1;
		while (i>=0){
			buffer.append(list.get(i)+", ");
			i--;
		}
		buffer.append("]");
		return StringUtils.replace(buffer.toString(), "], ]", "]]");
	}

	public RepeatedGameStrategy getMostPopularStrategy() {
		int index = getIndexOfMostPopularStrategy();
		return strategies.get(index); 
	}
	
	private int getIndexOfMostPopularStrategy(){
		int index = 0;
		for (int i = 1; i < frequencies.size(); i++) {
			if (frequencies.get(i)> frequencies.get(index)) {
				index = i;
			}
		}
		return index; 
	}
	public RepeatedGameStrategy getSecondMostPopularStrategy() {
		int mostPopular = getIndexOfMostPopularStrategy();
		int i = 0;
		if (mostPopular ==0) {
			i = 1;
		}
		int secondMostPopular = i;
		while (i < frequencies.size()){
			if (i != mostPopular) {
				if (frequencies.get(i) > frequencies.get(secondMostPopular)) {
					secondMostPopular = i;
				}
			}
			i++;
		}
		return strategies.get(secondMostPopular); 
	}
	
	//OJO ASUMES ORDER!!!!!
	public RepeatedGameStrategy getThirdMostPopularStrategy() {
		return strategies.get(2);
	}
	
	public Double getFrequencyMostPopularStrategy() {
		int index = getIndexOfMostPopularStrategy();
		return frequencies.get(index); 
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void incrementTime() {
		timeSpent++;
	}

	public PopulationStateStatus getStatus() {
		return status;
	}

	public void setStatus(PopulationStateStatus status) {
		this.status = status;
	}

	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}

	public Double getFrecSecondMostPopular() {
		return frecSecondMostPopular;
	}

	public void setFrecSecondMostPopular(Double frecSecondMostPopular) {
		this.frecSecondMostPopular = frecSecondMostPopular;
	}

	public Double getFrecThirdMostPopular() {
		return frecThirdMostPopular;
	}

	public void setFrecThirdMostPopular(Double frecThirdMostPopular) {
		this.frecThirdMostPopular = frecThirdMostPopular;
	}

}
