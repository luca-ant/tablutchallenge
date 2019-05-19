package it.unibo.ai.didattica.competition.tablut.teampallo.algorithms;

import java.util.ArrayList;
import java.util.Comparator;

import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
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
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.GeneticHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.StatsManager;

public class AlphaBetaIterative implements IA {

	private List<Node> rootChildren;
	private long endTime;
	private Action bestMove;
	private boolean ww;
	private boolean bw;
	private Heuristic heuristic;
	private String player;
	private boolean drawOption=false;
	private List<Action> drawAction;

	public AlphaBetaIterative(String player) {

		this.rootChildren = new ArrayList<>();
		this.heuristic = new GeneticHeuristic(player);
		this.bestMove = null;
		this.ww = false;
		this.bw = false;
		this.player=player;
		this.drawAction=new ArrayList<>();
		
		System.out.println("Istanziato minmax per "+player);

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

			if (this.ww && player.equalsIgnoreCase("white")) {
				this.bestMove = temp;
				break;
			}

			if (this.bw && player.equalsIgnoreCase("black")) {
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

		if(drawOption) {
			int countW=0;
			int countB=0;
			for(int i=0;i<state.getBoard().length-1;i++) {
				for(int j=0;j<state.getBoard().length-1;j++) {
					if(state.getPawn(i, j).equalsPawn(Pawn.WHITE.toString())) {
						countW++;
					}
					if(state.getPawn(i, j).equalsPawn(Pawn.BLACK.toString())) {
						countB++;
					}
				}
			}
			
			if(drawOption && countW<=2 && countB>=2) {
				if(drawAction.size()>0) {
					return drawAction.get(0);
				}
			}
		}
		
		if (System.currentTimeMillis() > this.endTime || this.rootChildren.isEmpty()) {

			return this.bestMove;
		}

		
		for (Node node : rootChildren) {
			if (node.getState().getTurn().equalsTurn(State.Turn.WHITEWIN.toString())
					&& player.equalsIgnoreCase("white")) {
				this.ww = true;
				return node.getMove();
			}
			if (node.getState().getTurn().equalsTurn(State.Turn.BLACKWIN.toString())
					&& player.equalsIgnoreCase("black")) {

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

		if (depth == 0 || node.getState().getTurn().equalsTurn(Turn.BLACKWIN.toString()) ||
				node.getState().getTurn().equalsTurn(Turn.WHITEWIN.toString()) ||
				node.getState().getTurn().equalsTurn(Turn.DRAW.toString())) {
			// return this.heuristic.heuristicBlack(node.getState());
			// return this.heuristic.heuristicWhite(node.getState());

			return this.heuristic.heuristic(node.getState());

		}

		List<Action> possibleMoves = GameManager.getInstance().getRules().getNextMovesFromState(node.getState());

		Double v = Double.NEGATIVE_INFINITY;

		for (Action a : possibleMoves) {

			State nextState = GameManager.getInstance().getRules().movePawn(node.getState().clone(), a);

			if (drawOption && GameManager.getInstance().contains(nextState.hashCode())) {
				System.out.println("Salto lo stato");
				
				if(drawOption && depth==maxDepth) {
					drawAction.add(a);
				}

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
		if (depth == 0 || node.getState().getTurn().equalsTurn(Turn.BLACKWIN.toString()) ||
				node.getState().getTurn().equalsTurn(Turn.WHITEWIN.toString()) ||
				node.getState().getTurn().equalsTurn(Turn.DRAW.toString())) {
			// return this.heuristic.heuristicWhite(node.getState());
			// return this.heuristic.heuristicBlack(node.getState());
			return this.heuristic.heuristic(node.getState());
		}

		List<Action> possibleMoves = GameManager.getInstance().getRules().getNextMovesFromState(node.getState());

		Double v = Double.POSITIVE_INFINITY;
		for (Action a : possibleMoves) {
			State nextState = GameManager.getInstance().getRules().movePawn(node.getState().clone(), a);

			if (drawOption && GameManager.getInstance().contains(nextState.hashCode())) {
				System.out.println("Salto lo stato");
				
				if(drawOption && depth==maxDepth) {
					drawAction.add(a);
				}
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
