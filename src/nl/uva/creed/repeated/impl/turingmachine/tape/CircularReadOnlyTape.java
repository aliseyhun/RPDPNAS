package nl.uva.creed.repeated.impl.turingmachine.tape;

import java.util.ArrayList;
import java.util.Iterator;

import nl.uva.creed.game.Action;
import nl.uva.creed.repeated.History;

public class CircularReadOnlyTape extends ReadOnlyTape {

	public CircularReadOnlyTape(ArrayList<String> tape) {
		super(tape);
		
	}

	public CircularReadOnlyTape(int head, ArrayList<String> tape) {
		super(head, tape);
		
	}

	public CircularReadOnlyTape(String tape) {
		super(tape);
		
	}

	public CircularReadOnlyTape() {
		super();
	}
	@Override
	public void moveRight(){
		head = head+1;
		if (head > maximumIndex()) {
			head=0;
		}
		return;
	}
	
	@Override
	public void moveLeft(){
		head = head-1;
		if (head < 0) {
			head = maximumIndex();
		}
		return;
	}
	
	public static CircularReadOnlyTape toTape(History history) {
		if (!history.isEmpty()) {
        StringBuffer buffer = new StringBuffer();
		for (Iterator<Action> iterator = history.getActions().iterator(); iterator.hasNext();) {
			Action action = (Action) iterator.next();
			buffer.append(action.toString());
		}
		return new CircularReadOnlyTape(buffer.toString());			
		}else{
			return new CircularReadOnlyTape();
		}
	}

}
