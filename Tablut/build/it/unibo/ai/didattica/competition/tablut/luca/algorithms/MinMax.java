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
import it.unibo.ai.didattica.competition.tablut.luca.domain.MyAshtonTablutRules;
import it.unibo.ai.didattica.competition.tablut.luca.domain.MyGame;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.luca.heuristics.RandomHeuristic;

public class MinMax implements IA {

	public final static int DEPTH = 5;

	private MyGame rules;
	private int timeout;
	private List<Node> rootChildren;
	private List<int[]> pawns;
	private Thread wd;
	private Heuristic heuristic;


	public MinMax(MyGame rules, int timeout) {
		this.timeout = timeout;
		this.rules = rules;
		this.rootChildren = new ArrayList<>();
		this.pawns = new ArrayList<int[]>();
		this.heuristic	= new RandomHeuristic();

	}

	@Override
	public Action getBestAction(State state, Turn yourColor)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {
		return this.minmaxAlg(state, DEPTH, yourColor, this.timeout);
	}

	private Action minmaxAlg(State state, int depth, Turn yourColor, int timeout)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		this.wd = new Thread() {
			@Override
			public void run() {
				try {
					int counter = 0;
					while (counter < timeout) {
						Thread.sleep(1000);
						counter++;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		this.wd.start();

		Node root = new Node(state);

		NodeUtil.getIstance().incrementExpandedNodes();

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

		if (depth == 0 || !this.wd.isAlive()) {
			return this.heuristic.heuristic(node.getState());

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

		node.setValue(Double.MIN_VALUE);
		Double v = Double.MIN_VALUE;

		for (Action a : possibleMoves) {
			State nextState = rules.movePawn(node.getState(), a);
			Node n = new Node(nextState.clone(), Double.MAX_VALUE, a);

			NodeUtil.getIstance().incrementExpandedNodes();

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

		if (depth == 0 || !this.wd.isAlive()) {
			return this.heuristic.heuristic(node.getState());

		}

		int[] buf;
		for (int i = 0; i < node.getState().getBoard().length; i++) {
			for (int j = 0; j < node.getState().getBoard().length; j++) {
				if (node.getState().getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString()) || node.getState().getPawn(i, j).equalsPawn(State.Pawn.KING.toString())  ) {
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

		node.setValue(Double.MAX_VALUE);
		Double v = Double.MAX_VALUE;
		for (Action a : possibleMoves) {
			State nextState = rules.movePawn(node.getState(), a);

			Node n = new Node(nextState, Double.MIN_VALUE, a);

			NodeUtil.getIstance().incrementExpandedNodes();

			v = Math.min(v, maxValue(n, depth - 1));

			if (depth == DEPTH - 1) {

				rootChildren.add(n);

			}
		}
		possibleMoves.clear();

		return v;
	}

}