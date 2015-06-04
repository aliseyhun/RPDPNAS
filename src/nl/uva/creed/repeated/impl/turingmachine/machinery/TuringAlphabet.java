package nl.uva.creed.repeated.impl.turingmachine.machinery;

import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.impl.turingmachine.tape.AbstractTape;

import org.apache.commons.lang.StringUtils;

public class TuringAlphabet {

	
	//first tape
	public static final String COOPERATE = Action.COOPERATE_STRING;
	public static final String DEFECT = Action.DEFECT_STRING;
	
	
	//second tape
	
	public static final String ONE = "1";
	public static final String ZERO = "0";
	
	public static final String NULL = "*";
	
	//shared
	public static final String BLANK = AbstractTape.BLANK_SYMBOL;
	
	
	//head actions
	public static final String RIGHT = "R";
	public static final String LEFT = "L";
	public static final String STAY = "S";
	
	
	//tHINGS I CAN READ FROM READING TAPE
	//public static final String[] CAN_READ_READING_TAPE = { BLANK, COOPERATE, DEFECT};
	public static final String[] CAN_READ_READING_TAPE = { BLANK, COOPERATE, DEFECT};
	
	//tHINGS I CAN READ FROM WRITING TAPE
	public static final String[] CAN_READ_WRITING_TAPE = { BLANK, COOPERATE, DEFECT};
	
	//tHINGS I CAN WRITE TO THE WRITING TAPE
	public static final String[] CAN_WRITE_WRITING_TAPE = { BLANK, COOPERATE, DEFECT, NULL};
	
	//Actions
	public static final String[] ACTIONS = { RIGHT, LEFT, STAY};
	
	
	public boolean isValidWord(String symbol){
		return contains(symbol, CAN_READ_READING_TAPE) || contains(symbol, CAN_READ_WRITING_TAPE)
		|| contains(symbol, CAN_WRITE_WRITING_TAPE) || contains(symbol, ACTIONS);
	}
	
	private static boolean contains(String character, String[] dictionary){
		return StringUtils.contains(StringUtils.join(dictionary), character);
	}
	
	
	
	
}
