package nl.uva.creed.game;

public class GameResult {

	protected double payoff1;
	protected double payoff2;
	
	public GameResult(double payoff1, double payoff2) {
		super();
		this.payoff1 = payoff1;
		this.payoff2 = payoff2;
	}

	public double getPayoff() {
		return payoff1;
	}

	public double getPayoff2() {
		return payoff2;
	}

	

}
