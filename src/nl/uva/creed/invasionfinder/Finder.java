package nl.uva.creed.invasionfinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.evolution.utils.ArrayUtils;
import nl.uva.creed.game.Game;
import nl.uva.creed.game.impl.PDGame;
import nl.uva.creed.invasionfinder.PopulationState.Cardinality;
import nl.uva.creed.invasionfinder.PopulationState.PopulationStateStatus;
import nl.uva.creed.invasionfinder.classifier2.AggregateClassifier;
import nl.uva.creed.repeated.RepeatedGame;
import nl.uva.creed.repeated.exceptions.GameException;
import nl.uva.creed.repeated.exceptions.UnrecognizableString;
import nl.uva.creed.repeated.impl.StandardRepeatedGame;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategy;
import nl.uva.creed.repeated.nash.NashCheck;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.linear.RealMatrixImpl;

import au.com.bytecode.opencsv.CSVReader;

public class Finder {

	// CONSTANTS
	public static Double MIXED_UPPER_BOUND = 0.80;
	private static Integer ROUNDS = 100;
	
	
	// VARIABLES
	private String sourceFilename;
	private int populationSize;
	private double continuationProbability;
	private double complexityCost;
	private double p;
	private double t;
	private double s;
	private double r;
	private boolean containsRun = false;
	private boolean containsSeed = false;

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnrecognizableString
	 * @throws GameException
	 * @throws MalformedInvasionException 
	 * @throws UnclassifiableStatusException 
	 * @throws InvalidStrategyException 
	 */
	public static void main(String[] args) throws IOException,UnrecognizableString, GameException, MalformedInvasionException, UnclassifiableStatusException, InvalidStrategyException {
		
		
		//THIS IS THE FILE THAT CONTAINS THE TIME SERIES
		String fileName;
		if (args.length <= 0 || args.length > 1) {
			//fileName = "interesting.csv";
			fileName = "chunk.txt";
		}else{
			fileName = args[0];	
		}
		Finder finder = new Finder(fileName);
		//This procedure reduces the series by looking at the zones 
		ArrayList<PopulationState> summary = finder.getRawSummary();
		// Here we call the cleaner if it happens to be necessary - ListCleaner.clean(summary);
		
		ArrayList<Invasion> invasions  = InvasionClassifier.classify(summary, finder.s, finder.p, finder.r, ROUNDS, finder.continuationProbability, finder.complexityCost, finder.t);
		ArrayUtils.printoFile(invasions, fileName+"Summary.txt");
		
		//NewClassifier classifier = new NewClassifier(invasions);
		//System.out.println(classifier);
		//System.out.println(classifier.summary());
		//check for time spent in nash populationState 
		AggregateClassifier.classify(invasions);
		
		//print(invasions, summary);
		
		/*ConcurrentHashMap<Integer, Integer> ans  = IndirectInvasionDetector.detect(invasions, finder.s, finder.p, finder.r, ROUNDS, finder.continuationProbability, finder.t);
		System.out.println(ans);
		
*/		
	}

	@SuppressWarnings("unused")
	private static void print(ArrayList<Invasion> invasions, ArrayList<PopulationState> summary) {
		for (Iterator<Invasion> iterator = invasions.iterator(); iterator.hasNext();) {
			Invasion invasion = (Invasion) iterator.next();
			System.out.println(invasion);
		}
		System.out.println(summary.get(summary.size()-1));
	}

	

	@SuppressWarnings("unused")
	private static String getStats(ArrayList<Invasion> invasions) {
		int pure = 0;
		int timeSpentPure = 0;
		int allTime = 0;
		for (Iterator<Invasion> iterator = invasions.iterator(); iterator.hasNext();) {
			Invasion invasion = iterator.next();
			allTime = allTime + invasion.getPopulationState().getTimeSpent();
			if (invasion.getPopulationState().getCardinality() == Cardinality.ONE_DIMENSION) {
				pure++;
				timeSpentPure = timeSpentPure + invasion.getPopulationState().getTimeSpent();
				
			}
		}
		StringBuffer ans = new StringBuffer();
		
		ans.append("pure states " + pure+ "\n");
		ans.append("total states " + invasions.size()+ "\n");
		ans.append("-------------"+ "\n");
		ans.append("time pure " + timeSpentPure+ "\n");
		ans.append("time total " + allTime+ "\n");
		return ans.toString();
	}

	public ArrayList<PopulationState> getRawSummary() throws IOException,
			UnrecognizableString, GameException, UnclassifiableStatusException, InvalidStrategyException {
		//CSVReader reader = processHeader();
		CSVReader reader = processHeaderWithAssortmentAndPopSize();
		
		//List<PopulationState> list = Collections.synchronizedList(new ArrayList<PopulationState>());
		ArrayList<PopulationState> list = new ArrayList<PopulationState>();
		
		// go to the actual data
		String[] nextLine = reader.readNext();
		this.continuationProbability = new Double(nextLine[4]).doubleValue();
		//Row currentRow = Row.create(nextLine, this.isContainsRun(),this.isContainsSeed(),this.populationSize);
		//this.populationSize = new Double(nextLine[2]).intValue();		
		Row currentRow = Row.createWithAssortment(nextLine, this.populationSize);
		//Row currentRow = Row.create(nextLine, true, true, this.populationSize);
		list.add(RowEqulibriumTransformer.transform(currentRow,Cardinality.ONE_DIMENSION, this.getPopulationSize()));
		setEquilibriumStatus(list.get(0));
		while (nextLine != null && !StringUtils.isEmpty(nextLine[0])) {
			//currentRow = Row.create(nextLine, this.isContainsRun(),this.isContainsSeed(),this.populationSize);
			currentRow = Row.createWithAssortment(nextLine, this.populationSize);
			
			
			
			PopulationState populationState = RowEqulibriumTransformer.transform(currentRow, Cardinality.UNDEFINED,this.populationSize);
			if (!Finder.areInTheSameArea(populationState, last(list))) {
				setCardinality(populationState);
				if (populationState.getCardinality() == Cardinality.TWO_DIMENSION && 
						populationState.getFrecMostPopular() == 0.5) {
					//this is unclassifiable so ignore it
				}else{
					setEquilibriumStatus(populationState);
					populationState.incrementTime();
					list.add(populationState);
				}
			}else{
				last(list).incrementTime();
			}
			nextLine = reader.readNext();
		}
		return list;
	}

	//Assign the colour
	private void setEquilibriumStatus(PopulationState populationState) throws GameException, UnclassifiableStatusException, InvalidStrategyException {
		if (populationState.getCardinality() == Cardinality.ONE_DIMENSION) {
			setEquilibriumStatusOneDimension(populationState);
			return;
		}else if (populationState.getCardinality() == Cardinality.TWO_DIMENSION){
			setEquilibriumStatusTwoDimension(populationState);
			return;
	
		}else if (populationState.getCardinality() == Cardinality.THREE_DIMENSION){
			setEquilibriumStatusThreeDimension(populationState);
			return;
	
		} else if (populationState.getCardinality() == Cardinality.DARK_ZONE){
			populationState.setStatus(PopulationStateStatus.HYPERDIMENSIONAL);
			return;
	
		} else{
			throw new RuntimeException("Cardinality is required before setting status!");
		}
	}
	
	private void setEquilibriumStatusThreeDimension(PopulationState populationState) throws GameException, UnclassifiableStatusException, InvalidStrategyException {
		ExplicitAutomatonStrategy st1 = (ExplicitAutomatonStrategy) populationState.getMostPopularStrategy();
		ExplicitAutomatonStrategy st2 = (ExplicitAutomatonStrategy) populationState.getSecondMostPopularStrategy();
		ExplicitAutomatonStrategy st3 = (ExplicitAutomatonStrategy) populationState.getThirdMostPopularStrategy();
		RealMatrixImpl gameMatrix = InvasionClassifier.build3x3Matrix(st1, st2, st3, this.continuationProbability, this.complexityCost, this.r, this.s, this.t, this.p, Finder.ROUNDS);
		int numberOfEquilibria = numberOfNash(st1, st2, st3);
		
		double aa = gameMatrix.getData()[0][0];
		double ab = gameMatrix.getData()[0][1];
		double ac = gameMatrix.getData()[0][2];
		
		double ba = gameMatrix.getData()[1][0];
		double bb = gameMatrix.getData()[1][1];
		double bc = gameMatrix.getData()[1][2];
		
		double ca = gameMatrix.getData()[2][0];
		double cb = gameMatrix.getData()[2][1];
		double cc = gameMatrix.getData()[2][2];
		
		if (numberOfEquilibria ==3) {
			if (DominanceChecker.neutralCondition(aa,ab,ac,ba,bb,bc,ca,cb,cc)) {
				populationState.setStatus(PopulationStateStatus.THREE_ALL_NASH_MIX_NASH);
				return;
			}else{
				populationState.setStatus(PopulationStateStatus.THREE_ALL_NASH_MIX_NOT_NASH);
				return;
			}
		}else if (numberOfEquilibria ==2){
			if (DominanceChecker.dominance(aa,ab,ac,ba,bb,bc,ca,cb,cc)) {
				populationState.setStatus(PopulationStateStatus.THREE_TWO_NASH_ONE_NOT_NASH_MIX_NOT_NASH);
				return;
			}
			populationState.setStatus(PopulationStateStatus.THREE_TWO_NASH_ONE_NOT_NASH);
			return;
		}else if (numberOfEquilibria ==1){
			if (DominanceChecker.dominance(aa,ab,ac,ba,bb,bc,ca,cb,cc)) {
				populationState.setStatus(PopulationStateStatus.THREE_ONE_NASH_TWO_NOT_NASH_MIX_NOT_NASH);
				return;
			}
			populationState.setStatus(PopulationStateStatus.THREE_ONE_NASH_TWO_NOT_NASH);
			return;
		}else{
			
			if (DominanceChecker.neutralCondition(aa,ab,ac,ba,bb,bc,ca,cb,cc)) {
				populationState.setStatus(PopulationStateStatus.THREE_ALL_NOT_NASH_NEUTRAL);
				return;
			}else if (DominanceChecker.dominance(aa,ab,ac,ba,bb,bc,ca,cb,cc)){
				populationState.setStatus(PopulationStateStatus.THREE_ALL_NOT_NASH_MIX_NOT_NASH);
				return;
				
			}else if (isTheMixtureStableNash(aa,ab,ac,ba,bb,bc,ca,cb,cc)){
				populationState.setStatus(PopulationStateStatus.THREE_ALL_NOT_NASH_MIX_COULD_BE_NASH);
				return;
			}else{

				populationState.setStatus(PopulationStateStatus.THREE_ALL_NOT_NASH);
				return;
			}

			
		}
		
		
		
		
		
	}
	
	private boolean isTheMixtureStableNash(double aa, double ab, double ac, double ba,double bb, double bc, double ca, double cb, double cc) {
		
		return aa<ba && aa<ca && bb<ab && bb<cb && cc<ac && cc<bc;
		
	}

	
	private int numberOfNash(ExplicitAutomatonStrategy st1, ExplicitAutomatonStrategy st2, ExplicitAutomatonStrategy st3) throws GameException, InvalidStrategyException{
		int ans = 0;
		if (NashCheck.isNash(st1, this.getRepeatedGame())) {
			ans++;
		}
		if (NashCheck.isNash(st2, this.getRepeatedGame())) {
			ans++;
		}
		if (NashCheck.isNash(st2, this.getRepeatedGame())) {
			ans++;
		}
		return ans;
	}
	
	

	private void setEquilibriumStatusTwoDimension(PopulationState populationState) throws GameException, UnclassifiableStatusException, InvalidStrategyException {
		ExplicitAutomatonStrategy st1 = (ExplicitAutomatonStrategy) populationState.getMostPopularStrategy();
		ExplicitAutomatonStrategy st2 = (ExplicitAutomatonStrategy) populationState.getSecondMostPopularStrategy();
		RealMatrixImpl gameMatrix = InvasionClassifier.build2x2Matrix(st1, st2,this.continuationProbability, this.complexityCost, this.r, this.s, this.t, this.p, Finder.ROUNDS);
		RealMatrixImpl summary = InvasionClassifier.summarize2x2Matrix(gameMatrix);
		double a1 = summary.getData()[0][0];
		double a2 = summary.getData()[1][1];
		
		if (NashCheck.isNash(st1, this.getRepeatedGame()) && NashCheck.isNash(st2, this.getRepeatedGame()) && a1 == 0 && a2 ==0) {
		populationState.setStatus(PopulationStateStatus.TWO_BOTH_NASH_MIX_NASH);
		return;
		}else if(NashCheck.isNash(st1, this.getRepeatedGame()) && NashCheck.isNash(st2, this.getRepeatedGame())){
			populationState.setStatus(PopulationStateStatus.TWO_BOTH_NASH_MIX_NOT_NASH);	
		}
		
		if (justOneIsNash(st1, st2)) {
			if ( (a1<0 && a2>0) || (a1>0 && a2>0) || (a1>0 && a2<0)) {
				populationState.setStatus(PopulationStateStatus.TWO_ONE_NASH_ONE_NOT_NASH_MIX_NOT_NASH);
				return;
			}else{
				populationState.setStatus(PopulationStateStatus.TWO_ONE_NASH_ONE_NOT_NASH);
				
			}
		return;
		}
		
		if (a1 ==0 && a2 ==0 ) {
			populationState.setStatus(PopulationStateStatus.TWO_BOTH_NOT_NASH);
			return;
		}
		
		if ( (a1<0 && a2>0) || (a1>0 && a2>0) || (a1>0 && a2<0)) {
			populationState.setStatus(PopulationStateStatus.TWO_BOTH_NOT_NASH_MIX_NOT_NASH);
			return;
		}
		if (a1<0 && a2<0) {
			populationState.setStatus(PopulationStateStatus.TWO_BOTH_NOT_NASH_MIX_COULD_BE_NASH);
			return;
		}
		
		
		throw new UnclassifiableStatusException();
		
		
		
	}

	private boolean justOneIsNash(ExplicitAutomatonStrategy st1, ExplicitAutomatonStrategy st2) throws GameException, InvalidStrategyException {
		
		if (NashCheck.isNash(st1, this.getRepeatedGame()) && !NashCheck.isNash(st2, this.getRepeatedGame())) {
			return true;
		}
		if (NashCheck.isNash(st2, this.getRepeatedGame()) && !NashCheck.isNash(st1, this.getRepeatedGame())) {
			return true;
		}
		
		return false;
	}

	private RepeatedGame getRepeatedGame() {
		Game oneShot = new PDGame(this.r, this.s, this.t, this.p);
		return new StandardRepeatedGame(oneShot, this.continuationProbability, this.complexityCost);
	}

	private void setEquilibriumStatusOneDimension(PopulationState populationState) throws GameException, InvalidStrategyException {
		ExplicitAutomatonStrategy eq = (ExplicitAutomatonStrategy) populationState.getMostPopularStrategy();
		if (NashCheck.isNash(eq, this.getRepeatedGame())) {
			populationState.setStatus(PopulationStateStatus.PURE_NASH);
		}else{
			populationState.setStatus(PopulationStateStatus.PURE_NOT_NASH);
		}
	}

	

	private void setCardinality(PopulationState currentEquilibrium) {
		if (currentEquilibrium.getFrecMostPopular() >= MIXED_UPPER_BOUND) {
			currentEquilibrium.setCardinality(Cardinality.ONE_DIMENSION);
		}else if (currentEquilibrium.getFrecMostPopular() + currentEquilibrium.getFrecSecondMostPopular() >= MIXED_UPPER_BOUND){
			currentEquilibrium.setCardinality(Cardinality.TWO_DIMENSION);
		}else if (currentEquilibrium.getFrecMostPopular() + currentEquilibrium.getFrecSecondMostPopular() + currentEquilibrium.getFrecThirdMostPopular() >= MIXED_UPPER_BOUND){
			currentEquilibrium.setCardinality(Cardinality.THREE_DIMENSION);
		}else{
			currentEquilibrium.setCardinality(Cardinality.DARK_ZONE);
		}
	}

	private static boolean areInTheSameArea(PopulationState currentEquilibrium, PopulationState last) {
		
		
		
		//If the most popular strategy is the same and the share is at least MAX in both
		if (currentEquilibrium.getMostPopularStrategy().equals(last.getMostPopularStrategy()) && 
			currentEquilibrium.getFrequencyMostPopularStrategy() >= MIXED_UPPER_BOUND && 
			last.getFrequencyMostPopularStrategy() >= MIXED_UPPER_BOUND) {
			return true;
		}
		
		if (currentEquilibrium.getFrecSecondMostPopular() !=null && last.getFrecSecondMostPopular()!= null ) {
			if (currentEquilibrium.getFrecMostPopular() + currentEquilibrium.getFrecSecondMostPopular() >= MIXED_UPPER_BOUND &&
				currentEquilibrium.getFrecMostPopular() < MIXED_UPPER_BOUND && 	
				last.getFrecMostPopular() + last.getFrecSecondMostPopular() >= MIXED_UPPER_BOUND &&
				last.getFrecMostPopular() < MIXED_UPPER_BOUND &&
				currentEquilibrium.getMostPopularStrategy().equals(last.getMostPopularStrategy()) &&
				currentEquilibrium.getSecondMostPopularStrategy().equals(last.getSecondMostPopularStrategy())) {
					return true;
				}
		}
		if (currentEquilibrium.getFrecThirdMostPopular() != null && last.getFrecThirdMostPopular() != null) {
			if (currentEquilibrium.getFrecMostPopular() + currentEquilibrium.getFrecSecondMostPopular() + currentEquilibrium.getFrecThirdMostPopular() >= MIXED_UPPER_BOUND &&
				currentEquilibrium.getFrecMostPopular() + currentEquilibrium.getFrecSecondMostPopular()  < MIXED_UPPER_BOUND &&	
				last.getFrecMostPopular() + last.getFrecSecondMostPopular() + last.getFrecThirdMostPopular() >= MIXED_UPPER_BOUND &&
				last.getFrecMostPopular() + last.getFrecSecondMostPopular() < MIXED_UPPER_BOUND &&
				currentEquilibrium.getMostPopularStrategy().equals(last.getMostPopularStrategy()) &&
				currentEquilibrium.getSecondMostPopularStrategy().equals(last.getSecondMostPopularStrategy()) &&
				currentEquilibrium.getThirdMostPopularStrategy().equals(last.getThirdMostPopularStrategy())) {
				return true;
			}
		}
		return false;
	}

	private PopulationState last(ArrayList<PopulationState> list) {
		return list.get(list.size() - 1);
	}

	@SuppressWarnings("unused")
	private CSVReader processHeader() throws IOException {
		CSVReader reader = SequentialBatchManipulation
				.getReader(this.sourceFilename);
		String[] nextLine;
		nextLine = reader.readNext();
		// let us first go through the header
		while (nextLine.length == 1) {

			// tokenize header to find population size
			if (!StringUtils.isEmpty(nextLine[0])) {
				StringTokenizer tokenizer = new StringTokenizer(nextLine[0],
						": ");
				String token = tokenizer.nextToken();
				if (token.equalsIgnoreCase("PopulationSize")) {
					this.setPopulationSize(new Double(tokenizer.nextToken())
							.intValue());
				}
				if (token.equalsIgnoreCase("ContinuationProbability")) {
					this.setContinuationProbability(new Double(tokenizer
							.nextToken()).doubleValue());
				}
				if (token.equalsIgnoreCase("R")) {
					this.setR(new Double(tokenizer.nextToken()).doubleValue());
				}
				if (token.equalsIgnoreCase("S")) {
					this.setS(new Double(tokenizer.nextToken()).doubleValue());
				}
				if (token.equalsIgnoreCase("T")) {
					this.setT(new Double(tokenizer.nextToken()).doubleValue());
				}
				if (token.equalsIgnoreCase("P")) {
					this.setP(new Double(tokenizer.nextToken()).doubleValue());
				}
			}
			nextLine = reader.readNext();
		}

		// last line of the header - column names
		if (nextLine.length == 5) {
			this.setContainsRun(false);
			this.setContainsSeed(false);
		} else if (nextLine.length == 6) {
			this.setContainsRun(true);
			this.setContainsSeed(false);
		} else if (nextLine.length == 7) {
			this.setContainsRun(true);
			this.setContainsSeed(true);
		}else {
			throw new IllegalArgumentException(
					"File should contain 5 or 6 columns");
		}
		return reader;

	}
	
	
	
	private CSVReader processHeaderWithAssortmentAndPopSize() throws IOException {
		CSVReader reader = SequentialBatchManipulation
				.getReader(this.sourceFilename);
		String[] nextLine;
		nextLine = reader.readNext();
		// let us first go through the header
		while (nextLine.length == 1) {

			// tokenize header to find population size
			if (!StringUtils.isEmpty(nextLine[0])) {
				StringTokenizer tokenizer = new StringTokenizer(nextLine[0],
						": ");
				String token = tokenizer.nextToken();
				if (token.equalsIgnoreCase("PopulationSize")) {
					this.setPopulationSize(new Double(tokenizer.nextToken())
							.intValue());
				}
				if (token.equalsIgnoreCase("ContinuationProbability")) {
					this.setContinuationProbability(new Double(tokenizer
							.nextToken()).doubleValue());
				}
				if (token.equalsIgnoreCase("ComplexityCost")) {
					this.setComplexityCost(new Double(tokenizer
							.nextToken()).doubleValue());
				}
				if (token.equalsIgnoreCase("R")) {
					this.setR(new Double(tokenizer.nextToken()).doubleValue());
				}
				if (token.equalsIgnoreCase("S")) {
					this.setS(new Double(tokenizer.nextToken()).doubleValue());
				}
				if (token.equalsIgnoreCase("T")) {
					this.setT(new Double(tokenizer.nextToken()).doubleValue());
				}
				if (token.equalsIgnoreCase("P")) {
					this.setP(new Double(tokenizer.nextToken()).doubleValue());
				}
			}
			nextLine = reader.readNext();
		}

		
		return reader;

	}

	public Finder(String sourceFilename) {
		super();
		this.sourceFilename = sourceFilename;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public double getContinuationProbability() {
		return continuationProbability;
	}

	public void setContinuationProbability(double continuationProbability) {
		this.continuationProbability = continuationProbability;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public double getS() {
		return s;
	}

	public void setS(double s) {
		this.s = s;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public boolean isContainsRun() {
		return containsRun;
	}

	public void setContainsRun(boolean containsRun) {
		this.containsRun = containsRun;
	}

	public boolean isContainsSeed() {
		return containsSeed;
	}

	public void setContainsSeed(boolean containsSeed) {
		this.containsSeed = containsSeed;
	}

	public double getComplexityCost() {
		return complexityCost;
	}

	public void setComplexityCost(double complexityCost) {
		this.complexityCost = complexityCost;
	}

}
