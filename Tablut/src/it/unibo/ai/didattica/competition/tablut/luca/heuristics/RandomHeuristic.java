package it.unibo.ai.didattica.competition.tablut.luca.heuristics;

import java.security.SecureRandom;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.luca.algorithms.Node;

public class RandomHeuristic implements Heuristic {

	private Random r;

	public RandomHeuristic() {
		this.r = new Random();
	}

	// BLACK -> MAX
	// WHITE -> MIN
	@Override
	public double heuristic(State state) {

//		int countB = countBlackPawns(state);

//		int countW = countWhitePawns(state);

//		if (countB > countW) {
//			return myRandom(0, 1);
//		} else {
//			return myRandom(-1, 0);
//		}

		
		return myRandom(-1, 1);
	}

	private int countWhitePawns(State state) {
		int result = 0;

		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard().length; j++) {
				if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
						|| state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
					result++;

				}
			}
		}
		return result;
	}

	private int countBlackPawns(State state) {
		int result = 0;
		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard().length; j++) {
				if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
					result++;
				}
			}
		}

		return result;
	}

	private double myRandom(double start, double end) {

		double random = r.nextDouble();
		double result = start + (random * (end - start));
		return result;
	}
}
