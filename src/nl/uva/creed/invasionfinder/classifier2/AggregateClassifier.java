package nl.uva.creed.invasionfinder.classifier2;

import java.util.ArrayList;

import nl.uva.creed.invasionfinder.Invasion;

public class AggregateClassifier {
	
	public static void classify(ArrayList<Invasion> list) {
		EquilibriumCounter counter = new EquilibriumCounter(list);
		System.out.println(counter.toString());
		System.out.println("----------------------");
		@SuppressWarnings("unused")
		ClassifierOneDimension oneD = new ClassifierOneDimension(list);
		System.out.println("----------------------");
		@SuppressWarnings("unused")
		ClassifierTwoDimensions twoD = new ClassifierTwoDimensions(list);
		System.out.println("----------------------");
		@SuppressWarnings("unused")
		ClassifierThreeDimensions threeD = new ClassifierThreeDimensions(list);
		System.out.println("----------------------");
	}
}
