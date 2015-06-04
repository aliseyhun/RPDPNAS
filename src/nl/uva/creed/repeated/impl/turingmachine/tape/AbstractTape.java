package nl.uva.creed.repeated.impl.turingmachine.tape;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class  AbstractTape {

	public final static String BLANK_SYMBOL = "B";
	
	ArrayList<String> tape;
	int head;
	
	public AbstractTape(int head, ArrayList<String> tape) {
		super();
		this.head = head;
		this.tape = tape;
	}
	
	public AbstractTape(ArrayList<String> tape) {
		super();
		this.tape = tape;
		this.head = 0;
	}
	
	public AbstractTape(String tape) {
		super();
		ArrayList<String> result = new ArrayList<String>();
		CharacterIterator it = new StringCharacterIterator(tape);
	    for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
	    	Character character = new Character(ch); 
	        result.add(character.toString());
	    }
		this.head = 0;
		this.tape = result;
	}
	
	public AbstractTape() {
		super();
		this.head = 0;
		this.tape = new ArrayList<String>();
		this.tape.add(BLANK_SYMBOL);
	}

	public ArrayList<String> getTape() {
		return tape;
	}
	
	public String getTapeAsString() {
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = tape.iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			buffer.append(type);
		}
		return buffer.toString();
	}
	
	public int getHead() {
		return head;
	}
	
	protected int maximumIndex(){
		return this.tape.size()-1;
	}
	
	public abstract void moveRight();
	public abstract void moveLeft();
	public abstract void stay();
	public abstract String read();
	
	 
	//----------------------------------
	
	
	
	
	
	
}
