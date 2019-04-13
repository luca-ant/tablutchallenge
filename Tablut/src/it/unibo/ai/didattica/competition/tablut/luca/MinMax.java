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
	public final static int DEPTH = 2;

	private Game rules;
	private List<Node> rootChildren;
	private List<int[]> pawns;
	private List<int[]> empty;

	public MinMax(Game rules) {
		this.rules = rules;
		this.rootChildren = new ArrayList<>();
		this.pawns = new ArrayList<int[]>();
		this.empty = new ArrayList<int[]>();
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

		if (yourColor.equals(State.Turn.BLACK))
			root.setValue(maxValue(state, depth - 1));
		else if (yourColor.equals(State.Turn.WHITE))
			root.setValue(minValue(state, depth - 1));

		Node bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();

		return bestNextNode.getMove();

	}

	private double maxValue(State state, int depth)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (depth == 0) {
			Heuristic h = new RandomHeuristic();
			return h.heuristic(state);
		}

		Double v = Double.MIN_VALUE;

		int[] buf;
		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard().length; j++) {
				if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
					buf = new int[2];
					buf[0] = i;
					buf[1] = j;
					pawns.add(buf);
				} else if (state.getPawn(i, j).equalsPawn(State.Pawn.EMPTY.toString())) {
					buf = new int[2];
					buf[0] = i;
					buf[1] = j;
					empty.add(buf);
				}
			}
		}

		List<Action> possibleMoves = new ArrayList<Action>();
		String from = "";
		String to = "";
		int[] p;
		int[] e;
		for (int i = 0; i < this.pawns.size(); i++) {
			p = this.pawns.get(i);

			for (int j = 0; j < this.empty.size(); i++) {

				e = this.empty.get(j);

				from = state.getBox(p[0], p[1]);

				to = state.getBox(e[0], e[1]);
				try {
					Action a = new Action(from, to, State.Turn.BLACK);
					rules.checkMove(state, a);
					possibleMoves.add(a);
				} catch (Exception e1) {

				}
			}

		}
		this.pawns.clear();
		this.empty.clear();

		for (Action a : possibleMoves) {
			State nextState = this.rules.checkMove(state, a);
			v = Math.max(v, minValue(nextState, depth - 1));

			if (depth == DEPTH - 1) {
				Node n = new Node(nextState, v, a);

				rootChildren.add(n);

			}
		}

		return v;
	}

	private double minValue(State state, int depth)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (depth == 0) {
			Heuristic h = new RandomHeuristic();
			return h.heuristic(state);
		}

		Double v = Double.MAX_VALUE;

		int[] buf;

		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard().length; j++) {
				if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
						|| state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
					buf = new int[2];
					buf[0] = i;
					buf[1] = j;
					pawns.add(buf);
				} else if (state.getPawn(i, j).equalsPawn(State.Pawn.EMPTY.toString())) {
					buf = new int[2];
					buf[0] = i;
					buf[1] = j;
					empty.add(buf);
				}
			}
		}
		List<Action> possibleMoves = new ArrayList<Action>();
		String from = "";
		String to = "";
		int[] p;
		int[] e;
		for (int i = 0; i < this.pawns.size(); i++) {
			p = this.pawns.get(i);

			for (int j = 0; j < this.empty.size(); i++) {

				e = this.empty.get(j);

				from = state.getBox(p[0], p[1]);

				to = state.getBox(e[0], e[1]);
				try {
					Action a = new Action(from, to, State.Turn.WHITE);
					rules.checkMove(state, a);
					possibleMoves.add(a);
				} catch (Exception e1) {

				}
			}

		}
		this.pawns.clear();
		this.empty.clear();

		for (Action a : possibleMoves) {
			State nextState = this.rules.checkMove(state, a);
			v = Math.max(v, minValue(nextState, depth - 1));

			if (depth == DEPTH - 1) {
				Node n = new Node(nextState, v, a);

				rootChildren.add(n);

			}
		}

		return v;
	}

}
