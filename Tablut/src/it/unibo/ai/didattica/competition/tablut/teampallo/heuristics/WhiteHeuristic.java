package it.unibo.ai.didattica.competition.tablut.teampallo.heuristics;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

public class WhiteHeuristic implements Heuristic {

	private double WHITE_WEIGHT_COUNT_WHITE_PAWNS = 6.0;
	private double WHITE_WEIGHT_COUNT_BLACK_PAWNS = 3.0;
	private double WHITE_WEIGHT_SINGLE_FREE_WAY_KING = 5.65; // 5
	private double WHITE_WEIGHT_MULTIPLE_FREE_WAY_KING = 10.0;
	private double WHITE_WEIGHT_KING_ON_STAR = 13.0;
	private double WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED = 1.0; // 3
	private double WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED = 0.5; // 1
	private double WHITE_WEIGHT_KING_OVERHANGED = 18.0;
	private double WHITE_WEIGHT_KING_FAVOURITE = 5.0; // 9
	private double WHITE_WEIGHT_GUARDS = 14.0;

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
	private int kingOverhanged;
	private int kingOnFavourite;
	private int guards;
	private int disPos;
	private int strategy;

	private int quadrante1;
	private int quadrante2;
	private int quadrante3;
	private int quadrante4;
	private int quadranteF;

	private int pawnsB;
	private int pawnsW;

	private Random r;
	private List<String> citadels;
	private List<String> stars;
	private List<String> nearsThrone;
	private List<String> guardsPos;
	private List<String> disPosL;
	private String throne;

	private String player;
	private List<String> blackBarrier;

	public WhiteHeuristic(String player) {
		this.player = player;
		this.pawnsB = 16;
		this.pawnsW = 9;
		this.r = new Random(System.currentTimeMillis());

		this.citadels = Arrays.asList("a4", "a5", "a6", "b5", "d1", "e1", "f1", "e2", "i4", "i5", "i6", "h5", "d9",
				"e9", "f9", "e8");

		this.stars = Arrays.asList("a2", "a3", "a7", "a8", "b1", "b9", "c1", "c9", "g1", "g9", "h1", "h9", "i2", "i3",
				"i7", "i8");

		this.nearsThrone = Arrays.asList("e4", "e6", "d5", "f5");
		this.throne = "e5";

		this.blackBarrier = Arrays.asList("b3", "b7", "c2", "c8", "g2", "g8", "h3", "h7");

		this.guardsPos = Arrays.asList("a1", "a2", "b1", "h1", "i1", "i2", "i8", "i9", "h9", "b9", "a9", "a8");

		this.disPosL = Arrays.asList("2-0", "0-2", "0-6", "2-8", "6-8", "8-6", "8-2", "6-0");

	}

	@Override
	public double heuristic(State state) {

		this.resetValues();
		this.extractValues(state);
		// this.calculatePhasis();

		printValues();

		// double result = myRandom(-1, 1);
		double result = 0;

		// result -= env.getWeight("WHITE_WEIGHT_DIFF_PAWNS") * (this.countW -
		// this.countB);

		if (state.getTurn().equalsTurn(Turn.BLACKWIN.toString())) {
			return -50;
		}

		if (this.countB < this.pawnsB) {
			result += WHITE_WEIGHT_COUNT_BLACK_PAWNS * (this.pawnsB - this.countB);
		}
		if (this.countW < this.pawnsW) {
			result -= WHITE_WEIGHT_COUNT_WHITE_PAWNS * (this.pawnsW - this.countW);
		}

		/*
		 * result += WHITE_WEIGHT_COUNT_BLACK_PAWNS * 16/this.countB;
		 * 
		 * result += WHITE_WEIGHT_COUNT_WHITE_PAWNS * this.countW / 9;
		 */

		result -= WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED * this.whitePawnsOverhanged;

		// result -= WHITE_WEIGHT_BLACK_NEAR_KING * this.blackNearKing;

		// result += WHITE_WEIGHT_WHITE_NEAR_KING * this.whiteNearKing;

		if (this.kingOverhanged > 0) {
			result -= WHITE_WEIGHT_KING_OVERHANGED * this.kingOverhanged;
		} else {
			if (this.kingFreeWay == 1) {
				result += WHITE_WEIGHT_SINGLE_FREE_WAY_KING * this.kingFreeWay;
			} else {
				if (this.kingFreeWay > 1) {
					result += WHITE_WEIGHT_MULTIPLE_FREE_WAY_KING * (this.kingFreeWay / 2);
				}
			}
		}

		// result -= WHITE_WEIGHT_KING_ON_THRONE * this.kingOnThrone;

		// result += env.getWeight("WHITE_WEIGHT_KING_NEAR_THRONE") *
		// this.kingNearThrone;

		result += WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED * this.blackPawnsOverhanged;

		result += WHITE_WEIGHT_KING_ON_STAR * this.kingOnStar;

		result += WHITE_WEIGHT_KING_FAVOURITE * this.kingOnFavourite;

		result += WHITE_WEIGHT_GUARDS * this.guards;// * ( 1 + ((initial)?1:0)*3);

		// result -= env.getWeight("WHITE_WEIGHT_DIS_POS") * this.disPos * ( 1 +
		// ((initial)?1:0)*3);

		/*
		 * if(this.strategy>0) result += env.getWeight("WHITE_WEIGHT_STRATEGY") * (1 +
		 * (this.strategy-1)/8);
		 */

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
		this.kingOverhanged = 0;
		this.kingOnFavourite = 0;
		this.guards = 0;
		this.disPos = 0;
		this.strategy = 0;

		this.quadrante1 = 0;
		this.quadrante2 = 0;
		this.quadrante3 = 0;
		this.quadrante4 = 0;
		this.quadranteF = 0;

	}

	private void extractValues(State state) {

		// valuto i quadranti
		int minore = 50;
		// QUADRANTE1
		for (int i = 0; i <= 4; i++) {
			for (int j = 0; j <= 4; j++) {
				if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
					quadrante1++;
				}

			}
		}
		/*
		 * if (state.getPawn(5, 0).equalsPawn(State.Pawn.BLACK.toString())) {
		 * quadrante1++; } if (state.getPawn(0,
		 * 5).equalsPawn(State.Pawn.BLACK.toString())) { quadrante1++; }
		 */
		if (quadrante1 < minore) {
			minore = quadrante1;
			quadranteF = 1;
		}

		// QUADRANTE2
		for (int i = 0; i <= 4; i++) {
			for (int j = 4; j <= state.getBoard().length - 1; j++) {
				if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
					quadrante2++;
				}

			}
		}
		/*
		 * if (state.getPawn(5, 8).equalsPawn(State.Pawn.BLACK.toString())) {
		 * quadrante2++; } if (state.getPawn(0,
		 * 3).equalsPawn(State.Pawn.BLACK.toString())) { quadrante2++; }
		 */
		if (quadrante2 < minore) {
			minore = quadrante2;
			quadranteF = 2;
		}

		// QUADRANTE3
		for (int i = 4; i <= state.getBoard().length - 1; i++) {
			for (int j = 0; j <= 4; j++) {
				if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
					quadrante3++;
				}

			}
		}
		/*
		 * if (state.getPawn(3, 0).equalsPawn(State.Pawn.BLACK.toString())) {
		 * quadrante3++; } if (state.getPawn(8,
		 * 5).equalsPawn(State.Pawn.BLACK.toString())) { quadrante3++; }
		 */
		if (quadrante3 < minore) {
			minore = quadrante3;
			quadranteF = 3;
		}

		// QUADRANTE4
		for (int i = 4; i <= state.getBoard().length - 1; i++) {
			for (int j = 4; j <= state.getBoard().length - 1; j++) {
				if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
					quadrante4++;
				}

			}
		}
		/*
		 * if (state.getPawn(5, 8).equalsPawn(State.Pawn.BLACK.toString())) {
		 * quadrante4++; } if (state.getPawn(8,
		 * 5).equalsPawn(State.Pawn.BLACK.toString())) { quadrante4++; }
		 */
		if (quadrante4 < minore) {
			minore = quadrante4;
			quadranteF = 4;
		}

		if (quadrante1 == quadrante2 || quadrante1 == quadrante3 || quadrante1 == quadrante4 || quadrante2 == quadrante3
				|| quadrante2 == quadrante4 || quadrante3 == quadrante4) {
			quadranteF = 0;
		}

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
						&& (j == 1 || j == 2 || j == 6 || j == 7)) {
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

				// controllo se il re è vicino al bordo
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
					this.kingFromBorder = Math.min(state.getBoard().length - 1 - i, state.getBoard().length - 1 - j);
				}

				// controllo se il re è minacciato
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {

					// ho una nera sotto e controllo sopra-destra-sinistra
					if (i + 1 < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.BLACK.toString())
									|| citadels.contains(state.getBox(i + 1, j)))) {
						boolean minacciato = false;
						for (int itemp = i - 1; itemp >= 0 && !minacciato; itemp--) {
							if (state.getPawn(itemp, j).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(itemp, j).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(itemp, j))
									|| state.getPawn(itemp, j).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						for (int jtemp = j - 1; jtemp >= 0 && !minacciato; jtemp--) {
							if (state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(i - 1, jtemp))
									|| state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						for (int jtemp = j + 1; jtemp < state.getBoard().length - 1 && !minacciato; jtemp++) {
							if (state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(i - 1, jtemp))
									|| state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						if (minacciato) {
							kingOverhanged++;
						}
					}

					// ho una nera sopra e controllo sotto-destra-sinistra
					if (i - 1 >= 0 && (state.getPawn(i - 1, j).equalsPawn(State.Pawn.BLACK.toString())
							|| citadels.contains(state.getBox(i - 1, j)))) {
						boolean minacciato = false;
						for (int itemp = i + 1; itemp < state.getBoard().length - 1 && !minacciato; itemp++) {
							if (state.getPawn(itemp, j).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(itemp, j).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(itemp, j))
									|| state.getPawn(itemp, j).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						for (int jtemp = j - 1; jtemp >= 0 && !minacciato; jtemp--) {
							if (state.getPawn(i + 1, jtemp).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(i + 1, jtemp).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(i + 1, jtemp))
									|| state.getPawn(i + 1, jtemp).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						for (int jtemp = j + 1; jtemp < state.getBoard().length - 1 && !minacciato; jtemp++) {
							if (state.getPawn(i + 1, jtemp).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(i + 1, jtemp).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(i + 1, jtemp))
									|| state.getPawn(i + 1, jtemp).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						if (minacciato) {
							kingOverhanged++;
						}
					}

					// ho una nera a destra e controllo a sinistra-sopra-sotto
					if (j + 1 < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.BLACK.toString())
									|| citadels.contains(state.getBox(i, j + 1)))) {
						boolean minacciato = false;
						for (int jtemp = j - 1; jtemp >= 0 && !minacciato; jtemp--) {
							if (state.getPawn(i, jtemp).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(i, jtemp).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(i, jtemp))
									|| state.getPawn(i, jtemp).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						for (int itemp = i - 1; itemp >= 0 && !minacciato; itemp--) {
							if (state.getPawn(itemp, j - 1).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(itemp, j - 1).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(itemp, j - 1))
									|| state.getPawn(itemp, j - 1).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						for (int itemp = i + 1; itemp < state.getBoard().length - 1 && !minacciato; itemp++) {
							if (state.getPawn(itemp, j - 1).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(itemp, j - 1).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(itemp, j - 1))
									|| state.getPawn(itemp, j - 1).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						if (minacciato) {
							kingOverhanged++;
						}
					}
					// ho una nera a sinistra e controllo a destra-sopra-sotto
					if (j - 1 >= 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.BLACK.toString())
							|| citadels.contains(state.getBox(i, j - 1)))) {
						boolean minacciato = false;
						for (int jtemp = j + 1; jtemp < state.getBoard().length - 1 && !minacciato; jtemp++) {
							if (state.getPawn(i, jtemp).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(i, jtemp).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(i, jtemp))
									|| state.getPawn(i, jtemp).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						for (int itemp = i - 1; itemp >= 0 && !minacciato; itemp--) {
							if (state.getPawn(itemp, j + 1).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(itemp, j + 1).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(itemp, j + 1))
									|| state.getPawn(itemp, j + 1).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						for (int itemp = i + 1; itemp < state.getBoard().length - 1 && !minacciato; itemp++) {
							if (state.getPawn(itemp, j + 1).equalsPawn(State.Pawn.BLACK.toString()))
								minacciato = true;
							if (state.getPawn(itemp, j + 1).equalsPawn(State.Pawn.THRONE.toString())
									|| citadels.contains(state.getBox(itemp, j + 1))
									|| state.getPawn(itemp, j + 1).equalsPawn(State.Pawn.WHITE.toString()))
								break;
						}
						if (minacciato) {
							kingOverhanged++;
						}
					}

					// controllo se è nel quadrante favorito
					if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
						switch (quadranteF) {
						case 1: {
							if (i >= 0 && i <= 4 && j >= 0 && j <= 4) {
								this.kingOnFavourite = 1;
							}
							break;
						}
						case 2: {
							if (i >= 0 && i <= 4 && j >= 4 && j <= 8) {
								this.kingOnFavourite = 1;
							}
							break;
						}
						case 3: {
							if (i >= 4 && i <= 8 && j >= 0 && j <= 4) {
								this.kingOnFavourite = 1;
							}
							break;
						}
						case 4: {
							if (i >= 4 && i <= 8 && j >= 4 && j <= 8) {
								this.kingOnFavourite = 1;
							}
							break;
						}
						default: {

						}
						}
					}

					if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
							&& this.guardsPos.contains(state.getBox(i, j))) {
						this.guards++;
					}

					/*
					 * if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString()) &&
					 * this.disPosL.contains(state.getBox(i, j))) { this.disPos++; }
					 */

				}

			}
		}

		// calcolo strategy
//		for(String s : this.disPosL) {
//			double value=0;
//			int row=Integer.parseInt(s.split("-")[0]);
//			int column=Integer.parseInt(s.split("-")[1]);
//			
//			if(state.getPawn(row, column).equalsPawn(State.Pawn.WHITE.toString())) {
//				value+=1.0;
//				
//				//controllo la nera nell'accampamento sopra
//				for(int i=row-4;i<9;i++) {
//					if(state.getPawn(i, column-1).equalsPawn(State.Pawn.BLACK.toString())) {
//						value-=1.25;
//						break;
//					}
//				}
//				
//				//controllo se serve anche la bianca sopra o meno
//				if(state.getPawn(row-1, column+2).equalsPawn(State.Pawn.BLACK.toString())
//						&& state.getPawn(row-1, column).equalsPawn(State.Pawn.WHITE.toString())){
//					value+=1.3;
//				}
//				
//				for(int i=row-3;i<9;i++) {
//					if(state.getPawn(i, column-1).equalsPawn(State.Pawn.KING.toString())) {
//						value+=8.0;
//						break;
//					}
//				}
//				
//				if(value>8) {
//					this.strategy++;
//				}
//			}
//		}

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
		System.out.println("kingOverhanged = " + this.kingOverhanged);
		System.out.println("strategy = " + this.strategy);
		System.out.println("quadranteF = " + this.quadranteF);

	}

}
