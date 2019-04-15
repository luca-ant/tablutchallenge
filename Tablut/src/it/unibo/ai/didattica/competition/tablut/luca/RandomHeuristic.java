package it.unibo.ai.didattica.competition.tablut.luca;

import java.security.SecureRandom;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public class RandomHeuristic implements Heuristic {

	@Override
	public double heuristic(Node state) {

		return new SecureRandom().nextDouble();
	}

}
