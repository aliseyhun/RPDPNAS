package nl.uva.creed.invasionfinder;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;

import org.apache.commons.lang.StringUtils;

public class Row {
	
	private Integer run;
	private Integer tick;
	private Double averageFitness;
	private Integer numberOfRules;
	private Double frecMostPopular;
	private Double frecSecondMostPopular;
	private Double frecThirdMostPopular;
	
	private ArrayList<PopulationElement> population;
	
	
	public Integer getRun() {
		return run;
	}
	public Integer getTick() {
		return tick;
	}
	public Double getAverageFitness() {
		return averageFitness;
	}
	public Integer getNumberOfRules() {
		return numberOfRules;
	}
	public Double getFrecMostPopular() {
		return frecMostPopular;
	}
	public ArrayList<PopulationElement> getPopulation() {
		return population;
	}
	
	public static Row create(String[] nextLine, boolean containsRun, boolean containsSeed, int populationSize) throws UnrecognizableString {
		Row row = new Row();
		if (containsRun && !containsSeed) {
			row.run = new Double(nextLine[0]).intValue();
			row.tick = new Double(nextLine[1]).intValue();
			row.averageFitness = new Double(nextLine[2]);
			row.population = createPopulation(nextLine[5]);
			row.population = reduce(row.population);
			row.numberOfRules = row.population.size();
			row.frecMostPopular = new Double(row.population.get(0).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();
			if (row.numberOfRules>=2) {
				row.frecSecondMostPopular = new Double(row.population.get(1).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();	
			}
			if (row.numberOfRules>=3) {
				row.frecThirdMostPopular = new Double(row.population.get(2).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();	
			}
			return row; 
		
		}else if (containsRun && containsSeed){
			row.run = new Double(nextLine[0]).intValue();
			row.tick = new Double(nextLine[1]).intValue();
			row.averageFitness = new Double(nextLine[3]);
			row.population = createPopulation(nextLine[6]);
			row.population = reduce(row.population);
			row.numberOfRules = row.population.size();
			row.frecMostPopular = new Double(row.population.get(0).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();
			if (row.numberOfRules>=2) {
				row.frecSecondMostPopular = new Double(row.population.get(1).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();	
			}
			if (row.numberOfRules>=3) {
				row.frecThirdMostPopular = new Double(row.population.get(2).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();	
			}
			return row;
		}else{
		
			row.run = null;
			row.tick = new Double(nextLine[0]).intValue();
			row.averageFitness = new Double(nextLine[1]);
			row.population = createPopulation(nextLine[4]);
						row.population = reduce(row.population);
			row.numberOfRules = row.population.size();
			row.frecMostPopular = new Double(row.population.get(0).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();
			if (row.numberOfRules>=2) {
				row.frecSecondMostPopular = new Double(row.population.get(1).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();	
			}
			if (row.numberOfRules>=3) {
				row.frecThirdMostPopular = new Double(row.population.get(2).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();	
			}
			
		}
		return row;
	}
	
	public static Row createWithAssortmentAndPopSize(String[] nextLine) throws UnrecognizableString {
		Row row = new Row();
			row.run = new Double(nextLine[0]).intValue();
			row.tick = new Double(nextLine[1]).intValue();
			row.averageFitness = new Double(nextLine[5]);
			row.population = createPopulation(nextLine[8]);
			row.population = reduce(row.population);
			row.numberOfRules = row.population.size();
			PopulationElement.orderByNumberOfCopies(row.population);
			row.frecMostPopular = new Double(row.population.get(0).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();
			if (row.numberOfRules>=2) {
				row.frecSecondMostPopular = new Double(row.population.get(1).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();	
			}
			if (row.numberOfRules>=3) {
				row.frecThirdMostPopular = new Double(row.population.get(2).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();	
			}
			return row;
	}
	

	public static Row createWithAssortmentAndPopSize2(String[] nextLine) throws UnrecognizableString {
		Row row = new Row();
			row.run = new Double(nextLine[0]).intValue();
			row.tick = new Double(nextLine[1]).intValue();
			row.averageFitness = new Double(nextLine[5]);
			row.population = createPopulation(nextLine[10]);
			row.population = reduce(row.population);
			row.numberOfRules = row.population.size();
			PopulationElement.orderByNumberOfCopies(row.population);
			row.frecMostPopular = new Double(row.population.get(0).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();
			if (row.numberOfRules>=2) {
				row.frecSecondMostPopular = new Double(row.population.get(1).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();	
			}
			if (row.numberOfRules>=3) {
				row.frecThirdMostPopular = new Double(row.population.get(2).getNumberOfCopies())/new Double(nextLine[2]).doubleValue();	
			}
			return row;
	}
	
	public static Row createWithAssortment(String[] nextLine, int populationSize) throws UnrecognizableString {
		Row row = new Row();
			row.run = new Double(nextLine[0]).intValue();
			row.tick = new Double(nextLine[1]).intValue();
			row.averageFitness = new Double(nextLine[5]);
			row.population = createPopulation(nextLine[10]);
			//TODO this was [8] instead of [10]
			row.population = reduce(row.population);
			row.numberOfRules = row.population.size();
			PopulationElement.orderByNumberOfCopies(row.population);
			row.frecMostPopular = new Double(row.population.get(0).getNumberOfCopies())/new Double(populationSize).doubleValue();
			if (row.numberOfRules>=2) {
				row.frecSecondMostPopular = new Double(row.population.get(1).getNumberOfCopies())/new Double(populationSize).doubleValue();	
			}
			if (row.numberOfRules>=3) {
				row.frecThirdMostPopular = new Double(row.population.get(2).getNumberOfCopies())/new Double(populationSize).doubleValue();	
			}
			return row;
	}
	
	
	
	//REDUCTION METHOD!!!
	private static ArrayList<PopulationElement> reduce(ArrayList<PopulationElement> population) throws UnrecognizableString {
		for (int i = 0; i < population.size(); i++) {
			//reduce the rule
			ExplicitAutomatonStrategy strategy = ExplicitAutomatonStrategy.readFromString(population.get(i).getRule());
			strategy = ExplicitAutomatonStrategy.minimize(strategy);
			population.get(i).setRule(strategy.toString());
		}
		
		Hashtable<String, Integer> set = new Hashtable<String, Integer>();
		for (int i = 0; i < population.size(); i++) {
			if (!set.containsKey(population.get(i).getRule())) {
				set.put(population.get(i).getRule(), population.get(i).getNumberOfCopies());
			}else{
				set.put(population.get(i).getRule(), population.get(i).getNumberOfCopies()+set.get(population.get(i).getRule()));
			}
		}
		
		ArrayList<PopulationElement> ans = new  ArrayList<PopulationElement>();
		
		for (Iterator<Map.Entry<String, Integer>> iterator = set.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator.next();
			ans.add(new PopulationElement(entry.getValue(), entry.getKey()));
		}
		
		return ans;
	}
	
	
	private static ArrayList<PopulationElement> createPopulation(String string) {
		ArrayList<PopulationElement> ans = new ArrayList<PopulationElement>();
		StringTokenizer tokenizer = new StringTokenizer(string, ";");
		 while (tokenizer.hasMoreTokens()) {
	         PopulationElement element = createPopulationElement(tokenizer.nextToken());
	         ans.add(element);
	     }
		return ans;
	}
	private static PopulationElement createPopulationElement(String nextToken) {
		
		//N: 99 Str: [D 0 0] Avg fit: 10.434343434343434
		PopulationElement  populationElement = new PopulationElement();
		int numberOfCopies = new Integer(StringUtils.substringBetween(nextToken, "N: "," Str:")).intValue();
		populationElement.setNumberOfCopies(numberOfCopies);
		String rule = StringUtils.substringBetween(nextToken, "Str: ", " Avg fit:");
		populationElement.setRule(rule);
		//double averageFitness = new Double(StringUtils.substringAfterLast(nextToken, "Avg fit: ")).doubleValue();
		//populationElement.setAverageFitness(averageFitness);
		return populationElement;
	}
	
	public String getMostPopularRule() {
		PopulationElement mostPopular = population.get(0);
		for (Iterator<PopulationElement> iterator = population.iterator(); iterator.hasNext();) {
			PopulationElement element = (PopulationElement) iterator.next();
			if (element.getNumberOfCopies() > mostPopular.getNumberOfCopies()) {
				mostPopular = element;
			}
		}
		return mostPopular.getRule();
	}
	
	public String getSecondMostPopularRule() {
		String mostPopular = getMostPopularRule();
		ArrayList<PopulationElement> copyArrayList = new ArrayList<PopulationElement>();
		for (Iterator<PopulationElement> iterator = this.population.iterator(); iterator.hasNext();) {
			PopulationElement populationElement = (PopulationElement) iterator.next();
			if (!populationElement.getRule().equalsIgnoreCase(mostPopular)) {
				copyArrayList.add(populationElement);
			}	
		}
		if (copyArrayList.size() == 0) {
			return null;
		}
		if (copyArrayList.size() == 1){
			return copyArrayList.get(0).getRule();
		}
		
		PopulationElement secondmostPopular = copyArrayList.get(0);
		for (Iterator<PopulationElement> iterator = copyArrayList.iterator(); iterator.hasNext();) {
			PopulationElement element = (PopulationElement) iterator.next();
			if (element.getNumberOfCopies() > secondmostPopular.getNumberOfCopies()) {
				secondmostPopular = element;
			}
		}
		return secondmostPopular.getRule();
	}
	public Double getFrecSecondMostPopular() {
		return frecSecondMostPopular;
	}
	public Double getFrecThirdMostPopular() {
		return frecThirdMostPopular;
	}
	
}
