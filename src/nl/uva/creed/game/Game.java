package nl.uva.creed.game;

import nl.uva.creed.repeated.exceptions.GameException;


public interface Game {

	public double payoffPlayer1(Action player1, Action player2)throws GameException;
	public double payoffPlayer2(Action player1, Action player2) throws GameException;
	
}
