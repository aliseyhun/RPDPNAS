package nl.uva.creed.invasionfinder;

import java.util.ArrayList;

import nl.uva.creed.game.Game;
import nl.uva.creed.game.impl.PDGame;
import nl.uva.creed.invasionfinder.Invasion.Type;
import nl.uva.creed.invasionfinder.PopulationState.Cardinality;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.exceptions.GameException;
import nl.uva.creed.repeated.impl.StandardRepeatedGame;

import org.apache.commons.math.linear.RealMatrixImpl;

public class InvasionClassifier {

	public static ArrayList<Invasion> classify(ArrayList<PopulationState> list,
			double s, double p, double r, int rounds,
			double continuationProbability, double complexityCost, double t)
			throws MalformedInvasionException, GameException {
		ArrayList<Invasion> ans = new ArrayList<Invasion>();
		for (int i = 0; i < list.size() - 1; i++) {
			PopulationState equilibriumOrigin = list.get(i);
			PopulationState equilibriumDestiny = list.get(i + 1);
			if (equilibriumOrigin.getTick() == 6213) {
				System.out.println("Hello");
			}
			
			Invasion invasion = buildInvasion(equilibriumOrigin,
					equilibriumDestiny, s, p, r, rounds,
					continuationProbability, complexityCost, t);
			ans.add(invasion);

		}
		return ans;
	}

	public static Invasion buildInvasion(PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny, double s, double p, double r,
			int rounds, double continuationProbability, double complexityCost, double t)
			throws MalformedInvasionException, GameException {
		if (equilibriumOrigin.getCardinality() == Cardinality.ONE_DIMENSION) {
			return buildInvasionFromPocket(equilibriumOrigin,
					equilibriumDestiny, s, p, r, rounds,
					continuationProbability,complexityCost, t);
		} else if (equilibriumOrigin.getCardinality() == Cardinality.TWO_DIMENSION) {
			return buildInvasionFromTwoDimensionalInterior(equilibriumOrigin,
					equilibriumDestiny, s, p, r, rounds,
					continuationProbability, complexityCost, t);
		} else if (equilibriumOrigin.getCardinality() == Cardinality.THREE_DIMENSION) {
			return buildInvasionFromThreeDimensionalInterior(equilibriumOrigin,
					equilibriumDestiny, s, p, r, rounds,
					continuationProbability, complexityCost, t);
		} else {
			return buildDarkInvasion(equilibriumOrigin);
		}

	}

	private static Invasion buildInvasionFromThreeDimensionalInterior(
			PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny, double s, double p, double r,
			int rounds, double continuationProbability,double complexityCost, double t)
			throws GameException {

		RepeatedGameStrategy str1 = null;
		RepeatedGameStrategy str2 = null;
		RepeatedGameStrategy str3 = null;

		if (equilibriumDestiny.cardinality() == 1) {
			return new Invasion(equilibriumOrigin, Type.KAPUT);
		} else if (equilibriumDestiny.cardinality() == 2) {

			if (numberOfNewStrategies(equilibriumOrigin, equilibriumDestiny) == 0) {
				str1 = equilibriumDestiny.getMostPopularStrategy();
				str2 = equilibriumDestiny.getSecondMostPopularStrategy();
				str3 = mostPopularNewStrategy(equilibriumDestiny,
						equilibriumOrigin);
				return buildInvasionFrom3StrategiesModified(str1, str2, str3,
						continuationProbability,complexityCost, r, s, t, p, rounds, equilibriumOrigin);
			} else {
				return new Invasion(equilibriumOrigin, Type.KAPUT);
			}

		} else if (equilibriumDestiny.cardinality() == 3) {

			if (numberOfNewStrategies(equilibriumOrigin, equilibriumDestiny) == 0) {
				
				str1 = equilibriumOrigin.getMostPopularStrategy();
				str2 = equilibriumOrigin.getSecondMostPopularStrategy();
				str3 = equilibriumOrigin.getThirdMostPopularStrategy();
				
				return buildInvasionFrom3StrategiesTo3StrategiesNoNewStrategies(
						str1, str2, str3, continuationProbability,complexityCost, r, s, t, p, rounds, equilibriumOrigin,
						equilibriumDestiny);
			} else  if (numberOfNewStrategies(equilibriumOrigin, equilibriumDestiny) == 1){
				str1 = equilibriumOrigin.getMostPopularStrategy();
				str2 = equilibriumOrigin.getSecondMostPopularStrategy();
				str3 = equilibriumOrigin.getThirdMostPopularStrategy();
				
				RepeatedGameStrategy str4 = mostPopularNewStrategy(equilibriumOrigin, equilibriumDestiny);
				return buildInvasionFrom3StrategiesTo3StrategiesOneNewStrategy(
						str1, str2, str3,str4, continuationProbability, complexityCost, r, s, t, p, rounds, equilibriumOrigin,
						equilibriumDestiny);

			}else{
				return new Invasion(equilibriumOrigin, Type.KAPUT);
			}

		} else {
			return buildDarkInvasion(equilibriumOrigin);
		}
	}

	private static Invasion buildInvasionFrom3StrategiesTo3StrategiesOneNewStrategy(
			RepeatedGameStrategy str1, RepeatedGameStrategy str2, RepeatedGameStrategy str3, RepeatedGameStrategy str4,
			double continuationProbability, double complexityCost,double r, double s, double t,
			double p, int rounds,
			PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny) throws GameException {
		RealMatrixImpl matrixImpl = build4x4Matrix(str1, str2, str3,str4,
				continuationProbability, complexityCost, r, s, t, p, rounds);
		
		double aa = matrixImpl.getData()[0][0];
		double ab = matrixImpl.getData()[0][1];
		double ac = matrixImpl.getData()[0][2];
		double ad = matrixImpl.getData()[0][3];

		
		double ba = matrixImpl.getData()[1][0];
		double bb = matrixImpl.getData()[1][1];
		double bc = matrixImpl.getData()[1][2];
		double bd = matrixImpl.getData()[1][3];

		
		double ca = matrixImpl.getData()[2][0];
		double cb = matrixImpl.getData()[2][1];
		double cc = matrixImpl.getData()[2][2];
		double cd = matrixImpl.getData()[2][3];
		
		double da = matrixImpl.getData()[3][0];
		double db = matrixImpl.getData()[3][1];
		double dc = matrixImpl.getData()[3][2];
		double dd = matrixImpl.getData()[3][3];
		
		if (holdsForAllGamma(aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd)){
			if (ad==bd && bd==cd && cd==dd) {
				return new Invasion(equilibriumOrigin, Type.NEUTRAL);
			}else if(cornerOneOutOfFour(aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd)){
				return new Invasion(equilibriumOrigin, Type.WEAKLY_ADVANTAGEOUS);
			}else if(cornerTwoOutOfFour(aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd)){
				return new Invasion(equilibriumOrigin, Type.WEAKLY_ADVANTAGEOUS);	
			}else if(cornerThreeOutOfFour(aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd)){
				return new Invasion(equilibriumOrigin, Type.WEAKLY_ADVANTAGEOUS);
			}else if(cornerFourOutOfFour(aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd)){
				return new Invasion(equilibriumOrigin, Type.WEAKLY_ADVANTAGEOUS);
			}else{
				return new Invasion(equilibriumOrigin, Type.WEAKLY_DISADVANTAGEOUS);	
			}
		}
		
		if (neutralDofA(aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd)) {
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		}
		if (neutralDofB(aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd)) {
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		}
		if (neutralDofC(aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd)) {
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		}
		
		return heuristic(aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd, equilibriumOrigin);
		
	}

	private static Invasion heuristic(double aa, double ab, double ac,
			double ad, double ba, double bb, double bc, double bd, double ca,
			double cb, double cc, double cd, double da, double db, double dc,
			double dd,PopulationState equilibriumOrigin ) {
		
		
		if (heuristicCheck(1,aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd) > 0) {
			return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
		}
		if (heuristicCheck(2,aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd) > 0) {
			return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
		}
		if (heuristicCheck(3,aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd) > 0) {
			return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
		}
		if (heuristicCheck(4,aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd) > 0) {
			return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
		}
		if (heuristicCheck(5,aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd) > 0) {
			return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
		}
		if (heuristicCheck(6,aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd) > 0) {
			return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
		}
		if (heuristicCheck(7,aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd) > 0) {
			return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
		}
		if (heuristicCheck(8,aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd) > 0) {
			return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
		}
		
		System.out.println("better check needed departing from origin at generation " + equilibriumOrigin.getTick());
		return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
	}

	private static double heuristicCheck(int function, double aa, double ab, double ac,
			double ad, double ba, double bb, double bc, double bd, double ca,
			double cb, double cc, double cd, double da, double db, double dc,
			double dd) {
		if (function ==1 ) {
			double gamma = Finder.MIXED_UPPER_BOUND;
			double delta = 1.0 - Finder.MIXED_UPPER_BOUND;
			return function(gamma,delta, aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd);
		}
		
		if (function ==2 ) {
			double gamma = 0.5*(Finder.MIXED_UPPER_BOUND + 0.5);
			double delta =  0.5*(1.0-Finder.MIXED_UPPER_BOUND + 0.5);
			return function(gamma,delta, aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd);
		}
		if (function ==3 ) {
			double gamma = 0.5;
			double delta =   0.5;
			return function(gamma,delta, aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd);
		}
		if (function ==4 ) {
			double gamma = 0.5*(0.5 + 1.0/3.0);
			double delta =   0.5*(0.5 + 1.0/3.0);
			return function(gamma,delta, aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd);
		}
		if (function ==5 ) {
			double gamma = 1.0/3.0;
			double delta =   1.0/3.0;
			return function(gamma,delta, aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd);
		}
		if (function ==6 ) {
			double gamma = 0.5*(Finder.MIXED_UPPER_BOUND + 1.0/3.0);
			double delta =   0.5*((1.0 - Finder.MIXED_UPPER_BOUND/(2.0))+1.0/3.0);
			return function(gamma,delta, aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd);
		}
		if (function ==7 ) {
			double gamma = Finder.MIXED_UPPER_BOUND;
			double delta =   (1.0- Finder.MIXED_UPPER_BOUND)/2.0;
			return function(gamma,delta, aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd);
		}
		if (function == 8) {
			double gamma = 0.5*(0.5*(Finder.MIXED_UPPER_BOUND+0.5)+0.5*(Finder.MIXED_UPPER_BOUND+ 1.0/3.0));
			double delta =   0.5*(0.5*(1-Finder.MIXED_UPPER_BOUND+0.5+0.5*(((1-Finder.MIXED_UPPER_BOUND)/2.0)+(1/3.0))));
			return function(gamma,delta, aa, ab, ac, ad, ba, bb, bc, bd, ca, cb,cc, cd, da, db,dc, dd);
		}
		return Double.NaN;
		
	}

	private static double function(double gamma, double delta,double aa, double ab, double ac,
			double ad, double ba, double bb, double bc, double bd, double ca,
			double cb, double cc, double cd, double da, double db, double dc,
			double dd) {
		double a = gamma*da  + delta*db + (1.0 - gamma - delta)*dc;
		double b = Math.pow(gamma, 2.0)*aa + gamma*delta*(ab+ba) + gamma*(1.0-gamma - delta)*(ac+ca)+
		Math.pow(delta,2.0)*bb + delta*(1.0-gamma-delta)*(bc+cb)+Math.pow((1.0 - gamma -delta),2.0)*cc;
		return a-b;
	}

	private static boolean neutralDofC(double aa, double ab, double ac,
			double ad, double ba, double bb, double bc, double bd, double ca,
			double cb, double cc, double cd, double da, double db, double dc,
			double dd) {
		if (ca == da && cb==db && cc == dc && cd == dd) {
			return true;
		}
		return false;
	}

	private static boolean neutralDofB(double aa, double ab, double ac,
			double ad, double ba, double bb, double bc, double bd, double ca,
			double cb, double cc, double cd, double da, double db, double dc,
			double dd) {
		if (ba == da && bb==db && bc == dc && bd == dd) {
			return true;
		}
		
		return false;
	}

	private static boolean neutralDofA(double aa, double ab, double ac,
			double ad, double ba, double bb, double bc, double bd, double ca,
			double cb, double cc, double cd, double da, double db, double dc,
			double dd) {
		if (aa == da && ab==db && ac == dc && ad == dd) {
			return true;
		}
		
		return false;
	}

	private static boolean cornerFourOutOfFour(double aa, double ab, double ac,
			double ad, double ba, double bb, double bc, double bd, double ca,
			double cb, double cc, double cd, double da, double db, double dc,
			double dd) {
		if ((1.0/3.0)*(dd-ad) + (1.0/3.0)*(dd-bd) + (1.0/3.0)*(dd-cd) >0) {
			return true;
		}
		
		return false;
	}

	private static boolean cornerThreeOutOfFour(double aa, double ab,
			double ac, double ad, double ba, double bb, double bc, double bd,
			double ca, double cb, double cc, double cd, double da, double db,
			double dc, double dd) {
		if (Finder.MIXED_UPPER_BOUND*(dd-ad)+0.5*(1-Finder.MIXED_UPPER_BOUND*(dd-bd) + 0.5*(1-Finder.MIXED_UPPER_BOUND)*(dd-cd))>0) {
			return true;
		}
		return false;
	}

	private static boolean cornerTwoOutOfFour(double aa, double ab, double ac,
			double ad, double ba, double bb, double bc, double bd, double ca,
			double cb, double cc, double cd, double da, double db, double dc,
			double dd) {
		if (0.5*(dd-ad)+(1-0.5)*(dd-bd) >0) {
			return true;
		}
		return false;
	}

	private static boolean cornerOneOutOfFour(double aa, double ab, double ac,
			double ad, double ba, double bb, double bc, double bd, double ca,
			double cb, double cc, double cd, double da, double db, double dc,
			double dd) {
		if (Finder.MIXED_UPPER_BOUND*(dd-ad)+(1-Finder.MIXED_UPPER_BOUND)*(dd-bd) >0) {
			return true;
		}
		return false;
	}

	private static boolean holdsForAllGamma(double aa, double ab, double ac,
			double ad, double ba, double bb, double bc, double bd, double ca,
			double cb, double cc, double cd, double da, double db, double dc,
			double dd) {
	
		if (aa==ba && ba==ca && ca== da && 
			ab==bb && bb==cb && cb== db &&
			ac==bc && bc==cc && cc== dc) {
			return true;
		}
		
		return false;
	}

	private static Invasion buildInvasionFrom3StrategiesTo3StrategiesNoNewStrategies(
			RepeatedGameStrategy str1, RepeatedGameStrategy str2, RepeatedGameStrategy str3,
			double continuationProbability, double complexityCost,double r, double s, double t,
			double p,  int rounds,
			PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny) throws GameException {

		RealMatrixImpl matrixImpl = build3x3Matrix(str1, str2, str3,
				continuationProbability, complexityCost,  r, s, t, p, rounds);
		double aa = matrixImpl.getData()[0][0];
		double ab = matrixImpl.getData()[0][1];
		double ac = matrixImpl.getData()[0][2];

		double ba = matrixImpl.getData()[1][0];
		double bb = matrixImpl.getData()[1][1];
		double bc = matrixImpl.getData()[1][2];

		double ca = matrixImpl.getData()[2][0];
		double cb = matrixImpl.getData()[2][1];
		double cc = matrixImpl.getData()[2][2];

		if (strategyBisNeutralOfA(aa, ab, ac, ba, bb, bc, ca, cb, cc) || 
			strategyCisNeutralOfA(aa, ab, ac, ba, bb, bc, ca, cb, cc) ||
			strategyCisNeutralOfB(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		}
		if (abcToAcb(equilibriumOrigin, equilibriumDestiny)) {

			if (DominanceChecker.dominanceCB(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
			} else if (DominanceChecker.dominanceBC(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
			} else {
				return new Invasion(equilibriumOrigin, Type.WAIVER);
			}
		}

		if (abcToBac(equilibriumOrigin, equilibriumDestiny)) {

			if (DominanceChecker
					.dominanceBA(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
			} else if (DominanceChecker.dominanceAB(aa, ab, ac, ba, bb, bc, ca,
					cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
			} else {
				return new Invasion(equilibriumOrigin, Type.WAIVER);
			}
		}
		
		if (abcToBca(equilibriumOrigin, equilibriumDestiny)) {

			if (DominanceChecker.dominanceBA(aa, ab, ac, ba, bb, bc, ca, cb, cc) && DominanceChecker.dominanceCA(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
			} else if (DominanceChecker.dominanceAB(aa, ab, ac, ba, bb, bc, ca,cb, cc) && DominanceChecker.dominanceAC(aa, ab, ac, ba, bb, bc, ca,cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
			} else {
				return new Invasion(equilibriumOrigin, Type.WAIVER);
			}
		}

		if (abcToCab(equilibriumOrigin, equilibriumDestiny)) {

			if (DominanceChecker.dominanceCA(aa, ab, ac, ba, bb, bc, ca, cb, cc) && DominanceChecker.dominanceCB(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
			} else if (DominanceChecker.dominanceAC(aa, ab, ac, ba, bb, bc, ca,cb, cc) && DominanceChecker.dominanceBC(aa, ab, ac, ba, bb, bc, ca,cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
			} else {
				return new Invasion(equilibriumOrigin, Type.WAIVER);
			}
		}
		
		if (abcToCba(equilibriumOrigin, equilibriumDestiny)) {

			if (DominanceChecker.dominanceCB(aa, ab, ac, ba, bb, bc, ca, cb, cc) && DominanceChecker.dominanceBA(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
			} else if (DominanceChecker.dominanceAB(aa, ab, ac, ba, bb, bc, ca,cb, cc) && DominanceChecker.dominanceBC(aa, ab, ac, ba, bb, bc, ca,cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
			} else {
				return new Invasion(equilibriumOrigin, Type.WAIVER);
			}
		}
		return null;
	}
	
	
	private static boolean abcToCba(PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny) {
		if (equilibriumOrigin.getMostPopularStrategy().equals(
				equilibriumDestiny.getThirdMostPopularStrategy())
				&& equilibriumOrigin.getSecondMostPopularStrategy().equals(
						equilibriumDestiny.getSecondMostPopularStrategy())
				&& equilibriumOrigin.getThirdMostPopularStrategy().equals(
						equilibriumDestiny.getMostPopularStrategy())) {
			return true;
		}
		return false;
	}
	
	private static boolean abcToCab(PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny) {
		if (equilibriumOrigin.getMostPopularStrategy().equals(
				equilibriumDestiny.getThirdMostPopularStrategy())
				&& equilibriumOrigin.getSecondMostPopularStrategy().equals(
						equilibriumDestiny.getMostPopularStrategy())
				&& equilibriumOrigin.getThirdMostPopularStrategy().equals(
						equilibriumDestiny.getSecondMostPopularStrategy())) {
			return true;
		}
		return false;
	}

	
	private static boolean abcToBca(PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny) {
		if (equilibriumOrigin.getMostPopularStrategy().equals(
				equilibriumDestiny.getSecondMostPopularStrategy())
				&& equilibriumOrigin.getSecondMostPopularStrategy().equals(
						equilibriumDestiny.getThirdMostPopularStrategy())
				&& equilibriumOrigin.getThirdMostPopularStrategy().equals(
						equilibriumDestiny.getMostPopularStrategy())) {
			return true;
		}
		return false;
	}

	private static boolean abcToAcb(PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny) {
		if (equilibriumOrigin.getMostPopularStrategy().equals(
				equilibriumDestiny.getMostPopularStrategy())
				&& equilibriumOrigin.getSecondMostPopularStrategy().equals(
						equilibriumDestiny.getThirdMostPopularStrategy())
				&& equilibriumOrigin.getThirdMostPopularStrategy().equals(
								equilibriumDestiny.getSecondMostPopularStrategy())) {
			return true;
		}
		return false;
	}

	private static boolean abcToBac(PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny) {
		if (equilibriumOrigin.getMostPopularStrategy().equals(
				equilibriumDestiny.getSecondMostPopularStrategy())
				&& equilibriumOrigin.getSecondMostPopularStrategy().equals(
						equilibriumDestiny.getMostPopularStrategy())
				&& equilibriumOrigin.getThirdMostPopularStrategy().equals(
						equilibriumDestiny.getThirdMostPopularStrategy())) {
			return true;
		}
		return false;
	}

	private static Invasion buildDarkInvasion(PopulationState equilibriumOrigin) {
		return new Invasion(equilibriumOrigin, Type.DARK_ZONE);
	}

	private static Invasion buildInvasionFromTwoDimensionalInterior(
			PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny, double s, double p, double r,
			int rounds, double continuationProbability, double complexityCost, double t)
			throws MalformedInvasionException, GameException {
		RepeatedGameStrategy str1 = null;
		RepeatedGameStrategy str2 = null;
		RepeatedGameStrategy str3 = null;

		if (equilibriumDestiny.getCardinality() == Cardinality.ONE_DIMENSION) {
			if (equilibriumOrigin.getMostPopularStrategy().equals(
					equilibriumDestiny.getMostPopularStrategy())) {
				str1 = equilibriumOrigin.getMostPopularStrategy();
				str2 = equilibriumOrigin.getSecondMostPopularStrategy();
				return buildInvasionFrom2StrategiesTwoToOneARemains(str1, str2,
						continuationProbability, complexityCost, r, s, t, p, rounds, equilibriumOrigin);
			} else if (equilibriumOrigin.getSecondMostPopularStrategy().equals(
					equilibriumDestiny.getMostPopularStrategy())) {
				str1 = equilibriumOrigin.getMostPopularStrategy();
				str2 = equilibriumOrigin.getSecondMostPopularStrategy();
				return buildInvasionFrom2StrategiesTwoToOneBRemains(str1, str2,
						continuationProbability, complexityCost, r, s, t, p, 
						rounds, equilibriumOrigin);
			} else {
				return new Invasion(equilibriumOrigin, Type.KAPUT);
			}

		} else if (equilibriumDestiny.getCardinality() == Cardinality.TWO_DIMENSION) {
			if (equilibriumOrigin.getSecondMostPopularStrategy().equals(
					equilibriumDestiny.getMostPopularStrategy())
					&& equilibriumOrigin.getMostPopularStrategy().equals(
							equilibriumDestiny.getSecondMostPopularStrategy())) {
				str1 = equilibriumOrigin.getMostPopularStrategy();
				str2 = equilibriumOrigin.getSecondMostPopularStrategy();
				return buildInvasionFrom2StrategiesABtoBA(str1, str2,
						continuationProbability,complexityCost, r, s, t, p, rounds,
						equilibriumOrigin);
			} else if (equilibriumOrigin.getMostPopularStrategy().equals(
					equilibriumDestiny.getMostPopularStrategy())
					&& !equilibriumDestiny.getSecondMostPopularStrategy()
							.equals(equilibriumOrigin.getMostPopularStrategy())
					&& !equilibriumDestiny.getSecondMostPopularStrategy()
							.equals(
									equilibriumOrigin
											.getSecondMostPopularStrategy())) {
				str1 = equilibriumOrigin.getMostPopularStrategy();
				str2 = equilibriumOrigin.getSecondMostPopularStrategy();
				str3 = equilibriumDestiny.getSecondMostPopularStrategy();
				return buildInvasionFrom3Strategies(str1, str2, str3,
						continuationProbability, r, s, t, p,
						continuationProbability, rounds, equilibriumOrigin);
			} else if (equilibriumOrigin.getMostPopularStrategy().equals(
					equilibriumDestiny.getSecondMostPopularStrategy())
					&& !equilibriumDestiny.getMostPopularStrategy().equals(
							equilibriumOrigin.getMostPopularStrategy())
					&& !equilibriumDestiny.getMostPopularStrategy().equals(
							equilibriumOrigin.getSecondMostPopularStrategy())) {
				str1 = equilibriumOrigin.getMostPopularStrategy();
				str2 = equilibriumOrigin.getSecondMostPopularStrategy();
				str3 = equilibriumDestiny.getMostPopularStrategy();
				return buildInvasionFrom3Strategies(str1, str2, str3,
						continuationProbability, r, s, t, p,
						continuationProbability, rounds, equilibriumOrigin);
			} else if (equilibriumOrigin.getSecondMostPopularStrategy().equals(
					equilibriumDestiny.getMostPopularStrategy())
					&& !equilibriumDestiny.getSecondMostPopularStrategy()
							.equals(equilibriumOrigin.getMostPopularStrategy())
					&& !equilibriumDestiny.getSecondMostPopularStrategy()
							.equals(
									equilibriumOrigin
											.getSecondMostPopularStrategy())) {
				str1 = equilibriumOrigin.getMostPopularStrategy();
				str2 = equilibriumOrigin.getSecondMostPopularStrategy();
				str3 = equilibriumDestiny.getSecondMostPopularStrategy();
				return buildInvasionFrom3Strategies(str1, str2, str3,
						continuationProbability, r, s, t, p,
						continuationProbability, rounds, equilibriumOrigin);
			} else if (equilibriumOrigin.getSecondMostPopularStrategy().equals(
					equilibriumDestiny.getSecondMostPopularStrategy())
					&& !equilibriumDestiny.getMostPopularStrategy().equals(
							equilibriumOrigin.getMostPopularStrategy())
					&& !equilibriumDestiny.getMostPopularStrategy().equals(
							equilibriumOrigin.getSecondMostPopularStrategy())) {
				str1 = equilibriumOrigin.getMostPopularStrategy();
				str2 = equilibriumOrigin.getSecondMostPopularStrategy();
				str3 = equilibriumDestiny.getMostPopularStrategy();
				return buildInvasionFrom3Strategies(str1, str2, str3,
						continuationProbability, r, s, t, p,
						continuationProbability, rounds, equilibriumOrigin);
			} else {
				return new Invasion(equilibriumOrigin, Type.KAPUT);
			}
		} else if (equilibriumDestiny.getCardinality() == Cardinality.THREE_DIMENSION) {
			if (numberOfNewStrategies(equilibriumOrigin, equilibriumDestiny) == 1) {
				str1 = equilibriumOrigin.getMostPopularStrategy();
				str2 = equilibriumOrigin.getSecondMostPopularStrategy();
				str3 = mostPopularNewStrategy(equilibriumOrigin,
						equilibriumDestiny);
				return buildInvasionFrom3Strategies(str1, str2, str3,
						continuationProbability, r, s, t, p,
						continuationProbability, rounds, equilibriumOrigin);
			} else {
				return new Invasion(equilibriumOrigin, Type.KAPUT);
			}

		} else if (equilibriumDestiny.getCardinality() == Cardinality.DARK_ZONE) {
			return buildDarkInvasion(equilibriumOrigin);
		}
		throw new MalformedInvasionException();
	}

	// OJO ASSUMES ORDER
	private static int numberOfNewStrategies(PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny) {
		int ans = 0;
		for (int i = 0; i < equilibriumDestiny.cardinality(); i++) {
			if (!equilibriumOrigin.getCoreStrategiesSet().contains(
					equilibriumDestiny.getStrategies().get(i)))
				ans++;
		}
		return ans;
	}

	private static RepeatedGameStrategy mostPopularNewStrategy(
			PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny) {
		for (int i = 0; i < equilibriumDestiny.cardinality(); i++) {
			if (!equilibriumOrigin.getCoreStrategiesSet().contains(
					equilibriumDestiny.getStrategies().get(i)))
				return equilibriumDestiny.getStrategies().get(i);
		}
		return null;
	}

	private static Invasion buildInvasionFrom3Strategies(RepeatedGameStrategy str1,
			RepeatedGameStrategy str2, RepeatedGameStrategy str3, double continuationProbability, double complexityCost,
			double r, double s, double t, double p,int rounds,
			PopulationState equilibriumOrigin) throws GameException {
		RealMatrixImpl matrixImpl = build3x3Matrix(str1, str2, str3,
				continuationProbability, complexityCost, r, s, t, p, rounds);
		double aa = matrixImpl.getData()[0][0];
		double ab = matrixImpl.getData()[0][1];
		double ac = matrixImpl.getData()[0][2];

		double ba = matrixImpl.getData()[1][0];
		double bb = matrixImpl.getData()[1][1];
		double bc = matrixImpl.getData()[1][2];

		double ca = matrixImpl.getData()[2][0];
		double cb = matrixImpl.getData()[2][1];
		double cc = matrixImpl.getData()[2][2];

		if (allAreEqualAgainstABMix(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
			if (ac == bc && bc == cc) {
				return new Invasion(equilibriumOrigin, Type.NEUTRAL);
			} else if (cc >= ac && cc >= bc) {
				return new Invasion(equilibriumOrigin, Type.WEAKLY_ADVANTAGEOUS);
			} else if ((Finder.MIXED_UPPER_BOUND * (cc - ac))
					+ ((1 - Finder.MIXED_UPPER_BOUND) * (cc - bc)) > 0) {
				return new Invasion(equilibriumOrigin, Type.WEAKLY_ADVANTAGEOUS);
			} else if ((0.5 * (cc - ac)) + ((1 - 0.5) * (cc - bc)) > 0) {
				return new Invasion(equilibriumOrigin, Type.WEAKLY_ADVANTAGEOUS);
			}else {
				return new Invasion(equilibriumOrigin,Type.WEAKLY_DISADVANTAGEOUS);
			}
		}else if(strategyCisNeutralOfA(aa, ab, ac, ba, bb, bc, ca, cb, cc) || 
				strategyCisNeutralOfB(aa, ab, ac, ba, bb, bc, ca, cb, cc)){
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		} else if (noSolutionsParallelCheck(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
			if (noSolutionsParallelAdvantageousCheck(aa, ab, ac, ba, bb, bc,
					ca, cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
			} else {
				return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
			}
		} else if (oneSolutionLinearCheck(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
			// double solution = (ab-cb)/(aa-ab+ca-cb);
			double solution = (ab - cb) / (ca - aa + bb - cb);

			if (0.5 <= solution && solution <= Finder.MIXED_UPPER_BOUND) {
				return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
			} else {
				if (oneSolutionLinearAdvantageousWholeIntervalCheck(aa, ab, ac,
						ba, bb, bc, ca, cb, cc)) {
					return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
				} else {
					return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
				}
			}
		} else {
			// condition 4
			if (noSolutionNoParallelCheck(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				if (noSolutionNoParallelCheckAdvantageous(aa, ab, ac, ba, bb,
						bc, ca, cb, cc)) {
					return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
				} else {
					return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
				}
			}
			if (oneSolutionNoParallelCheck(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				if (oneSolutionNoParallelCheckAdvantegeous(aa, ab, ac, ba, bb,
						bc, ca, cb, cc)) {
					return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
				} else {
					return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
				}
			}

			if (oneSolutionNoParallelPranoidCheck(aa, ab, ac, ba, bb, bc, ca,
					cb, cc)) {
				if (oneSolutionNoParallelPranoidCheckdAvantegeous(aa, ab, ac,
						ba, bb, bc, ca, cb, cc)) {
					return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
				} else {
					return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
				}
			}

			if (twoSolutionsCheck(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				double zeroLeft = zeroLeft(aa, ab, ac, ba, bb, bc, ca, cb, cc);
				double zeroRight = zeroRight(aa, ab, ac, ba, bb, bc, ca, cb, cc);
				if ((0.5 <= zeroLeft && zeroLeft <= Finder.MIXED_UPPER_BOUND)
						|| (0.5 <= zeroRight && zeroRight <= Finder.MIXED_UPPER_BOUND)) {
					return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
				} else {
					if (twoSolutionsCheckAdvantageous(aa, ab, ac, ba, bb, bc,
							ca, cb, cc)) {
						return new Invasion(equilibriumOrigin,
								Type.ADVANTAGEOUS);
					} else {
						return new Invasion(equilibriumOrigin,
								Type.DISADVANTEGOUS);
					}
				}
			}
		}
		return null;

	}

	private static boolean strategyCisNeutralOfA(double aa, double ab,double ac, double ba, double bb, double bc, double ca, double cb, double cc) {
		return (aa==ca && ab==cb && ac==cc);
	}
	
	private static boolean strategyBisNeutralOfA(double aa, double ab,double ac, double ba, double bb, double bc, double ca, double cb, double cc) {
		return (aa==ba && ab==bb && ac==bc);
	}

	private static boolean strategyCisNeutralOfB(double aa, double ab, double ac, double ba, double bb, double bc, double ca, double cb, double cc) {
		return (ba==ca && bb==cb && bc==cc);
	}

	// TODO HAVE A LOOK AT THIS SHARING SCREEN, FURTHER POINT NEEDED?
	private static boolean twoSolutionsCheckAdvantageous(double aa, double ab,
			double ac, double ba, double bb, double bc, double ca, double cb,
			double cc) {
		double middle = (Finder.MIXED_UPPER_BOUND + 0.5) / 2.0;
		double middlepoint = (Finder.MIXED_UPPER_BOUND + middle) / 2.0;
		double left = (middlepoint * ca) + ((1 - middlepoint) * cb);
		double right = Math.pow(middlepoint, 2.0) * aa + middlepoint
				* (1.0 - middlepoint) * (ab + ba)
				+ Math.pow((1.0 - middlepoint), 2.0) * bb;
		return left > right;
	}

	private static double zeroRight(double aa, double ab, double ac, double ba,
			double bb, double bc, double ca, double cb, double cc) {
		double B = (ab + ba - 2 * bb - ca + cb);
		double A = aa - ab - ba + bb;
		double C = bb - cb;
		return (-B + Math.sqrt(Math.pow(B, 2.0) - 4 * A * C)) / 2 * A;
	}

	private static double zeroLeft(double aa, double ab, double ac, double ba,
			double bb, double bc, double ca, double cb, double cc) {
		double B = (ab + ba - 2 * bb - ca + cb);
		double A = aa - ab - ba + bb;
		double C = bb - cb;
		return (-B - Math.sqrt(Math.pow(B, 2.0) - 4 * A * C)) / 2 * A;
	}

	private static boolean twoSolutionsCheck(double aa, double ab, double ac,
			double ba, double bb, double bc, double ca, double cb, double cc) {
		double one = Math.pow(ab + ba - 2 * bb - ca + cb, 2.0);
		double two = 4 * (aa - ab - ba + bb) * (bb - cb);
		return one - two > 0.0;
	}

	private static boolean oneSolutionNoParallelPranoidCheckdAvantegeous(
			double aa, double ab, double ac, double ba, double bb, double bc,
			double ca, double cb, double cc) {
		double middle = (Finder.MIXED_UPPER_BOUND + 0.5) / 2.0;
		double middlepoint = (Finder.MIXED_UPPER_BOUND + middle) / 2.0;
		double left = (middlepoint * ca) + ((1 - middlepoint) * cb);
		double right = Math.pow(middlepoint, 2.0) * aa + middlepoint
				* (1.0 - middlepoint) * (ab + ba)
				+ Math.pow((1.0 - middlepoint), 2.0) * bb;
		return left > right;

	}

	private static boolean oneSolutionNoParallelPranoidCheck(double aa,
			double ab, double ac, double ba, double bb, double bc, double ca,
			double cb, double cc) {
		double middlepoint = (Finder.MIXED_UPPER_BOUND + 0.5) / 2.0;
		double left = (middlepoint * ca) + ((1 - middlepoint) * cb);
		double right = Math.pow(middlepoint, 2.0) * aa + middlepoint
				* (1.0 - middlepoint) * (ab + ba)
				+ Math.pow((1.0 - middlepoint), 2.0) * bb;
		return left == right;

	}

	private static boolean oneSolutionNoParallelCheckAdvantegeous(double aa,
			double ab, double ac, double ba, double bb, double bc, double ca,
			double cb, double cc) {
		double middlepoint = (Finder.MIXED_UPPER_BOUND + 0.5) / 2.0;
		double left = (middlepoint * ca) + ((1 - middlepoint) * cb);
		double right = Math.pow(middlepoint, 2.0) * aa + middlepoint
				* (1.0 - middlepoint) * (ab + ba)
				+ Math.pow((1.0 - middlepoint), 2.0) * bb;
		return left > right;

	}

	private static boolean oneSolutionNoParallelCheck(double aa, double ab,
			double ac, double ba, double bb, double bc, double ca, double cb,
			double cc) {
		double one = Math.pow(ab + ba - 2 * bb - ca + cb, 2.0);
		double two = 4 * (aa - ab - ba + bb) * (bb - cb);
		return one - two == 0.0;
	}

	private static boolean noSolutionNoParallelCheckAdvantageous(double aa,
			double ab, double ac, double ba, double bb, double bc, double ca,
			double cb, double cc) {
		double middlepoint = (Finder.MIXED_UPPER_BOUND + 0.5) / 2.0;
		double left = (middlepoint * ca) + ((1 - middlepoint) * cb);
		double right = Math.pow(middlepoint, 2.0) * aa + middlepoint
				* (1.0 - middlepoint) * (ab + ba)
				+ Math.pow((1.0 - middlepoint), 2.0) * bb;
		return left > right;
	}

	private static boolean noSolutionNoParallelCheck(double aa, double ab,
			double ac, double ba, double bb, double bc, double ca, double cb,
			double cc) {
		double one = Math.pow(ab + ba - 2 * bb - ca + cb, 2.0);
		double two = 4 * (aa - ab - ba + bb) * (bb - cb);
		return one - two < 0.0;
	}

	private static boolean oneSolutionLinearAdvantageousWholeIntervalCheck(
			double aa, double ab, double ac, double ba, double bb, double bc,
			double ca, double cb, double cc) {
		double middlepoint = (Finder.MIXED_UPPER_BOUND + 0.5) / 2;
		double left = (middlepoint * ca) + ((1 - middlepoint) * cb);
		double right = Math.pow(middlepoint, 2.0) * aa + middlepoint
				* (1.0 - middlepoint) * (ab + ba)
				+ Math.pow((1.0 - middlepoint), 2.0) * bb;
		return left > right;
	}

	private static boolean oneSolutionLinearCheck(double aa, double ab,
			double ac, double ba, double bb, double bc, double ca, double cb,
			double cc) {
		return (aa == ba && ab == bb && ca - aa != cb - ab);
	}

	private static boolean noSolutionsParallelAdvantageousCheck(double aa,
			double ab, double ac, double ba, double bb, double bc, double ca,
			double cb, double cc) {
		double middlepoint = (Finder.MIXED_UPPER_BOUND + 0.5) / 2.0;
		double left = (middlepoint * ca) + ((1 - middlepoint) * cb);
		double right = Math.pow(middlepoint, 2.0) * aa + middlepoint
				* (1.0 - middlepoint) * (ab + ba)
				+ Math.pow((1.0 - middlepoint), 2.0) * bb;
		return left > right;
	}

	private static boolean noSolutionsParallelCheck(double aa, double ab,
			double ac, double ba, double bb, double bc, double ca, double cb,
			double cc) {
		// return (aa==ba && ab==bb && ca-ab==cb-ab && cb-ab!=0);
		return (aa == ba && ab == bb && ca - aa == cb - ab && cb - ab != 0);
	}

	private static boolean allAreEqualAgainstABMix(double aa, double ab, double ac,
			double ba, double bb, double bc, double ca, double cb, double cc) {
		return (aa == ba && ba == ca && ab == bb && bb == cb);
	}

	private static Invasion buildInvasionFromPocket(
			PopulationState equilibriumOrigin,
			PopulationState equilibriumDestiny, double s, double p, double r,
			int rounds, double continuationProbability, double complexityCost, double t)
			throws MalformedInvasionException, GameException {
		if (equilibriumDestiny.getCardinality() == Cardinality.ONE_DIMENSION) {
			if (equilibriumDestiny.getMostPopularStrategy().equals(
					equilibriumOrigin.getMostPopularStrategy())) {
				throw new MalformedInvasionException();
			} else {
				return buildInvasionFrom2StrategiesOneToOne(equilibriumOrigin
						.getMostPopularStrategy(), equilibriumDestiny
						.getMostPopularStrategy(), continuationProbability,complexityCost, r,
						s, t, p, rounds, equilibriumOrigin);
			}

		} else if (equilibriumDestiny.getCardinality() == Cardinality.TWO_DIMENSION) {
			RepeatedGameStrategy str1 = equilibriumOrigin.getMostPopularStrategy();
			RepeatedGameStrategy str2 = null;
			if (equilibriumDestiny.getMostPopularStrategy().equals(str1)) {
				str2 = equilibriumDestiny.getSecondMostPopularStrategy();
			} else if (equilibriumDestiny.getSecondMostPopularStrategy()
					.equals(str1)) {
				str2 = equilibriumDestiny.getMostPopularStrategy();
			} else {
				return new Invasion(equilibriumOrigin, Type.KAPUT);
			}
			return buildInvasionFrom2StrategiesOneToTwo(str1, str2,
					continuationProbability, complexityCost, r, s, t, p, rounds,
					equilibriumOrigin);
		} else if (equilibriumDestiny.getCardinality() == Cardinality.THREE_DIMENSION) {
			return new Invasion(equilibriumOrigin, Type.KAPUT);
		} else if (equilibriumDestiny.getCardinality() == Cardinality.DARK_ZONE) {
			return new Invasion(equilibriumOrigin, Type.KAPUT);
		}
		throw new MalformedInvasionException();
	}

	public static RealMatrixImpl summarize2x2Matrix(RealMatrixImpl matrixImpl) {
		RealMatrixImpl ans = new RealMatrixImpl(2, 2);
		ans.getDataRef()[0][0] = matrixImpl.getData()[0][0]
				- matrixImpl.getData()[1][0];
		ans.getDataRef()[0][1] = 0;
		ans.getDataRef()[1][0] = 0;
		ans.getDataRef()[1][1] = matrixImpl.getData()[1][1]
				- matrixImpl.getData()[0][1];
		return ans;
	}

	public static RealMatrixImpl build2x2Matrix(RepeatedGameStrategy str1, RepeatedGameStrategy str2,
			double continuationProbability, double complexityCost,  double r, double s, double t,
			double p, int rounds) throws GameException {
		RealMatrixImpl matrix = new RealMatrixImpl(2, 2);
		Game oneShot = new PDGame(r, s, t, p);

		StandardRepeatedGame repeatedGame = new StandardRepeatedGame(oneShot,
				continuationProbability, complexityCost);

		matrix.getDataRef()[0][0] = repeatedGame.expectedPayoff1(str1, str1,
				rounds);
		matrix.getDataRef()[0][1] = repeatedGame.expectedPayoff1(str1, str2,
				rounds);
		matrix.getDataRef()[1][0] = repeatedGame.expectedPayoff1(str2, str1,
				rounds);
		matrix.getDataRef()[1][1] = repeatedGame.expectedPayoff1(str2, str2,
				rounds);
		return matrix;
	}

	public static RealMatrixImpl build3x3Matrix(RepeatedGameStrategy str1, RepeatedGameStrategy str2,
			RepeatedGameStrategy str3, double continuationProbability, double complexityCost,double r, double s,
			double t, double p, int rounds) throws GameException {
		RealMatrixImpl matrix = new RealMatrixImpl(3, 3);
		Game oneShot = new PDGame(r, s, t, p);

		StandardRepeatedGame repeatedGame = new StandardRepeatedGame(oneShot,
				continuationProbability,complexityCost);

		matrix.getDataRef()[0][0] = repeatedGame.expectedPayoff1(str1, str1,
				rounds);
		matrix.getDataRef()[0][1] = repeatedGame.expectedPayoff1(str1, str2,
				rounds);
		matrix.getDataRef()[0][2] = repeatedGame.expectedPayoff1(str1, str3,
				rounds);
		matrix.getDataRef()[1][0] = repeatedGame.expectedPayoff1(str2, str1,
				rounds);
		matrix.getDataRef()[1][1] = repeatedGame.expectedPayoff1(str2, str2,
				rounds);
		matrix.getDataRef()[1][2] = repeatedGame.expectedPayoff1(str2, str3,
				rounds);
		matrix.getDataRef()[2][0] = repeatedGame.expectedPayoff1(str3, str1,
				rounds);
		matrix.getDataRef()[2][1] = repeatedGame.expectedPayoff1(str3, str2,
				rounds);
		matrix.getDataRef()[2][2] = repeatedGame.expectedPayoff1(str3, str3,
				rounds);
		return matrix;
	}
	
	public static RealMatrixImpl build4x4Matrix(RepeatedGameStrategy str1, RepeatedGameStrategy str2,
			RepeatedGameStrategy str3, RepeatedGameStrategy str4, double continuationProbability,double complexityCost, double r, double s,
			double t, double p, int rounds) throws GameException {
		RealMatrixImpl matrix = new RealMatrixImpl(4, 4);
		Game oneShot = new PDGame(r, s, t, p);

		StandardRepeatedGame repeatedGame = new StandardRepeatedGame(oneShot,
				continuationProbability,complexityCost);

		matrix.getDataRef()[0][0] = repeatedGame.expectedPayoff1(str1, str1,
				rounds);
		matrix.getDataRef()[0][1] = repeatedGame.expectedPayoff1(str1, str2,
				rounds);
		matrix.getDataRef()[0][2] = repeatedGame.expectedPayoff1(str1, str3,
				rounds);
		matrix.getDataRef()[0][3] = repeatedGame.expectedPayoff1(str1, str4,
				rounds);
		
		
		matrix.getDataRef()[1][0] = repeatedGame.expectedPayoff1(str2, str1,
				rounds);
		matrix.getDataRef()[1][1] = repeatedGame.expectedPayoff1(str2, str2,
				rounds);
		matrix.getDataRef()[1][2] = repeatedGame.expectedPayoff1(str2, str3,
				rounds);
		matrix.getDataRef()[1][3] = repeatedGame.expectedPayoff1(str2, str4,
				rounds);
		
		
		matrix.getDataRef()[2][0] = repeatedGame.expectedPayoff1(str3, str1,
				rounds);
		matrix.getDataRef()[2][1] = repeatedGame.expectedPayoff1(str3, str2,
				rounds);
		matrix.getDataRef()[2][2] = repeatedGame.expectedPayoff1(str3, str3,
				rounds);
		matrix.getDataRef()[2][3] = repeatedGame.expectedPayoff1(str3, str4,
				rounds);
		
		matrix.getDataRef()[3][0] = repeatedGame.expectedPayoff1(str4, str1,
				rounds);
		matrix.getDataRef()[3][1] = repeatedGame.expectedPayoff1(str4, str2,
				rounds);
		matrix.getDataRef()[3][2] = repeatedGame.expectedPayoff1(str4, str3,
				rounds);
		matrix.getDataRef()[3][3] = repeatedGame.expectedPayoff1(str4, str4,
				rounds);
		
		return matrix;
	}

	private static Invasion buildInvasionFrom2StrategiesOneToOne(RepeatedGameStrategy str1,
			RepeatedGameStrategy str2, double continuationProbability, double complexityCost, double r, double s,
			double t, double p, int rounds, PopulationState equilibriumOrigin)
			throws GameException, MalformedInvasionException {
		RealMatrixImpl matrixImpl = build2x2Matrix(str1, str2,
				continuationProbability,complexityCost, r, s, t, p, rounds);
		RealMatrixImpl summary = summarize2x2Matrix(matrixImpl);
		double a1 = summary.getData()[0][0];
		double a2 = summary.getData()[1][1];
		if (a1 <= 0 && a2 >= 0 & (a1 != 0 || a2 != 0)) {
			return new Invasion(equilibriumOrigin, Type.ADV_NEW_DOMINATES_OLD);
		}
		if (a1 > 0 && a2 > 0) {
			return new Invasion(equilibriumOrigin, Type.DIS_COORDINATION);
		}
		if (a1 < 0 && a2 < 0) {
			return new Invasion(equilibriumOrigin, Type.ADV_STEPS_OVER_MIXED);
		}
		if (a1 >= 0 && a2 <= 0 & (a1 != 0 || a2 != 0)) {
			return new Invasion(equilibriumOrigin, Type.DIS_OLD_DOMINATES_NEW);
		}
		if (a1 == 0 && a2 == 0) {
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		}
		throw new MalformedInvasionException();
	}

	private static Invasion buildInvasionFrom2StrategiesOneToTwo(RepeatedGameStrategy str1,
			RepeatedGameStrategy str2, double continuationProbability, double complexityCost, double r, double s,
			double t, double p, int rounds, PopulationState equilibriumOrigin)
			throws GameException, MalformedInvasionException {
		RealMatrixImpl matrixImpl = build2x2Matrix(str1, str2,
				continuationProbability, complexityCost, r, s, t, p, rounds);
		RealMatrixImpl summary = summarize2x2Matrix(matrixImpl);
		double a1 = summary.getData()[0][0];
		double a2 = summary.getData()[1][1];
		if (a1 <= 0 && a2 >= 0 & (a1 != 0 || a2 != 0)) {
			return new Invasion(equilibriumOrigin, Type.ADV_NEW_DOMINATES_OLD);
		}
		if (a1 > 0 && a2 > 0) {
			return new Invasion(equilibriumOrigin, Type.DIS_COORDINATION);
		}
		if (a1 < 0 && a2 < 0) {
			return new Invasion(equilibriumOrigin, Type.ADV_TO_MIXED);
		}
		if (a1 >= 0 && a2 <= 0 & (a1 != 0 || a2 != 0)) {
			return new Invasion(equilibriumOrigin, Type.DIS_OLD_DOMINATES_NEW);
		}
		if (a1 == 0 && a2 == 0) {
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		}
		throw new MalformedInvasionException();
	}

	private static Invasion buildInvasionFrom2StrategiesTwoToOneARemains(
			RepeatedGameStrategy str1, RepeatedGameStrategy str2, double continuationProbability, double complexityCost,
			double r, double s, double t, double p, int rounds,
			PopulationState equilibriumOrigin) throws GameException,
			MalformedInvasionException {
		RealMatrixImpl matrixImpl = build2x2Matrix(str1, str2,
				continuationProbability,complexityCost, r, s, t, p, rounds);
		RealMatrixImpl summary = summarize2x2Matrix(matrixImpl);
		double a1 = summary.getData()[0][0];
		double a2 = summary.getData()[1][1];
		if (a1 <= 0 && a2 >= 0 & (a1 != 0 || a2 != 0)) {
			return new Invasion(equilibriumOrigin, Type.DIS_OLD_DOMINATES_NEW);
		}
		if (a1 > 0 && a2 > 0) {
			return new Invasion(equilibriumOrigin, Type.ADV_COORDINATION);
		}
		if (a1 < 0 && a2 < 0) {
			return new Invasion(equilibriumOrigin, Type.DIS_OUT_OF_MIXED);
		}
		if (a1 >= 0 && a2 <= 0 & (a1 != 0 || a2 != 0)) {
			return new Invasion(equilibriumOrigin, Type.ADV_NEW_DOMINATES_OLD);
		}
		if (a1 == 0 && a2 == 0) {
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		}
		throw new MalformedInvasionException();
	}

	private static Invasion buildInvasionFrom2StrategiesTwoToOneBRemains(
			RepeatedGameStrategy str1, RepeatedGameStrategy str2, double continuationProbability, double complexityCost,
			double r, double s, double t, double p, int rounds,
			PopulationState equilibriumOrigin) throws GameException,
			MalformedInvasionException {
		RealMatrixImpl matrixImpl = build2x2Matrix(str1, str2,
				continuationProbability, complexityCost, r, s, t, p, rounds);
		RealMatrixImpl summary = summarize2x2Matrix(matrixImpl);
		double a1 = summary.getData()[0][0];
		double a2 = summary.getData()[1][1];
		if (a1 <= 0 && a2 >= 0 & (a1 != 0 || a2 != 0)) {
			return new Invasion(equilibriumOrigin, Type.ADV_NEW_DOMINATES_OLD);
		}
		if (a1 > 0 && a2 > 0) {
			return new Invasion(equilibriumOrigin, Type.ADV_COORDINATION);
		}
		if (a1 < 0 && a2 < 0) {
			return new Invasion(equilibriumOrigin, Type.DIS_OUT_OF_MIXED);
		}
		if (a1 >= 0 && a2 <= 0 & (a1 != 0 || a2 != 0)) {
			return new Invasion(equilibriumOrigin, Type.DIS_OLD_DOMINATES_NEW);
		}
		if (a1 == 0 && a2 == 0) {
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		}
		throw new MalformedInvasionException();
	}

	private static Invasion buildInvasionFrom2StrategiesABtoBA(RepeatedGameStrategy str1,
			RepeatedGameStrategy str2, double continuationProbability, double complexityCost, double r, double s,
			double t, double p, int rounds, PopulationState equilibriumOrigin)
			throws GameException, MalformedInvasionException {
		RealMatrixImpl matrixImpl = build2x2Matrix(str1, str2,
				continuationProbability,complexityCost, r, s, t, p, rounds);
		RealMatrixImpl summary = summarize2x2Matrix(matrixImpl);
		double a1 = summary.getData()[0][0];
		double a2 = summary.getData()[1][1];
		if (a1 <= 0 && a2 >= 0 & (a1 != 0 || a2 != 0)) {
			return new Invasion(equilibriumOrigin, Type.ADV_NEW_DOMINATES_OLD);
		}
		if (a1 > 0 && a2 > 0) {
			return new Invasion(equilibriumOrigin, Type.UNKNOWN_COORDINATION);
		}
		if (a1 < 0 && a2 < 0) {
			return new Invasion(equilibriumOrigin,
					Type.UNKNOWN_STEPS_OVER_MIXED);
		}
		if (a1 >= 0 && a2 <= 0 & (a1 != 0 || a2 != 0)) {
			return new Invasion(equilibriumOrigin, Type.DIS_OLD_DOMINATES_NEW);
		}
		if (a1 == 0 && a2 == 0) {
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		}
		throw new MalformedInvasionException();
	}

	// flipepd when checks whole interval
	private static Invasion buildInvasionFrom3StrategiesModified(RepeatedGameStrategy str1,
			RepeatedGameStrategy str2, RepeatedGameStrategy str3, double continuationProbability, double complexityCost,
			double r, double s, double t, double p, int rounds,
			PopulationState equilibriumOrigin) throws GameException {
		RealMatrixImpl matrixImpl = build3x3Matrix(str1, str2, str3,
				continuationProbability, complexityCost,r, s, t, p, rounds);
		double aa = matrixImpl.getData()[0][0];
		double ab = matrixImpl.getData()[0][1];
		double ac = matrixImpl.getData()[0][2];

		double ba = matrixImpl.getData()[1][0];
		double bb = matrixImpl.getData()[1][1];
		double bc = matrixImpl.getData()[1][2];

		double ca = matrixImpl.getData()[2][0];
		double cb = matrixImpl.getData()[2][1];
		double cc = matrixImpl.getData()[2][2];

		if (allAreEqualAgainstABMix(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
			if (ac == bc && bc == cc) {
				return new Invasion(equilibriumOrigin, Type.NEUTRAL);
			} else if (cc <= ac && cc <= bc) {
				return new Invasion(equilibriumOrigin, Type.WEAKLY_ADVANTAGEOUS);
			} else if ((Finder.MIXED_UPPER_BOUND * (cc - ac))
					+ ((1 - Finder.MIXED_UPPER_BOUND) * (cc - bc)) < 0) {
				return new Invasion(equilibriumOrigin, Type.WEAKLY_ADVANTAGEOUS);
			} else if ((0.5 * (cc - ac)) + ((1 - 0.5) * (cc - bc)) < 0) {
				return new Invasion(equilibriumOrigin, Type.WEAKLY_ADVANTAGEOUS);
			} else {
				return new Invasion(equilibriumOrigin,
						Type.WEAKLY_DISADVANTAGEOUS);
			}
		}else  if(strategyCisNeutralOfA(aa, ab, ac, ba, bb, bc, ca, cb, cc) || 
				strategyCisNeutralOfB(aa, ab, ac, ba, bb, bc, ca, cb, cc)){
			return new Invasion(equilibriumOrigin, Type.NEUTRAL);
		} else if (noSolutionsParallelCheck(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
			if (noSolutionsParallelAdvantageousCheck(aa, ab, ac, ba, bb, bc,
					ca, cb, cc)) {
				return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
			} else {
				return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
			}
		} else if (oneSolutionLinearCheck(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
			// double solution = (ab-cb)/(aa-ab+ca-cb);
			double solution = (ab - cb) / (ca - aa + bb - cb);

			if (0.5 <= solution && solution <= Finder.MIXED_UPPER_BOUND) {
				return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
			} else {
				if (oneSolutionLinearAdvantageousWholeIntervalCheck(aa, ab, ac,
						ba, bb, bc, ca, cb, cc)) {
					return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
				} else {
					return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
				}
			}
		} else {
			// condition 4
			if (noSolutionNoParallelCheck(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				if (noSolutionNoParallelCheckAdvantageous(aa, ab, ac, ba, bb,
						bc, ca, cb, cc)) {
					return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
				} else {
					return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
				}
			}
			if (oneSolutionNoParallelCheck(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				if (oneSolutionNoParallelCheckAdvantegeous(aa, ab, ac, ba, bb,
						bc, ca, cb, cc)) {
					return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
				} else {
					return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
				}
			}

			if (oneSolutionNoParallelPranoidCheck(aa, ab, ac, ba, bb, bc, ca,
					cb, cc)) {
				if (oneSolutionNoParallelPranoidCheckdAvantegeous(aa, ab, ac,
						ba, bb, bc, ca, cb, cc)) {
					return new Invasion(equilibriumOrigin, Type.DISADVANTEGOUS);
				} else {
					return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
				}
			}

			if (twoSolutionsCheck(aa, ab, ac, ba, bb, bc, ca, cb, cc)) {
				double zeroLeft = zeroLeft(aa, ab, ac, ba, bb, bc, ca, cb, cc);
				double zeroRight = zeroRight(aa, ab, ac, ba, bb, bc, ca, cb, cc);
				if ((0.5 <= zeroLeft && zeroLeft <= Finder.MIXED_UPPER_BOUND)
						|| (0.5 <= zeroRight && zeroRight <= Finder.MIXED_UPPER_BOUND)) {
					return new Invasion(equilibriumOrigin, Type.ADVANTAGEOUS);
				} else {
					if (twoSolutionsCheckAdvantageous(aa, ab, ac, ba, bb, bc,
							ca, cb, cc)) {
						return new Invasion(equilibriumOrigin,
								Type.DISADVANTEGOUS);
					} else {
						return new Invasion(equilibriumOrigin,
								Type.ADVANTAGEOUS);
					}
				}
			}
		}
		return null;

	}

}
