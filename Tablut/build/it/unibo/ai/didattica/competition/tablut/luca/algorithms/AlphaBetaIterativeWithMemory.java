package it.unibo.ai.didattica.competition.tablut.luca.algorithms;

import java.awt.event.HierarchyEvent;
import java.lang.instrument.Instrumentation;

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
import it.unibo.ai.didattica.competition.tablut.luca.domain.MyRules;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.BasicHeuristic;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.RandomHeuristic;

public class AlphaBetaIterativeWithMemory implements IA {
	public final static int MAX_DEPTH = 10;

	private MyRules rules;
	private int timeout;
	private List<Node> rootChildren;
	private Map<Integer, Node> transpositionTable;
	private long endTime;
	private Action bestMove;
	private boolean ww;
	private boolean bw;

	private Heuristic heuristic;

	public AlphaBetaIterativeWithMemory(MyRules rules, int timeout) {
		this.timeout = timeout;

		this.rules = rules;
		this.rootChildren = new ArrayList<>();
		this.heuristic = new BasicHeuristic();
		this.transpositionTable = new Hashtable<Integer, Node>();
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
			System.out.println("START DEPTH = " + d);
			NodeUtil.getIstance().reset();
			temp = this.minmaxAlg(state, d, d, yourColor);

			System.out.println("END DEPTH = " + d);

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

//		if (yourColor.equals(State.Turn.BLACK))
		root.setValue(maxValue(root, depth, maxDepth, yourColor, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
//		else if (yourColor.equals(State.Turn.WHITE))
		// root.setValue(minValue(root, depth, maxDepth, yourColor,
		// Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));

		if (System.currentTimeMillis() > this.endTime || this.rootChildren.isEmpty()) {
			return this.bestMove;
		}

		Node bestNextNode = null;
//		if (yourColor.equals(State.Turn.BLACK)) {
		bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();
//		} else if (yourColor.equals(State.Turn.WHITE)) {
//			bestNextNode = rootChildren.stream().min(Comparator.comparing(n -> n.getValue())).get();
//		}
		rootChildren.clear();

		if (bestNextNode.getState().getTurn().equalsTurn("WW")) {

			this.ww = true;
		}
		if (bestNextNode.getState().getTurn().equalsTurn("BW")) {

			this.bw = true;
		}

		if (bestNextNode != null) {
			System.out.println("H: " + bestNextNode.getValue());

			return bestNextNode.getMove();

		} else {
			return this.bestMove;
		}

	}

	private double maxValue(Node node, int depth, int maxDepth, Turn yourColor, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (System.currentTimeMillis() > this.endTime) {
			return 0;
		}
		if (depth == 0) {
			// return this.heuristic.heuristicBlack(node.getState());
			return this.heuristic.heuristic(node.getState(), yourColor);

		}

		if (this.transpositionTable.containsKey(node.getState().hashCode())) {
			System.out.println("Get node from transposition table");
			return this.transpositionTable.get(node.getState().hashCode()).getValue();
		}

		List<Action> possibleMoves = this.rules.getNextMovesFromState(node.getState());

		Double v = Double.NEGATIVE_INFINITY;

		for (Action a : possibleMoves) {
			State nextState = this.rules.movePawn(node.getState().clone(), a);
			Node n = new Node(nextState, Double.POSITIVE_INFINITY, a);

			NodeUtil.getIstance().incrementExpandedNodes();

			v = Math.max(v, minValue(n, depth - 1, maxDepth, yourColor, alpha, beta));

			n.setValue(v);

			if (!this.transpositionTable.containsKey(n.getState().hashCode())) {

				this.transpositionTable.put(n.getState().hashCode(), n);
			}

			if (depth == maxDepth) {

				rootChildren.add(n);

			}

			if (v >= beta)
				return v;

			alpha = Math.max(alpha, v);
			

			if (System.currentTimeMillis() > this.endTime) {
				return 0;
			}

		}

		return v;
	}

	private double minValue(Node node, int depth, int maxDepth, Turn yourColor, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (System.currentTimeMillis() > this.endTime) {
			return 0;
		}
		if (depth == 0) {
			// return this.heuristic.heuristicWhite(node.getState());
			return this.heuristic.heuristic(node.getState(), yourColor);

		}

		if (this.transpositionTable.containsKey(node.getState().hashCode())) {
			System.out.println("Get node from transposition table");

			return this.transpositionTable.get(node.getState().hashCode()).getValue();
		}

		List<Action> possibleMoves = this.rules.getNextMovesFromState(node.getState());

		Double v = Double.POSITIVE_INFINITY;
		for (Action a : possibleMoves) {
			State nextState = this.rules.movePawn(node.getState().clone(), a);

			Node n = new Node(nextState, Double.NEGATIVE_INFINITY, a);

			NodeUtil.getIstance().incrementExpandedNodes();

			v = Math.min(v, maxValue(n, depth - 1, maxDepth, yourColor, alpha, beta));

			n.setValue(v);

			if (!this.transpositionTable.containsKey(n.getState().hashCode())) {

				this.transpositionTable.put(n.getState().hashCode(), n);
			}

			if (depth == maxDepth) {
				rootChildren.add(n);

			}
			if (v <= alpha)
				return v;

			beta = Math.min(beta, v);

			if (System.currentTimeMillis() > this.endTime) {
				return 0;
			}

		}
		possibleMoves.clear();

		return v;
	}

}
