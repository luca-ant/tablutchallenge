package it.unibo.ai.didattica.competition.tablut.luca;


import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public abstract class MyState extends State {
	public abstract List<Action> getNextMoves();

	protected abstract MyState calculateNextState();

}
