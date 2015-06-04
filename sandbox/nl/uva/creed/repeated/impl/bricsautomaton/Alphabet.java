
package nl.uva.creed.repeated.impl.bricsautomaton;

import java.util.HashMap;
import java.util.Map;

import nl.uva.creed.game.Action;
import nl.vu.feweb.simulation.PseudoRandomSequence;

import org.apache.commons.lang.StringUtils;
/**
 * @deprecated
 * @author jgarcia
 *
 */
public final class Alphabet {

    public static final String REPETITION = "*";
	public static final String ANY = ".";
	public static final String COOPERATE = Action.COOPERATE_STRING;
	public static final String DEFECT = Action.DEFECT_STRING;
	public static final String L_PAR = "(";
	public static final String R_PAR = ")";
	public static final String OR = "|";
	public static final String EMPTY_LANGUAGE = "#";
	public static final String EMPTY_STRING = "()";

	
	public static final String[] ALPHABETE = { OR, ANY, COOPERATE, DEFECT,
			L_PAR, R_PAR, REPETITION, EMPTY_LANGUAGE, EMPTY_STRING };

	public static final String[] NEED_MATCHING = { L_PAR, R_PAR, };
	public static final String[] INCONDITIONAL = { EMPTY_STRING, ANY, COOPERATE, DEFECT, EMPTY_LANGUAGE };
	public static final String[] NOT_BEGINNING = { REPETITION };
	public static final String[] IN_BETWEEN = { OR };
	public static final String[] LEFTS = { L_PAR,  };
	public static final String[] RIGHTS = { R_PAR,  };
	
	
	public static String getMatcher(String character){
		if (!doesItNeedMathing(character)) throw new IllegalArgumentException("Character does not need matching");
		
		Map< String, String> matching = new HashMap<String, String>();
		matching.put(L_PAR, R_PAR);
		matching.put(R_PAR, L_PAR);
		
		return matching.get(String.valueOf(character));
		
	}
	
	private static boolean contains(String character, String[] dictionary){
		return StringUtils.contains(StringUtils.join(dictionary), character);
	}
	
	public static boolean isInAlphabet(String character){
		return contains(character, ALPHABETE);
	}
	
	
	
	public static boolean doesItNeedMathing(String character){
		return contains(character, NEED_MATCHING);
	}
	
	public static boolean isInconditional(String character){
		return contains(character, INCONDITIONAL);
	}
	
	public static boolean isNotToBePlacedAtBeginning(String character){
		return contains(character, NOT_BEGINNING);
	}
	
	public static boolean isInBetween(String character){
		return contains(character, IN_BETWEEN);
	}
	
	public static boolean isLeft(String character){
		return contains(character, LEFTS);
	}
	
	public static boolean isRight(String character){
		return contains(character, RIGHTS);
	}
	
	public static String randomElementFromTheAlphabet(){
		int index = PseudoRandomSequence.nextInt(ALPHABETE.length);
		return ALPHABETE[index];
	}
	
	
	
	

}
