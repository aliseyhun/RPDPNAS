package nl.uva.creed.invasionfinder;

import java.util.ArrayList;
import java.util.Iterator;

import nl.uva.creed.invasionfinder.PopulationState.Cardinality;
import nl.uva.creed.invasionfinder.PopulationState.PopulationStateStatus;

public class NewClassifier {

	private int	badNewsDisadvatageous			= 0;
	private int	kaputNews						= 0;
	private int	darkZoneNews						= 0;
	private int	unknownNews						= 0;
	
	private int	goodNews						= 0;
	
	private int	badNewsDisadvatageousOutOfMixed	= 0;
	private int	kaputOutOfMixed			= 0;
	private int	darkZoneOutOfMixed			= 0;
	private int	unknownOutOfMixed			= 0;
	private int	goodNewsOutOfMixed				= 0;
	private int	veryBadNewsDisadvantageous		= 0;
	private int	mixedThatTurnedOutNotToBeEq		= 0;
	
	
	private int	badNewsDisadvatageousOutOfThreeDimensions	= 0;
	private int	kaputOutOfThreeDimensions			= 0;
	private int	darkZoneOutOfThreeDimensions			= 0;
	private int	unknownOutOfThreeDimensions			= 0;
	private int	goodNewsOutOfThreeDimensions			= 0;
	private int	veryBadNewsDisadvantageousThreeDimensions		= 0;
	

	
	private int candidatesIndirectPure = 0;
	private int candidatesIndirectMixed = 0;
	private int candidatesIndirectThreeDimensions = 0;
	
	
	private int timeSpentEquilibrium = 0;
	private int timeSpentPossiblyEquilibrium = 0;
	private int timeSpentNotEquilibrium = 0;

	private int timePure = 0;
	private int timeMixed = 0;
	private int	timeSpentInMixedThatTurnedOutNotTobeEquilibrium = 0;
	
	public NewClassifier(ArrayList<Invasion> invasions) {
		super();
		this.classify(invasions);
		this.timeSpent(invasions);

	}
	
	public String summary(){

		StringBuffer buffer = new StringBuffer();
		int totalTime = timeSpentEquilibrium + timeSpentPossiblyEquilibrium + timeSpentNotEquilibrium;
		
		double ptimePureStates = (double)timePure/((double)timePure+(double)timeMixed);
		buffer.append("Percentage of time in pure states " + ptimePureStates +"\n");
		
		double ptimeMixedStates = (double)timeMixed/((double)timePure+(double)timeMixed);
		buffer.append("Percentage of time in mixed states " + ptimeMixedStates  + "\n");
		
		double minTimeInEq = (double)timeSpentEquilibrium/(double)totalTime;
		buffer.append("Minimum percentage of time in equilibrium " + minTimeInEq+ "\n");
		double maxTimeInEq = ((double)timeSpentEquilibrium + (double)timeSpentPossiblyEquilibrium- (double)timeSpentInMixedThatTurnedOutNotTobeEquilibrium)/(double)totalTime;
		buffer.append("Maximum percentage of time in equilibrium " + maxTimeInEq + "\n");
		
		buffer.append("--------------------------\n");
		
		double shareIndirectInvasionsOutOfPure = ((double)goodNews)/(double)candidatesIndirectPure;
		buffer.append("Share of indirect invasions out of pure " + shareIndirectInvasionsOutOfPure+ "\n");
		double shareKaputOutofPure = (double)kaputNews/(double)candidatesIndirectPure;
		buffer.append("Share of kaput out of pure  " +shareKaputOutofPure + "\n");
		double shareUnknownOutofPure = (double)unknownNews/(double)candidatesIndirectPure;
		buffer.append("Share of unknown out of pure  " +shareUnknownOutofPure + "\n");
		double shareDarkZoneOutofPure = (double)darkZoneNews/(double)candidatesIndirectPure;
		buffer.append("Share of dark zone out of pure  " +shareDarkZoneOutofPure + "\n");
		double shareBadOutOfPure = (double)badNewsDisadvatageous/(double)candidatesIndirectPure;
		buffer.append("Share of bad out of pure  " +shareBadOutOfPure + "\n");
		
		buffer.append("Number of candidates pure " + candidatesIndirectPure + "\n");
		buffer.append("--------------------------\n");
		
		
		double minShareIndirectInvasionsOutOfMixed = ((double)goodNewsOutOfMixed)/(double)candidatesIndirectMixed;
		buffer.append("Minimum share of indirect invasions out of mixed  " + minShareIndirectInvasionsOutOfMixed + "\n");
		
		
		double maxShareIndirectInvasionsOutOfMixed = ((double)goodNewsOutOfMixed+ (double)unknownOutOfMixed)/(double)candidatesIndirectMixed;
		buffer.append("Maximum share of indirect invasions out of mixed  " + maxShareIndirectInvasionsOutOfMixed + "\n");
		
		double darkZoneOutOfMixedC = ((double)darkZoneOutOfMixed)/(double)candidatesIndirectMixed;
		buffer.append("Dark zone follows out of mixed  " + darkZoneOutOfMixedC + "\n");
		
		double kaputOutOfMixedC = ((double)kaputOutOfMixed)/(double)candidatesIndirectMixed;
		buffer.append("Kaput out of mixed  " + kaputOutOfMixedC + "\n");
		
		
		double shareFixationOutOfMixed = ((double)badNewsDisadvatageousOutOfMixed)/(double)candidatesIndirectMixed;
		buffer.append("Share of fixation out of mixed " + shareFixationOutOfMixed  + "\n");
		double shareBadNewsOutOfMixed = ((double)veryBadNewsDisadvantageous)/(double)candidatesIndirectMixed; 
		buffer.append("Share of bad news out of mixed " +shareBadNewsOutOfMixed + "\n");
		buffer.append("Number of candidates mixed " + candidatesIndirectMixed + "\n");
		
		buffer.append("--------------------------\n");
	
		
		double minShareIndirectInvasionsOutOfThree = ((double)goodNewsOutOfThreeDimensions)/(double)candidatesIndirectThreeDimensions;
		buffer.append("Minimum share of indirect invasions out of three  " + minShareIndirectInvasionsOutOfThree + "\n");
		
		
		double maxShareIndirectInvasionsOutOfThree = ((double)goodNewsOutOfThreeDimensions+ (double)unknownOutOfThreeDimensions)/(double)candidatesIndirectThreeDimensions;
		buffer.append("Maximum share of indirect invasions out of three  " + maxShareIndirectInvasionsOutOfThree + "\n");
		
		
		double darkZoneOutOfThreeC = ((double)darkZoneOutOfThreeDimensions)/(double)candidatesIndirectThreeDimensions;
		buffer.append("Dark zone follows out of mixed  " + darkZoneOutOfThreeC + "\n");
		
		double kaputOutOfThreeC = ((double)kaputOutOfThreeDimensions)/(double)candidatesIndirectThreeDimensions;
		buffer.append("Kaput out of mixed  " + kaputOutOfThreeC + "\n");
		
		
		double shareFixationOutOfThree = ((double)badNewsDisadvatageousOutOfThreeDimensions)/(double)candidatesIndirectThreeDimensions;
		buffer.append("Share of fixation out of three " + shareFixationOutOfThree  + "\n");
		double shareBadNewsOutOfThree = ((double)veryBadNewsDisadvantageousThreeDimensions)/(double)candidatesIndirectThreeDimensions; 
		buffer.append("Share of bad news out of three " +shareBadNewsOutOfThree + "\n");
		buffer.append("Number of candidates three " + candidatesIndirectThreeDimensions + "\n");
	
		
		
		return buffer.toString();
		
	}

	private void timeSpent(ArrayList<Invasion> invasions) {
		for (Iterator<Invasion> iterator = invasions.iterator(); iterator.hasNext();) {
			Invasion invasion = (Invasion) iterator.next();
			if (invasion.getPopulationState().getCardinality() == Cardinality.ONE_DIMENSION) {
				timePure = timePure + invasion.getPopulationState().getTimeSpent();
			}
			if (invasion.getPopulationState().getCardinality() == Cardinality.TWO_DIMENSION || 
					invasion.getPopulationState().getCardinality() == Cardinality.THREE_DIMENSION ) {
				timeMixed = timeMixed + invasion.getPopulationState().getTimeSpent();
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

	public void classify(ArrayList<Invasion> invasions) {
		classifyDepartingFromPureEquilibria(invasions);
		classifyDepartingFromMixedCouldBeEquilibria(invasions);
		classifyDepartingFromThreeDimensionalCouldBeEquilibria(invasions);

	}

	private void classifyDepartingFromMixedCouldBeEquilibria(
			ArrayList<Invasion> invasions) {
		int focal = 0;
		focal = findNextCouldBeMixedEqTwoDimensions(invasions, focal);

		while (focal >= 0) {
			// CASE 3
			if (invasions.get(focal).isAdvantageous()) {
				timeSpentInMixedThatTurnedOutNotTobeEquilibrium  = timeSpentInMixedThatTurnedOutNotTobeEquilibrium + invasions.get(focal).getPopulationState().getTimeSpent();
				focal = findNextCouldBeMixedEqTwoDimensions(invasions, focal);
				mixedThatTurnedOutNotToBeEq++;

			} else {
				int neutralMutantsThatFollow = numberOfNeutralMutantsThatFollowDepartingFromMixed(
						invasions, focal);
				if (focal + neutralMutantsThatFollow >= invasions.size()) {
					focal = -1;
					break;
				}

				// CASE 1
				candidatesIndirectMixed++;
				if (invasions.get(focal + neutralMutantsThatFollow)
						.isDisadvantageousOutOfMixed()) {
					badNewsDisadvatageousOutOfMixed++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isDisadvantageous()) { // CASE 2
					// neutralMutantsThatFollow >0
					veryBadNewsDisadvantageous++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isKaput()) { 
					kaputOutOfMixed++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isDarkZone()) { 
					darkZoneOutOfMixed++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isUnknown()) { 
					unknownOutOfMixed++;
				} else if (neutralMutantsThatFollow > 0
						&& invasions.get(focal + neutralMutantsThatFollow)
								.isAdvantageous()) {
					goodNewsOutOfMixed++;
				} else {
					throw new RuntimeException("Uncoded case?");
				}
				focal = findNextCouldBeMixedEqTwoDimensions(invasions, focal+neutralMutantsThatFollow);
			}
		}
	}
	
	
	private void classifyDepartingFromThreeDimensionalCouldBeEquilibria(
			ArrayList<Invasion> invasions) {
		int focal = 0;
		focal = findNextCouldBeMixedEqThreeDimensions(invasions, focal);

		while (focal >= 0) {
			// CASE 3
			if (invasions.get(focal).isAdvantageous()) {
				timeSpentInMixedThatTurnedOutNotTobeEquilibrium  = timeSpentInMixedThatTurnedOutNotTobeEquilibrium + invasions.get(focal).getPopulationState().getTimeSpent();
				focal = findNextCouldBeMixedEqThreeDimensions(invasions, focal);
				mixedThatTurnedOutNotToBeEq++;

			} else {
				int neutralMutantsThatFollow = numberOfNeutralMutantsThatFollowDepartingFromMixed(
						invasions, focal);
				if (focal + neutralMutantsThatFollow >= invasions.size()) {
					focal = -1;
					break;
				}

				// CASE 1
				candidatesIndirectThreeDimensions++;
				if (invasions.get(focal + neutralMutantsThatFollow)
						.isDisadvantageousOutOfMixed()) {
					badNewsDisadvatageousOutOfThreeDimensions++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isDisadvantageous()) { // CASE 2
					// neutralMutantsThatFollow >0
					veryBadNewsDisadvantageousThreeDimensions++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isKaput()) { 
					kaputOutOfThreeDimensions++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isDarkZone()) { 
					darkZoneOutOfThreeDimensions++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isUnknown()) { 
					unknownOutOfThreeDimensions++;
				} else if (neutralMutantsThatFollow > 0
						&& invasions.get(focal + neutralMutantsThatFollow)
								.isAdvantageous()) {
					goodNewsOutOfThreeDimensions++;
				} else {
					throw new RuntimeException("Uncoded case?");
				}
				focal = findNextCouldBeMixedEqThreeDimensions(invasions, focal+neutralMutantsThatFollow);
			}
		}
	}
	
	

	private void classifyDepartingFromPureEquilibria(
			ArrayList<Invasion> invasions) {

		int focal = 0;
		if (invasions.get(0).getPopulationState().getStatus() != PopulationStateStatus.PURE_NASH) {
			throw new RuntimeException("First state should be Nash");
		}

		while (focal >= 0) {
			// CASE 3
			if (invasions.get(focal).isAdvantageous()) {
				throw new RuntimeException("Error in Nash checker at "+ invasions.get(focal).getPopulationState().getTick());
			} else {
				int neutralMutantsThatFollow = numberOfNeutralMutantsThatFollowDepartingFromPure(
						invasions, focal);
				if (focal + neutralMutantsThatFollow >= invasions.size()) {
					focal = -1;
					break;
				}

				// CASE 1
				candidatesIndirectPure++;
				if (invasions.get(focal + neutralMutantsThatFollow)
						.isDisadvantageous()) {
					badNewsDisadvatageous++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isKaput()) { 
					kaputNews++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isUnknown()) { 
					unknownNews++;
				} else if (invasions.get(focal + neutralMutantsThatFollow)
						.isDarkZone()) { 
					darkZoneNews++;
				} else if (neutralMutantsThatFollow > 0
						&& invasions.get(focal + neutralMutantsThatFollow)
								.isAdvantageous()) {
					goodNews++;
				} else {
					throw new RuntimeException("Uncoded case?");
				}
				focal = findNextPure(invasions, focal+neutralMutantsThatFollow);
			}

			
		}

	}

	private static int numberOfNeutralMutantsThatFollowDepartingFromPure(
			ArrayList<Invasion> invasions, int nextPureEquilibria) {
		int i = nextPureEquilibria;
		int ans = 0;
		while (invasions.size() > i && invasions.get(i).isNeutral()) {
			ans++;
			i++;
		}
		return ans;
	}
	
	private static int numberOfNeutralMutantsThatFollowDepartingFromMixed(
			ArrayList<Invasion> invasions, int nextPureEquilibria) {
		int i = nextPureEquilibria;
		int ans = 0;
		while (invasions.size() > i && (invasions.get(i).isNeutral() || invasions.get(i).isUnknownStepsOverMixed())) {
			ans++;
			i++;
		}
		return ans;
	}

	/**
	 * Starting from start returns the index of the next pure equilibrium (start
	 * is also checked)
	 * 
	 * @param invasions
	 * @param start
	 * @return -1 if there is no pure eq. in the remaining of the list
	 */
	private static int findNextPure(ArrayList<Invasion> invasions, int start) {
		if (start >= invasions.size()) {
			return -1;
		}
		for (int i = start + 1; i < invasions.size(); i++) {
			if (invasions.get(i).getPopulationState().getStatus() == PopulationStateStatus.PURE_NASH)
				return i;
		}
		return -1;
	}

	private static int findNextCouldBeMixedEqTwoDimensions(ArrayList<Invasion> invasions,
			int start) {
		if (start >= invasions.size()) {
			return -1;
		}
		for (int i = start + 1; i < invasions.size(); i++) {
			if (invasions.get(i).getPopulationState().getStatus() == PopulationStateStatus.TWO_BOTH_NOT_NASH_MIX_COULD_BE_NASH)
				return i;
		}
		return -1;
	}
	
	
	private static int findNextCouldBeMixedEqThreeDimensions(ArrayList<Invasion> invasions,
			int start) {
		if (start >= invasions.size()) {
			return -1;
		}
		for (int i = start + 1; i < invasions.size(); i++) {
			if (invasions.get(i).getPopulationState().getStatus() == PopulationStateStatus.THREE_ALL_NOT_NASH_MIX_COULD_BE_NASH)
				return i;
		}
		return -1;
	}

	public int getBadNewsDisadvatageous() {
		return badNewsDisadvatageous;
	}


	public int getGoodNews() {
		return goodNews;
	}

	
}
