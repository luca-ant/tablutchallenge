package it.unibo.ai.didattica.competition.tablut.teampallo.algorithms;

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
import it.unibo.ai.didattica.competition.tablut.teampallo.domain.MyRules;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.MyHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.WhiteHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.AdvancedHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.BlackHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.StatsManager;

public class AlphaBetaIterative implements IA {

	private List<Node> rootChildren;
	private long endTime;
	private Action bestMove;
	private Heuristic heuristic;

	public AlphaBetaIterative() {

		this.rootChildren = new ArrayList<>();

		if (GameManager.getInstance().getPlayer().equalsIgnoreCase("black")) {
			this.heuristic = new BlackHeuristic();
		} else if (GameManager.getInstance().getPlayer().equalsIgnoreCase("white")) {
			this.heuristic = new WhiteHeuristic();
		}

		this.bestMove = null;

	}

	@Override
	public Action getBestAction(State state)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		this.endTime = System.currentTimeMillis() + GameManager.getInstance().getTimeout() * 1000;

		this.rootChildren.clear();

		List<Action> actionFromRoot = GameManager.getInstance().getRules().getNextMovesFromState(state);

		for (Action a : actionFromRoot) {

			State nextState = GameManager.getInstance().getRules().movePawn(state.clone(), a);

			Node n = new Node(nextState, 0, a);

			if (n.getState().getTurn().equalsTurn(State.Turn.WHITEWIN.toString())
					&& GameManager.getInstance().getPlayer().equalsIgnoreCase("white")) {
				return n.getMove();
			}
			if (n.getState().getTurn().equalsTurn(State.Turn.BLACKWIN.toString())
					&& GameManager.getInstance().getPlayer().equalsIgnoreCase("black")) {

				return n.getMove();

			}

		}

		Action temp;
		this.bestMove = null;

		for (int d = 2; d <= GameManager.getInstance().getMaxDepth(); ++d) {
			System.out.println("\nSTART DEPTH = " + d);

			StatsManager.getInstance().reset();
			StatsManager.getInstance().setStart(System.currentTimeMillis());

			temp = this.minmaxAlg(state, d, d);

			StatsManager.getInstance().setEnd(System.currentTimeMillis());

			if (System.currentTimeMillis() > this.endTime) {
				System.out.println("END DUE TO TIMEOUT\n");
				StatsManager.getInstance().printResults();

				return this.bestMove;

			}
			System.out.println("END DEPTH = " + d + "\n");
			StatsManager.getInstance().printResults();

			if (temp != null) {
				System.out.println("Temp move found: " + temp);
				this.bestMove = temp;
			}
		}

		return this.bestMove;

	}

	private Action minmaxAlg(State state, int depth, int maxDepth)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		Node root = new Node(state);

		StatsManager.getInstance().incrementExpandedNodes();

		root.setValue(maxValue(root, depth, maxDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));

		if (System.currentTimeMillis() > this.endTime) {

			rootChildren.clear();
			// return this.bestMove;
			return null;
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

		if (depth == 0 || node.getState().getTurn().equalsTurn("WW") || node.getState().getTurn().equalsTurn("BW")) {
			// return this.heuristic.heuristicBlack(node.getState());
			// return this.heuristic.heuristicWhite(node.getState());

			return this.heuristic.heuristic(node.getState());

		}

		List<Action> possibleMoves = GameManager.getInstance().getRules().getNextMovesFromState(node.getState());

		Double v = Double.NEGATIVE_INFINITY;

		for (Action a : possibleMoves) {

			State nextState = GameManager.getInstance().getRules().movePawn(node.getState().clone(), a);

			if (GameManager.getInstance().contains(nextState)) {
				System.out.println("Salto lo stato");

				continue;
			}

			Node n = new Node(nextState, Double.POSITIVE_INFINITY, a);

			StatsManager.getInstance().incrementExpandedNodes();

			v = Math.max(v, minValue(n, depth - 1, maxDepth, alpha, beta));

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

	private double minValue(Node node, int depth, int maxDepth, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (System.currentTimeMillis() > this.endTime) {

			return 0;
		}
		if (depth == 0 || node.getState().getTurn().equalsTurn("WW") || node.getState().getTurn().equalsTurn("BW")) {
			// return this.heuristic.heuristicWhite(node.getState());
			// return this.heuristic.heuristicBlack(node.getState());
			return this.heuristic.heuristic(node.getState());
		}

		List<Action> possibleMoves = GameManager.getInstance().getRules().getNextMovesFromState(node.getState());

		Double v = Double.POSITIVE_INFINITY;
		for (Action a : possibleMoves) {
			State nextState = GameManager.getInstance().getRules().movePawn(node.getState().clone(), a);

			if (GameManager.getInstance().contains(nextState)) {
				System.out.println("Salto lo stato");
				continue;
			}

			Node n = new Node(nextState, Double.NEGATIVE_INFINITY, a);

			StatsManager.getInstance().incrementExpandedNodes();

			v = Math.min(v, maxValue(n, depth - 1, maxDepth, alpha, beta));

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
