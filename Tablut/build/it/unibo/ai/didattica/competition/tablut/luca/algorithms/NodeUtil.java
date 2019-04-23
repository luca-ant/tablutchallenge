package it.unibo.ai.didattica.competition.tablut.luca.algorithms;

public class NodeUtil {

	private int expanseNodes;
	private static NodeUtil instance = null;
	
	private NodeUtil() {
		this.expanseNodes = 0;
	}

	public void incrementExpandedNodes() {
		this.expanseNodes++;
	}

	public void printExpandedNodes() {
		System.out.println("Nodi espansi : "+this.expanseNodes);
	}

	public void reset() {
		this.expanseNodes = 0;
	}

	public static NodeUtil getIstance() {

		if(NodeUtil.instance == null)
			instance = new NodeUtil();

		return instance;


	}
}
