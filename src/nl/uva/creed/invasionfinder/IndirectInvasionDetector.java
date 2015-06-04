package nl.uva.creed.invasionfinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nl.uva.creed.invasionfinder.Invasion.Type;
import nl.uva.creed.repeated.exceptions.GameException;

public class IndirectInvasionDetector {

	public static Type neutral[] = {Type.NEUTRAL};
	public static Type advantageous[] = {Type.ADVANTAGEOUS, Type.ADV_COORDINATION, Type.ADV_NEW_DOMINATES_OLD, Type.ADV_STEPS_OVER_MIXED, Type.ADV_TO_MIXED};
	
	
	
	public static ConcurrentHashMap<Integer, Integer> detect(ArrayList<Invasion> list, double s,double p, double r, int rounds, double continuationProbability, double complexityCost, double t) throws MalformedInvasionException, GameException{
		ConcurrentHashMap<Integer, Integer> candidates = candidates(list);
		
		Set<Integer> starting  = candidates.keySet();
		
		for (Iterator<Integer> iterator = starting.iterator(); iterator.hasNext();) {
			Integer startingPoint = (Integer) iterator.next();
			Integer endingPoint = candidates.get(startingPoint);
			if (!isIndirect(startingPoint, endingPoint, list, s, p, r, rounds, continuationProbability, complexityCost, t)) {
				candidates.remove(startingPoint);
			}else{
				System.out.println("Indirect from " + startingPoint + " to " + endingPoint);
			}
			
		}
		return candidates;
	}
	
	
	protected static boolean isIndirect(Integer startingPoint,Integer endingPoint, ArrayList<Invasion> list, double s, double p, double r, int rounds, double continuationProbability, double complexityCost, double t) throws MalformedInvasionException, GameException {
		PopulationState start = list.get(startingPoint).getPopulationState();
		PopulationState end = list.get(endingPoint).getPopulationState();
		
		
		System.out.println("from " + start.getCardinality() + "/" +list.get(startingPoint).getType() + " to " +
				end.getCardinality() + "/" +list.get(endingPoint).getType());
		
		PopulationState origin = list.get(startingPoint).getPopulationState();
		PopulationState destiny = list.get(endingPoint+1).getPopulationState();
		
		Invasion inv  = InvasionClassifier.buildInvasion(origin,destiny, s, p, r, rounds, continuationProbability, complexityCost,  t);
		System.out.println(inv.getType());
		
		/*for (int i = startingPoint; i< endingPoint; i++) {
			
		}*/
		
		return false;
	}


	public static ConcurrentHashMap<Integer, Integer> candidates(ArrayList<Invasion> list){
		ConcurrentHashMap<Integer, Integer> candidates = new ConcurrentHashMap<Integer, Integer>();
		for (int i = 0; i < list.size(); i++) {
			
			if (isNeutral(list.get(i).getType())) {
				int endsAt  = check(i, list);
				if (endsAt !=-1) {
					candidates.put(i, endsAt);
					i=endsAt+1;
				}
				
			}
		}
		return candidates;
	}
	
	
	
	
	private static int check(int i, ArrayList<Invasion> list) {
		while (i < list.size() && isNeutral(list.get(i).getType())  ) {
			i++;
		}
		if (list.size() == i) {
			return -1;
		}
		if (isAdvantageous(list.get(i).getType())) {
			return i;
		}else{
			return -1;
		}
	}




	protected static boolean isNeutral(Type type){
		return contains(neutral, type);
	}
	
	protected static boolean isAdvantageous(Type type){
		return contains(advantageous, type);
	}
	
	private static boolean contains(Type[] array, Type type){
		for (int i = 0; i < array.length; i++) {
			if (type == array[i]) return true;
		}
		return false;
	}
	

}
