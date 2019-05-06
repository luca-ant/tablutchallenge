package it.unibo.ai.didattica.competition.tablut.teampallo.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.MyHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.StatsManager;

public class AlphaBetaParallel implements IA {

	private List<Node> rootChildren;
	private long endTime;
	private Map<Integer, Action> bestMoves;
	private boolean ww;
	private boolean bw;
	private int depthReached;

	public AlphaBetaParallel() {

		this.rootChildren = new ArrayList<>();
		this.bestMoves = new HashMap<Integer, Action>();
		this.ww = false;
		this.bw = false;
		this.depthReached = 0;

	}

	@Override
	public Action getBestAction(State state)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {

		this.endTime = System.currentTimeMillis() + GameManager.getInstance().getTimeout() * 1000;

		List<MinMaxThread> threads = new ArrayList<>();

		for (int d = 1; d <= GameManager.getInstance().getMaxDepth(); ++d) {

			MinMaxThread t = new MinMaxThread(state, d, this);

			threads.add(t);

		}

		for (MinMaxThread t : threads) {

			t.start();

		}

		for (MinMaxThread t : threads) {

			try {
				System.out.println("Attendo i risultati...");
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		int md = Collections.max(getBestMoves().keySet());

		return getBestMoves().get(md);

	}

	public synchronized Map<Integer, Action> getBestMoves() {
		return bestMoves;
	}

	public boolean isWw() {
		return ww;
	}

	public void setWw(boolean ww) {
		this.ww = ww;
	}

	public boolean isBw() {
		return bw;
	}

	public void setBw(boolean bw) {
		this.bw = bw;
	}

	public synchronized void addMoves(int depth, Action a) {

		this.bestMoves.put(depth, a);

	}

	public synchronized int getDepthReached() {
		return depthReached;
	}

	public synchronized void setDepthReached(int depthReached) {
		this.depthReached = depthReached;
	}

	public long getEndTime() {
		return endTime;
	}

}
