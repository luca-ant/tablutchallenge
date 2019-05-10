package it.unibo.ai.didattica.competition.tablut.teampallo.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.exceptions.ActionException;
import it.unibo.ai.didattica.competition.tablut.exceptions.BoardException;
import it.unibo.ai.didattica.competition.tablut.exceptions.CitadelException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ClimbingCitadelException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ClimbingException;
import it.unibo.ai.didattica.competition.tablut.exceptions.DiagonalException;
import it.unibo.ai.didattica.competition.tablut.exceptions.OccupitedException;
import it.unibo.ai.didattica.competition.tablut.exceptions.PawnException;
import it.unibo.ai.didattica.competition.tablut.exceptions.StopException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ThroneException;

public class MyGameAshtonTablutRules implements MyRules {

	private static MyGameAshtonTablutRules instance = null;
	private List<String> citadels;
	private int movesWithutCapturing;

	private MyGameAshtonTablutRules() {
		this.citadels = Arrays.asList("a4", "a5", "a6", "b5", "d1", "e1", "f1", "e2", "i4", "i5", "i6", "h5", "d9",
				"e9", "f9", "e8");
		this.movesWithutCapturing = 0;

	}

	public static MyGameAshtonTablutRules getInstance() {
		if (MyGameAshtonTablutRules.instance == null)
			MyGameAshtonTablutRules.instance = new MyGameAshtonTablutRules();
		return instance;
	}

//	private State checkCaptureWhite(State state, Action a) {
//		// controllo se mangio a destra
//		if (a.getColumnTo() < state.getBoard().length - 2
//				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("B")
//				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("W")
//						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")
//						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("K")
//						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))
//								&& !(a.getColumnTo() + 2 == 8 && a.getRowTo() == 4)
//								&& !(a.getColumnTo() + 2 == 4 && a.getRowTo() == 0)
//								&& !(a.getColumnTo() + 2 == 4 && a.getRowTo() == 0)
//								&& !(a.getColumnTo() + 2 == 0 && a.getRowTo() == 4)))) {
//			state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
//		}
//		// controllo se mangio a sinistra
//		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("B")
//				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("W")
//						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
//						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("K")
//						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
//								&& !(a.getColumnTo() - 2 == 8 && a.getRowTo() == 4)
//								&& !(a.getColumnTo() - 2 == 4 && a.getRowTo() == 0)
//								&& !(a.getColumnTo() - 2 == 4 && a.getRowTo() == 0)
//								&& !(a.getColumnTo() - 2 == 0 && a.getRowTo() == 4)))) {
//			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
//		}
//		// controllo se mangio sopra
//		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("B")
//				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("W")
//						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
//						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("K")
//						|| (this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
//								&& !(a.getColumnTo() == 8 && a.getRowTo() - 2 == 4)
//								&& !(a.getColumnTo() == 4 && a.getRowTo() - 2 == 0)
//								&& !(a.getColumnTo() == 4 && a.getRowTo() - 2 == 0)
//								&& !(a.getColumnTo() == 0 && a.getRowTo() - 2 == 4)))) {
//			state.removePawn(a.getRowTo() - 1, a.getColumnTo());
//		}
//		// controllo se mangio sotto
//		if (a.getRowTo() < state.getBoard().length - 2
//				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("B")
//				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("W")
//						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
//						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("K")
//						|| (this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
//								&& !(a.getColumnTo() == 8 && a.getRowTo() + 2 == 4)
//								&& !(a.getColumnTo() == 4 && a.getRowTo() + 2 == 0)
//								&& !(a.getColumnTo() == 4 && a.getRowTo() + 2 == 0)
//								&& !(a.getColumnTo() == 0 && a.getRowTo() + 2 == 4)))) {
//			state.removePawn(a.getRowTo() + 1, a.getColumnTo());
//		}
//		// controllo se ho vinto
//		if (a.getRowTo() == 0 || a.getRowTo() == state.getBoard().length - 1 || a.getColumnTo() == 0
//				|| a.getColumnTo() == state.getBoard().length - 1) {
//			if (state.getPawn(a.getRowTo(), a.getColumnTo()).equalsPawn("K")) {
//				state.setTurn(State.Turn.WHITEWIN);
//			}
//		}
//
//		return state;
//	}
//
//	private State checkCaptureBlackKingLeft(State state, Action a) {
//		// ho il re sulla sinistra
//		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("K")) {
//			// re sul trono
//			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e5")) {
//				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 3).equalsPawn("B")
//						&& state.getPawn(5, 4).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			// re adiacente al trono
//			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e4")) {
//				if (state.getPawn(2, 4).equalsPawn("B") && state.getPawn(3, 3).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e6")) {
//				if (state.getPawn(5, 3).equalsPawn("B") && state.getPawn(6, 4).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("f5")) {
//				if (state.getPawn(3, 5).equalsPawn("B") && state.getPawn(5, 5).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			// sono fuori dalle zone del trono
//			if (!state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e5")
//					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e6")
//					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e4")
//					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("f5")) {
//				if (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
//						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//		}
//		return state;
//	}
//
//	private State checkCaptureBlackKingRight(State state, Action a) {
//		// ho il re sulla destra
//		if (a.getColumnTo() < state.getBoard().length - 2
//				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("K"))) {
//			// re sul trono
//			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e5")) {
//				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
//						&& state.getPawn(5, 4).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			// re adiacente al trono
//			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e4")) {
//				if (state.getPawn(2, 4).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e6")) {
//				if (state.getPawn(5, 5).equalsPawn("B") && state.getPawn(6, 4).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("d5")) {
//				if (state.getPawn(3, 3).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			// sono fuori dalle zone del trono
//			if (!state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("d5")
//					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e6")
//					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e4")
//					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e5")) {
//				if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")
//						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//		}
//		return state;
//	}
//
//	private State checkCaptureBlackKingDown(State state, Action a) {
//		// ho il re sotto
//		if (a.getRowTo() < state.getBoard().length - 2
//				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("K")) {
//
//			// re sul trono
//			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e5")) {
//				if (state.getPawn(5, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
//						&& state.getPawn(4, 3).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			// re adiacente al trono
//			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e4")) {
//				if (state.getPawn(3, 3).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("d5")) {
//				if (state.getPawn(4, 2).equalsPawn("B") && state.getPawn(5, 3).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("f5")) {
//				if (state.getPawn(4, 6).equalsPawn("B") && state.getPawn(5, 5).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			// sono fuori dalle zone del trono
//			if (!state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("d5")
//					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e4")
//					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("f5")
//					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e5")) {
//				if (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
//						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//		}
//		return state;
//	}
//
//	private State checkCaptureBlackKingUp(State state, Action a) {
//		// ho il re sopra
//		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("K")) {
//			// re sul trono
//			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e5")) {
//				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
//						&& state.getPawn(4, 3).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			// re adiacente al trono
//			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e6")) {
//				if (state.getPawn(5, 3).equalsPawn("B") && state.getPawn(5, 5).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("d5")) {
//				if (state.getPawn(4, 2).equalsPawn("B") && state.getPawn(3, 3).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("f5")) {
//				if (state.getPawn(4, 4).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//			// sono fuori dalle zone del trono
//			if (!state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("d5")
//					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e4")
//					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("f5")
//					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e5")) {
//				if (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
//						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))) {
//					state.setTurn(State.Turn.BLACKWIN);
//				}
//			}
//		}
//		return state;
//	}
//
//	private State checkCaptureBlackPawnRight(State state, Action a) {
//		// mangio a destra
//		if (a.getColumnTo() < state.getBoard().length - 2
//				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("W")) {
//			if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")) {
//				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
//			}
//			if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")) {
//				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
//			}
//			if (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))) {
//				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
//			}
//			if (state.getBox(a.getRowTo(), a.getColumnTo() + 2).equals("e5")) {
//				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
//			}
//
//		}
//
//		return state;
//	}
//
//	private State checkCaptureBlackPawnLeft(State state, Action a) {
//		// mangio a sinistra
//		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("W")
//				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
//						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
//						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
//						|| (state.getBox(a.getRowTo(), a.getColumnTo() - 2).equals("e5")))) {
//			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
//		}
//		return state;
//	}
//
//	private State checkCaptureBlackPawnUp(State state, Action a) {
//		// controllo se mangio sopra
//		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("W")
//				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
//						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
//						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
//						|| (state.getBox(a.getRowTo() - 2, a.getColumnTo()).equals("e5")))) {
//			state.removePawn(a.getRowTo() - 1, a.getColumnTo());
//		}
//		return state;
//	}
//
//	private State checkCaptureBlackPawnDown(State state, Action a) {
//		// controllo se mangio sotto
//		if (a.getRowTo() < state.getBoard().length - 2
//				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("W")
//				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
//						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
//						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
//						|| (state.getBox(a.getRowTo() + 2, a.getColumnTo()).equals("e5")))) {
//			state.removePawn(a.getRowTo() + 1, a.getColumnTo());
//		}
//		return state;
//	}
//
//	private State checkCaptureBlack(State state, Action a) {
//
//		this.checkCaptureBlackPawnRight(state, a);
//		this.checkCaptureBlackPawnLeft(state, a);
//		this.checkCaptureBlackPawnUp(state, a);
//		this.checkCaptureBlackPawnDown(state, a);
//		this.checkCaptureBlackKingRight(state, a);
//		this.checkCaptureBlackKingLeft(state, a);
//		this.checkCaptureBlackKingDown(state, a);
//		this.checkCaptureBlackKingUp(state, a);
//
//		return state;
//	}

	private State checkCaptureWhite(State state, Action a) {
		// controllo se mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))
								&& !(a.getColumnTo() + 2 == 8 && a.getRowTo() == 4)
								&& !(a.getColumnTo() + 2 == 4 && a.getRowTo() == 0)
								&& !(a.getColumnTo() + 2 == 4 && a.getRowTo() == 8)
								&& !(a.getColumnTo() + 2 == 0 && a.getRowTo() == 4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			this.movesWithutCapturing = -1;
	//		this.loggGame.fine("Pedina nera rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
		}
		// controllo se mangio a sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
								&& !(a.getColumnTo() - 2 == 8 && a.getRowTo() == 4)
								&& !(a.getColumnTo() - 2 == 4 && a.getRowTo() == 0)
								&& !(a.getColumnTo() - 2 == 4 && a.getRowTo() == 8)
								&& !(a.getColumnTo() - 2 == 0 && a.getRowTo() == 4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
			this.movesWithutCapturing = -1;
	//		this.loggGame.fine("Pedina nera rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() - 1));
		}
		// controllo se mangio sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
								&& !(a.getColumnTo() == 8 && a.getRowTo() - 2 == 4)
								&& !(a.getColumnTo() == 4 && a.getRowTo() - 2 == 0)
								&& !(a.getColumnTo() == 4 && a.getRowTo() - 2 == 8)
								&& !(a.getColumnTo() == 0 && a.getRowTo() - 2 == 4)))) {
			state.removePawn(a.getRowTo() - 1, a.getColumnTo());
			this.movesWithutCapturing = -1;
	//		this.loggGame.fine("Pedina nera rimossa in: " + state.getBox(a.getRowTo() - 1, a.getColumnTo()));
		}
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
								&& !(a.getColumnTo() == 8 && a.getRowTo() + 2 == 4)
								&& !(a.getColumnTo() == 4 && a.getRowTo() + 2 == 0)
								&& !(a.getColumnTo() == 4 && a.getRowTo() + 2 == 8)
								&& !(a.getColumnTo() == 0 && a.getRowTo() + 2 == 4)))) {
			state.removePawn(a.getRowTo() + 1, a.getColumnTo());
			this.movesWithutCapturing = -1;
	//		this.loggGame.fine("Pedina nera rimossa in: " + state.getBox(a.getRowTo() + 1, a.getColumnTo()));
		}
		// controllo se ho vinto
		if (a.getRowTo() == 0 || a.getRowTo() == state.getBoard().length - 1 || a.getColumnTo() == 0
				|| a.getColumnTo() == state.getBoard().length - 1) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo()).equalsPawn("K")) {
				state.setTurn(State.Turn.WHITEWIN);
	//			this.loggGame.fine("Bianco vince con re in " + a.getTo());
			}
		}
		// TODO: implement the winning condition of the capture of the last
		// black checker

		this.movesWithutCapturing++;
		return state;
	}

	private State checkCaptureBlackKingLeft(State state, Action a) {
		// ho il re sulla sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("K")) {
			// re sul trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e5")) {
				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 3).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
		//			this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo(), a.getColumnTo() - 1));
				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e4")) {
				if (state.getPawn(2, 4).equalsPawn("B") && state.getPawn(3, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
		//			this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo(), a.getColumnTo() - 1));
				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("f5")) {
				if (state.getPawn(5, 5).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
		//			this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo(), a.getColumnTo() - 1));
				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e6")) {
				if (state.getPawn(6, 4).equalsPawn("B") && state.getPawn(5, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
		//			this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo(), a.getColumnTo() - 1));
				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("f5")) {
				if (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))) {
					state.setTurn(State.Turn.BLACKWIN);
		//			this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo(), a.getColumnTo() - 1));
				}
			}
		}
		return state;
	}

	private State checkCaptureBlackKingRight(State state, Action a) {
		// ho il re sulla destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("K"))) {
			// re sul trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e5")) {
				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
		//			this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e4")) {
				if (state.getPawn(2, 4).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
		//			this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e6")) {
				if (state.getPawn(5, 5).equalsPawn("B") && state.getPawn(6, 4).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
		//			this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("d5")) {
				if (state.getPawn(3, 3).equalsPawn("B") && state.getPawn(5, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
		//			this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("d5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e5")) {
				if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))) {
					state.setTurn(State.Turn.BLACKWIN);
			//		this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
				}
			}
		}
		return state;
	}

	private State checkCaptureBlackKingDown(State state, Action a) {
		// ho il re sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("K")) {
		//	System.out.println("Ho il re sotto");
			// re sul trono
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(5, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
			//		this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo() + 1, a.getColumnTo()));
				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e4")) {
				if (state.getPawn(3, 3).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
			//		this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo() + 1, a.getColumnTo()));
				}
			}
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("d5")) {
				if (state.getPawn(4, 2).equalsPawn("B") && state.getPawn(5, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
			//		this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo() + 1, a.getColumnTo()));
				}
			}
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("f5")) {
				if (state.getPawn(4, 6).equalsPawn("B") && state.getPawn(5, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
		//			this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo() + 1, a.getColumnTo()));
				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))) {
					state.setTurn(State.Turn.BLACKWIN);
					// this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo() + 1, a.getColumnTo()));
				}
			}
		}
		return state;
	}

	private State checkCaptureBlackKingUp(State state, Action a) {
		// ho il re sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("K")) {
			// re sul trono
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
//					this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo() - 1, a.getColumnTo()));
				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e6")) {
				if (state.getPawn(5, 3).equalsPawn("B") && state.getPawn(5, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
	//				this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo() - 1, a.getColumnTo()));
				}
			}
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("d5")) {
				if (state.getPawn(4, 2).equalsPawn("B") && state.getPawn(3, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
	//				this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo() - 1, a.getColumnTo()));
				}
			}
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("f5")) {
				if (state.getPawn(4, 6).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
//					this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo() - 1, a.getColumnTo()));
				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))) {
					state.setTurn(State.Turn.BLACKWIN);
//					this.loggGame.fine("Nero vince con re catturato in: " + state.getBox(a.getRowTo() - 1, a.getColumnTo()));
				}
			}
		}
		return state;
	}

	private State checkCaptureBlackPawnRight(State state, Action a) {
		// mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("W")) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
//				this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
			}
			if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
//				this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
			}
			if (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
//				this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 2).equals("e5")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
//				this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() + 1));
			}

		}

		return state;
	}

	private State checkCaptureBlackPawnLeft(State state, Action a) {
		// mangio a sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("W")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
						|| (state.getBox(a.getRowTo(), a.getColumnTo() - 2).equals("e5")))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
			this.movesWithutCapturing = -1;
//			this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo(), a.getColumnTo() - 1));
		}
		return state;
	}

	private State checkCaptureBlackPawnUp(State state, Action a) {
		// controllo se mangio sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo() - 2, a.getColumnTo()).equals("e5")))) {
			state.removePawn(a.getRowTo() - 1, a.getColumnTo());
			this.movesWithutCapturing = -1;
//			this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo() - 1, a.getColumnTo()));
		}
		return state;
	}

	private State checkCaptureBlackPawnDown(State state, Action a) {
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo() + 2, a.getColumnTo()).equals("e5")))) {
			state.removePawn(a.getRowTo() + 1, a.getColumnTo());
			this.movesWithutCapturing = -1;
//			this.loggGame.fine("Pedina bianca rimossa in: " + state.getBox(a.getRowTo() + 1, a.getColumnTo()));
		}
		return state;
	}

	private State checkCaptureBlack(State state, Action a) {

		this.checkCaptureBlackPawnRight(state, a);
		this.checkCaptureBlackPawnLeft(state, a);
		this.checkCaptureBlackPawnUp(state, a);
		this.checkCaptureBlackPawnDown(state, a);
		this.checkCaptureBlackKingRight(state, a);
		this.checkCaptureBlackKingLeft(state, a);
		this.checkCaptureBlackKingDown(state, a);
		this.checkCaptureBlackKingUp(state, a);

		this.movesWithutCapturing++;
		return state;
	}

	@Override
	public State movePawn(State state, Action a) {

		State.Pawn pawn = state.getPawn(a.getRowFrom(), a.getColumnFrom());
		State.Pawn[][] newBoard = state.getBoard();
		// State newState = new State();
//		this.loggGame.fine("Movimento pedina");
		// libero il trono o una casella qualunque
		if (a.getColumnFrom() == 4 && a.getRowFrom() == 4) {
			newBoard[a.getRowFrom()][a.getColumnFrom()] = State.Pawn.THRONE;
		} else {
			newBoard[a.getRowFrom()][a.getColumnFrom()] = State.Pawn.EMPTY;
		}

		// metto nel nuovo tabellone la pedina mossa
		newBoard[a.getRowTo()][a.getColumnTo()] = pawn;
		// aggiorno il tabellone
		state.setBoard(newBoard);
		// cambio il turno
		if (state.getTurn().equalsTurn(State.Turn.WHITE.toString())) {
			state.setTurn(State.Turn.BLACK);
		} else {
			state.setTurn(State.Turn.WHITE);
		}

		
		
		if (state.getTurn().equalsTurn("W")) {
			state = this.checkCaptureBlack(state, a);
		} else if (state.getTurn().equalsTurn("B")) {
			state = this.checkCaptureWhite(state, a);

		}
		
		
		
		return state;
	}

	private State checkMove(State state, Action a)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {
		// this.loggGame.fine(a.toString());
		// controllo la mossa
		if (a.getTo().length() != 2 || a.getFrom().length() != 2) {
			// this.loggGame.warning("Formato mossa errato");
			throw new ActionException(a);
		}
		int columnFrom = a.getColumnFrom();
		int columnTo = a.getColumnTo();
		int rowFrom = a.getRowFrom();
		int rowTo = a.getRowTo();

		// controllo se sono fuori dal tabellone
		if (columnFrom > state.getBoard().length - 1 || rowFrom > state.getBoard().length - 1
				|| rowTo > state.getBoard().length - 1 || columnTo > state.getBoard().length - 1 || columnFrom < 0
				|| rowFrom < 0 || rowTo < 0 || columnTo < 0) {
			// this.loggGame.warning("Mossa fuori tabellone");
			throw new BoardException(a);
		}

		// controllo che non vada sul trono
		if (state.getPawn(rowTo, columnTo).equalsPawn(State.Pawn.THRONE.toString())) {
			// this.loggGame.warning("Mossa sul trono");
			throw new ThroneException(a);
		}

		// controllo la casella di arrivo
		if (!state.getPawn(rowTo, columnTo).equalsPawn(State.Pawn.EMPTY.toString())) {
			// this.loggGame.warning("Mossa sopra una casella occupata");
			throw new OccupitedException(a);
		}
		if (this.citadels.contains(state.getBox(rowTo, columnTo))
				&& !this.citadels.contains(state.getBox(rowFrom, columnFrom))) {
			// this.loggGame.warning("Mossa che arriva sopra una citadel");
			throw new CitadelException(a);
		}
		if (this.citadels.contains(state.getBox(rowTo, columnTo))
				&& this.citadels.contains(state.getBox(rowFrom, columnFrom))) {
			if (rowFrom == rowTo) {
				if (columnFrom - columnTo > 5 || columnFrom - columnTo < -5) {
					// this.loggGame.warning("Mossa che arriva sopra una citadel");
					throw new CitadelException(a);
				}
			} else {
				if (rowFrom - rowTo > 5 || rowFrom - rowTo < -5) {
					// this.loggGame.warning("Mossa che arriva sopra una citadel");
					throw new CitadelException(a);
				}
			}

		}

		// controllo se cerco di stare fermo
		if (rowFrom == rowTo && columnFrom == columnTo) {
			// this.loggGame.warning("Nessuna mossa");
			throw new StopException(a);
		}

		// controllo se sto muovendo una pedina giusta
		if (state.getTurn().equalsTurn(State.Turn.WHITE.toString())) {
			if (!state.getPawn(rowFrom, columnFrom).equalsPawn("W")
					&& !state.getPawn(rowFrom, columnFrom).equalsPawn("K")) {
				// this.loggGame.warning("Giocatore " + a.getTurn() + " cerca di muovere una
				// pedina avversaria");
				throw new PawnException(a);
			}
		}
		if (state.getTurn().equalsTurn(State.Turn.BLACK.toString())) {
			if (!state.getPawn(rowFrom, columnFrom).equalsPawn("B")) {
				// this.loggGame.warning("Giocatore " + a.getTurn() + " cerca di muovere una
				// pedina avversaria");
				throw new PawnException(a);
			}
		}

		// controllo di non muovere in diagonale
		if (rowFrom != rowTo && columnFrom != columnTo) {
			// this.loggGame.warning("Mossa in diagonale");
			throw new DiagonalException(a);
		}

		// controllo di non scavalcare pedine
		if (rowFrom == rowTo) {
			if (columnFrom > columnTo) {
				for (int i = columnTo; i < columnFrom; i++) {
					if (!state.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString())) {
						if (state.getPawn(rowFrom, i).equalsPawn(State.Pawn.THRONE.toString())) {
							// this.loggGame.warning("Mossa che scavalca il trono");
							throw new ClimbingException(a);
						} else {
							// this.loggGame.warning("Mossa che scavalca una pedina");
							throw new ClimbingException(a);
						}
					}
					if (this.citadels.contains(state.getBox(rowFrom, i))
							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
						// this.loggGame.warning("Mossa che scavalca una citadel");
						throw new ClimbingCitadelException(a);
					}
				}
			} else {
				for (int i = columnFrom + 1; i <= columnTo; i++) {
					if (!state.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString())) {
						if (state.getPawn(rowFrom, i).equalsPawn(State.Pawn.THRONE.toString())) {
							// this.loggGame.warning("Mossa che scavalca il trono");
							throw new ClimbingException(a);
						} else {
							// this.loggGame.warning("Mossa che scavalca una pedina");
							throw new ClimbingException(a);
						}
					}
					if (this.citadels.contains(state.getBox(rowFrom, i))
							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
//						this.loggGame.warning("Mossa che scavalca una citadel");
						throw new ClimbingCitadelException(a);
					}
				}
			}
		} else {
			if (rowFrom > rowTo) {
				for (int i = rowTo; i < rowFrom; i++) {
					if (!state.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString())) {
						if (state.getPawn(i, columnFrom).equalsPawn(State.Pawn.THRONE.toString())) {
							// this.loggGame.warning("Mossa che scavalca il trono");
							throw new ClimbingException(a);
						} else {
							// this.loggGame.warning("Mossa che scavalca una pedina");
							throw new ClimbingException(a);
						}
					}
					if (this.citadels.contains(state.getBox(i, columnFrom))
							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
						// this.loggGame.warning("Mossa che scavalca una citadel");
						throw new ClimbingCitadelException(a);
					}
				}
			} else {
				for (int i = rowFrom + 1; i <= rowTo; i++) {
					if (!state.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString())) {
						if (state.getPawn(i, columnFrom).equalsPawn(State.Pawn.THRONE.toString())) {
							// this.loggGame.warning("Mossa che scavalca il trono");
							throw new ClimbingException(a);
						} else {
							// this.loggGame.warning("Mossa che scavalca una pedina");
							throw new ClimbingException(a);
						}
					}
					if (this.citadels.contains(state.getBox(i, columnFrom))
							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
						// this.loggGame.warning("Mossa che scavalca una citadel");
						throw new ClimbingCitadelException(a);
					}
				}
			}
		}

		return state;
	}

	// OLD VERSION

//	private State checkMove(State state, Action a)
//			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
//			ThroneException, OccupitedException, CitadelException, ClimbingCitadelException {
////		this.loggGame.fine(a.toString());
//		// controllo la mossa
//		if (a.getTo().length() != 2 || a.getFrom().length() != 2) {
////			this.loggGame.warning("Formato mossa errato");
//			throw new ActionException(a);
//		}
//		int columnFrom = a.getColumnFrom();
//		int columnTo = a.getColumnTo();
//		int rowFrom = a.getRowFrom();
//		int rowTo = a.getRowTo();
//
//		// controllo se sono fuori dal tabellone
//		if (columnFrom > state.getBoard().length - 1 || rowFrom > state.getBoard().length - 1
//				|| rowTo > state.getBoard().length - 1 || columnTo > state.getBoard().length - 1 || columnFrom < 0
//				|| rowFrom < 0 || rowTo < 0 || columnTo < 0) {
////			this.loggGame.warning("Mossa fuori tabellone");
//			throw new BoardException(a);
//		}
//
//		// controllo che non vada sul trono
//		if (state.getPawn(rowTo, columnTo).equalsPawn(State.Pawn.THRONE.toString())) {
////			this.loggGame.warning("Mossa sul trono");
//			throw new ThroneException(a);
//		}
//
//		// controllo la casella di arrivo
//		if (!state.getPawn(rowTo, columnTo).equalsPawn(State.Pawn.EMPTY.toString())) {
////			this.loggGame.warning("Mossa sopra una casella occupata");
//			throw new OccupitedException(a);
//		}
//		if (this.citadels.contains(state.getBox(rowTo, columnTo))
//				&& !this.citadels.contains(state.getBox(rowFrom, columnFrom))) {
////			this.loggGame.warning("Mossa che arriva sopra una citadel");
//			throw new CitadelException(a);
//		}
//		if (this.citadels.contains(state.getBox(rowTo, columnTo))
//				&& this.citadels.contains(state.getBox(rowFrom, columnFrom))) {
//			if (rowFrom == rowTo) {
//				if (columnFrom - columnTo > 5 || columnFrom - columnTo < -5) {
////					this.loggGame.warning("Mossa che arriva sopra una citadel");
//					throw new CitadelException(a);
//				}
//			} else {
//				if (rowFrom - rowTo > 5 || rowFrom - rowTo < -5) {
////					this.loggGame.warning("Mossa che arriva sopra una citadel");
//					throw new CitadelException(a);
//				}
//			}
//
//		}
//
//		// controllo se cerco di stare fermo
//		if (rowFrom == rowTo && columnFrom == columnTo) {
////			this.loggGame.warning("Nessuna mossa");
//			throw new StopException(a);
//		}
//
//		// controllo se sto muovendo una pedina giusta
//		if (state.getTurn().equalsTurn(State.Turn.WHITE.toString())) {
//			if (!state.getPawn(rowFrom, columnFrom).equalsPawn("W")
//					&& !state.getPawn(rowFrom, columnFrom).equalsPawn("K")) {
////				this.loggGame.warning("Giocatore " + a.getTurn() + " cerca di muovere una pedina avversaria");
//				throw new PawnException(a);
//			}
//		}
//		if (state.getTurn().equalsTurn(State.Turn.BLACK.toString())) {
//			if (!state.getPawn(rowFrom, columnFrom).equalsPawn("B")) {
////				this.loggGame.warning("Giocatore " + a.getTurn() + " cerca di muovere una pedina avversaria");
//				throw new PawnException(a);
//			}
//		}
//
//		// controllo di non muovere in diagonale
//		if (rowFrom != rowTo && columnFrom != columnTo) {
////			this.loggGame.warning("Mossa in diagonale");
//			throw new DiagonalException(a);
//		}
//
//		// controllo di non scavalcare pedine
//		if (rowFrom == rowTo) {
//			if (columnFrom > columnTo) {
//				for (int i = columnTo; i < columnFrom; i++) {
//					if (!state.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString())) {
//						if (state.getPawn(rowFrom, i).equalsPawn(State.Pawn.THRONE.toString())) {
////							this.loggGame.warning("Mossa che scavalca il trono");
//							throw new ClimbingException(a);
//						} else {
////							this.loggGame.warning("Mossa che scavalca una pedina");
//							throw new ClimbingException(a);
//						}
//					}
//					if (this.citadels.contains(state.getBox(rowFrom, i))
//							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
////						this.loggGame.warning("Mossa che scavalca una citadel");
//						throw new ClimbingCitadelException(a);
//					}
//				}
//			} else {
//				for (int i = columnFrom + 1; i <= columnTo; i++) {
//					if (!state.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString())) {
//						if (state.getPawn(rowFrom, i).equalsPawn(State.Pawn.THRONE.toString())) {
////							this.loggGame.warning("Mossa che scavalca il trono");
//							throw new ClimbingException(a);
//						} else {
////							this.loggGame.warning("Mossa che scavalca una pedina");
//							throw new ClimbingException(a);
//						}
//					}
//					if (this.citadels.contains(state.getBox(rowFrom, i))
//							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
////						this.loggGame.warning("Mossa che scavalca una citadel");
//						throw new ClimbingCitadelException(a);
//					}
//				}
//			}
//		} else {
//			if (rowFrom > rowTo) {
//				for (int i = rowTo; i < rowFrom; i++) {
//					if (!state.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString())) {
//						if (state.getPawn(i, columnFrom).equalsPawn(State.Pawn.THRONE.toString())) {
////							this.loggGame.warning("Mossa che scavalca il trono");
//							throw new ClimbingException(a);
//						} else {
////							this.loggGame.warning("Mossa che scavalca una pedina");
//							throw new ClimbingException(a);
//						}
//					}
//					if (this.citadels.contains(state.getBox(i, columnFrom))
//							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
////						this.loggGame.warning("Mossa che scavalca una citadel");
//						throw new ClimbingCitadelException(a);
//					}
//				}
//			} else {
//				for (int i = rowFrom + 1; i <= rowTo; i++) {
//					if (!state.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString())) {
//						if (state.getPawn(i, columnFrom).equalsPawn(State.Pawn.THRONE.toString())) {
////							this.loggGame.warning("Mossa che scavalca il trono");
//							throw new ClimbingException(a);
//						} else {
////							this.loggGame.warning("Mossa che scavalca una pedina");
//							throw new ClimbingException(a);
//						}
//					}
//					if (this.citadels.contains(state.getBox(i, columnFrom))
//							&& !this.citadels.contains(state.getBox(a.getRowFrom(), a.getColumnFrom()))) {
////						this.loggGame.warning("Mossa che scavalca una citadel");
//						throw new ClimbingCitadelException(a);
//					}
//				}
//			}
//		}
//
//		return state;
//	}

	public List<Action> getNextMovesFromState(State state) {

		List<Action> possibleMoves = new ArrayList<Action>();

		if (state.getTurn().equalsTurn(State.Turn.WHITE.toString())) {
			List<int[]> pawns = new ArrayList<int[]>();

			int[] buf;
			for (int i = 0; i < state.getBoard().length; i++) {
				for (int j = 0; j < state.getBoard().length; j++) {
					if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
							|| state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
						buf = new int[2];
						buf[0] = i;
						buf[1] = j;
						pawns.add(buf);

					}
				}
			}

			String from = "";
			String to = "";
			int[] p;

			for (int i = 0; i < pawns.size(); i++) {
				p = pawns.get(i);

				for (int j = 0; j < state.getBoard().length; j++) {

					int[] orr = new int[2];
					int[] ver = new int[2];

					orr[0] = p[0];
					orr[1] = j;

					ver[0] = j;
					ver[1] = p[1];

					from = state.getBox(p[0], p[1]);

					to = state.getBox(orr[0], orr[1]);
					try {
						Action a = new Action(from, to, state.getTurn());
						this.checkMove(state, a);

						possibleMoves.add(a);
					} catch (Exception e1) {

					}

					from = state.getBox(p[0], p[1]);

					to = state.getBox(ver[0], ver[1]);
					try {
						Action a = new Action(from, to, state.getTurn());
						this.checkMove(state, a);

						possibleMoves.add(a);

					} catch (Exception e1) {

					}

				}

			}

		} else if (state.getTurn().equalsTurn(State.Turn.BLACK.toString())) {

			List<int[]> pawns = new ArrayList<int[]>();

			int[] buf;
			for (int i = 0; i < state.getBoard().length; i++) {
				for (int j = 0; j < state.getBoard().length; j++) {
					if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
						buf = new int[2];
						buf[0] = i;
						buf[1] = j;
						pawns.add(buf);

					}
				}
			}

			String from = "";
			String to = "";
			int[] p;

			for (int i = 0; i < pawns.size(); i++) {
				p = pawns.get(i);

				for (int j = 0; j < state.getBoard().length; j++) {

					int[] orr = new int[2];
					int[] ver = new int[2];

					orr[0] = p[0];
					orr[1] = j;

					ver[0] = j;
					ver[1] = p[1];

					from = state.getBox(p[0], p[1]);

					to = state.getBox(orr[0], orr[1]);
					try {
						Action a = new Action(from, to, state.getTurn());
						this.checkMove(state, a);

						possibleMoves.add(a);
					} catch (Exception e1) {

					}

					from = state.getBox(p[0], p[1]);

					to = state.getBox(ver[0], ver[1]);
					try {
						Action a = new Action(from, to, state.getTurn());
						this.checkMove(state, a);

						possibleMoves.add(a);

					} catch (Exception e1) {

					}

				}

			}
		}
		return possibleMoves;
	}

}
