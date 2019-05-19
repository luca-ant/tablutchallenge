package it.unibo.ai.didattica.competition.tablut.luca.heuristics;

import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

public class RandomHeuristic implements Heuristic {

	private Random r;

	public RandomHeuristic() {
		this.r = new Random();
	}

	@Override
		public double heuristic(State state) {

		return myRandom(-1, 1);
	}
	
	public double heuristicBlack(State state) {

		return myRandom(-1, 1);
	}
	public double heuristicWhite(State state) {

		return myRandom(-1, 1);
	}

	private double myRandom(double start, double end) {

		double random = r.nextDouble();
		double result = start + (random * (end - start));
		return result;
	}
}
