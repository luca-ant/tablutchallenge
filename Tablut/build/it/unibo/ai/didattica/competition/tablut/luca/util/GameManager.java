package it.unibo.ai.didattica.competition.tablut.luca.util;

import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.luca.domain.MyRules;

public class GameManager {

	/*
	 * Singleton
	 */

	private static GameManager instance = null;

	public final static int MEMORY_LIMIT = 1024;
	private final static int MAX_DEPTH = 10;
	private final static int TIMEOUT = 55;

	private StatsManager stats;
	private MyRules rules;
	private String player;
	private int timeout;
	private int memoryLimit;
	private int maxDepth;
	private List<Integer> statiVisitati;

	private GameManager() {
		this.statiVisitati = new ArrayList<>();
		this.timeout = TIMEOUT;
		this.maxDepth = MAX_DEPTH;
		this.memoryLimit = MEMORY_LIMIT;
		this.pedineOnBoard = 25;
		this.player = "";
		this.stats = StatsManager.getInstance();

	}

	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}

		return instance;
	}

	public void setParameters(int timeout, int maxDepth, String player) {
		this.timeout = timeout;
		this.maxDepth = maxDepth;
		this.player = player;
	}

	public MyRules getRules() {
		return rules;
	}

	public void setRules(MyRules rules) {
		this.rules = rules;
	}

	private int pedineOnBoard;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getPlayer() {
		return player;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public int getMemoryLimit() {
		return memoryLimit;
	}

	public void setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	public void clearStateCache() {
		this.statiVisitati.clear();
	}

	public void addNewState(State state) {
		this.statiVisitati.add(state.hashCode());
		stats.update("cache");

		int num = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (state.getPawn(i, j).equalsPawn("W") || state.getPawn(i, j).equalsPawn("B")
						|| state.getPawn(i, j).equalsPawn("K"))
					num++;
			}
		}

		if (num < pedineOnBoard) {
			stats.update("resetCache");
			pedineOnBoard = num;
			this.clearStateCache();
		}
	}

	public boolean contains(int hash) {
		return this.statiVisitati.contains(hash);
	}

}
