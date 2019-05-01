package it.unibo.ai.didattica.competition.tablut.luca.algorithms;

import java.util.ArrayList;
import java.util.Comparator;

import java.util.List;

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
import it.unibo.ai.didattica.competition.tablut.luca.util.StatsManager;

public class AlphaBetaIterative implements IA {
	public final static int MAX_DEPTH = 10;

	private MyRules rules;
	private int timeout;
	private List<Node> rootChildren;
	private long endTime;
	private Action bestMove;
	private boolean ww;
	private boolean bw;

	private Heuristic heuristic;

	public AlphaBetaIterative(MyRules rules, int timeout, String player) {
	//	public AlphaBetaIterative(MyRules rules, int timeout) {
		this.timeout = timeout;
		this.rules = rules;
		this.rootChildren = new ArrayList<>();
//		this.heuristic = new BasicHeuristic();
		this.heuristic = new BasicHeuristic(player);
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
			System.out.println("\nSTART DEPTH = " + d);
			
			StatsManager.getInstance().reset();
			StatsManager.getInstance().setStart(System.currentTimeMillis());
			
			temp = this.minmaxAlg(state, d, d, yourColor);
			
			StatsManager.getInstance().setEnd(System.currentTimeMillis());
			StatsManager.getInstance().printResults();
			

			
			if (System.currentTimeMillis() > this.endTime) {
				System.out.println("END DUE TO TIMEOUT\n");

				break;
			}
			System.out.println("END DEPTH = " + d +"\n");


			System.out.println("Temp move found: " + temp);

			if (this.ww && yourColor.equals(State.Turn.WHITE)) {
				this.bestMove = temp;
				break;
			}

			if (this.bw && yourColor.equals(State.Turn.BLACK)) {
				this.bestMove = temp;
				break;
			}

			this.bestMove = temp;

		}

		return this.bestMove;

	}

	private Action minmaxAlg(State state, int depth, int maxDepth, Turn yourColor)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		Node root = new Node(state);

		StatsManager.getInstance().incrementExpandedNodes();

		root.setValue(maxValue(root, depth, maxDepth, yourColor, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));

	if (System.currentTimeMillis() > this.endTime || this.rootChildren.isEmpty()) {

			return this.bestMove;
		}

		Node bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();

		rootChildren.clear();
		if (bestNextNode != null) {
			if (bestNextNode.getState().getTurn().equalsTurn("WW")) {

				this.ww = true;
			}
			if (bestNextNode.getState().getTurn().equalsTurn("BW")) {

				this.bw = true;
			}

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
			// return this.heuristic.heuristicWhite(node.getState());

			return this.heuristic.heuristic(node.getState(), yourColor);

		}

		List<Action> possibleMoves = this.rules.getNextMovesFromState(node.getState());

		Double v = Double.NEGATIVE_INFINITY;

		for (Action a : possibleMoves) {
			State nextState = this.rules.movePawn(node.getState().clone(), a);
			Node n = new Node(nextState, Double.POSITIVE_INFINITY, a);

			StatsManager.getInstance().incrementExpandedNodes();

			v = Math.max(v, minValue(n, depth - 1, maxDepth, yourColor, alpha, beta));

			n.setValue(v);

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
			// return this.heuristic.heuristicBlack(node.getState());
			return this.heuristic.heuristic(node.getState(), yourColor);
		}

		List<Action> possibleMoves = this.rules.getNextMovesFromState(node.getState());

		Double v = Double.POSITIVE_INFINITY;
		for (Action a : possibleMoves) {
			State nextState = this.rules.movePawn(node.getState().clone(), a);

			Node n = new Node(nextState, Double.NEGATIVE_INFINITY, a);

			StatsManager.getInstance().incrementExpandedNodes();

			v = Math.min(v, maxValue(n, depth - 1, maxDepth, yourColor, alpha, beta));

			n.setValue(v);

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
