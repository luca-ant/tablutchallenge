package it.unibo.ai.didattica.competition.tablut.luca;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class Node {

	private State state;
	private double value;
	private Action move;
	

	

	public Node(State state, double value, Action move) {
		super();
		this.state = state;
		this.value = value;
		this.move = move;
	}
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Action getMove() {
		return move;
	}
	public void setMove(Action move) {
		this.move = move;
	}

	public Node(State state) {
		this.state = state;
	}

	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

	
	
	
	
}
