package it.unibo.ai.didattica.competition.tablut.teampallo.algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
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
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.BlackHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.WhiteHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.StatsManager;

public class MinMaxThread extends Thread {

	private it.unibo.ai.didattica.competition.tablut.domain.State currentState;
	private List<Node> rootChildren;
	private Heuristic heuristic;
	private int depthToReach;

	private AlphaBetaParallel alphaBetaParallel;

	public MinMaxThread(it.unibo.ai.didattica.competition.tablut.domain.State startState, int d, AlphaBetaParallel alphaBetaParallel) {

		this.currentState = startState;
		this.rootChildren = new ArrayList<>();
		if (GameManager.getInstance().getPlayer().equalsIgnoreCase("black")) {
			this.heuristic = new BlackHeuristic();
		} else if (GameManager.getInstance().getPlayer().equalsIgnoreCase("white")) {
			this.heuristic = new WhiteHeuristic();
		}
		this.depthToReach = d;
		this.alphaBetaParallel = alphaBetaParallel;
	}

	
	@Override
	public void run() {
		System.out.println("\nSTART DEPTH " + this.depthToReach);
		Action temp = null;
		try {
			temp = minmaxAlg(this.currentState, this.depthToReach, this.depthToReach);
		} catch (BoardException | ActionException | StopException | PawnException | DiagonalException
				| ClimbingException | ThroneException | OccupitedException | ClimbingCitadelException
				| CitadelException e) {
			e.printStackTrace();
		}

		if (System.currentTimeMillis() > alphaBetaParallel.getEndTime()) {
			System.out.println("END DUE TO TIMEOUT DEPTH " + this.depthToReach + "\n");

			return;

		}

		if (temp != null && alphaBetaParallel.getDepthReached() < this.depthToReach) {

			alphaBetaParallel.addMoves(this.depthToReach,temp);
			System.out.println("BEST MOVE FROM DEPTH " + this.depthToReach + " -> " + temp + "\n");

		}

		System.out.println("END DEPTH = " + this.depthToReach + "\n");

	}

	private Action minmaxAlg(it.unibo.ai.didattica.competition.tablut.domain.State state, int depth, int maxDepth)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		Node root = new Node(state);

		StatsManager.getInstance().incrementExpandedNodes();

		root.setValue(maxValue(root, depth, maxDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));

		
		if (System.currentTimeMillis() > alphaBetaParallel.getEndTime() || this.rootChildren.isEmpty()) {

			return null;
		}
		
		
		
		for (Node node : rootChildren) {
			if (node.getState().getTurn().equalsTurn(it.unibo.ai.didattica.competition.tablut.domain.State.Turn.WHITEWIN.toString())
					&& GameManager.getInstance().getPlayer().equalsIgnoreCase("white")) {
				alphaBetaParallel.setWw(true);
				return node.getMove();
			}
			if (node.getState().getTurn().equalsTurn(it.unibo.ai.didattica.competition.tablut.domain.State.Turn.BLACKWIN.toString())
					&& GameManager.getInstance().getPlayer().equalsIgnoreCase("black")) {

				alphaBetaParallel.setBw(true);
				return node.getMove();

			}

		}

		Node bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();

		rootChildren.clear();



		return bestNextNode.getMove();

	}

	private double maxValue(Node node, int depth, int maxDepth, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (System.currentTimeMillis() > alphaBetaParallel.getEndTime()) {

			return 0;
		}

		if (depth == 0) {
			// return this.heuristic.heuristicBlack(node.getState());
			// return this.heuristic.heuristicWhite(node.getState());

			return this.heuristic.heuristic(node.getState());

		}

		List<Action> possibleMoves = GameManager.getInstance().getRules().getNextMovesFromState(node.getState());

		Double v = Double.NEGATIVE_INFINITY;

		for (Action a : possibleMoves) {

			it.unibo.ai.didattica.competition.tablut.domain.State nextState = GameManager.getInstance().getRules().movePawn(node.getState().clone(), a);

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

			if (System.currentTimeMillis() > alphaBetaParallel.getEndTime()) {

				return 0;
			}

		}

		return v;
	}

	private double minValue(Node node, int depth, int maxDepth, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (System.currentTimeMillis() > alphaBetaParallel.getEndTime()) {

			return 0;
		}
		if (depth == 0) {
			// return this.heuristic.heuristicWhite(node.getState());
			// return this.heuristic.heuristicBlack(node.getState());
			return this.heuristic.heuristic(node.getState());
		}

		List<Action> possibleMoves = GameManager.getInstance().getRules().getNextMovesFromState(node.getState());

		Double v = Double.POSITIVE_INFINITY;
		for (Action a : possibleMoves) {
			it.unibo.ai.didattica.competition.tablut.domain.State nextState = GameManager.getInstance().getRules().movePawn(node.getState().clone(), a);

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

			if (System.currentTimeMillis() > alphaBetaParallel.getEndTime()) {

				return 0;
			}

		}
		possibleMoves.clear();

		return v;
	}

}
