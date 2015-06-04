package nl.uva.creed.repeated.impl.turingmachine.tape;

import java.util.ArrayList;

public class ReadWriteTape extends ReadOnlyTape {

	public ReadWriteTape(ArrayList<String> tape) {
		super(tape);
		
	}

	public ReadWriteTape(int head, ArrayList<String> tape) {
		super(head, tape);
		
	}

	public ReadWriteTape(String tape) {
		super(tape);
	}
	
	public void write(String symbol){
		this.tape.set(head, symbol);
	}

	public ReadWriteTape() {
		super();
	}

}
