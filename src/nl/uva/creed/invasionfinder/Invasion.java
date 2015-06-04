package nl.uva.creed.invasionfinder;

import nl.uva.creed.invasionfinder.PopulationState.PopulationStateStatus;


public class Invasion {
	
	private Type type;
	private PopulationState populationState;
	
	
	
	public Invasion(PopulationState equilibrium, Type type) {
		super();
		this.populationState = equilibrium;
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public PopulationState getPopulationState() {
		return populationState;
	}

	public enum Type {
		KAPUT, //kaput
		ADV_NEW_DOMINATES_OLD, //ad
		ADVANTAGEOUS,//ad
		ADV_COORDINATION,//ad 
		ADV_STEPS_OVER_MIXED,//ad
		ADV_TO_MIXED,//ad
		WEAKLY_ADVANTAGEOUS,//ad
		DIS_COORDINATION, //dis
		DIS_OUT_OF_MIXED, //disOutMix
		DISADVANTEGOUS, //dis
		DIS_OLD_DOMINATES_NEW,//dis
		WEAKLY_DISADVANTAGEOUS, //dis
		NEUTRAL,  //neutral
		UNKNOWN_COORDINATION, //unknown
		UNKNOWN_STEPS_OVER_MIXED,//stepsOvMix 
		DARK_ZONE, //unknown
		WAIVER 	//neutral
	}
	
	
	public  boolean isDisadvantageous(){
		return this.type == Type.DIS_COORDINATION || this.type == Type.DIS_OLD_DOMINATES_NEW || 
		this.type == Type.DIS_OUT_OF_MIXED || this.type == Type.DISADVANTEGOUS || this.type == Type.WEAKLY_DISADVANTAGEOUS ;
	}
	
	
	
	public  boolean isDisadvantageousOutOfMixed(){
		return this.type == Type.DIS_OUT_OF_MIXED;
	}
	
	//TODO: Ojo assume WAIVER is NEUTRAL
	public  boolean isNeutral(){
		return this.type == Type.NEUTRAL || this.type == Type.WAIVER;
	}
	
	public boolean isUnknownStepsOverMixed() {
		return this.type == Type.UNKNOWN_STEPS_OVER_MIXED;
	}
	public  boolean isAdvantageous(){
		return this.type == Type.ADV_COORDINATION || this.type == Type.ADV_NEW_DOMINATES_OLD || 
		this.type == Type.ADV_STEPS_OVER_MIXED || this.type == Type.ADV_TO_MIXED || 
		this.type == Type.ADVANTAGEOUS || this.type == Type.WEAKLY_ADVANTAGEOUS;
	}
	
	

	
	public boolean isKaput(){
		return this.type == Type.KAPUT;
	}
	
	public boolean isDarkZone(){
		return this.type == Type.DARK_ZONE;
	}
	
	public boolean isUnknown(){
		return this.type == Type.UNKNOWN_COORDINATION;
	}
	
	
	public boolean isEquilibrium(){
		return this.getPopulationState().getStatus() == PopulationStateStatus.PURE_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.TWO_BOTH_NASH_MIX_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.THREE_ALL_NASH_MIX_NASH;
	}
	
	public boolean isNotEquilibrium(){
		return this.getPopulationState().getStatus() == PopulationStateStatus.PURE_NOT_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.TWO_BOTH_NOT_NASH_MIX_NOT_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.THREE_ALL_NASH_MIX_NOT_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.THREE_TWO_NASH_ONE_NOT_NASH_MIX_NOT_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.THREE_ONE_NASH_TWO_NOT_NASH_MIX_NOT_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.THREE_ALL_NOT_NASH_MIX_NOT_NASH;
	}
	
	public boolean isPossiblyEquilibrium(){
		return this.getPopulationState().getStatus() == PopulationStateStatus.TWO_ONE_NASH_ONE_NOT_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.TWO_BOTH_NOT_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.TWO_BOTH_NOT_NASH_MIX_COULD_BE_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.THREE_TWO_NASH_ONE_NOT_NASH ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.THREE_ALL_NOT_NASH_NEUTRAL ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.THREE_ALL_NOT_NASH_NEUTRAL ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.THREE_ALL_NOT_NASH_NEUTRAL ||
		 this.getPopulationState().getStatus() == PopulationStateStatus.THREE_ALL_NOT_NASH;
	}

	@Override
	public String toString() {
		return type + " / " + populationState.toString();
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setPopulationState(PopulationState populationState) {
		this.populationState = populationState;
	}

	
	
	
	
	

}
