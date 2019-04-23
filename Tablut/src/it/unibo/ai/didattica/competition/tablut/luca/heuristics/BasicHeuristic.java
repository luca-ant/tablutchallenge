package it.unibo.ai.didattica.competition.tablut.luca.heuristics;

import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class BasicHeuristic implements Heuristic {

	private int countB;
	private int countW;
	private int blackNearKing;
	private int kingFreeWay;
	private List<String> citadels;
	private List<String> stars;

	public BasicHeuristic() {
		this.citadels = new ArrayList<String>();
		this.citadels.add("a4");
		this.citadels.add("a5");
		this.citadels.add("a6");
		this.citadels.add("b5");
		this.citadels.add("d1");
		this.citadels.add("e1");
		this.citadels.add("f1");
		this.citadels.add("e2");
		this.citadels.add("i4");
		this.citadels.add("i5");
		this.citadels.add("i6");
		this.citadels.add("h5");
		this.citadels.add("d9");
		this.citadels.add("e9");
		this.citadels.add("f9");
		this.citadels.add("e8");

		this.stars = new ArrayList<String>();
		this.stars.add("a2");
		this.stars.add("a3");
		this.stars.add("a7");
		this.stars.add("a8");
		this.stars.add("b1");
		this.stars.add("b9");
		this.stars.add("c1");
		this.stars.add("c9");
		this.stars.add("g1");
		this.stars.add("g9");
		this.stars.add("h1");
		this.stars.add("h9");
		this.stars.add("i2");
		this.stars.add("i3");
		this.stars.add("i7");
		this.stars.add("i8");
	}

	// BLACK -> MAX
	// WHITE -> MIN
	@Override
	public double heuristic(State state) {
		this.resetValues();
		this.extractValues(state);

		double result = 0;

		if (this.countB > this.countW) {
			result += 0.1;
		} else {
			result -= 0.1;

		}

		if (this.blackNearKing > 0) {
			result += 0.75;
		}
		if (this.blackNearKing == 0 && this.kingFreeWay >= 2) {
			result -= 1;

		}

		return result;
	}

	private void resetValues() {
		this.countB = 0;
		this.countW = 0;
		this.blackNearKing = 0;
		this.kingFreeWay = 0;

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
				
				
				
				

			}
		}

	}

}
