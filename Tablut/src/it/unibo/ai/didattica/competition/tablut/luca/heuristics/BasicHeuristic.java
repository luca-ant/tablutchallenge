package it.unibo.ai.didattica.competition.tablut.luca.heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class BasicHeuristic implements Heuristic {

	private final static double WEIGHT_DIFF_PAWNS = 1.1;
	private final static double WEIGHT_BLACK_NEAR_KING = 5;
	private final static double WEIGHT_FREE_WAY_KING = 5;
	private final static double WEIGHT_KING_IN_THRONE = 1.5;
	private final static double WEIGHT_KING_ON_STAR = 10;

	private int countB;
	private int countW;
	private int blackNearKing;
	private int kingFreeWay;
	private int kingInThrone;
	private int kingOnStar;

	private Random r;
	private List<String> citadels;
	private List<String> stars;
	private String throne;

	public BasicHeuristic() {
		this.r = new Random(System.currentTimeMillis());

		this.citadels = Arrays.asList("a4", "a5", "a6", "b5", "d1", "e1", "f1", "e2", "i4", "i5", "i6", "h5", "d9",
				"e9", "f9", "e8");

		this.stars = Arrays.asList("a2", "a3", "a7", "a8", "b1", "b9", "c1", "c9", "g1", "g9", "h1", "h9", "i2", "i3",
				"i7", "i8");

		this.throne = "e5";
	}

	// BLACK -> MAX
	// WHITE -> MIN
	@Override
	public double heuristic(State state) {
		
		if(state.getTurn().equalsTurn("WW")) {
			return Double.NEGATIVE_INFINITY;
		}
		if(state.getTurn().equalsTurn("BW")) {
			return Double.POSITIVE_INFINITY;
		}
		
		this.resetValues();
		this.extractValues(state);

		double result = myRandom(-1, 1);
//		double result = 0;

		result += WEIGHT_DIFF_PAWNS * (this.countB - this.countW);

		result += WEIGHT_BLACK_NEAR_KING * this.blackNearKing;

		result -= WEIGHT_FREE_WAY_KING * this.kingFreeWay;

		result -= WEIGHT_KING_IN_THRONE * this.kingInThrone;
		
		result -= WEIGHT_KING_ON_STAR * this.kingOnStar;

		
		
		
		return result;
	}

	private void resetValues() {
		this.countB = 0;
		this.countW = 0;
		this.blackNearKing = 0;
		this.kingFreeWay = 0;
		this.kingInThrone = 0;
		this.kingOnStar = 0;

	}

	private void extractValues(State state) {

		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard().length; j++) {
				// conto le pedine bianche
				if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
						|| state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
					countW++;

				}

				// conto le pedine nere
				if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
						|| state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
					countB++;

				}

				// controllo se il re ha pedine nere intorno
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {

					if (i > 0 && state.getPawn(i - 1, j).equalsPawn(State.Pawn.BLACK.toString())) {
						this.blackNearKing++;
					}

					if (i < state.getBoard().length - 1
							&& state.getPawn(i + 1, j).equalsPawn(State.Pawn.BLACK.toString())) {
						this.blackNearKing++;
					}

					if (j > 0 && state.getPawn(i, j - 1).equalsPawn(State.Pawn.BLACK.toString())) {
						this.blackNearKing++;
					}

					if (j < state.getBoard().length - 1
							&& state.getPawn(i, j + 1).equalsPawn(State.Pawn.BLACK.toString())) {
						this.blackNearKing++;
					}

				}

				// controllo se il re ha vie libere per vincere
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString()) && (i == 1 || i == 2 || i == 6 || i == 7)
						&& (j == 1 || j == 2 || j == 7 || j == 7)) {
					boolean free = true;
					for (int w = 0; w < i; w++) {

						if (!state.getPawn(w, j).equalsPawn(State.Pawn.EMPTY.toString())
								|| this.citadels.contains(state.getBox(w, j))) {
							free = false;
							break;

						}

					}

					if (free) {
						this.kingFreeWay++;
					}

					free = true;

					for (int w = i + 1; w < state.getBoard().length; w++) {

						if (!state.getPawn(w, j).equalsPawn(State.Pawn.EMPTY.toString())
								|| this.citadels.contains(state.getBox(w, j))) {
							free = false;
							break;

						}

					}

					if (free) {
						this.kingFreeWay++;
					}

					free = true;
					for (int w = 0; w < j; w++) {

						if (!state.getPawn(i, w).equalsPawn(State.Pawn.EMPTY.toString())
								|| this.citadels.contains(state.getBox(i, w))) {
							free = false;
							break;
						}

					}

					if (free) {
						this.kingFreeWay++;
					}

					free = true;

					for (int w = i + 1; w < state.getBoard().length; w++) {

						if (!state.getPawn(i, w).equalsPawn(State.Pawn.EMPTY.toString())
								|| this.citadels.contains(state.getBox(i, w))) {
							free = false;
							break;

						}

					}

					if (free) {
						this.kingFreeWay++;
					}

				}

				// controllo se il re è sul trono
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())
						&& state.getBox(i, j).equals(this.throne)) {
					this.kingInThrone = 1;
				}
				
				// controllo se il re è su una stella
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())
						&& this.stars.contains(state.getBox(i, j))) {
					this.kingOnStar = 1;
				}

			}
		}

	}

	private double myRandom(double start, double end) {

		double random = this.r.nextDouble();
		double result = start + (random * (end - start));
		return result;
	}

}
