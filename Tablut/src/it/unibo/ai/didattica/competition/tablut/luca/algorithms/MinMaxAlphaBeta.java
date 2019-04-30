package it.unibo.ai.didattica.competition.tablut.luca.algorithms;

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
import it.unibo.ai.didattica.competition.tablut.luca.domain.MyGameAshtonTablutRules;
import it.unibo.ai.didattica.competition.tablut.luca.domain.MyRules;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.BasicHeuristic;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.RandomHeuristic;
import it.unibo.ai.didattica.competition.tablut.luca.util.StatsManager;

public class MinMaxAlphaBeta implements IA {

	public final static int DEPTH = 3;

	private MyRules rules;
	private int timeout;
	private List<Node> rootChildren;

	private long endTime;

	private Heuristic heuristic;

	public MinMaxAlphaBeta(MyRules rules, int timeout) {
		this.timeout = timeout;

		this.rules = rules;
		this.rootChildren = new ArrayList<>();
		this.heuristic = new RandomHeuristic();

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

		this.endTime = System.currentTimeMillis() + this.timeout * 1000;

		Node root = new Node(state);

		StatsManager.getInstance().incrementExpandedNodes();

		if (yourColor.equals(State.Turn.BLACK))
			root.setValue(maxValue(root, depth, yourColor, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
		else if (yourColor.equals(State.Turn.WHITE))
			root.setValue(minValue(root, depth, yourColor, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));

		Node bestNextNode = null;
		if (yourColor.equals(State.Turn.BLACK)) {
			bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();
		} else if (yourColor.equals(State.Turn.WHITE)) {
			bestNextNode = rootChildren.stream().min(Comparator.comparing(n -> n.getValue())).get();
		}

		rootChildren.clear();
		return bestNextNode.getMove();

	}

	private double maxValue(Node node, int depth, Turn yourColor, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (depth == 0 || System.currentTimeMillis() > this.endTime) {
			return this.heuristic.heuristic(node.getState(), yourColor);

		}

		List<Action> possibleMoves = this.rules.getNextMovesFromState(node.getState());

		Double v = Double.NEGATIVE_INFINITY;

		for (Action a : possibleMoves) {
			State nextState = rules.movePawn(node.getState().clone(), a);
			Node n = new Node(nextState, Double.POSITIVE_INFINITY, a);

			StatsManager.getInstance().incrementExpandedNodes();

			v = Math.max(v, minValue(n, depth - 1, yourColor, alpha, beta));

			n.setValue(v);

			if (depth == DEPTH) {

				rootChildren.add(n);

			}

			if (v >= beta)
				return v;

			alpha = Math.max(alpha, v);

		}

		return v;
	}

	private double minValue(Node node, int depth, Turn yourColor, double alpha, double beta)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		if (depth == 0 || System.currentTimeMillis() > this.endTime) {
			return this.heuristic.heuristic(node.getState(), yourColor);

		}

		List<Action> possibleMoves = this.rules.getNextMovesFromState(node.getState());

		Double v = Double.POSITIVE_INFINITY;
		for (Action a : possibleMoves) {
			State nextState = rules.movePawn(node.getState().clone(), a);

			Node n = new Node(nextState, Double.NEGATIVE_INFINITY, a);

			StatsManager.getInstance().incrementExpandedNodes();

			v = Math.min(v, maxValue(n, depth - 1, yourColor, alpha, beta));

			n.setValue(v);

			if (depth == DEPTH) {
				rootChildren.add(n);

			}
			if (v <= alpha)
				return v;

			beta = Math.min(beta, v);

		}
		possibleMoves.clear();

		return v;
	}

}
