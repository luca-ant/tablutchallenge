package it.unibo.ai.didattica.competition.tablut.teampallo.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.teampallo.domain.MyGameAshtonTablutRules;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

public class MainTest {

	public static void main(String[] args) {
		
		Pawn[][] board = new Pawn[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				board[i][j] = Pawn.EMPTY;
			}
		}

		board[4][4] = Pawn.THRONE;

		Turn turn = Turn.BLACK;

		board[6][1] = Pawn.KING;

		board[1][5] = Pawn.WHITE;
		board[0][6] = Pawn.WHITE;
		board[3][7] = Pawn.WHITE;
		board[2][8] = Pawn.WHITE;
		board[8][2] = Pawn.WHITE;
		board[4][3] = Pawn.WHITE;
		board[2][0] = Pawn.WHITE;
		board[2][1] = Pawn.EMPTY;

		board[0][3] = Pawn.BLACK;
		board[0][4] = Pawn.BLACK;
		board[0][5] = Pawn.BLACK;
		board[1][4] = Pawn.BLACK;
		board[8][3] = Pawn.BLACK;
		board[8][4] = Pawn.BLACK;
		board[8][5] = Pawn.BLACK;
		board[7][4] = Pawn.BLACK;
		board[3][0] = Pawn.BLACK;
		board[4][0] = Pawn.BLACK;
		board[5][0] = Pawn.BLACK;
		board[3][1] = Pawn.BLACK;
		board[3][8] = Pawn.BLACK;
		board[4][8] = Pawn.BLACK;
		board[5][8] = Pawn.BLACK;
		board[4][7] = Pawn.BLACK;

		
		StateTablut state=new StateTablut();
		
		state.setBoard(board);
		
		Heuristic h=new GeneticHeuristic("white");
		
		System.out.println(state);
		System.out.println("VALORE: "+h.heuristicWhite(state));
		
		/*MyGameAshtonTablutRules rules=MyGameAshtonTablutRules.getInstance();
		
		System.out.println("Simmetria D2 di [0,4] e' ["+rules.symmetryD2(0, 4)[0]+","+rules.symmetryD2(0, 4)[1]+"]");
		System.out.println("Simmetria D2 di [8,4] e' ["+rules.symmetryD2(8, 4)[0]+","+rules.symmetryD2(8, 4)[1]+"]");
		System.out.println("Simmetria D2 di [5,7] e' ["+rules.symmetryD2(5, 7)[0]+","+rules.symmetryD2(5, 7)[1]+"]");
		System.out.println("Simmetria D2 di [7,5] e' ["+rules.symmetryD2(7, 5)[0]+","+rules.symmetryD2(7, 5)[1]+"]");
		System.out.println("Simmetria D2 di [2,3] e' ["+rules.symmetryD2(2, 3)[0]+","+rules.symmetryD2(2, 3)[1]+"]");
		System.out.println("Simmetria D2 di [3,2] e' ["+rules.symmetryD2(3, 2)[0]+","+rules.symmetryD2(3, 2)[1]+"]");

	*/
	
	}

}
