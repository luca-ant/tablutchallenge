package it.unibo.ai.didattica.competition.tablut.luca;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class Node {

	private MyState state;
	private double value;
	private Action move;
	

	

	public Node(MyState state, double value, Action move) {
		super();
		this.state = state;
		this.value = value;
		this.move = move;
	}
	
	public MyState getState() {
		return state;
	}
	public void setState(MyState state) {
		this.state = state;
	}
	public Action getMove() {
		return move;
	}
	public void setMove(Action move) {
		this.move = move;
	}

	public Node(MyState state) {
		this.state = state;
	}

	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

	
	
	
	
}
