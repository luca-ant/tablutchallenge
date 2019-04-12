package it.unibo.ai.didattica.competition.tablut.luca;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class MinMax {

	public final static int MIN = -1;
	public final static int MAX = 1;
	public final static int DEPTH = 7;

	private List<Node> rootChildren;

	public MinMax() {
		this.rootChildren = new ArrayList<>();
	}

	private Action minmaxAlg(MyState state, int depth) {

		Node root = new Node(state);

		root.setValue(maxValue(state, depth - 1));

		
		Node bestNextNode = rootChildren.stream().max(Comparator.comparing(n -> n.getValue())).get();
		
		return bestNextNode.getMove();

	}

	private double maxValue(MyState state, int depth) {

		if (depth == 0) {
			Heuristic h = new RandomHeuristic();
			return h.heuristic(state);
		}

		Double v = Double.MIN_VALUE;

		for (Action m : state.getNextMoves()) {
			MyState nextState = state.calculateNextState();
			v = Math.max(v, minValue(nextState, depth - 1));

			if (depth == DEPTH - 1) {
				Node n = new Node(nextState, v, m);

				rootChildren.add(n);

			}
		}

		return v;
	}

	private double minValue(MyState state, int depth) {

		if (depth == 0) {
			Heuristic h = new RandomHeuristic();
			return h.heuristic(state);
		}

		Double v = Double.MAX_VALUE;

		for (Action m : state.getNextMoves()) {
			MyState nextState = state.calculateNextState();

			v = Math.min(v, maxValue(nextState, depth - 1));
			if (depth == DEPTH - 1) {
				Node n = new Node(nextState, v, m);

				rootChildren.add(n);

			}

		}

		return v;
	}

}
