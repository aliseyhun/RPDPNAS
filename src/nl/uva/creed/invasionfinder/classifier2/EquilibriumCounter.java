package nl.uva.creed.invasionfinder.classifier2;

import java.util.ArrayList;
import java.util.Iterator;

import nl.uva.creed.invasionfinder.Invasion;
import nl.uva.creed.invasionfinder.PopulationState.Cardinality;

public class EquilibriumCounter {

	private int timeSpentEquilibrium = 0;
	private int timeSpentPossiblyEquilibrium = 0;
	private int timeSpentNotEquilibrium = 0;

	private int timePure = 0;
	private int timeTwo = 0;
	private int timeThree = 0;
	
	private int	timeSpentInMixedThatTurnedOutNotTobeEquilibrium = 0;
	
	public EquilibriumCounter(ArrayList<Invasion> invasions) {
		super();
		this.timeSpent(invasions);

	}
	
	@Override
	public String toString(){

		StringBuffer buffer = new StringBuffer();
		double allTime = (double)timePure+(double)timeTwo+(double)timeThree;
		int totalTime = timeSpentEquilibrium + timeSpentPossiblyEquilibrium + timeSpentNotEquilibrium;
		
		double ptimePureStates = (double)timePure/(allTime);
		buffer.append("Percentage of time in pure states " + ptimePureStates +"\n");
		
		double ptimeMixedStates = (double)timeTwo/(allTime);
		buffer.append("Percentage of time in 2-d states " + ptimeMixedStates  + "\n");
		
		double ptimeThreeStates = (double)timeThree/(allTime);
		buffer.append("Percentage of time in 3-d states " + ptimeThreeStates  + "\n");
		
		double minTimeInEq = (double)timeSpentEquilibrium/(double)totalTime;
		buffer.append("Minimum percentage of time in equilibrium " + minTimeInEq+ "\n");
		double maxTimeInEq = ((double)timeSpentEquilibrium + (double)timeSpentPossiblyEquilibrium- (double)timeSpentInMixedThatTurnedOutNotTobeEquilibrium)/(double)totalTime;
		buffer.append("Maximum percentage of time in equilibrium " + maxTimeInEq + "\n");
		
		return buffer.toString();
		
	}

	private void timeSpent(ArrayList<Invasion> invasions) {
		for (Iterator<Invasion> iterator = invasions.iterator(); iterator.hasNext();) {
			Invasion invasion = (Invasion) iterator.next();
			if (invasion.getPopulationState().getCardinality() == Cardinality.ONE_DIMENSION) {
				timePure = timePure + invasion.getPopulationState().getTimeSpent();
			}
			if (invasion.getPopulationState().getCardinality() == Cardinality.TWO_DIMENSION ) {
				timeTwo = timeTwo + invasion.getPopulationState().getTimeSpent();
			}
			if (invasion.getPopulationState().getCardinality() == Cardinality.THREE_DIMENSION ) {
				timeThree = timeThree + invasion.getPopulationState().getTimeSpent();
			}
			
			
			if (invasion.isEquilibrium()) {
				timeSpentEquilibrium = timeSpentEquilibrium+ invasion.getPopulationState().getTimeSpent();
			}else if (invasion.isPossiblyEquilibrium()) {
				timeSpentPossiblyEquilibrium = timeSpentPossiblyEquilibrium + invasion.getPopulationState().getTimeSpent();
			}else if (invasion.isNotEquilibrium()) {
				timeSpentNotEquilibrium = timeSpentNotEquilibrium + invasion.getPopulationState().getTimeSpent();
			}
		}
	}

	
}
