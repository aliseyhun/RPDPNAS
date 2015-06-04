package nl.uva.creed.repeated;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

import nl.uva.creed.game.Action;
import nl.vu.feweb.simulation.PseudoRandomSequence;

import org.apache.commons.lang.StringUtils;

public class History {

	private List<Action> actions = null;

	public History() {
		super();
		this.actions = new ArrayList<Action>();
	}

	public History getCopy() {
		History ans = new History();
		ans.actions.addAll(this.actions);
		return ans;
	}

	public void addMove(Action action) {
		actions.add(action);
	}

	public boolean isEmpty() {
		return actions.isEmpty();
	}

	public String toString() {
		return StringUtils.join(actions, null);
	}

	public Action getActionAtTime(int index) {
		if (this.isEmpty() || index >= actions.size() || index < 0)
			return null;
		return actions.get(index);
	}

	public Action getLastAction() {
		return actions.get(actions.size() - 1);
	}

	public List<Action> getActions() {
		return actions;
	}

	public static History artificialHistory(String string) {
		History history = new History();
		CharacterIterator it = new StringCharacterIterator(string);

		// Iterate over the characters in the forward direction
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			if (ch == Action.COOPERATE_STRING.charAt(0)) {
				history.addMove(Action.COOPERATE);
			} else {
				history.addMove(Action.DEFECT);
			}
		}
		return history;
	}

	public static History concatenate(History initialHistory, History history) {

		StringBuffer representation = new StringBuffer();
		representation.append(initialHistory.toString());
		representation.append(history.toString());
		return History.artificialHistory(representation.toString());
	}
	
	public static History random(int size) {
		History history = new History();
		for (int i = 0; i < size; i++) {
			if (PseudoRandomSequence.nextBoolean()) {
				history.addMove(Action.COOPERATE);
			}else{
				history.addMove(Action.DEFECT);
			}
		}
		
		return history;
	}

}
