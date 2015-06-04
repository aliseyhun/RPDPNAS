package nl.vu.feweb.evolution.models.stationarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import nl.uva.creed.repeated.RepeatedGameStrategy;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.lang.StringUtils;

public class StationarityAnalysis {

	private Bag populationStatesBag = new HashBag(); 
	
	/**
	 * @return the populationStatesBag
	 */
	public Bag getPopulationStatesBag() {
		return populationStatesBag;
	}
	
	public void add(Set<RepeatedGameStrategy> popState){
		populationStatesBag.add(popState);
	}
	
	public int totalNumberOfStates(){
		return populationStatesBag.size();
	}
	
	public String frequenciesTable(){
		Set<Object> populations = populationStatesBag.uniqueSet();
		double size = new Double(this.totalNumberOfStates());
		ArrayList<String> list = new ArrayList<String>();
		for (Iterator<Object> iterator = populations.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			double frequency = new Double(populationStatesBag.getCount(object))/size;
			list.add(frequency + "\t" + object.toString() + "\n");
		}
		Collections.sort(list, Collections.reverseOrder());
		StringBuffer buffer = new StringBuffer(StringUtils.join(list.toArray()));
		buffer.append("End of run \n");
		return buffer.toString();
	}
	
	public void clear(){
		populationStatesBag.clear();
	}

	public static HashSet<RepeatedGameStrategy> populationStateWithMostRepresentedStrategies(Hashtable<RepeatedGameStrategy, Double> frequenciesTable, double minimumThreshold){
		HashSet<RepeatedGameStrategy> ans = new HashSet<RepeatedGameStrategy>();
		Enumeration<RepeatedGameStrategy> e = frequenciesTable.keys();
		while(e.hasMoreElements()){
			RepeatedGameStrategy strategy = e.nextElement();
			if (frequenciesTable.get(strategy) >= minimumThreshold) {
				ans.add(strategy);
			}
	  	}
		return ans;
	}
	
	
	
}