package it.unibo.ai.didattica.competition.tablut.teampallo.heuristics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;

public class MyHeuristic implements Heuristic {

	private static double BLACK_WEIGHT_DIFF_PAWNS = 5;
	private static double BLACK_WEIGHT_COUNT_WHITE_PAWNS = 3;
	private static double BLACK_WEIGHT_COUNT_BLACK_PAWNS = 2;
	private static double BLACK_WEIGHT_BLACK_NEAR_KING = 3;
	private static double BLACK_WEIGHT_FREE_WAY_KING = 5;
	private static double BLACK_WEIGHT_KING_ON_STAR = 10;
	private static double BLACK_WEIGHT_KING_CAPTURED = 10;
	private static double BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED = 0.7;
	private static double BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED = 3;
	private static double BLACK_WEIGHT_BLACKBARRIER = 2.5;

	private static double WHITE_WEIGHT_DIFF_PAWNS = 5;
	private static double WHITE_WEIGHT_COUNT_WHITE_PAWNS = 4;
	private static double WHITE_WEIGHT_COUNT_BLACK_PAWNS = 3;
	private static double WHITE_WEIGHT_BLACK_NEAR_KING = 3;
	private static double WHITE_WEIGHT_WHITE_NEAR_KING = 1.5;
	private static double WHITE_WEIGHT_FREE_WAY_KING = 10;
	private static double WHITE_WEIGHT_KING_ON_THRONE = 0.5;
	private static double WHITE_WEIGHT_KING_NEAR_THRONE = 0.3;
	private static double WHITE_WEIGHT_KING_ON_STAR = 10;
	private static double WHITE_WEIGHT_KING_FROM_BORDER = 0;
	private static double WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED = 4;
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
	private int blackNearKing;
	private int whiteNearKing;
	private int kingFreeWay;
	private int kingOnThrone;
	private int kingNearThrone;
	private int kingOnStar;
	private int kingFromBorder;
	private int blackPawnsOverhanged;
	private int whitePawnsOverhanged;
	private int blackBarrierPawns;
	private int kingCaptured;

	private Random r;
	private List<String> citadels;
	private List<String> stars;
	private List<String> blackBarrier;
	private List<String> nearsThrone;
	private String throne;

	public MyHeuristic() {
		this.r = new Random(System.currentTimeMillis());

		this.citadels = Arrays.asList("a4", "a5", "a6", "b5", "d1", "e1", "f1", "e2", "i4", "i5", "i6", "h5", "d9",
				"e9", "f9", "e8");

		this.stars = Arrays.asList("a2", "a3", "a7", "a8", "b1", "b9", "c1", "c9", "g1", "g9", "h1", "h9", "i2", "i3",
				"i7", "i8");

		this.nearsThrone = Arrays.asList("e4", "e6", "d5", "f5");
		this.throne = "e5";

		this.blackBarrier = Arrays.asList("b3", "b7", "c2", "c8", "g2", "g8", "h3", "h7");

		// ADD TO TRAINING

		// generateVWeightValues(GameManager.getInstance().getPlayer());

	}

	private void printValues() {

		double diff = countB - countW;

		System.out.println("countB - countW = " + diff);
		System.out.println("countB = " + this.countB);
		System.out.println("countW = " + this.countW);
		System.out.println("blackNearKing = " + this.blackNearKing);
		System.out.println("whiteNearKing = " + this.whiteNearKing);
		System.out.println("kingFreeWay = " + this.kingFreeWay);
		System.out.println("kingOnThrone = " + this.kingOnThrone);
		System.out.println("kingNearThrone = " + this.kingNearThrone);
		System.out.println("kingOnStar = " + this.kingOnStar);
		System.out.println("kingFromBorder = " + this.kingFromBorder);
		System.out.println("blackPawnsOverhanged = " + this.blackPawnsOverhanged);
		System.out.println("whitePawnsOverhanged = " + this.whitePawnsOverhanged);
		System.out.println("blackBarrierPawns = " + this.blackBarrierPawns);
		System.out.println("kingCaptured = " + this.kingCaptured);

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

	//	 printValues();

	//	double result = myRandom(-1, 1);
	double result = 0;

//		result -= WHITE_WEIGHT_DIFF_PAWNS * (((double)this.countW/16) - ((double)this.countB/9));

		result -= WHITE_WEIGHT_COUNT_BLACK_PAWNS * ((double)this.countB / 16);

		result -= WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED * ((double)this.whitePawnsOverhanged / this.countW);

	//	result -= WHITE_WEIGHT_BLACK_NEAR_KING * (this.blackNearKing / 4);

		result += WHITE_WEIGHT_COUNT_WHITE_PAWNS * ((double)this.countW/9);

//		result += WHITE_WEIGHT_WHITE_NEAR_KING * this.whiteNearKing;

		result += WHITE_WEIGHT_FREE_WAY_KING * ((double)this.kingFreeWay / 4);

		result += WHITE_WEIGHT_KING_ON_THRONE * this.kingOnThrone;

//		result += WHITE_WEIGHT_KING_NEAR_THRONE * this.kingNearThrone;

//		result += WHITE_WEIGHT_KING_FROM_BORDER * (state.getBoard().length - this.kingFromBorder);

		result += WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED * ((double)this.blackPawnsOverhanged / this.countB);

//		result += WHITE_WEIGHT_KING_ON_STAR * this.kingOnStar;

		return result;
	}

	public double heuristicBlack(State state) {

		this.resetValues();
		this.extractValues(state);

		// printValues();

//		double result = myRandom(-1, 1);
		double result = 0;

//		result += BLACK_WEIGHT_DIFF_PAWNS * (((double)this.countB/16) - ((double)this.countW/9));

		result += BLACK_WEIGHT_COUNT_BLACK_PAWNS * ((double)this.countB /16);

		result += BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED * ((double)this.whitePawnsOverhanged /this.countW);

		result += BLACK_WEIGHT_BLACK_NEAR_KING * ((double)this.blackNearKing / 4);

		result -= BLACK_WEIGHT_COUNT_WHITE_PAWNS * ((double)this.countW / 9);

		result -= BLACK_WEIGHT_FREE_WAY_KING * ((double)this.kingFreeWay /4);

		result -= BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED * ((double)this.blackPawnsOverhanged / this.countB);

		result -= BLACK_WEIGHT_KING_ON_STAR * this.kingOnStar;

		result += BLACK_WEIGHT_BLACKBARRIER * this.blackBarrierPawns;

	//	result += BLACK_WEIGHT_KING_CAPTURED * this.kingCaptured;

		return result;
	}

	private void resetValues() {
		this.countB = 0;
		this.countW = 0;
		this.blackNearKing = 0;
		this.whiteNearKing = 0;
		this.kingFreeWay = 0;
		this.kingOnThrone = 0;
		this.kingOnStar = 0;
		this.kingNearThrone = 0;
		this.kingFromBorder = 0;
		this.blackPawnsOverhanged = 0;
		this.whitePawnsOverhanged = 0;
		this.blackBarrierPawns = 0;
		this.kingCaptured = 0;

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
						this.blackNearKing++;
					}

					if (i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i + 1, j))
									|| state.getBox(i + 1, j).equals(this.throne))) {
						this.blackNearKing++;
					}

					if (j > 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.BLACK.toString())
							|| this.citadels.contains(state.getBox(i, j - 1))
							|| state.getBox(i, j - 1).contentEquals(this.throne))) {
						this.blackNearKing++;
					}

					if (j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i, j + 1))
									|| state.getBox(i, j + 1).contentEquals(this.throne))) {
						this.blackNearKing++;
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

				// controllo se il re è stato catturato
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {

					if (i > 0 && (state.getPawn(i - 1, j).equalsPawn(State.Pawn.BLACK.toString())
							|| this.citadels.contains(state.getBox(i - 1, j))
							|| state.getBox(i - 1, j).equals(this.throne)) && i < state.getBoard().length - 1 &&

							(state.getPawn(i + 1, j).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i + 1, j))
									|| state.getBox(i + 1, j).equals(this.throne))) {
						this.kingCaptured = 1;

					} else if (j > 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.BLACK.toString())
							|| this.citadels.contains(state.getBox(i, j - 1))
							|| state.getBox(i, j - 1).equals(this.throne)) && i < state.getBoard().length - 1 &&

							(state.getPawn(i, j + 1).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i, j + 1))
									|| state.getBox(i, j + 1).equals(this.throne))) {
						this.kingCaptured = 1;

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

				// controllo se il re è vicino al bordo
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
					this.kingFromBorder = Math.min(state.getBoard().length - 1 - i, state.getBoard().length - 1 - j);
				}

			}
		}

	}

	private double myRandom(double start, double end) {

		double random = this.r.nextDouble();
		double result = start + (random * (end - start));
		return result;
	}

	private void generateVWeightValues(String player) {

		// ADD TO LOG

		try {

			File logFile = new File("/home/luca/tablut_log.txt");
			if (!logFile.exists()) {
				logFile.createNewFile();
			}

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));

			String numPartita = System.getenv("NUMERO_PARTITA");

			if (numPartita == null) {
				numPartita = "--";
			}
			pw.println("\nVALORI " + player.toUpperCase() + " PARTITA " + numPartita);

			if (player.equalsIgnoreCase("white")) {

				WHITE_WEIGHT_DIFF_PAWNS = myRandom(0, 10);
				WHITE_WEIGHT_COUNT_WHITE_PAWNS = myRandom(0, 10);
				WHITE_WEIGHT_COUNT_BLACK_PAWNS = myRandom(0, 10);
				WHITE_WEIGHT_BLACK_NEAR_KING = myRandom(0, 10);
				WHITE_WEIGHT_WHITE_NEAR_KING = myRandom(0, 10);
				WHITE_WEIGHT_FREE_WAY_KING = myRandom(0, 10);
				WHITE_WEIGHT_KING_ON_THRONE = myRandom(0, 10);
				WHITE_WEIGHT_KING_NEAR_THRONE = myRandom(0, 10);
				WHITE_WEIGHT_KING_ON_STAR = myRandom(0, 10);
				WHITE_WEIGHT_KING_FROM_BORDER = myRandom(0, 10);
				WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED = myRandom(0, 10);
				WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED = myRandom(0, 10);

				pw.println("WHITE_WEIGHT_DIFF_PAWNS = " + WHITE_WEIGHT_DIFF_PAWNS);
				pw.println("WHITE_WEIGHT_COUNT_WHITE_PAWNS = " + WHITE_WEIGHT_COUNT_WHITE_PAWNS);
				pw.println("WHITE_WEIGHT_COUNT_BLACK_PAWNS = " + WHITE_WEIGHT_COUNT_BLACK_PAWNS);
				pw.println("WHITE_WEIGHT_BLACK_NEAR_KING = " + WHITE_WEIGHT_BLACK_NEAR_KING);
				pw.println("WHITE_WEIGHT_WHITE_NEAR_KING = " + WHITE_WEIGHT_WHITE_NEAR_KING);
				pw.println("WHITE_WEIGHT_FREE_WAY_KING = " + WHITE_WEIGHT_FREE_WAY_KING);
				pw.println("WHITE_WEIGHT_KING_ON_THRONE = " + WHITE_WEIGHT_KING_ON_THRONE);
				pw.println("WHITE_WEIGHT_KING_NEAR_THRONE = " + WHITE_WEIGHT_KING_NEAR_THRONE);
				pw.println("WHITE_WEIGHT_KING_ON_STAR = " + WHITE_WEIGHT_KING_ON_STAR);
				pw.println("WHITE_WEIGHT_KING_FROM_BORDER = " + WHITE_WEIGHT_KING_FROM_BORDER);
				pw.println("WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED = " + WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED);
				pw.println("WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED = " + WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED);

			} else if (player.equalsIgnoreCase("black")) {

				BLACK_WEIGHT_DIFF_PAWNS = myRandom(0, 10);
				BLACK_WEIGHT_COUNT_WHITE_PAWNS = myRandom(0, 10);
				BLACK_WEIGHT_COUNT_BLACK_PAWNS = myRandom(0, 10);
				BLACK_WEIGHT_BLACK_NEAR_KING = myRandom(0, 10);
				BLACK_WEIGHT_FREE_WAY_KING = myRandom(0, 10);
				BLACK_WEIGHT_KING_ON_STAR = myRandom(0, 10);
				BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED = myRandom(0, 10);
				BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED = myRandom(0, 10);

				pw.println("BLACK_WEIGHT_DIFF_PAWNS = " + BLACK_WEIGHT_DIFF_PAWNS);
				pw.println("BLACK_WEIGHT_COUNT_WHITE_PAWNS = " + BLACK_WEIGHT_COUNT_WHITE_PAWNS);
				pw.println("BLACK_WEIGHT_COUNT_BLACK_PAWNS = " + BLACK_WEIGHT_COUNT_BLACK_PAWNS);
				pw.println("BLACK_WEIGHT_BLACK_NEAR_KING = " + BLACK_WEIGHT_BLACK_NEAR_KING);
				pw.println("BLACK_WEIGHT_FREE_WAY_KING = " + BLACK_WEIGHT_FREE_WAY_KING);
				pw.println("BLACK_WEIGHT_KING_ON_STAR = " + BLACK_WEIGHT_KING_ON_STAR);
				pw.println("BLACK_WEIGHT_KING_FROM_BORDER = " + BLACK_WEIGHT_DIFF_PAWNS);
				pw.println("BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED = " + BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED);
				pw.println("BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED = " + BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED);
			}

			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ***

	}

}
