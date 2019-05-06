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
	private boolean ww;
	private boolean bw;
	private Heuristic heuristic;

	public AlphaBetaConcurrent() {

		this.rootChildren = new ArrayList<>();
		this.heuristic = new MyHeuristic();
		this.bestMove = null;
		this.ww = false;
		this.bw = false;

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

		Action temp;
		this.bestMove = null;

		for (int d = 1; d <= GameManager.getInstance().getMaxDepth(); ++d) {

			System.out.println("\nSTART DEPTH = " + d);

			StatsManager.getInstance().reset();
			StatsManager.getInstance().setStart(System.currentTimeMillis());

			List<Thread> threads = new ArrayList<Thread>();

			for (Node n : this.rootChildren) {

				MinMaxConcurrent t = new MinMaxConcurrent(n, d, this.endTime);
				threads.add(t);
				t.start();

			}

			for (Thread t : threads) {

				try {
					System.out.println("Attendo i risultati...");
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

				return this.bestMove;

			}
			System.out.println("END DEPTH = " + d + "\n");
			StatsManager.getInstance().printResults();

			
			
			Node bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();

			
			
			
			System.out.println("Temp move found: " + bestNextNode.getMove());

			this.bestMove = bestNextNode.getMove();

		}

		return this.bestMove;

	}

	

}
