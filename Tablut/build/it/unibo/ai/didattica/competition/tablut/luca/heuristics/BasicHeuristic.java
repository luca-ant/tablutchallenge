package it.unibo.ai.didattica.competition.tablut.luca.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public class BasicHeuristic implements Heuristic {
	
	
	
	
	
	public BasicHeuristic() {
	}
	
	
	// BLACK -> MAX
	// WHITE -> MIN
	@Override
	public double heuristic(State state) {
		int countB = countBlackPawns(state);
		int countW = countWhitePawns(state);

		if (countB > countW) {
			return 1;
		} else {
			return -1;
			
			
		}

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

}