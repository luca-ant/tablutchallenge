package it.unibo.ai.didattica.competition.tablut.luca.heuristics;

import java.security.SecureRandom;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.luca.algorithms.Node;

public class RandomHeuristic implements Heuristic {

	@Override
	public double heuristic(Node state) {

		return new Random(System.currentTimeMillis()).nextDouble();
	}

}
