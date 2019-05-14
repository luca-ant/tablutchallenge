package it.unibo.ai.didattica.competition.tablut.teampallo.test;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.teampallo.gui.GuiCli;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.AdvancedHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.BlackHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.Heuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.MyHeuristic;
import it.unibo.ai.didattica.competition.tablut.teampallo.heuristics.WhiteHeuristic;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

public class TestHeuristic {

	public static void main(String[] args) {

		Pawn board[][];

		State s = new StateTablut();
		GuiCli gui = new GuiCli();

		board = new Pawn[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				board[i][j] = Pawn.EMPTY;
			}
		}

		board[4][4] = Pawn.THRONE;

		board[4][4] = Pawn.KING;

		board[2][4] = Pawn.WHITE;
		board[3][4] = Pawn.WHITE;
		board[5][4] = Pawn.WHITE;
		board[6][4] = Pawn.WHITE;
		board[8][2] = Pawn.WHITE;
		board[4][3] = Pawn.WHITE;
		board[4][5] = Pawn.BLACK;
		board[4][6] = Pawn.EMPTY;

		board[0][3] = Pawn.EMPTY;
		board[0][4] = Pawn.BLACK;
		board[0][5] = Pawn.BLACK;
		board[1][4] = Pawn.BLACK;
		board[8][3] = Pawn.BLACK;
		board[8][4] = Pawn.BLACK;
		board[8][5] = Pawn.EMPTY;
		board[7][4] = Pawn.EMPTY;
		board[3][0] = Pawn.EMPTY;
		board[4][0] = Pawn.BLACK;
		board[5][0] = Pawn.BLACK;
		board[4][1] = Pawn.BLACK;
		board[3][8] = Pawn.BLACK;
		board[4][8] = Pawn.BLACK;
		board[5][8] = Pawn.BLACK;
		board[4][7] = Pawn.BLACK;

		
		board[5][5] = Pawn.WHITE;

		
		
		s.setBoard(board);
		
		
		gui.update(s);
		
		
		
		System.out.println();

		Heuristic hb= new BlackHeuristic();
		Heuristic hw= new WhiteHeuristic();
		System.out.println("\nBLACK HEURISTIC: "+ hb.heuristic(s)+ "\n");
		System.out.println("\nWHITE HEURISTIC: "+ hw.heuristic(s)+ "\n");
		
		
		

	}

}
