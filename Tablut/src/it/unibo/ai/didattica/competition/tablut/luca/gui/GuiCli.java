package it.unibo.ai.didattica.competition.tablut.luca.gui;

import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public class GuiCli {
	// Reset
	public static final String RESET = "\033[0m"; // Text Reset

	// Regular Colors
	public static final String BLACK = "\033[0;30m"; // BLACK
	public static final String RED = "\033[0;31m"; // RED
	public static final String GREEN = "\033[0;32m"; // GREEN
	public static final String YELLOW = "\033[0;33m"; // YELLOW
	public static final String BLUE = "\033[0;34m"; // BLUE
	public static final String PURPLE = "\033[0;35m"; // PURPLE
	public static final String CYAN = "\033[0;36m"; // CYAN
	public static final String WHITE = "\033[0;37m"; // WHITE

	// Bold
	public static final String BLACK_BOLD = "\033[1;30m"; // BLACK
	public static final String RED_BOLD = "\033[1;31m"; // RED
	public static final String GREEN_BOLD = "\033[1;32m"; // GREEN
	public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
	public static final String BLUE_BOLD = "\033[1;34m"; // BLUE
	public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
	public static final String CYAN_BOLD = "\033[1;36m"; // CYAN
	public static final String WHITE_BOLD = "\033[1;37m"; // WHITE

	// Background
	public static final String BLACK_BACKGROUND = "\033[40m"; // BLACK
	public static final String RED_BACKGROUND = "\033[41m"; // RED
	public static final String GREEN_BACKGROUND = "\033[42m"; // GREEN
	public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
	public static final String BLUE_BACKGROUND = "\033[44m"; // BLUE
	public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
	public static final String CYAN_BACKGROUND = "\033[46m"; // CYAN
	public static final String WHITE_BACKGROUND = "\033[47m"; // WHITE

	// High Intensity
	public static final String BLACK_BRIGHT = "\033[0;90m"; // BLACK
	public static final String RED_BRIGHT = "\033[0;91m"; // RED
	public static final String GREEN_BRIGHT = "\033[0;92m"; // GREEN
	public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
	public static final String BLUE_BRIGHT = "\033[0;94m"; // BLUE
	public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
	public static final String CYAN_BRIGHT = "\033[0;96m"; // CYAN
	public static final String WHITE_BRIGHT = "\033[0;97m"; // WHITE

	// Bold High Intensity
	public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
	public static final String RED_BOLD_BRIGHT = "\033[1;91m"; // RED
	public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
	public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
	public static final String BLUE_BOLD_BRIGHT = "\033[1;94m"; // BLUE
	public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
	public static final String CYAN_BOLD_BRIGHT = "\033[1;96m"; // CYAN
	public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

	// High Intensity backgrounds
	public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
	public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
	public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
	public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
	public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
	public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
	public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m"; // CYAN
	public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m"; // WHITE

	private List<String> citadels;
	private List<String> stars;

	public GuiCli() {
		this.citadels = new ArrayList<String>();
		this.citadels.add("a4");
		this.citadels.add("a5");
		this.citadels.add("a6");
		this.citadels.add("b5");
		this.citadels.add("d1");
		this.citadels.add("e1");
		this.citadels.add("f1");
		this.citadels.add("e2");
		this.citadels.add("i4");
		this.citadels.add("i5");
		this.citadels.add("i6");
		this.citadels.add("h5");
		this.citadels.add("d9");
		this.citadels.add("e9");
		this.citadels.add("f9");
		this.citadels.add("e8");
		
		
		this.stars = new ArrayList<String>();
		this.stars.add("a2");
		this.stars.add("a3");
		this.stars.add("a7");
		this.stars.add("a8");
		this.stars.add("b1");
		this.stars.add("b9");
		this.stars.add("c1");
		this.stars.add("c9");
		this.stars.add("g1");
		this.stars.add("g9");
		this.stars.add("h1");
		this.stars.add("h9");
		this.stars.add("i2");
		this.stars.add("i3");
		this.stars.add("i7");
		this.stars.add("i8");

	}

	public void update(State aState) {
		this.printGuiCli(aState);

	}

	
	public void printGuiCli(State aState) {
		System.out.println(this.RESET + "    A  B  C  D  E  F  G  H  I ");

		for (int i = 0; i < aState.getBoard().length; i++) {
			int row = i +1;
			System.out.print(this.RESET + " " + row + " ");

			for (int j = 0; j < aState.getBoard().length; j++) {
				if (aState.getPawn(i, j).equalsPawn("B")) {
					// pedina nera
					System.out.print(GuiCli.BLACK_BACKGROUND + GuiCli.WHITE_BOLD + "   ");
					
				} else if (aState.getPawn(i, j).equalsPawn("W")) {
					// pedina bianca
					System.out.print(GuiCli.WHITE_BACKGROUND_BRIGHT +GuiCli.BLACK_BOLD + "   ");

				} else if (aState.getPawn(i, j).equalsPawn("K")) {
					// re
					System.out.print(GuiCli.WHITE_BACKGROUND_BRIGHT + GuiCli.BLACK_BOLD + " + ");
				}

				else if (this.citadels.contains(aState.getBox(i, j))) {
					// accampamento
					System.out.print(GuiCli.WHITE_BACKGROUND + GuiCli.BLACK_BOLD + " A ");

				} 

				else if (this.stars.contains(aState.getBox(i, j))) {
					// stella
					System.out.print(GuiCli.WHITE_BACKGROUND + GuiCli.BLUE_BOLD_BRIGHT + " * ");

				}
				else if (aState.getPawn(i, j).equalsPawn("T")) {

					// trono
					System.out.print(GuiCli.WHITE_BACKGROUND + GuiCli.BLACK_BOLD + " T ");
				}

				else if (aState.getPawn(i, j).equalsPawn(State.Pawn.EMPTY.toString())) {
					// casella vuota
					System.out.print(GuiCli.WHITE_BACKGROUND + "   ");

				}
			}
			System.out.println(this.RESET);

		}
	}
}
