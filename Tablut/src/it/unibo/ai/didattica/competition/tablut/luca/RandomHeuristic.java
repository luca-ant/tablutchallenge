package it.unibo.ai.didattica.competition.tablut.luca;

import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public class RandomHeuristic implements Heuristic {

	@Override
	public double heuristic(State state) {

		return new Random().nextDouble();
	}

}
