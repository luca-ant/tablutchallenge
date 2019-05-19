package it.unibo.ai.didattica.competition.tablut.teampallo.algorithms;

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
import it.unibo.ai.didattica.competition.tablut.teampallo.domain.MyRules;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.AdvancedHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.StatsManager;

public class AlphaBetaIterativeWithMemory implements IA {

	private List<Node> rootChildren;
	private Map<Integer, Node> transpositionTable;
	private long endTime;
	private Action bestMove;
	private boolean ww;
	private boolean bw;
	private Heuristic heuristic;

	public AlphaBetaIterativeWithMemory() {

		this.rootChildren = new ArrayList<>();
		this.heuristic = new AdvancedHeuristic();
		this.transpositionTable = new Hashtable<Integer, Node>();
		this.bestMove = null;
		this.ww = false;
		this.bw = false;
	}

	@Override
	public Action getBestAction(State state)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		this.endTime = System.currentTimeMillis() + GameManager.getInstance().getTimeout() * 1000;

		Action temp;
		this.bestMove = null;

		for (int d = 1; d <= GameManager.getInstance().getMaxDepth(); ++d) {
			System.out.println("\nSTART DEPTH = " + d);

			StatsManager.getInstance().reset();
			StatsManager.getInstance().setStart(System.currentTimeMillis());

			temp = this.minmaxAlg(state, d, d);

			StatsManager.getInstance().setEnd(System.currentTimeMillis());

			if (System.currentTimeMillis() > this.endTime) {
				System.out.println("END DUE TO TIMEOUT\n");
				StatsManager.getInstance().printResults();

				break;
			}
			System.out.println("END DEPTH = " + d + "\n");
			StatsManager.getInstance().printResults();

			System.out.println("Temp move found: " + temp);

			if (this.ww && GameManager.getInstance().getPlayer().equalsIgnoreCase("white")) {
				this.bestMove = temp;
				break;
			}

			if (this.bw && GameManager.getInstance().getPlayer().equalsIgnoreCase("black")) {
				this.bestMove = temp;
				break;
			}

			this.bestMove = temp;

		}

		return this.bestMove;

	}

	private Action minmaxAlg(State state, int depth, int maxDepth)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		Node root = new Node(state);

		StatsManager.getInstance().incrementExpandedNodes();

		root.setValue(maxValue(root, depth, maxDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));

		if (System.currentTimeMillis() > this.endTime || this.rootChildren.isEmpty()) {

			return this.bestMove;
		}

		for (Node node : rootChildren) {
			if (node.getState().getTurn().equalsTurn(State.Turn.WHITEWIN.toString())
					&& GameManager.getInstance().getPlayer().equalsIgnoreCase("white")) {
				this.ww = true;
				return node.getMove();
			}
			if (node.getState().getTurn().equalsTurn(State.Turn.BLACKWIN.toString())
					&& GameManager.getInstance().getPlayer().equalsIgnoreCase("black")) {

				this.bw = true;
				return node.getMove();

			}

		}

		Node bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();

		rootChildren.clear();
		if (bestNextNode != null) {

			System.out.println("H: " + bestNextNode.getValue());

			return bestNextNode.getMove();

		} else {
			return this.bestMove;
		}

	}

	private double maxValue(Node node, int depth, int maxDepth, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (System.currentTimeMillis() > this.endTime) {

			return 0;
		}
		if (depth == 0) {
			// return this.heuristic.heuristicBlack(node.getState());
			return this.heuristic.heuristic(node.getState());

		}

		if (this.transpositionTable.containsKey(node.getState().hashCode())) {
			System.out.println("Get node from transposition table");
			return this.transpositionTable.get(node.getState().hashCode()).getValue();
		}

		List<Action> possibleMoves = GameManager.getInstance().getRules().getNextMovesFromState(node.getState());

		Double v = Double.NEGATIVE_INFINITY;

		for (Action a : possibleMoves) {
			State nextState = GameManager.getInstance().getRules().movePawn(node.getState().clone(), a);
			Node n = new Node(nextState, Double.POSITIVE_INFINITY, a);

			StatsManager.getInstance().incrementExpandedNodes();

			v = Math.max(v, minValue(n, depth - 1, maxDepth, alpha, beta));

			n.setValue(v);

			if (!this.transpositionTable.containsKey(n.getState().hashCode())) {

				if (StatsManager.getInstance().getOccupiedMemoryInMB() > GameManager.getInstance().getMemoryLimit()) {

					this.transpositionTable.remove(new ArrayList<>(this.transpositionTable.keySet()).get(0));
				}

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

	private double minValue(Node node, int depth, int maxDepth, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (System.currentTimeMillis() > this.endTime) {

			return 0;
		}
		if (depth == 0) {
			// return this.heuristic.heuristicWhite(node.getState());
			return this.heuristic.heuristic(node.getState());

		}

		if (this.transpositionTable.containsKey(node.getState().hashCode())) {
			System.out.println("Get node from transposition table");

			return this.transpositionTable.get(node.getState().hashCode()).getValue();
		}

		List<Action> possibleMoves = GameManager.getInstance().getRules().getNextMovesFromState(node.getState());

		Double v = Double.POSITIVE_INFINITY;
		for (Action a : possibleMoves) {
			State nextState = GameManager.getInstance().getRules().movePawn(node.getState().clone(), a);

			Node n = new Node(nextState, Double.NEGATIVE_INFINITY, a);

			StatsManager.getInstance().incrementExpandedNodes();

			v = Math.min(v, maxValue(n, depth - 1, maxDepth, alpha, beta));

			n.setValue(v);

			if (!this.transpositionTable.containsKey(n.getState().hashCode())) {
				if (StatsManager.getInstance().getOccupiedMemoryInMB() > GameManager.getInstance().getMemoryLimit()) {

					this.transpositionTable.remove(new ArrayList<>(this.transpositionTable.keySet()).get(0));
				}

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
