package it.unibo.ai.didattica.competition.tablut.luca.algorithms;

import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
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
import it.unibo.ai.didattica.competition.tablut.luca.domain.MyGame;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.BasicHeuristic;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.RandomHeuristic;

public class AlphaBetaIterative implements IA {
	public final static int MAX_DEPTH = 4;

	private MyGame rules;
	private int timeout;
	private List<Node> rootChildren;
	private List<int[]> pawns;

	private long endTime;
	private Action bestMove;
	private boolean ww;
	private boolean bw;

	private Heuristic heuristic;

	public AlphaBetaIterative(MyGame rules, int timeout) {
		this.timeout = timeout;

		this.rules = rules;
		this.rootChildren = new ArrayList<>();
		this.pawns = new ArrayList<int[]>();
		this.heuristic = new BasicHeuristic();
		this.bestMove = null;
		this.ww = false;
		this.bw = false;

	}

	@Override
	public Action getBestAction(State state, Turn yourColor)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		this.endTime = System.currentTimeMillis() + this.timeout * 1000;

		Action temp;
		this.bestMove = null;

		for (int d = 1; d <= MAX_DEPTH; ++d) {
			System.out.println("DEPTH = " + d);
			NodeUtil.getIstance().reset();
			temp = this.minmaxAlg(state, d, d, yourColor);

			if (this.ww && yourColor.equals(State.Turn.WHITE)) {
				this.bestMove = temp;
				break;
			}
			
			if (this.bw && yourColor.equals(State.Turn.BLACK)) {
				this.bestMove = temp;
				break;
			}

			if (System.currentTimeMillis() > this.endTime) {
				break;
			}
			System.out.println("Temp move found: " + temp);

			this.bestMove = temp;

		}

		return this.bestMove;

	}

	private Action minmaxAlg(State state, int depth, int maxDepth, Turn yourColor)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		Node root = new Node(state);

		NodeUtil.getIstance().incrementExpandedNodes();

		if (yourColor.equals(State.Turn.BLACK))
			root.setValue(maxValue(root, depth, maxDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
		else if (yourColor.equals(State.Turn.WHITE))
			root.setValue(minValue(root, depth, maxDepth, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));

		if (System.currentTimeMillis() > this.endTime && this.rootChildren.isEmpty()) {
			return this.bestMove;
		}

		Node bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();

		rootChildren.clear();

		if (bestNextNode.getState().getTurn().equalsTurn("WW")) {

			this.ww = true;
		}
		if (bestNextNode.getState().getTurn().equalsTurn("BW")) {

			this.bw = true;
		}

		if (bestNextNode != null) {
			return bestNextNode.getMove();

		} else {
			return this.bestMove;
		}
	}

	private double maxValue(Node node, int depth, int maxDepth, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (depth == 0 || System.currentTimeMillis() > this.endTime) {
	//	return this.heuristic.heuristicBlack(node.getState()); // ok
		return this.heuristic.heuristicWhite(node.getState()); 
		//	return this.heuristic.heuristic(node.getState());

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
					rules.checkMove(node.getState(), a);

					// state.setTurn(State.Turn.BLACK);

					possibleMoves.add(a);
				} catch (Exception e1) {

				}

				from = node.getState().getBox(p[0], p[1]);

				to = node.getState().getBox(ver[0], ver[1]);
				try {
					Action a = new Action(from, to, State.Turn.BLACK);
					rules.checkMove(node.getState(), a);

					possibleMoves.add(a);

					// state.setTurn(State.Turn.BLACK);

				} catch (Exception e1) {

				}

			}

		}
		this.pawns.clear();

		Double v = Double.NEGATIVE_INFINITY;

		for (Action a : possibleMoves) {
			State nextState = rules.movePawn(node.getState().clone(), a);
			Node n = new Node(nextState, Double.POSITIVE_INFINITY, a);

			NodeUtil.getIstance().incrementExpandedNodes();

			v = Math.max(v, minValue(n, depth - 1, maxDepth, alpha, beta));

			n.setValue(v);

			if (depth == maxDepth) {

				rootChildren.add(n);

			}

			if (v >= beta)
				return v;

			alpha = Math.max(alpha, v);

		}

		return v;
	}

	private double minValue(Node node, int depth, int maxDepth, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (depth == 0 || System.currentTimeMillis() > this.endTime) {
		//	 return this.heuristic.heuristicWhite(node.getState()); // ok
			 return this.heuristic.heuristicBlack(node.getState()); 
		//	return this.heuristic.heuristic(node.getState());
		}

		int[] buf;
		for (int i = 0; i < node.getState().getBoard().length; i++) {
			for (int j = 0; j < node.getState().getBoard().length; j++) {
				if (node.getState().getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
						|| node.getState().getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
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
					rules.checkMove(node.getState(), a);

					// state.setTurn(State.Turn.WHITE);

					possibleMoves.add(a);
				} catch (Exception e1) {

				}

				from = node.getState().getBox(p[0], p[1]);

				to = node.getState().getBox(ver[0], ver[1]);
				try {
					Action a = new Action(from, to, State.Turn.WHITE);
					rules.checkMove(node.getState(), a);
					possibleMoves.add(a);

					// state.setTurn(State.Turn.WHITE);

				} catch (Exception e1) {

				}

			}

		}
		this.pawns.clear();

		Double v = Double.POSITIVE_INFINITY;
		for (Action a : possibleMoves) {
			State nextState = rules.movePawn(node.getState().clone(), a);

			Node n = new Node(nextState, Double.NEGATIVE_INFINITY, a);

			NodeUtil.getIstance().incrementExpandedNodes();

			v = Math.min(v, maxValue(n, depth - 1, maxDepth, alpha, beta));

			n.setValue(v);

			if (depth == maxDepth) {
				rootChildren.add(n);

			}

			if (v <= alpha)

				return v;

			alpha = Math.min(beta, v);

		}
		possibleMoves.clear();

		return v;
	}

}
