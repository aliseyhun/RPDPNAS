package nl.uva.creed.game.impl;

import nl.uva.creed.game.Action;
import nl.uva.creed.game.Game;
import nl.uva.creed.repeated.exceptions.GameException;

public class PDGame implements Game {

	private double r;
	private double s;
	private double t;
	private double p;

	public PDGame(double r, double s, double t, double p) {
		super();
		this.r = r;
		this.s = s;
		this.t = t;
		this.p = p;
	}

	public PDGame() {
		super();
		this.r = 3.0;
		this.s = 0.0;
		this.t = 4.0;
		this.p = 1.0;
	}

	public double payoffPlayer1(Action player1, Action player2) throws GameException {

		if (player1.equals(Action.COOPERATE)) {
			if (player2.equals(Action.COOPERATE)) {
				return r;

			} else if (player2.equals(Action.DEFECT)) {
				return s;

			}
		} else if (player1.equals(Action.DEFECT)) {
			if (player2.equals(Action.COOPERATE)) {
				return t;

			} else if (player2.equals(Action.DEFECT)) {
				return p;
			}

		}
		throw new GameException("Error calculating payoffs");
	}

	public double payoffPlayer2(Action player1, Action player2) throws GameException {
		if (player1.equals(Action.COOPERATE)) {
			if (player2.equals(Action.COOPERATE)) {
				return r;

			} else if (player2.equals(Action.DEFECT)) {
				return t;
			}
		} else if (player1.equals(Action.DEFECT)) {
			if (player2.equals(Action.COOPERATE)) {
				return s;
			} else if (player2.equals(Action.DEFECT)) {
				return p;
			}
		}
		throw new GameException("Error calculating payoffs");
	}

	public double getR() {
		return r;
	}

	public double getS() {
		return s;
	}

	public double getT() {
		return t;
	}

	public double getP() {
		return p;
	}

}
