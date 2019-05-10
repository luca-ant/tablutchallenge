package it.unibo.ai.didattica.competition.tablut.teampallo.heuristics;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;

public class AdvancedHeuristic implements Heuristic {

	private static double BLACK_WEIGHT_DIFF_PAWNS = 1.5;
	private static double BLACK_WEIGHT_COUNT_WHITE_PAWNS = 2.5;
	private static double BLACK_WEIGHT_COUNT_BLACK_PAWNS = 2;
	private static double BLACK_WEIGHT_FREE_WAY_KING = 5;
	private static double BLACK_WEIGHT_KING_OVERHANGED = 1.5;
	private static double BLACK_WEIGHT_KING_ON_STAR = 10;
	private static double BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED = 1;
	private static double BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED = 2;
	private static double BLACK_WEIGHT_BLACKBARRIER = 2.5;
	private static double BLACK_WEIGHT_Q = 2;

	private static double WHITE_WEIGHT_DIFF_PAWNS = 5;
	private static double WHITE_WEIGHT_COUNT_WHITE_PAWNS = 5;
	private static double WHITE_WEIGHT_COUNT_BLACK_PAWNS = 3;
	private static double WHITE_WEIGHT_WHITE_NEAR_KING = 1.5;
	private static double WHITE_WEIGHT_FREE_WAY_KING = 10;
	private static double WHITE_WEIGHT_KING_ON_THRONE = 0.5;
	private static double WHITE_WEIGHT_KING_NEAR_THRONE = 0.3;
	private static double WHITE_WEIGHT_KING_ON_STAR = 10;
	private static double WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED = 5;
	private static double WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED = 7;

	// *** OK ***

//	private static double BLACK_WEIGHT_DIFF_PAWNS = 5;
//	private static double BLACK_WEIGHT_COUNT_WHITE_PAWNS = 3;
//	private static double BLACK_WEIGHT_COUNT_BLACK_PAWNS = 2;
//	private static double BLACK_WEIGHT_BLACK_NEAR_KING = 3;
//	private static double BLACK_WEIGHT_FREE_WAY_KING = 7;
//	private static double BLACK_WEIGHT_KING_ON_STAR = 10;
//	private static double BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED = 1.5;
//	private static double BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED = 2;
//	private static double BLACK_WEIGHT_BLACKBARRIER = 2.3;
//
//	private static double WHITE_WEIGHT_DIFF_PAWNS = 3;
//	private static double WHITE_WEIGHT_COUNT_WHITE_PAWNS = 4;
//	private static double WHITE_WEIGHT_COUNT_BLACK_PAWNS = 3;
//	private static double WHITE_WEIGHT_BLACK_NEAR_KING = 3;
//	private static double WHITE_WEIGHT_WHITE_NEAR_KING = 1.5;
//	private static double WHITE_WEIGHT_FREE_WAY_KING = 10;
//	private static double WHITE_WEIGHT_KING_ON_THRONE = 2;
//	private static double WHITE_WEIGHT_KING_NEAR_THRONE = 1.5;
//	private static double WHITE_WEIGHT_KING_ON_STAR = 10;
//	private static double WHITE_WEIGHT_KING_FROM_BORDER = 0;
//	private static double WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED = 2;
//	private static double WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED = 1.5;

	// *** ***

	private int countB;
	private int countW;
	private int kingOverhanged;
	private int whiteNearKing;
	private int kingFreeWay;
	private int kingOnThrone;
	private int kingNearThrone;
	private int kingOnStar;
	private int blackPawnsOverhanged;
	private int whitePawnsOverhanged;
	private int blackBarrierPawns;
	private int blackPawnsQ1;
	private int blackPawnsQ2;
	private int blackPawnsQ3;
	private int blackPawnsQ4;
	private int kingInQ1;
	private int kingInQ2;
	private int kingInQ3;
	private int kingInQ4;

	// Q1 Q2
	// Q3 Q4

	private Random r;
	private List<String> citadels;
	private List<String> stars;
	private List<String> blackBarrier;
	private List<String> nearsThrone;
	private String throne;

	public AdvancedHeuristic() {
		this.r = new Random(System.currentTimeMillis());

		this.citadels = Arrays.asList("a4", "a5", "a6", "b5", "d1", "e1", "f1", "e2", "i4", "i5", "i6", "h5", "d9",
				"e9", "f9", "e8");

		this.stars = Arrays.asList("a2", "a3", "a7", "a8", "b1", "b9", "c1", "c9", "g1", "g9", "h1", "h9", "i2", "i3",
				"i7", "i8");

		this.nearsThrone = Arrays.asList("e4", "e6", "d5", "f5");
		this.throne = "e5";

//		this.blackBarrier = Arrays.asList("b3", "b7", "c2", "c8", "g2", "g8", "h3", "h7");

		this.blackBarrier = Arrays.asList("c4", "c6", "d3", "d7", "f3", "f7", "g4", "g6");

	}

	private void printValues() {

		double diff = countB - countW;

		System.out.println("countB - countW = " + diff);
		System.out.println("countB = " + this.countB);
		System.out.println("countW = " + this.countW);
		System.out.println("kingOverhanged = " + this.kingOverhanged);
		System.out.println("whiteNearKing = " + this.whiteNearKing);
		System.out.println("kingFreeWay = " + this.kingFreeWay);
		System.out.println("kingOnThrone = " + this.kingOnThrone);
		System.out.println("kingNearThrone = " + this.kingNearThrone);
		System.out.println("kingOnStar = " + this.kingOnStar);
		System.out.println("blackPawnsOverhanged = " + this.blackPawnsOverhanged);
		System.out.println("whitePawnsOverhanged = " + this.whitePawnsOverhanged);
		System.out.println("blackPawnsQ1 = " + this.blackPawnsQ1);
		System.out.println("blackPawnsQ2 = " + this.blackPawnsQ2);
		System.out.println("blackPawnsQ3 = " + this.blackPawnsQ3);
		System.out.println("blackPawnsQ4 = " + this.blackPawnsQ4);
		System.out.println("kingInQ1 = " + this.kingInQ1);
		System.out.println("kingInQ2 = " + this.kingInQ2);
		System.out.println("kingInQ3 = " + this.kingInQ3);
		System.out.println("kingInQ4 = " + this.kingInQ4);

	}

	@Override
	public double heuristic(State state) {

		if (GameManager.getInstance().getPlayer().equalsIgnoreCase("white")) {
			return this.heuristicWhite(state);
		} else if (GameManager.getInstance().getPlayer().equalsIgnoreCase("black")) {
			return this.heuristicBlack(state);
		}

		return 0;

	}

	public double heuristicWhite(State state) {

		this.resetValues();
		this.extractValues(state);

		// printValues();

		// double result = myRandom(-1, 1);
		double result = 0;

//		result -= WHITE_WEIGHT_DIFF_PAWNS * (((double)this.countW/16) - ((double)this.countB/9));

		result -= WHITE_WEIGHT_COUNT_BLACK_PAWNS * ((double) this.countB / 16);

		result -= WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED * ((double) this.whitePawnsOverhanged / this.countW);

		// result -= WHITE_WEIGHT_BLACK_NEAR_KING * (this.kingOverhanged);

		result += WHITE_WEIGHT_COUNT_WHITE_PAWNS * ((double) this.countW / 9);

//		result += WHITE_WEIGHT_WHITE_NEAR_KING * this.whiteNearKing;

		result += WHITE_WEIGHT_FREE_WAY_KING * ((double) this.kingFreeWay / 4);

		result += WHITE_WEIGHT_KING_ON_THRONE * this.kingOnThrone;

//		result += WHITE_WEIGHT_KING_NEAR_THRONE * this.kingNearThrone;

		result += WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED * ((double) this.blackPawnsOverhanged / this.countB);

		result += WHITE_WEIGHT_KING_ON_STAR * this.kingOnStar;

		return result;
	}

	public double heuristicBlack(State state) {

		
		if(state.getTurn().equalsTurn("BW")) {
			return 100;
		}
		this.resetValues();
		this.extractValues(state);

	//	printValues();

//		double result = myRandom(-1, 1);
		double result = 0;

		result += BLACK_WEIGHT_DIFF_PAWNS * (((double) this.countB / 16) - ((double) this.countW / 9));

		result += BLACK_WEIGHT_COUNT_BLACK_PAWNS * ((double) this.countB / 16);

		result += BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED * ((double) this.whitePawnsOverhanged / this.countW);

		result += BLACK_WEIGHT_KING_OVERHANGED * ((double) this.kingOverhanged);

		result -= BLACK_WEIGHT_COUNT_WHITE_PAWNS * ((double) this.countW / 9);

		result -= BLACK_WEIGHT_FREE_WAY_KING * ((double) this.kingFreeWay / 4);

		result -= BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED * ((double) this.blackPawnsOverhanged / this.countB);

		result -= BLACK_WEIGHT_KING_ON_STAR * this.kingOnStar;

		result += BLACK_WEIGHT_BLACKBARRIER * ((double) this.blackBarrierPawns / 8);

		result += BLACK_WEIGHT_Q * ((double) this.blackPawnsQ1 / this.countB) * this.kingInQ1;

		result += BLACK_WEIGHT_Q * ((double) this.blackPawnsQ2 / this.countB) * this.kingInQ2;

		result += BLACK_WEIGHT_Q * ((double) this.blackPawnsQ3 / this.countB) * this.kingInQ3;

		result += BLACK_WEIGHT_Q * ((double) this.blackPawnsQ4 / this.countB) * this.kingInQ4;

		return result;
	}

	private void resetValues() {
		this.countB = 0;
		this.countW = 0;
		this.kingOverhanged = 0;
		this.whiteNearKing = 0;
		this.kingFreeWay = 0;
		this.kingOnThrone = 0;
		this.kingOnStar = 0;
		this.kingNearThrone = 0;
		this.blackPawnsOverhanged = 0;
		this.whitePawnsOverhanged = 0;
		this.blackBarrierPawns = 0;
		this.blackPawnsQ1 = 0;
		this.blackPawnsQ2 = 0;
		this.blackPawnsQ3 = 0;
		this.blackPawnsQ4 = 0;
		this.kingInQ1 = 0;
		this.kingInQ2 = 0;
		this.kingInQ3 = 0;
		this.kingInQ4 = 0;

	}

	private void extractValues(State state) {

		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard().length; j++) {
				// conto le pedine bianche
				if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
						|| state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
					this.countW++;

				}

				// conto le pedine nere
				if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
					this.countB++;

					// conto le nere che formano la barriera
					if (this.blackBarrier.contains(state.getBox(i, j))) {
						this.blackBarrierPawns++;
					}

					// conto le pedine nere in Q1
					if ((i >= 0 && i <= state.getBoard().length / 2) && (j >= 0 && j <= state.getBoard().length / 2)) {
						this.blackPawnsQ1++;
					}

					// conto le pedine nere in Q2
					if ((i >= 0 && i <= state.getBoard().length / 2)
							&& (j >= state.getBoard().length / 2 & j < state.getBoard().length)) {
						this.blackPawnsQ2++;
					}
					// conto le pedine nere in Q3
					if ((i >= state.getBoard().length / 2 && i < state.getBoard().length)
							&& (j >= 0 && j <= state.getBoard().length / 2)) {
						this.blackPawnsQ3++;
					}

					// conto le pedine nere in Q4
					if ((i >= state.getBoard().length / 2 && i < state.getBoard().length)
							&& (j >= state.getBoard().length / 2 && j < state.getBoard().length)) {
						this.blackPawnsQ4++;
					}

				}

				// conto le pedine nere con una bianca o un accampamento o il trono vicino
				if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())
						&& !this.citadels.contains(state.getBox(i, j).toString())) {

					if (i > 0
							&& (state.getPawn(i - 1, j).equalsPawn(State.Pawn.WHITE.toString())
									|| state.getPawn(i - 1, j).equalsPawn(State.Pawn.KING.toString())
									|| this.citadels.contains(state.getBox(i - 1, j))
									|| state.getBox(i - 1, j).equals(this.throne))
							&& i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.EMPTY.toString()))) {

						this.blackPawnsOverhanged++;
					}

					else if (i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.WHITE.toString())
									|| state.getPawn(i + 1, j).equalsPawn(State.Pawn.KING.toString())
									|| this.citadels.contains(state.getBox(i + 1, j))
									|| state.getBox(i + 1, j).equals(this.throne))
							&& i > 0 && (state.getPawn(i - 1, j).equalsPawn(State.Pawn.EMPTY.toString()))) {
						this.blackPawnsOverhanged++;

					}

					else if (j > 0
							&& (state.getPawn(i, j - 1).equalsPawn(State.Pawn.WHITE.toString())
									|| state.getPawn(i, j - 1).equalsPawn(State.Pawn.KING.toString())
									|| this.citadels.contains(state.getBox(i, j - 1))
									|| state.getBox(i, j - 1).contentEquals(this.throne))
							&& j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.EMPTY.toString()))) {
						this.blackPawnsOverhanged++;

					}

					else if (j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.WHITE.toString())
									|| state.getPawn(i, j + 1).equalsPawn(State.Pawn.KING.toString())
									|| this.citadels.contains(state.getBox(i, j + 1))
									|| state.getBox(i, j - 1).contentEquals(this.throne))
							&& j > 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.EMPTY.toString()))) {
						this.blackPawnsOverhanged++;

					}

				}

				// conto le pedine bianche con una nera o un accampamento o il trono vicino
				if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())) {

					if (i > 0
							&& (state.getPawn(i - 1, j).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i - 1, j))
									|| state.getBox(i - 1, j).equals(this.throne))
							&& i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.EMPTY.toString()))) {

						this.whitePawnsOverhanged++;

					}

					else if (i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i + 1, j))
									|| state.getBox(i + 1, j).equals(this.throne))
							&& i > 0 && (state.getPawn(i - 1, j).equalsPawn(State.Pawn.EMPTY.toString()))) {

						this.whitePawnsOverhanged++;

					}

					else if (j > 0
							&& (state.getPawn(i, j - 1).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i, j - 1))
									|| state.getBox(i, j - 1).contentEquals(this.throne))
							&& j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.EMPTY.toString()))) {

						this.whitePawnsOverhanged++;

					}

					else if (j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i, j + 1))
									|| state.getBox(i, j + 1).contentEquals(this.throne))
							&& j > 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.EMPTY.toString()))) {

						this.whitePawnsOverhanged++;

					}

				}

				// controllo se il re ha pedine nere intorno o accampamenti o il trono
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {

					if (i > 0 && (state.getPawn(i - 1, j).equalsPawn(State.Pawn.BLACK.toString())
							|| this.citadels.contains(state.getBox(i - 1, j))
							|| state.getBox(i - 1, j).equals(this.throne))) {
						this.kingOverhanged = 1;
					}

					if (i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i + 1, j))
									|| state.getBox(i + 1, j).equals(this.throne))) {
						this.kingOverhanged = 1;
					}

					if (j > 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.BLACK.toString())
							|| this.citadels.contains(state.getBox(i, j - 1))
							|| state.getBox(i, j - 1).contentEquals(this.throne))) {
						this.kingOverhanged = 1;
					}

					if (j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i, j + 1))
									|| state.getBox(i, j + 1).contentEquals(this.throne))) {
						this.kingOverhanged = 1;
					}

					// controllo se il re è in Q1
					if ((i >= 0 && i <= state.getBoard().length / 2) && (j >= 0 && j <= state.getBoard().length / 2)) {
						this.kingInQ1 = 1;
					}

					// controllo se il re è in Q2
					if ((i >= 0 && i <= state.getBoard().length / 2)
							&& (j >= state.getBoard().length / 2 && j < state.getBoard().length)) {
						this.kingInQ2 = 1;
					}
					// controllo se il re è in Q3
					if ((i >= state.getBoard().length / 2 && i < state.getBoard().length)
							&& (j >= 0 && j <= state.getBoard().length / 2)) {
						this.kingInQ3 = 1;
					}

					// controllo se il re è in Q4
					if ((i >= state.getBoard().length / 2 && i < state.getBoard().length)
							&& (j >= state.getBoard().length / 2 && j < state.getBoard().length)) {
						this.kingInQ4 = 1;
					}

				}

				// controllo se il re ha pedine bianche intorno
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {

					if (i > 0 && (state.getPawn(i - 1, j).equalsPawn(State.Pawn.WHITE.toString()))) {
						this.whiteNearKing++;
					}

					if (i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.WHITE.toString()))) {
						this.whiteNearKing++;
					}

					if (j > 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.WHITE.toString())
							|| this.citadels.contains(state.getBox(i, j - 1))
							|| state.getBox(i, j - 1).contentEquals(this.throne))) {
						this.whiteNearKing++;

					}

					if (j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.WHITE.toString()))) {
						this.whiteNearKing++;
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
					this.kingOnThrone = 1;
				}

				// controllo se il re è vicino al trono
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())
						&& this.nearsThrone.contains(state.getBox(i, j))) {
					this.kingNearThrone = 1;
				}

				// controllo se il re è su una stella
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())
						&& this.stars.contains(state.getBox(i, j))) {
					this.kingOnStar = 1;
				}

			}
		}

	}

}
