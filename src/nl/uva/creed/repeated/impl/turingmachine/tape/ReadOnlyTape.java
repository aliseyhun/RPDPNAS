package nl.uva.creed.repeated.impl.turingmachine.tape;

import java.util.ArrayList;
import java.util.Iterator;

import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.History;

public class ReadOnlyTape extends AbstractTape {

	public ReadOnlyTape(ArrayList<String> tape) {
		super(tape);
	}

	public ReadOnlyTape(int head, ArrayList<String> tape) {
		super(head, tape);
		
	}

	public ReadOnlyTape(String tape) {
		super(tape);
	}
	
	public void moveRight(){
		head = head+1;
		if (head > maximumIndex()) {
			tape.add(BLANK_SYMBOL);
		}
		if (head > maximumIndex()) {
			throw new IllegalStateException("head > maximum index");
		}
		return;
	}
	
	public void moveLeft(){
		head = head-1;
		if (head < 0) {
			tape.add(0,BLANK_SYMBOL);
			head = 0;
		}
		return;
	}
	
	public void stay(){
		return;
	}
	
	public String read(){
		return tape.get(head);
	}

	public ReadOnlyTape() {
		super();
	}

	public static ReadOnlyTape toTape(History history) {
		if (!history.isEmpty()) {
        StringBuffer buffer = new StringBuffer();
		for (Iterator<Action> iterator = history.getActions().iterator(); iterator.hasNext();) {
			Action action = (Action) iterator.next();
			buffer.append(action.toString());
		}
		//start head at the last movement
		buffer = buffer.reverse();
		return new ReadOnlyTape(buffer.toString());			
		}else{
			return new ReadOnlyTape();
		}
	}

	

}
