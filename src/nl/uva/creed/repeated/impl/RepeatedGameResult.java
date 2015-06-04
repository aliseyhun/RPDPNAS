package nl.uva.creed.repeated.impl;

import nl.uva.creed.game.GameResult;

public class RepeatedGameResult extends GameResult {

	double payoff1WithoutComplexityCost;
	double payoff2WithoutComplexityCost;
	
	public RepeatedGameResult(double payoff1, double payoff2, double payoff1WithoutComplexityCost, double payoff2WithoutComplexityCost) {
		super(payoff1,payoff2);
		this.payoff1WithoutComplexityCost = payoff1WithoutComplexityCost;
		this.payoff2WithoutComplexityCost = payoff2WithoutComplexityCost;
	}


	@Override
	public String toString() {
		return "RepeatedGameResult [ payoff1=" + payoff1
				+ ", payoff2=" + payoff2 + "]";
	}


	public double getPayoff1WithoutComplexityCost() {
		return payoff1WithoutComplexityCost;
	}


	public double getPayoff2WithoutComplexityCost() {
		return payoff2WithoutComplexityCost;
	}

}
