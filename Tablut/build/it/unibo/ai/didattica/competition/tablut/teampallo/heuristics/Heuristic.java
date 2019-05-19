package it.unibo.ai.didattica.competition.tablut.teampallo.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.teampallo.algorithms.Node;

public interface Heuristic {

	public double heuristic(State state);

	public double heuristicWhite(State state);

	public double heuristicBlack(State state);

}
