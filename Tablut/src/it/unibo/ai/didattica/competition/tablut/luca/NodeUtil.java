package it.unibo.ai.didattica.competition.tablut.luca;

public class NodeUtil {

	private static int expanseNodes = 0;

	public static void incrementExpanseNodes() {
		NodeUtil.expanseNodes++;
	}

	public static void printExpanseNodes() {
		System.out.println("Nodi espansi : "+NodeUtil.expanseNodes);
	}

	public static void reset() {
		NodeUtil.expanseNodes = 0;
	}
}
