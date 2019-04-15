package it.unibo.ai.didattica.competition.tablut.luca;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.Game;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
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

public class MinMax implements IA {

	public final static int MIN = -1;
	public final static int MAX = 1;
	public final static int DEPTH = 5;

	private Game rules;
	private List<Node> rootChildren;
	private List<int[]> pawns;


	public MinMax(Game rules) {
		this.rules = rules;
		this.rootChildren = new ArrayList<>();
		this.pawns = new ArrayList<int[]>();

	}

	@Override
	public Action getBestAction(State state, Turn yourColor)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {
		return this.minmaxAlg(state, DEPTH, yourColor);
	}

	private Action minmaxAlg(State state, int depth, Turn yourColor)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		Node root = new Node(state);
		
		NodeUtil.incrementExpanseNodes();
		
		if (yourColor.equals(State.Turn.BLACK))
			root.setValue(maxValue(root, depth - 1));
		else if (yourColor.equals(State.Turn.WHITE))
			root.setValue(minValue(root, depth - 1));

		Node bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();

		rootChildren.clear();
		return bestNextNode.getMove();

	}

	private double maxValue(Node node, int depth)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (depth == 0) {
			Heuristic h = new RandomHeuristic();
			return h.heuristic(node);
		}


		int[] buf;
		for (int i = 0; i < node.getState().getBoard().length; i++) {
			for (int j = 0; j < node.getState().getBoard().length; j++) {
				if (node.getState().getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
					buf = new int[2];
					buf[0] = i;
					buf[1] = j;
					pawns.add(buf);

				}
			}
		}

		List<Action> possibleMoves = new ArrayList<Action>();
		String from = "";
		String to = "";
		int[] p;

		for (int i = 0; i < this.pawns.size(); i++) {
			p = this.pawns.get(i);

			for (int j = 0; j < node.getState().getBoard().length; j++) {

				int[] orr = new int[2];
				int[] ver = new int[2];

				orr[0] = p[0];
				orr[1] = j;

				ver[0] = j;
				ver[1] = p[1];

				from = node.getState().getBox(p[0], p[1]);

				to = node.getState().getBox(orr[0], orr[1]);
				try {
					Action a = new Action(from, to, State.Turn.BLACK);
					checkMove(node.getState(), a);

					// state.setTurn(State.Turn.BLACK);

					possibleMoves.add(a);
				} catch (Exception e1) {

				}

				from = node.getState().getBox(p[0], p[1]);

				to = node.getState().getBox(ver[0], ver[1]);
				try {
					Action a = new Action(from, to, State.Turn.BLACK);
					checkMove(node.getState(), a);

					possibleMoves.add(a);

					// state.setTurn(State.Turn.BLACK);

				} catch (Exception e1) {

				}

			}

		}
		this.pawns.clear();

		node.setValue(Double.MIN_VALUE);
		Double v = Double.MIN_VALUE;
		
		for (Action a : possibleMoves) {
			State nextState = movePawn(node.getState(), a);
			Node n = new Node(nextState.clone(), Double.MAX_VALUE, a);
			
			NodeUtil.incrementExpanseNodes();
			
			v = Math.max(v, minValue(n, depth - 1));

			if (depth == DEPTH - 1) {

				rootChildren.add(n);

			}
		}

		return v;
	}

	private double minValue(Node node, int depth)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (depth == 0) {
			Heuristic h = new RandomHeuristic();
			return h.heuristic(node);
		}


		int[] buf;
		for (int i = 0; i < node.getState().getBoard().length; i++) {
			for (int j = 0; j < node.getState().getBoard().length; j++) {
				if (node.getState().getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())) {
					buf = new int[2];
					buf[0] = i;
					buf[1] = j;
					pawns.add(buf);

				}
			}
		}

		List<Action> possibleMoves = new ArrayList<Action>();
		String from = "";
		String to = "";
		int[] p;

		for (int i = 0; i < this.pawns.size(); i++) {
			p = this.pawns.get(i);

			for (int j = 0; j < node.getState().getBoard().length; j++) {

				int[] orr = new int[2];
				int[] ver = new int[2];

				orr[0] = p[0];
				orr[1] = j;

				ver[0] = j;
				ver[1] = p[1];

				from = node.getState().getBox(p[0], p[1]);

				to = node.getState().getBox(orr[0], orr[1]);
				try {
					Action a = new Action(from, to, State.Turn.WHITE);
					checkMove(node.getState(), a);

					// state.setTurn(State.Turn.WHITE);

					possibleMoves.add(a);
				} catch (Exception e1) {

				}

				from = node.getState().getBox(p[0], p[1]);

				to = node.getState().getBox(ver[0], ver[1]);
				try {
					Action a = new Action(from, to, State.Turn.WHITE);
					checkMove(node.getState(), a);
					possibleMoves.add(a);

					// state.setTurn(State.Turn.WHITE);

				} catch (Exception e1) {

				}

			}

		}
		this.pawns.clear();
		
		node.setValue(Double.MAX_VALUE);
		Double v = Double.MAX_VALUE;
		for (Action a : possibleMoves) {
			State nextState = movePawn( node.getState(), a);
			
			Node n = new Node(nextState, Double.MIN_VALUE, a);
			
			NodeUtil.incrementExpanseNodes();
			
			v = Math.min(v, maxValue(n, depth - 1));

			if (depth == DEPTH - 1) {

				rootChildren.add(n);

			}
		}
		possibleMoves.clear();

		return v;
	}

	private State movePawn(State state, Action a) {
		State.Pawn pawn = state.getPawn(a.getRowFrom(), a.getColumnFrom());
		State.Pawn[][] newBoard = state.getBoard();
		// State newState = new State();
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

		return state;
	}

	private State checkMove(State state, Action a) throws BoardException, ActionException, StopException, PawnException,
			DiagonalException, ClimbingException, ThroneException, OccupitedException {
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

		// controllo se cerco di stare fermo
		if (rowFrom == rowTo && columnFrom == columnTo) {
			// this.loggGame.warning("Nessuna mossa");
			throw new StopException(a);
		}

		// controllo se sto muovendo una pedina giusta
		if (state.getTurn().equalsTurn(State.Turn.WHITE.toString())) {
			if (!state.getPawn(rowFrom, columnFrom).equalsPawn("W")
					&& !state.getPawn(rowFrom, columnFrom).equalsPawn("K")) {
				// this.loggGame.warning("Giocatore "+a.getTurn()+" cerca di
				// muovere una pedina avversaria");
				throw new PawnException(a);
			}
		}
		if (state.getTurn().equalsTurn(State.Turn.BLACK.toString())) {
			if (!state.getPawn(rowFrom, columnFrom).equalsPawn("B")) {
				// this.loggGame.warning("Giocatore "+a.getTurn()+" cerca di
				// muovere una pedina avversaria");
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
					if (!state.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString())
							&& !state.getPawn(rowFrom, i).equalsPawn(State.Pawn.THRONE.toString())) {
						// this.loggGame.warning("Mossa che scavalca una
						// pedina");
						throw new ClimbingException(a);
					}
				}
			} else {
				for (int i = columnFrom + 1; i <= columnTo; i++) {
					if (!state.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString())
							&& !state.getPawn(rowFrom, i).equalsPawn(State.Pawn.THRONE.toString())) {
						// this.loggGame.warning("Mossa che scavalca una
						// pedina");
						throw new ClimbingException(a);
					}
				}
			}
		} else {
			if (rowFrom > rowTo) {
				for (int i = rowTo; i < rowFrom; i++) {
					if (!state.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString())
							&& !state.getPawn(i, columnFrom).equalsPawn(State.Pawn.THRONE.toString())) {
						// this.loggGame.warning("Mossa che scavalca una
						// pedina");
						throw new ClimbingException(a);
					}
				}
			} else {
				for (int i = rowFrom + 1; i <= rowTo; i++) {
					if (!state.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString())
							&& !state.getPawn(i, columnFrom).equalsPawn(State.Pawn.THRONE.toString())) {
						// this.loggGame.warning("Mossa che scavalca una
						// pedina");
						throw new ClimbingException(a);
					}
				}
			}
		}

		return state;
	}

}
