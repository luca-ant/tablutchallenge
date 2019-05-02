package it.unibo.ai.didattica.competition.tablut.luca.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.luca.algorithms.Node;

public interface Heuristic {

	public double heuristic(State state);

	public double heuristicWhite(State state);

	public double heuristicBlack(State state);

}
