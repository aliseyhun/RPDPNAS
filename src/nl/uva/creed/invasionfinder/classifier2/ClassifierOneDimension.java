package nl.uva.creed.invasionfinder.classifier2;

import java.util.ArrayList;

import nl.uva.creed.invasionfinder.Invasion;
import nl.uva.creed.invasionfinder.Invasion.Type;
import nl.uva.creed.invasionfinder.PopulationState.PopulationStateStatus;

public class ClassifierOneDimension {

	int	kaput						= 0;
	int	adv_new_dominates_old		= 0;
	int	advantageous				= 0;
	int	adv_coordination			= 0;
	int	adv_steps_over_mixed		= 0;
	int	adv_to_mixed				= 0;
	int	weakly_advantageous			= 0;
	int	dis_coordination			= 0;
	int	dis_out_of_mixed			= 0;
	int	disadvantegous				= 0;
	int	dis_old_dominates_new		= 0;
	int	weakly_disadvantageous		= 0;
	int	unknown_coordination		= 0;
	int	unknown_steps_over_mixed	= 0;
	int	dark_zone					= 0;
	int	waiver						= 0;

	int	kaput_w						= 0;
	int	adv_new_dominates_old_w		= 0;
	int	advantageous_w				= 0;
	int	adv_coordination_w			= 0;
	int	adv_steps_over_mixed_w		= 0;
	int	adv_to_mixed_w				= 0;
	int	weakly_advantageous_w		= 0;
	int	dis_coordination_w			= 0;
	int	dis_out_of_mixed_w			= 0;
	int	disadvantegous_w			= 0;
	int	dis_old_dominates_new_w		= 0;
	int	weakly_disadvantageous_w	= 0;
	int	unknown_coordination_w		= 0;
	int	unknown_steps_over_mixed_w	= 0;
	int	dark_zone_w					= 0;
	int	waiver_w					= 0;

	int	candidates					= 0;

	public ClassifierOneDimension(ArrayList<Invasion> invasions) {
		super();
		classifyOnlyNeutrals(invasions);
		classifyOnlyNeutralsAndWaivers(invasions);
		System.out.println(this.toString());

	}

	private static int numberOfNeutralMutantsThatFollowDepartingFromPure(
			ArrayList<Invasion> invasions, int nextPureEquilibria) {
		int i = nextPureEquilibria;
		int ans = 0;
		while (invasions.size() > i
				&& invasions.get(i).getType() == Type.NEUTRAL) {
			ans++;
			i++;
		}
		return ans;
	}

	private static int numberOfNeutralMutantsAndWaiversThatFollowDepartingFromPure(
			ArrayList<Invasion> invasions, int nextPureEquilibria) {
		int i = nextPureEquilibria;
		int ans = 0;
		while (invasions.size() > i
				&& (invasions.get(i).getType() == Type.NEUTRAL || invasions
						.get(i).getType() == Type.WAIVER)) {
			ans++;
			i++;
		}
		return ans;
	}

	private void classifyOnlyNeutrals(ArrayList<Invasion> invasions){
		int focal = 0;
		if (invasions.get(0).getPopulationState().getStatus() != PopulationStateStatus.PURE_NASH) {
			throw new RuntimeException("First state should be Nash");
		}

		while (focal >= 0) {
			// CASE 3
			if (invasions.get(focal).isAdvantageous()) {
				throw new RuntimeException("Error in Nash checker at "+ invasions.get(focal).getPopulationState().getTick());
			} else {
				int neutralMutantsThatFollow = numberOfNeutralMutantsThatFollowDepartingFromPure(invasions, focal);
				if (focal + neutralMutantsThatFollow >= invasions.size()) {
					focal = -1;
					break;
				}

				candidates ++;
				
				if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.KAPUT){
					kaput++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.ADV_NEW_DOMINATES_OLD && 
						neutralMutantsThatFollow > 0){ adv_new_dominates_old++;
				}else if (invasions.get(focal + neutralMutantsThatFollow).getType()== Type.ADVANTAGEOUS && neutralMutantsThatFollow > 0){
							advantageous++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.ADV_COORDINATION && neutralMutantsThatFollow > 0){
					adv_coordination++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.ADV_STEPS_OVER_MIXED && neutralMutantsThatFollow > 0){
					adv_steps_over_mixed++;
				}else if( invasions.get(focal + neutralMutantsThatFollow).getType()== Type.ADV_TO_MIXED && neutralMutantsThatFollow > 0){
					adv_to_mixed++;
				}else if (invasions.get(focal + neutralMutantsThatFollow).getType()== Type.WEAKLY_ADVANTAGEOUS && neutralMutantsThatFollow > 0) {
					weakly_advantageous++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.DIS_COORDINATION){
					dis_coordination++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.DIS_OUT_OF_MIXED){
					dis_out_of_mixed++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.DISADVANTEGOUS){
					disadvantegous++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.DIS_OLD_DOMINATES_NEW){
					dis_old_dominates_new++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.WEAKLY_DISADVANTAGEOUS){
					weakly_disadvantageous++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.UNKNOWN_COORDINATION){
					unknown_coordination++;	
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.UNKNOWN_STEPS_OVER_MIXED){
					unknown_steps_over_mixed++;
				}else if (invasions.get(focal + neutralMutantsThatFollow).getType()== Type.DARK_ZONE){
					dark_zone++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType()== Type.WAIVER){
					waiver++;
				}
				
				focal = findNextPure(invasions, focal+neutralMutantsThatFollow);
				
				
			}
		
		}
		
	}

	private void classifyOnlyNeutralsAndWaivers(ArrayList<Invasion> invasions) {
		int focal = 0;
		if (invasions.get(0).getPopulationState().getStatus() != PopulationStateStatus.PURE_NASH) {
			throw new RuntimeException("First state should be Nash");
		}

		while (focal >= 0) {
			// CASE 3
			if (invasions.get(focal).isAdvantageous()) {
				throw new RuntimeException("Error in Nash checker at "
						+ invasions.get(focal).getPopulationState().getTick());
			} else {
				int neutralMutantsThatFollow = numberOfNeutralMutantsAndWaiversThatFollowDepartingFromPure(
						invasions, focal);
				if (focal + neutralMutantsThatFollow >= invasions.size()) {
					focal = -1;
					break;
				}

				//candidates++;
				
				if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.KAPUT){
					kaput_w++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.ADV_NEW_DOMINATES_OLD && neutralMutantsThatFollow > 0){
					adv_new_dominates_old_w++;
				}else if (invasions.get(focal + neutralMutantsThatFollow).getType() == Type.ADVANTAGEOUS && neutralMutantsThatFollow > 0){
					advantageous_w++;
				}
				else  if (invasions.get(focal + neutralMutantsThatFollow).getType() == Type.ADV_COORDINATION && neutralMutantsThatFollow > 0){
					adv_coordination_w++;
				}
				else  if (invasions.get(focal + neutralMutantsThatFollow).getType() == Type.ADV_STEPS_OVER_MIXED && neutralMutantsThatFollow > 0){
					adv_steps_over_mixed_w++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.ADV_TO_MIXED && neutralMutantsThatFollow > 0){
					adv_to_mixed_w++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.WEAKLY_ADVANTAGEOUS && neutralMutantsThatFollow > 0){
					weakly_advantageous_w++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.DIS_COORDINATION){
					dis_coordination_w++;	
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.DIS_OUT_OF_MIXED){
					dis_out_of_mixed_w++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.DISADVANTEGOUS){
					disadvantegous_w++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.DIS_OLD_DOMINATES_NEW){
					dis_old_dominates_new_w++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.WEAKLY_DISADVANTAGEOUS){
					weakly_disadvantageous_w++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.UNKNOWN_COORDINATION){
					unknown_coordination_w++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.UNKNOWN_STEPS_OVER_MIXED){
					unknown_steps_over_mixed_w++;
				}else if(invasions.get(focal + neutralMutantsThatFollow).getType() == Type.DARK_ZONE){
					dark_zone_w++;
				}	
				focal = findNextPure(invasions, focal+ neutralMutantsThatFollow);
				
			}

		}

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

	/**
	 * Constructs a <code>String</code> with all attributes in name = value
	 * format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	public String toString() {
		final String TAB = "\n";

		String retValue = "";

		retValue = "ClassifierOneDimension ( " + super.toString() + TAB
				+ "kaput = " + this.kaput + " "
				+ (double) this.kaput / (double) candidates + TAB
				+ "adv_new_dominates_old = " + this.adv_new_dominates_old + " "
				+ (double) this.adv_new_dominates_old / (double) candidates
				+ TAB + "advantageous = " + this.advantageous + " "
				+ (double) this.advantageous / (double) candidates + TAB
				+ "adv_coordination = " + this.adv_coordination + " "
				+ (double) this.adv_coordination / (double) candidates + TAB
				+ "adv_steps_over_mixed = " + this.adv_steps_over_mixed + " "
				+ (double) this.adv_steps_over_mixed / (double) candidates
				+ TAB + "adv_to_mixed = " + this.adv_to_mixed + " "
				+ (double) this.adv_to_mixed / (double) candidates + TAB
				+ "weakly_advantageous = " + this.weakly_advantageous + " "
				+ (double) this.weakly_advantageous / (double) candidates + TAB
				+ "dis_coordination = " + this.dis_coordination + " "
				+ (double) this.dis_coordination / (double) candidates + TAB
				+ "dis_out_of_mixed = " + this.dis_out_of_mixed + " "
				+ (double) this.dis_out_of_mixed / (double) candidates + TAB
				+ "disadvantegous = " + this.disadvantegous + " "
				+ (double) this.disadvantegous / (double) candidates + TAB
				+ "dis_old_dominates_new = " + this.dis_old_dominates_new + " "
				+ (double) this.dis_old_dominates_new / (double) candidates
				+ TAB + "weakly_disadvantageous = "
				+ this.weakly_disadvantageous + " "
				+ (double) this.weakly_disadvantageous / (double) candidates
				+ TAB + "unknown_coordination = " + this.unknown_coordination
				+ " "
				+ (double) this.unknown_coordination / (double) candidates
				+ TAB + "unknown_steps_over_mixed = "
				+ this.unknown_steps_over_mixed + " "
				+ (double) this.unknown_steps_over_mixed / (double) candidates
				+ TAB + "dark_zone = " + this.dark_zone + " "
				+ (double) this.dark_zone / (double) candidates + TAB
				+ "waiver = " + this.waiver + " "
				+ (double) this.waiver / (double) candidates + TAB
				+ "kaput_w = " + this.kaput_w + " "
				+ (double) this.kaput_w / (double) candidates + TAB
				+ "adv_new_dominates_old_w = " + this.adv_new_dominates_old_w
				+ " "
				+ (double) this.adv_new_dominates_old_w / (double) candidates
				+ TAB + "advantageous_w = " + this.advantageous_w + " "
				+ (double) this.advantageous_w / (double) candidates + TAB
				+ "adv_coordination_w = " + this.adv_coordination_w + " "
				+ (double) this.adv_coordination_w / (double) candidates + TAB
				+ "adv_steps_over_mixed_w = " + this.adv_steps_over_mixed_w
				+ " "
				+ (double) this.adv_steps_over_mixed_w / (double) candidates
				+ TAB + "adv_to_mixed_w = " + this.adv_to_mixed_w + " "
				+ (double) this.adv_to_mixed_w / (double) candidates + TAB
				+ "weakly_advantageous_w = " + this.weakly_advantageous_w + " "
				+ (double) this.weakly_advantageous_w / (double) candidates
				+ TAB + "dis_coordination_w = " + this.dis_coordination_w + " "
				+ (double) this.dis_coordination_w / (double) candidates + TAB
				+ "dis_out_of_mixed_w = " + this.dis_out_of_mixed_w + " "
				+ (double) this.dis_out_of_mixed_w / (double) candidates + TAB
				+ "disadvantegous_w = " + this.disadvantegous_w + " "
				+ (double) this.disadvantegous_w / (double) candidates + TAB
				+ "dis_old_dominates_new_w = " + this.dis_old_dominates_new_w
				+ " "
				+ (double) this.dis_old_dominates_new_w / (double) candidates
				+ TAB + "weakly_disadvantageous_w = "
				+ this.weakly_disadvantageous_w + " "
				+ (double) this.weakly_disadvantageous_w / (double) candidates
				+ TAB + "unknown_coordination_w = "
				+ this.unknown_coordination_w + " "
				+ (double) this.unknown_coordination_w / (double) candidates
				+ TAB + "unknown_steps_over_mixed_w = "
				+ this.unknown_steps_over_mixed_w + " "
				+ (double) this.unknown_steps_over_mixed_w
				/ (double) candidates + TAB + "dark_zone_w = "
				+ this.dark_zone_w + " " + (double) this.dark_zone_w
				/ (double) candidates + TAB + "waiver_w = " + this.waiver_w
				+ " " + (double) this.waiver_w / (double) candidates + TAB
				+ "candidates = " + this.candidates + TAB + " )";

		return retValue;
	}

}
