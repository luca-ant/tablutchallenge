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
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.StatsManager;

public class AlphaBetaConcurrent implements IA {

	private List<Node> rootChildren;
	private long endTime;
	private Action bestMove;

	public AlphaBetaConcurrent() {

		this.rootChildren = new ArrayList<>();
		this.bestMove = null;

	}

	@Override
	public Action getBestAction(State state)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		this.endTime = System.currentTimeMillis() + GameManager.getInstance().getTimeout() * 1000;

		List<Action> actionFromRoot = GameManager.getInstance().getRules().getNextMovesFromState(state);

		for (Action a : actionFromRoot) {

			State nextState = GameManager.getInstance().getRules().movePawn(state, a);

			Node n = new Node(nextState, 0, a);

			if (n.getState().getTurn().equalsTurn(State.Turn.WHITEWIN.toString())
					&& GameManager.getInstance().getPlayer().equalsIgnoreCase("white")) {
				return n.getMove();
			}
			if (n.getState().getTurn().equalsTurn(State.Turn.BLACKWIN.toString())
					&& GameManager.getInstance().getPlayer().equalsIgnoreCase("black")) {

				return n.getMove();

			}

			rootChildren.add(n);

		}

		this.bestMove = null;

		List<MinMaxConcurrent> threads = new ArrayList<MinMaxConcurrent>();

		for (int d = 1; d <= GameManager.getInstance().getMaxDepth(); ++d) {

			System.out.println("\nSTART DEPTH = " + (d+1));

			StatsManager.getInstance().reset();
			StatsManager.getInstance().setStart(System.currentTimeMillis());

			for (Node n : this.rootChildren) {

				MinMaxConcurrent t = new MinMaxConcurrent(n, d, this.endTime);
				threads.add(t);
				t.start();

			}

			for (MinMaxConcurrent t : threads) {

				try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			StatsManager.getInstance().setEnd(System.currentTimeMillis());

			if (System.currentTimeMillis() > this.endTime) {
				System.out.println("END DUE TO TIMEOUT\n");
				StatsManager.getInstance().printResults();
				this.rootChildren.clear();

				return this.bestMove;

			}
			System.out.println("END DEPTH = " + (d+1)+ "\n");
			StatsManager.getInstance().printResults();

			Node bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();

			System.out.println("Temp move found: " + bestNextNode.getMove());

			this.bestMove = bestNextNode.getMove();

			threads.clear();
		}

		this.rootChildren.clear();
		return this.bestMove;

	}

}
